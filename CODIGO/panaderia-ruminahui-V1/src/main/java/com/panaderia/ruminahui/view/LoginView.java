package com.panaderia.ruminahui.view;

import com.panaderia.ruminahui.controller.AuthController;
import com.panaderia.ruminahui.model.UsuarioRepository;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private final AuthController authController;

    public LoginView() {
        authController = new AuthController(new UsuarioRepository());
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Panadería Rumiñahui - Iniciar Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Bakery-themed header
        JLabel headerLabel = new JLabel("Panadería Rumiñahui", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Serif", Font.BOLD, 24));
        headerLabel.setForeground(new Color(139, 69, 19)); // Brown
        add(headerLabel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(245, 222, 179)); // Wheat
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Usuario:"), gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(15);
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Contraseña:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        formPanel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        loginButton = new JButton("Iniciar Sesión");
        loginButton.setBackground(new Color(210, 105, 30)); // Chocolate
        loginButton.setForeground(Color.WHITE);
        formPanel.add(loginButton, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Action listeners
        loginButton.addActionListener(e -> handleLogin());
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String result = authController.login(username, password);
        if (result == null) {
            dispose();
            new MainView().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}