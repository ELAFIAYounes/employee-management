package com.employee.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeneratePassword {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "admin123";
        String encodedPassword = encoder.encode(password);
        System.out.println("\nPassword: " + password);
        System.out.println("Encoded Password: " + encodedPassword);
        System.out.println("Password Matches: " + encoder.matches(password, encodedPassword));
    }
}
