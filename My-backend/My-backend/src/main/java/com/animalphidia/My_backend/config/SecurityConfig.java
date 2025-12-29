package com.animalphidia.My_backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired(required = false)
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz -> authz
                        // Public endpoints (no authentication required)
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/test").permitAll()
                        .requestMatchers("/home", "/index", "/page/**", "/debug/**").permitAll()

                        // Static files
                        .requestMatchers(
                                "/",
                                "/index.html",
                                "/explore.html",
                                "/login.html",
                                "/signup.html",
                                "/about.html",
                                "/contact.html",
                                "/contribute.html",
                                "/dashboard.html",
                                "/profile-settings.html",
                                "/my-collections.html",
                                "/notifications.html",
                                "/privacy.html",
                                "/terms.html",
                                "/user-profile.html",
                                "/admin-dashboard.html",
                                "/super-admin.html",
                                "/forgot-password.html",
                                "/animal.html",
                                "/404.html",
                                "/error.html"
                        ).permitAll()

                        .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()

                        // API endpoints - public
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/public/**").permitAll()
                        .requestMatchers("/api/health").permitAll()
                        .requestMatchers("/api/info").permitAll()
                        .requestMatchers("/api", "/api/").permitAll()

                        // PUBLIC Animal endpoints (viewable by everyone)
                        .requestMatchers(HttpMethod.GET, "/api/animals").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/animals/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/animals/featured").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/animals/search/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/animals/filter/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/animals/tags/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/animals/island/**").permitAll()

                        // Swagger/OpenAPI
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/swagger-ui.html").permitAll()
                        .requestMatchers("/webjars/**").permitAll()

                        // CONTRIBUTOR, MODERATOR, ADMIN endpoints
                        .requestMatchers(HttpMethod.POST, "/api/animals").hasAnyRole("CONTRIBUTOR", "MODERATOR", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/animals/**").hasAnyRole("MODERATOR", "ADMIN", "CONTRIBUTOR")

                        // MODERATOR & ADMIN only endpoints
                        .requestMatchers(HttpMethod.DELETE, "/api/animals/**").hasAnyRole("ADMIN", "MODERATOR")
                        .requestMatchers(HttpMethod.POST, "/api/animals/{id}/verify").hasAnyRole("MODERATOR", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/animals/pending").hasAnyRole("MODERATOR", "ADMIN")

                        // User endpoints
                        .requestMatchers(HttpMethod.GET, "/api/users/profile").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/users/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/users/favorites").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/users/favorites/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/users/favorites/**").authenticated()

                        // MODERATOR endpoints
                        .requestMatchers("/api/moderator/**").hasAnyRole("MODERATOR", "ADMIN")

                        // ADMIN endpoints
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // Allow everything else (for development)
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(401);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"" + authException.getMessage() + "\"}");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(403);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\": \"Forbidden\", \"message\": \"Access denied\"}");
                        })
                );

        if (jwtAuthenticationFilter != null) {
            http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        }

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:8081",
                "http://127.0.0.1:8081",
                "http://localhost:8080",
                "http://127.0.0.1:8080",
                "http://localhost:3000",
                "http://localhost:8000",
                "http://localhost:4200",
                "http://127.0.0.1:3000",
                "http://127.0.0.1:8000",
                "http://127.0.0.1:4200"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}