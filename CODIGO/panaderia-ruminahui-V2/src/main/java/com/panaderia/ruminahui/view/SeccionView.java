package com.panaderia.ruminahui.view;

import com.panaderia.ruminahui.controller.SeccionController;
import com.panaderia.ruminahui.model.Seccion;
import com.panaderia.ruminahui.model.SeccionRepository;
import com.panaderia.ruminahui.util.SessionManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class SeccionView extends JFrame {
    private JTextField searchField;
    private DefaultTableModel tableModel;
    private JTable seccionTable;
    private SeccionController controller;

    public SeccionView() {
        if (!SessionManager.isLoggedIn()) {
            dispose();
            new LoginView().setVisible(true);
            return;
        }
        controller = new SeccionController(new SeccionRepository());
        initializeUI();
        loadSecciones();
    }

    private void initializeUI() {
        setTitle("Panadería Rumiñahui - Gestión de Secciones");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Custom FlatLaf theme
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(245, 222, 179));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header with search
        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        headerPanel.setBackground(new Color(139, 69, 19));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Search bar
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(new Color(139, 69, 19));
        JLabel searchLabel = new JLabel("Buscar por nombre: ");
        searchLabel.setForeground(Color.WHITE);
        searchLabel.setFont(new Font("Serif", Font.BOLD, 14));
        searchField = new JTextField(20);
        searchField.setFont(new Font("Serif", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 105, 30), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterSecciones(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterSecciones(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterSecciones(); }
        });
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);

        headerPanel.add(searchPanel, BorderLayout.NORTH);

        // Active filters display
        JPanel activeFiltersPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        activeFiltersPanel.setBackground(new Color(245, 222, 179));
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(activeFiltersPanel, BorderLayout.SOUTH);

        // Table
        String[] columns = {"ID", "Nombre", "Descripción"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        seccionTable = new JTable(tableModel);
        seccionTable.setFont(new Font("Serif", Font.PLAIN, 14));
        seccionTable.getTableHeader().setFont(new Font("Serif", Font.BOLD, 14));
        seccionTable.setRowHeight(30);
        seccionTable.setSelectionBackground(new Color(210, 105, 30));
        seccionTable.setSelectionForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(seccionTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(139, 69, 19), 1));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(new Color(245, 222, 179));
        JButton addButton = createStyledButton("Agregar");
        JButton editButton = createStyledButton("Editar");
        JButton deleteButton = createStyledButton("Eliminar");
        JButton backButton = createStyledButton("Volver");
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel, BorderLayout.CENTER);

        // Responsive design
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent e) {
                adjustLayoutForScreenSize();
            }
        });

        // Action listeners
        addButton.addActionListener(e -> showAddEditDialog(null));
        editButton.addActionListener(e -> {
            int selectedRow = seccionTable.getSelectedRow();
            if (selectedRow >= 0) {
                String id = (String) tableModel.getValueAt(selectedRow, 0);
                Seccion seccion = controller.getSeccionById(id);
                showAddEditDialog(seccion);
            } else {
                showCustomDialog("Seleccione una sección para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        });
        deleteButton.addActionListener(e -> {
            int selectedRow = seccionTable.getSelectedRow();
            if (selectedRow >= 0) {
                String id = (String) tableModel.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(this,
                        "¿Está seguro de que desea eliminar esta sección?",
                        "Confirmar Eliminación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    String result = controller.deleteSeccion(id);
                    if (result == null) {
                        showCustomDialog("Sección eliminada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        loadSecciones();
                    } else {
                        showCustomDialog(result, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                showCustomDialog("Seleccione una sección para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        });
        backButton.addActionListener(e -> {
            dispose();
            new MainView().setVisible(true);
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

    private void showCustomDialog(String message, String title, int messageType) {
        JOptionPane optionPane = new JOptionPane(
                message,
                messageType,
                JOptionPane.DEFAULT_OPTION
        );
        JDialog dialog = optionPane.createDialog(this, title);
        dialog.getContentPane().setBackground(new Color(245, 222, 179));
        dialog.setModal(true);
        dialog.setVisible(true);
    }

    private void showAddEditDialog(Seccion seccion) {
        JDialog dialog = new JDialog(this, seccion == null ? "Agregar Sección" : "Editar Sección", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        dialog.getContentPane().setBackground(new Color(245, 222, 179));

        JTextField nombreField = new JTextField(15);
        JTextArea descripcionField = new JTextArea(3, 15);
        descripcionField.setLineWrap(true);
        descripcionField.setWrapStyleWord(true);
        if (seccion != null) {
            nombreField.setText(seccion.getNombre());
            descripcionField.setText(seccion.getDescripcion());
        }

        gbc.gridx = 0;
        gbc.gridy = 0;
        dialog.add(new JLabel("Nombre:"), gbc);
        gbc.gridx = 1;
        dialog.add(nombreField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        dialog.add(new JLabel("Descripción:"), gbc);
        gbc.gridx = 1;
        dialog.add(new JScrollPane(descripcionField), gbc);

        JButton saveButton = createStyledButton("Guardar");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        dialog.add(saveButton, gbc);

        saveButton.addActionListener(e -> {
            String nombre = nombreField.getText();
            String descripcion = descripcionField.getText();
            Seccion newSeccion = new Seccion(seccion != null ? seccion.getId() : null, nombre, descripcion);
            String result = seccion != null ? controller.updateSeccion(newSeccion) : controller.createSeccion(newSeccion);
            if (result == null) {
                showCustomDialog(seccion != null ? "Sección actualizada con éxito." : "Sección creada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                loadSecciones();
                dialog.dispose();
            } else {
                showCustomDialog(result, "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void loadSecciones() {
        tableModel.setRowCount(0);
        List<Seccion> secciones = controller.getAllSecciones();
        for (Seccion seccion : secciones) {
            tableModel.addRow(new Object[]{seccion.getId(), seccion.getNombre(), seccion.getDescripcion()});
        }
    }

    private void filterSecciones() {
        String searchText = searchField.getText().toLowerCase();
        List<Seccion> secciones = controller.getAllSecciones();
        List<Seccion> filtered = secciones.stream()
                .filter(s -> s.getNombre().toLowerCase().contains(searchText))
                .collect(Collectors.toList());
        tableModel.setRowCount(0);
        for (Seccion seccion : filtered) {
            tableModel.addRow(new Object[]{seccion.getId(), seccion.getNombre(), seccion.getDescripcion()});
        }
    }

    private void adjustLayoutForScreenSize() {
        int width = getWidth();
        if (width < 600) {
            seccionTable.setFont(new Font("Serif", Font.PLAIN, 12));
            seccionTable.getTableHeader().setFont(new Font("Serif", Font.BOLD, 12));
            seccionTable.setRowHeight(25);
        } else {
            seccionTable.setFont(new Font("Serif", Font.PLAIN, 14));
            seccionTable.getTableHeader().setFont(new Font("Serif", Font.BOLD, 14));
            seccionTable.setRowHeight(30);
        }
    }
}