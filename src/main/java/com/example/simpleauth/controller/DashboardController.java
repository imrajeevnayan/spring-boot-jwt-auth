package com.example.simpleauth.controller;

import com.example.simpleauth.entity.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    @GetMapping("/dashboard")
    public String dashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = (User) userDetails;
        model.addAttribute("fullName", user.getFullName());
        model.addAttribute("email", user.getUsername());
        return "dashboard";
    }
}