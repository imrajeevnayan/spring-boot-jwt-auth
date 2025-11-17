package com.example.simpleauth.controller;

import com.example.simpleauth.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class VerifyController {
    private final UserService userService;

    public VerifyController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/verify")
    public String verify(@RequestParam("token") String token) {
        try {
            userService.verifyUser(token);
            return "redirect:/verify-success";
        } catch (Exception e) {
            return "redirect:/login?error=invalidToken";
        }
    }
}