package com.animalphidia.My_backend.service;

import com.animalphidia.My_backend.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String fromEmail;

    @Value("${app.mail.from-name:Animalphidia}")
    private String fromName;

    @Value("${app.base-url:http://localhost:8081}")
    private String baseUrl;

    // This runs once at startup to check email config
    @PostConstruct
    public void init() {
        log.info("📧 Email Service Initialized");
        log.info("📧 Using email: {}", fromEmail);
        log.info("📧 Base URL: {}", baseUrl);
    }

    @Async
    public void sendVerificationEmail(User user) {
        try {
            String verificationLink = baseUrl + "/api/auth/verify-email?token=" + user.getVerificationToken();

            String subject = "Verify Your Animalphidia Account";
            String htmlContent = buildVerificationEmail(user.getUsername(), verificationLink);

            sendHtmlEmail(user.getEmail(), subject, htmlContent);
            log.info("✅ Verification email sent to: {}", user.getEmail());

        } catch (Exception e) {
            log.error("❌ Error sending verification email to: {}", user.getEmail(), e);
            // Don't throw - just log it so registration still works
        }
    }

    @Async
    public void sendPasswordResetEmail(User user) {
        try {
            String resetLink = baseUrl + "/api/auth/reset-password?token=" + user.getPasswordResetToken();

            String subject = "Reset Your Animalphidia Password";
            String htmlContent = buildPasswordResetEmail(user.getUsername(), resetLink);

            sendHtmlEmail(user.getEmail(), subject, htmlContent);
            log.info("✅ Password reset email sent to: {}", user.getEmail());

        } catch (Exception e) {
            log.error("❌ Error sending password reset email to: {}", user.getEmail(), e);
        }
    }

    private void sendHtmlEmail(String toEmail, String subject, String htmlContent)
            throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(fromEmail, fromName);
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText(htmlContent, true); // true = HTML content

        mailSender.send(message);
    }

    private String buildVerificationEmail(String username, String verificationLink) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                <meta charset="UTF-8">
                <style>
                body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 20px; }
                .container { max-width: 600px; margin: 0 auto; background-color: white; padding: 30px; border-radius: 10px; }
                .header { text-align: center; padding-bottom: 20px; border-bottom: 2px solid #27ae60; }
                .header h1 { color: #27ae60; margin: 0; }
                .content { padding: 20px 0; line-height: 1.6; color: #333; }
                .button { display: inline-block; padding: 12px 24px; background-color: #27ae60; color: white;
                text-decoration: none; border-radius: 5px; font-weight: bold; margin: 15px 0; }
                .footer { margin-top: 30px; padding-top: 20px; border-top: 1px solid #eee;
                text-align: center; color: #777; font-size: 12px; }
                </style>
                </head>
                <body>
                <div class="container">
                <div class="header">
                <h1>Welcome to Animalphidia! 🐾</h1>
                </div>
                <div class="content">
                <p>Hello <strong>%s</strong>,</p>
                <p>Thank you for registering with Animalphidia - The Philippine Animal Encyclopedia.</p>
                <p>Please verify your email address by clicking the button below:</p>

                <div style="text-align: center;">
                <a href="%s" class="button">Verify Email Address</a>
                </div>

                <p>This verification link will expire in 24 hours.</p>
                <p>If you did not create an account, please ignore this email.</p>
                </div>
                <div class="footer">
                <p>© 2024 Animalphidia. All rights reserved.</p>
                <p>Philippine Animal Encyclopedia</p>
                </div>
                </div>
                </body>
                </html>
                """.formatted(username, verificationLink);
    }

    private String buildPasswordResetEmail(String username, String resetLink) {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                <meta charset="UTF-8">
                <style>
                body { font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 20px; }
                .container { max-width: 600px; margin: 0 auto; background-color: white; padding: 30px; border-radius: 10px; }
                .header { text-align: center; padding-bottom: 20px; border-bottom: 2px solid #e74c3c; }
                .header h1 { color: #e74c3c; margin: 0; }
                .content { padding: 20px 0; line-height: 1.6; color: #333; }
                .button { display: inline-block; padding: 12px 24px; background-color: #e74c3c; color: white;
                text-decoration: none; border-radius: 5px; font-weight: bold; margin: 15px 0; }
                .warning { background-color: #fff3cd; border: 1px solid #ffeaa7; padding: 10px; border-radius: 5px; }
                </style>
                </head>
                <body>
                <div class="container">
                <div class="header">
                <h1>Password Reset 🔐</h1>
                </div>
                <div class="content">
                <p>Hello <strong>%s</strong>,</p>
                <p>We received a request to reset your password for your Animalphidia account.</p>
                <p>Click the button below to reset your password:</p>

                <div style="text-align: center;">
                <a href="%s" class="button">Reset Password</a>
                </div>

                <div class="warning">
                <p><strong>Important:</strong> This link will expire in 1 hour.</p>
                <p>If you did not request a password reset, please ignore this email.</p>
                </div>
                </div>
                </div>
                </body>
                </html>
                """.formatted(username, resetLink);
    }
}