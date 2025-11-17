package com.example.simpleauth.controller;

import com.example.simpleauth.entity.User;
import com.example.simpleauth.mail.EmailService;
import com.example.simpleauth.service.UserService;
import com.example.simpleauth.util.JwtUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AuthController {
    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    @Value("${app.baseUrl:http://localhost:8080}")
    private String baseUrl;

    public AuthController(UserService userService, JwtUtils jwtUtils, AuthenticationManager authenticationManager, EmailService emailService) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/api/auth/register")
    @ResponseBody
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userService.loadUserByUsername(user.getEmail()) != null) {
            return ResponseEntity.badRequest().body("User exists");
        }
        user.setVerified(false);
        userService.save(user);
        userService.generateVerificationToken(user);
        emailService.sendVerificationEmail(user, baseUrl);
        return ResponseEntity.ok("Registered! Check email to verify.");
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/api/auth/login")
    @ResponseBody
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
            String jwt = jwtUtils.generateJwtToken(authentication);
            return ResponseEntity.ok(new JwtResponse(jwt));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    public static class LoginRequest {
        private String email;
        private String password;
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class JwtResponse {
        private String token;
        public JwtResponse(String token) { this.token = token; }
        public String getToken() { return token; }
    }
}