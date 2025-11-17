package com.example.simpleauth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("redirect:/login");
        registry.addViewController("/login");
        registry.addViewController("/register");
        registry.addViewController("/verify-success");
        registry.addViewController("/oauth2-redirect").setViewName("redirect:/dashboard");
    }
}