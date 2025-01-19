package com.employee.service;

import com.employee.security.User;
import com.employee.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDetailsServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("Attempting to load user: {}", username);
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.error("User not found: {}", username);
                    return new UsernameNotFoundException("User not found: " + username);
                });
        
        logger.debug("Found user: {}", user.getUsername());
        logger.debug("User password hash: {}", user.getPassword());
        logger.debug("User authorities: {}", user.getAuthorities());
        
        // Test password matching
        String testPassword = "admin123";
        String encodedTest = passwordEncoder.encode(testPassword);
        logger.debug("Test encode of 'admin123': {}", encodedTest);
        logger.debug("Test password matches stored: {}", 
            passwordEncoder.matches(testPassword, user.getPassword()));
        
        return user;
    }
}
