package com.panaderia.ruminahui.view;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {
    public MainView() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Panadería Rumiñahui - Panel Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JLabel headerLabel = new JLabel("Panel de Administración", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Serif", Font.BOLD, 24));
        headerLabel.setForeground(new Color(139, 69, 19));
        add(headerLabel, BorderLayout.NORTH);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        buttonPanel.setBackground(new Color(245, 222, 179));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton seccionButton = createButton("Gestionar Secciones");
        JButton materiaPrimaButton = createButton("Gestionar Materias Primas");
        JButton stockButton = createButton("Gestionar Stock");
        JButton recetaButton = createButton("Gestionar Recetas");
        JButton detalleRecetaButton = createButton("Gestionar Detalles de Receta");
        JButton produccionButton = createButton("Gestionar Producciones");
        JButton usuarioButton = createButton("Editar Perfil");
        JButton logoutButton = createButton("Cerrar Sesión");

        buttonPanel.add(seccionButton);
        buttonPanel.add(materiaPrimaButton);
        buttonPanel.add(stockButton);
        buttonPanel.add(recetaButton);
        buttonPanel.add(detalleRecetaButton);
        buttonPanel.add(produccionButton);
        buttonPanel.add(usuarioButton);
        buttonPanel.add(logoutButton);

        add(buttonPanel, BorderLayout.CENTER);

        // Action listeners
        seccionButton.addActionListener(e -> {
            dispose();
            new SeccionView().setVisible(true);
        });
        materiaPrimaButton.addActionListener(e -> {
            dispose();
            new MateriaPrimaView().setVisible(true);
        });
        stockButton.addActionListener(e -> {
            dispose();
            new StockView().setVisible(true);
        });
        recetaButton.addActionListener(e -> {
            dispose();
            new RecetaView().setVisible(true);
        });
        detalleRecetaButton.addActionListener(e -> {
            dispose();
            new DetalleRecetaView().setVisible(true);
        });
        produccionButton.addActionListener(e -> {
            dispose();
            new ProduccionView().setVisible(true);
        });
        usuarioButton.addActionListener(e -> {
            dispose();
            new UsuarioView().setVisible(true);
        });
        logoutButton.addActionListener(e -> {
            dispose();
            new LoginView().setVisible(true);
        });
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(210, 105, 30));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Serif", Font.PLAIN, 16));
        return button;
    }
}