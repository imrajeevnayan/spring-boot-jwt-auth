package com.example.simpleauth.mail;

import com.example.simpleauth.entity.User;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(User user, String appUrl) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Verify Your Email");
        message.setText("Click to verify: " + appUrl + "/verify?token=" + user.getVerificationToken());
        mailSender.send(message);
    }

    public void sendResetEmail(User user, String appUrl) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Password Reset");
        message.setText("Click to reset: " + appUrl + "/reset-password?token=" + user.getResetToken());
        mailSender.send(message);
    }
}