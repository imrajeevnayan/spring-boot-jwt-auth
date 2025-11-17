package com.example.simpleauth.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "users")
@Data
public class User implements UserDetails, OAuth2User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String roles = "USER";
    private boolean verified = false;
    private String verificationToken;
    private String resetToken;
    private LocalDateTime resetTokenExpiry;

    // OAuth2User fields (transient = not persisted to DB)
    @Transient
    private Map<String, Object> attributes;
    @Transient
    private String nameAttributeKey;

    // Shared getAuthorities() for both UserDetails and OAuth2User
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + roles));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return verified;
    }

    // OAuth2User methods
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return getFullName();
    }

    // Setters for OAuth2User (not in interface, but used in DefaultOAuth2User-like flow)
    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public void setNameAttributeKey(String nameAttributeKey) {
        this.nameAttributeKey = nameAttributeKey;
    }

    // Helper method
    public String getFullName() {
        return firstName + " " + lastName;
    }
}