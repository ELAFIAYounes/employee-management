package com.employee.ui;

import net.miginfocom.swing.MigLayout;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;

@Component
public class LoginPanel extends JPanel {
    private final AuthenticationManager authenticationManager;
    private LoginListener loginListener;
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JButton loginButton;

    public LoginPanel(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        setLayout(new MigLayout("fillx, wrap 2", "[][grow,fill]", "[][]"));

        // Create components
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");

        // Add components
        add(new JLabel("Username:"));
        add(usernameField);
        add(new JLabel("Password:"));
        add(passwordField);
        add(loginButton, "span 2, align center");

        // Configure login button
        loginButton.addActionListener(e -> performLogin());

        // Add enter key listener to password field
        passwordField.addActionListener(e -> performLogin());
    }

    private void performLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try {
            // Create authentication token
            UsernamePasswordAuthenticationToken authRequest = 
                new UsernamePasswordAuthenticationToken(username, password);

            // Attempt authentication
            Authentication authentication = authenticationManager.authenticate(authRequest);

            // If we get here, authentication was successful
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Notify listener of successful login
            if (loginListener != null) {
                loginListener.onLoginSuccess();
            }

        } catch (AuthenticationException e) {
            JOptionPane.showMessageDialog(this,
                    "Invalid username or password",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
            passwordField.setText("");
        }
    }

    public void setLoginListener(LoginListener listener) {
        this.loginListener = listener;
    }

    public void reset() {
        usernameField.setText("");
        passwordField.setText("");
        usernameField.requestFocusInWindow();
    }
}
