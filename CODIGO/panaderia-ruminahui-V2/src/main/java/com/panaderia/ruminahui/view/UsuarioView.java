package com.panaderia.ruminahui.view;

import com.panaderia.ruminahui.controller.UsuarioController;
import com.panaderia.ruminahui.model.Usuario;
import com.panaderia.ruminahui.model.UsuarioRepository;
import com.panaderia.ruminahui.util.SessionManager;

import javax.swing.*;
import java.awt.*;

public class UsuarioView extends JFrame {
    private JTextField usernameField, nombreField;
    private JPasswordField passwordField;

    public UsuarioView() {
        if (!SessionManager.isLoggedIn()) {
            dispose();
            new LoginView().setVisible(true);
            return;
        }
        initializeUI();
        loadUserData();
    }

    private void initializeUI() {
        setTitle("Panadería Rumiñahui - Editar Perfil");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Main panel with custom background
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(245, 222, 179));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(139, 69, 19));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("Editar Perfil", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

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
        usernameField.setEnabled(false);
        formPanel.add(usernameField, gbc);

        // Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel nameLabel = new JLabel("Nombre:");
        nameLabel.setFont(new Font("Serif", Font.BOLD, 14));
        formPanel.add(nameLabel, gbc);

        gbc.gridx = 1;
        nombreField = new JTextField(15);
        nombreField.setFont(new Font("Serif", Font.PLAIN, 14));
        nombreField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 105, 30), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        formPanel.add(nombreField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 2;
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

        // Buttons
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JButton saveButton = createStyledButton("Guardar Cambios");
        formPanel.add(saveButton, gbc);

        gbc.gridy = 4;
        JButton backButton = createStyledButton("Volver al Menú");
        formPanel.add(backButton, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);

        // Action Listeners
        UsuarioController controller = new UsuarioController(new UsuarioRepository());

        saveButton.addActionListener(e -> {
            String nombre = nombreField.getText();
            String password = new String(passwordField.getPassword());
            String id = SessionManager.getLoggedInUser().getId();

            String result = controller.updateUsuario(id, nombre, password);
            if (result == null) {
                Usuario updatedUser = controller.getUsuarioById(id);
                SessionManager.setLoggedInUser(updatedUser);
                JOptionPane.showMessageDialog(this, "Perfil actualizado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> {
            dispose();
            new MainView().setVisible(true);
        });
    }

    private void loadUserData() {
        Usuario user = SessionManager.getLoggedInUser();
        usernameField.setText(user.getUsername());
        nombreField.setText(user.getNombre());
        passwordField.setText("");
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