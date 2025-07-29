package com.panaderia.ruminahui.view;

import com.panaderia.ruminahui.util.ReportGenerator;
import com.panaderia.ruminahui.util.ReportGeneratorFunction;
import com.panaderia.ruminahui.util.SessionManager;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {
    public MainView() {
        if (!SessionManager.isLoggedIn()) {
            dispose();
            new LoginView().setVisible(true);
            return;
        }
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Panadería Rumiñahui - Menú Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 800);
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

        // User info
        JLabel userLabel = new JLabel("Usuario: " + SessionManager.getLoggedInUser().getUsername(), SwingConstants.RIGHT);
        userLabel.setFont(new Font("Serif", Font.PLAIN, 14));
        userLabel.setForeground(Color.WHITE);
        headerPanel.add(userLabel, BorderLayout.EAST);

        // Title
        JLabel titleLabel = new JLabel("Panadería Rumiñahui", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        buttonsPanel.setBackground(new Color(245, 222, 179));

        // Management buttons
        buttonsPanel.add(createStyledButton("Gestionar Secciones"));
        buttonsPanel.add(createStyledButton("Gestionar Materias Primas"));
        buttonsPanel.add(createStyledButton("Gestionar Stock"));
        buttonsPanel.add(createStyledButton("Gestionar Recetas"));
        buttonsPanel.add(createStyledButton("Gestionar Detalles Receta"));
        buttonsPanel.add(createStyledButton("Gestionar Producciones"));
        buttonsPanel.add(createStyledButton("Editar Perfil"));

        // Report buttons
        buttonsPanel.add(createStyledButton("Reporte Secciones"));
        buttonsPanel.add(createStyledButton("Reporte Materias Primas"));
        buttonsPanel.add(createStyledButton("Reporte Stock"));
        buttonsPanel.add(createStyledButton("Reporte Recetas"));
        buttonsPanel.add(createStyledButton("Reporte Detalles Receta"));
        buttonsPanel.add(createStyledButton("Reporte Producciones"));

        JScrollPane scrollPane = new JScrollPane(buttonsPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(139, 69, 19), 1));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Logout button
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(245, 222, 179));
        JButton logoutButton = createStyledButton("Cerrar Sesión");
        footerPanel.add(logoutButton);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Action listeners
        // In the MainView.java, replace the action listener assignment section with:

// Get all components and cast them to JButton before adding listeners
        Component[] components = buttonsPanel.getComponents();
        ((JButton) components[0]).addActionListener(e -> { dispose(); new SeccionView().setVisible(true); });
        ((JButton) components[1]).addActionListener(e -> { dispose(); new MateriaPrimaView().setVisible(true); });
        ((JButton) components[2]).addActionListener(e -> { dispose(); new StockView().setVisible(true); });
        ((JButton) components[3]).addActionListener(e -> { dispose(); new RecetaView().setVisible(true); });
        ((JButton) components[4]).addActionListener(e -> { dispose(); new DetalleRecetaView().setVisible(true); });
        ((JButton) components[5]).addActionListener(e -> { dispose(); new ProduccionView().setVisible(true); });
        ((JButton) components[6]).addActionListener(e -> { dispose(); new UsuarioView().setVisible(true); });

        ((JButton) components[7]).addActionListener(e -> handleGenerateReport("seccion_report.pdf", "Secciones", ReportGenerator::generateSeccionReport));
        ((JButton) components[8]).addActionListener(e -> handleGenerateReport("materia_prima_report.pdf", "Materias Primas", ReportGenerator::generateMateriaPrimaReport));
        ((JButton) components[9]).addActionListener(e -> handleGenerateReport("stock_report.pdf", "Stock", ReportGenerator::generateStockReport));
        ((JButton) components[10]).addActionListener(e -> handleGenerateReport("receta_report.pdf", "Recetas", ReportGenerator::generateRecetaReport));
        ((JButton) components[11]).addActionListener(e -> handleGenerateReport("detalle_receta_report.pdf", "Detalles de Receta", ReportGenerator::generateDetalleRecetaReport));
        ((JButton) components[12]).addActionListener(e -> handleGenerateReport("produccion_report.pdf", "Producciones", ReportGenerator::generateProductionReport));
        logoutButton.addActionListener(e -> {
            SessionManager.logout();
            dispose();
            new LoginView().setVisible(true);
        });
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(210, 105, 30));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Serif", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));
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

    private void handleGenerateReport(String filePath, String reportName, ReportGeneratorFunction generator) {
        ReportGenerator reportGenerator = new ReportGenerator();
        try {
            generator.generate(reportGenerator, filePath);
            JOptionPane.showMessageDialog(this, "Reporte de " + reportName + " generado en " + filePath, "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al generar el reporte: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}