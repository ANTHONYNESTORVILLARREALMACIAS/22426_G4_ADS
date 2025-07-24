package com.panaderia.ruminahui.view;

import com.panaderia.ruminahui.controller.UsuarioController;
import com.panaderia.ruminahui.model.Usuario;
import com.panaderia.ruminahui.model.UsuarioRepository;

import javax.swing.*;
import java.awt.*;

public class UsuarioView extends JFrame {
    private final UsuarioController usuarioController;
    private JTextField usernameField, nombreField, emailField;
    private JPasswordField passwordField;
    private JButton saveButton, backButton;
    private String userId;

    public UsuarioView() {
        usuarioController = new UsuarioController(new UsuarioRepository());
        initializeUI();
        loadUser();
    }

    private void initializeUI() {
        setTitle("Panadería Rumiñahui - Editar Perfil");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JLabel headerLabel = new JLabel("Editar Perfil", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Serif", Font.BOLD, 24));
        headerLabel.setForeground(new Color(139, 69, 19));
        add(headerLabel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(245, 222, 179));
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
        formPanel.add(new JLabel("Nombre:"), gbc);

        gbc.gridx = 1;
        nombreField = new JTextField(15);
        formPanel.add(nombreField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        emailField = new JTextField(15);
        formPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 222, 179));
        saveButton = new JButton("Guardar");
        backButton = new JButton("Volver");
        saveButton.setBackground(new Color(210, 105, 30));
        backButton.setBackground(new Color(210, 105, 30));
        saveButton.setForeground(Color.WHITE);
        backButton.setForeground(Color.WHITE);
        buttonPanel.add(saveButton);
        buttonPanel.add(backButton);
        formPanel.add(buttonPanel, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Action listeners
        saveButton.addActionListener(e -> handleSave());
        backButton.addActionListener(e -> {
            dispose();
            new MainView().setVisible(true);
        });
    }

    private void loadUser() {
        Usuario usuario = new UsuarioRepository().findByUsername("admin");
        if (usuario != null) {
            userId = usuario.getId();
            usernameField.setText(usuario.getUsername());
            passwordField.setText(usuario.getPassword());
            nombreField.setText(usuario.getNombre());
            emailField.setText(usuario.getEmail());
        }
    }

    private void handleSave() {
        String result = usuarioController.updateUsuario(
                userId,
                usernameField.getText(),
                new String(passwordField.getPassword()),
                nombreField.getText(),
                emailField.getText()
        );
        if (result == null) {
            JOptionPane.showMessageDialog(this, "Perfil actualizado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new MainView().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}