package com.employee.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeneratePasswordHash {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10); // Using strength 10
        String password = "admin123";
        
        // Generate 5 different hashes and verify each one
        for (int i = 0; i < 5; i++) {
            String hash = encoder.encode(password);
            boolean matches = encoder.matches(password, hash);
            System.out.println("\nTest " + (i + 1));
            System.out.println("Generated hash: " + hash);
            System.out.println("Verified matches: " + matches);
            
            // Also test against our stored hash
            String storedHash = "$2a$10$WkrRJhh/vDtMwz1ZZy.UeuK6LnV3w3Zz6B5p.vlYAhQ9B1nXQkgXm";
            boolean storedMatches = encoder.matches(password, storedHash);
            System.out.println("Stored hash matches: " + storedMatches);
        }
    }
}
