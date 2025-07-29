package com.panaderia.ruminahui.view;

import com.panaderia.ruminahui.controller.LoginController;
import com.panaderia.ruminahui.model.UsuarioRepository;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginView() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Panadería Rumiñahui - Iniciar Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Main panel with custom background
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(245, 222, 179));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(139, 69, 19));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel headerLabel = new JLabel("Iniciar Sesión", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Serif", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(245, 222, 179));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel userLabel = new JLabel("Usuario:");
        userLabel.setFont(new Font("Serif", Font.BOLD, 14));
        formPanel.add(userLabel, gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(15);
        usernameField.setFont(new Font("Serif", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 105, 30), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        formPanel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel passLabel = new JLabel("Contraseña:");
        passLabel.setFont(new Font("Serif", Font.BOLD, 14));
        formPanel.add(passLabel, gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Serif", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 105, 30), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        formPanel.add(passwordField, gbc);

        // Login Button
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JButton loginButton = createStyledButton("Iniciar Sesión");
        formPanel.add(loginButton, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);

        // Action Listener
        LoginController loginController = new LoginController(new UsuarioRepository());
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String result = loginController.authenticate(username, password);
            if (result == null) {
                dispose();
                new MainView().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(210, 105, 30));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Serif", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(184, 92, 26));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(210, 105, 30));
            }
        });
        return button;
    }
}