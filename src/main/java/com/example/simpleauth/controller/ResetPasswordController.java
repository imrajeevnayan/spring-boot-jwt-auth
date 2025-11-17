package com.example.simpleauth.controller;

import com.example.simpleauth.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ResetPasswordController {
    private final UserService userService;

    public ResetPasswordController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/reset-password")
    public String showResetForm(@RequestParam("token") String token) {
        if (!userService.isResetTokenValid(token)) {
            return "redirect:/login?error=invalidReset";
        }
        return "reset-password";
    }

    @PostMapping("/api/auth/reset-password")
    public String resetPassword(@RequestParam("token") String token, ResetRequest request) {
        try {
            userService.resetPassword(token, request.getNewPassword());
            return "redirect:/login?success=reset";
        } catch (Exception e) {
            return "redirect:/reset-password?token=" + token + "&error=invalid";
        }
    }

    public static class ResetRequest {
        private String newPassword;
        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    }
}