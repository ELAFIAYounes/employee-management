package com.employee.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PasswordEncoderTest {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    public void testPasswordEncoding() {
        String rawPassword = "admin123";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        System.out.println("Raw password: " + rawPassword);
        System.out.println("Encoded password: " + encodedPassword);
        
        // Test if the password matches its encoded version
        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
        
        // Test if the password matches the one in data.sql
        String storedHash = "$2a$10$N/qM6VeL9Nj8TT4T8SfPU.hYr4.8QNH3di9ddVD1MuNWUj9WoZmMi";
        System.out.println("Stored hash matches: " + passwordEncoder.matches(rawPassword, storedHash));
    }
}
