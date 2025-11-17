package com.example.simpleauth.service;

import com.example.simpleauth.entity.User;
import com.example.simpleauth.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;  // Autowired now (from PasswordConfig)

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
        if (!user.isVerified()) {
            throw new UsernameNotFoundException("Email not verified. Please check your inbox.");
        }
        return user;
    }

    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public void generateVerificationToken(User user) {
        user.setVerificationToken(UUID.randomUUID().toString());
        userRepository.save(user);
    }

    public void verifyUser(String token) {
        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid verification token"));
        user.setVerified(true);
        user.setVerificationToken(null);
        userRepository.save(user);
    }

    public void generateResetToken(User user) {
        user.setResetToken(UUID.randomUUID().toString());
        user.setResetTokenExpiry(LocalDateTime.now().plusHours(1));
        userRepository.save(user);
    }

    public boolean isResetTokenValid(String token) {
        User user = userRepository.findByResetToken(token).orElse(null);
        return user != null && user.getResetTokenExpiry().isAfter(LocalDateTime.now()) && user.isVerified();
    }

    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid reset token"));
        if (!isResetTokenValid(token)) {
            throw new RuntimeException("Token expired or invalid");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);
    }

    public User saveOrUpdateFromOAuth(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = (String) attributes.get("email");
        if (email == null) {
            email = oAuth2User.getAttribute("login") + "@github.com";
        }
        User user = userRepository.findByEmail(email).orElse(new User());
        if (user.getId() == null) {
            user.setEmail(email);
            user.setFirstName((String) attributes.get("given_name"));
            user.setLastName((String) attributes.get("family_name"));
            user.setRoles("USER");
            user.setVerified(true);
            user.setAttributes(attributes);
            user.setNameAttributeKey("email");
        }
        user.setPassword("");  // No password for OAuth
        return userRepository.save(user);
    }
}