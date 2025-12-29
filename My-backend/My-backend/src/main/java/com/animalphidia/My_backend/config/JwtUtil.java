package com.animalphidia.My_backend.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration}")
    private long jwtExpirationMs;

    @Value("${app.jwt.refresh-expiration}")
    private long refreshTokenExpirationMs;

    private SecretKey signingKey;

    @PostConstruct
    public void init() {
        log.info("🔄 JwtUtil Initializing...");

        // Generate a secure key
        this.signingKey = getSigningKey();

        if (signingKey == null) {
            log.error("❌ Failed to create JWT signing key!");
        } else {
            log.info("✅ JWT Signing key created successfully");
            log.info("🔐 Algorithm: {}", signingKey.getAlgorithm());
            log.info("⏰ JWT Expiration: {} ms ({} hours)",
                    jwtExpirationMs, jwtExpirationMs / 3600000);
        }
    }

    public String generateToken(String username, String role) {
        try {
            log.info("🔧 Generating JWT token for user: {}, role: {}", username, role);

            if (username == null || username.isEmpty()) {
                throw new IllegalArgumentException("Username cannot be null or empty");
            }

            if (role == null || role.isEmpty()) {
                role = "viewer"; // Default role
            }

            // Ensure we have a valid signing key
            if (signingKey == null) {
                log.warn("Signing key is null, regenerating...");
                signingKey = getSigningKey();
            }

            Map<String, Object> claims = new HashMap<>();
            claims.put("role", role);

            Date now = new Date();
            Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

            log.debug("Token expiration: {}", expiryDate);

            String token = Jwts.builder()
                    .setClaims(claims)
                    .setSubject(username)
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    .signWith(signingKey, SignatureAlgorithm.HS512)
                    .compact();

            if (token == null || token.isEmpty()) {
                log.error("❌ Generated token is null or empty!");
                throw new RuntimeException("Failed to generate JWT token");
            }

            String[] parts = token.split("\\.");
            log.info("✅ JWT Token generated successfully. Parts: {}", parts.length);
            log.debug("📝 Token sample: {}...", token.substring(0, Math.min(50, token.length())));

            return token;

        } catch (Exception e) {
            log.error("❌ JWT Token generation failed: {}", e.getMessage(), e);
            throw new RuntimeException("JWT generation failed: " + e.getMessage(), e);
        }
    }

    public String generateRefreshToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username, refreshTokenExpirationMs);
    }

    private String createToken(Map<String, Object> claims, String subject, long expirationTime) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(signingKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractRole(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return (String) claims.get("role");
        } catch (Exception e) {
            log.error("Error extracting role from token: {}", e.getMessage());
            return null;
        }
    }

    public <T> T extractClaim(String token, java.util.function.Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.error("Error extracting claims: {}", e.getMessage());
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    public Boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token);
            log.debug("Token is valid");
            return true;
        } catch (SecurityException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        } catch (Exception e) {
            log.error("JWT validation error: {}", e.getMessage());
        }
        return false;
    }

    public Boolean isTokenExpired(String token) {
        try {
            extractAllClaims(token);
            return false;
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    private SecretKey getSigningKey() {
        try {
            log.info("🔑 Creating JWT signing key...");

            if (jwtSecret == null || jwtSecret.trim().isEmpty()) {
                log.warn("⚠️ JWT Secret is not configured. Generating a secure random key.");
                // Generate a secure random key of appropriate length for HS512
                return Keys.secretKeyFor(SignatureAlgorithm.HS512);
            }

            log.debug("Using configured JWT secret (length: {})", jwtSecret.length());

            // For HS512, we need at least 512 bits (64 bytes)
            byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);

            // Check if key is long enough
            if (keyBytes.length < 64) {
                log.warn("⚠️ Configured secret is only {} bytes ({} bits). Need at least 64 bytes for HS512.",
                        keyBytes.length, keyBytes.length * 8);
                log.warn("⚠️ Generating a secure random key instead.");
                return Keys.secretKeyFor(SignatureAlgorithm.HS512);
            }

            log.info("✅ Using configured secret key ({} bytes, {} bits)",
                    keyBytes.length, keyBytes.length * 8);

            return Keys.hmacShaKeyFor(keyBytes);

        } catch (Exception e) {
            log.error("❌ Failed to create signing key: {}", e.getMessage(), e);
            // Fallback to generating a secure random key
            return Keys.secretKeyFor(SignatureAlgorithm.HS512);
        }
    }
}