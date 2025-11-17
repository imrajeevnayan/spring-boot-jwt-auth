package com.example.simpleauth.controller;

import com.example.simpleauth.entity.User;
import com.example.simpleauth.mail.EmailService;
import com.example.simpleauth.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ForgotPasswordController {
    private final UserService userService;
    private final EmailService emailService;

    @Value("${app.baseUrl:http://localhost:8080}")
    private String baseUrl;

    public ForgotPasswordController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @GetMapping("/forgot-password")
    public String showForgotForm() {
        return "forgot-password";
    }

    @PostMapping("/api/auth/forgot-password")
    @ResponseBody
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotRequest request) {
        try {
            User user = (User) userService.loadUserByUsername(request.getEmail());
            userService.generateResetToken(user);
            emailService.sendResetEmail(user, baseUrl);
            return ResponseEntity.ok("Reset email sent!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Email not found or unverified.");
        }
    }

    public static class ForgotRequest {
        private String email;
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }
}