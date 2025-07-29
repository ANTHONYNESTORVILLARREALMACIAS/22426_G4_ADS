package com.panaderia.ruminahui.view;

import com.panaderia.ruminahui.controller.RecetaController;
import com.panaderia.ruminahui.model.DetalleReceta;
import com.panaderia.ruminahui.model.DetalleRecetaRepository;
import com.panaderia.ruminahui.model.MateriaPrima;
import com.panaderia.ruminahui.model.MateriaPrimaRepository;
import com.panaderia.ruminahui.model.Receta;
import com.panaderia.ruminahui.model.RecetaRepository;
import com.panaderia.ruminahui.util.SessionManager;
import com.panaderia.ruminahui.util.Validator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class RecetaView extends JFrame {
    private JTextField searchField;
    private DefaultTableModel tableModel;
    private JTable recetaTable;
    private RecetaController controller;
    private DetalleRecetaRepository detalleRecetaRepository;
    private MateriaPrimaRepository materiaPrimaRepository;

    public RecetaView() {
        if (!SessionManager.isLoggedIn()) {
            dispose();
            new LoginView().setVisible(true);
            return;
        }
        controller = new RecetaController(new RecetaRepository());
        detalleRecetaRepository = new DetalleRecetaRepository();
        materiaPrimaRepository = new MateriaPrimaRepository();
        initializeUI();
        loadRecetas();
    }

    private void initializeUI() {
        setTitle("Panadería Rumiñahui - Gestión de Recetas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 700);
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
        JLabel searchLabel = new JLabel("Buscar por Nombre: ");
        searchLabel.setForeground(Color.WHITE);
        searchLabel.setFont(new Font("Serif", Font.BOLD, 14));
        searchField = new JTextField(20);
        searchField.setFont(new Font("Serif", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 105, 30), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterRecetas(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterRecetas(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterRecetas(); }
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
        String[] columns = {"ID", "Nombre", "Descripción", "Ingredientes"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        recetaTable = new JTable(tableModel);
        recetaTable.setFont(new Font("Serif", Font.PLAIN, 14));
        recetaTable.getTableHeader().setFont(new Font("Serif", Font.BOLD, 14));
        recetaTable.setRowHeight(30);
        recetaTable.setSelectionBackground(new Color(210, 105, 30));
        recetaTable.setSelectionForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(recetaTable);
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
            int selectedRow = recetaTable.getSelectedRow();
            if (selectedRow >= 0) {
                String id = (String) tableModel.getValueAt(selectedRow, 0);
                Receta receta = controller.getAllRecetas().stream()
                        .filter(r -> r.getId().equals(id))
                        .findFirst()
                        .orElse(null);
                showAddEditDialog(receta);
            } else {
                showCustomDialog("Seleccione una receta para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        });
        deleteButton.addActionListener(e -> {
            int selectedRow = recetaTable.getSelectedRow();
            if (selectedRow >= 0) {
                String id = (String) tableModel.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(this,
                        "¿Está seguro de que desea eliminar esta receta?",
                        "Confirmar Eliminación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    String result = controller.deleteReceta(id);
                    if (result == null) {
                        showCustomDialog("Receta eliminada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        loadRecetas();
                    } else {
                        showCustomDialog(result, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                showCustomDialog("Seleccione una receta para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
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
                JOptionPane.DEFAULT_OPTION,
                null
        );
        JDialog dialog = optionPane.createDialog(this, title);
        dialog.getContentPane().setBackground(new Color(245, 222, 179));
        dialog.setModal(true);
        dialog.setVisible(true);
    }

    private void showAddEditDialog(Receta receta) {
        JDialog dialog = new JDialog(this, receta == null ? "Agregar Receta" : "Editar Receta", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        dialog.getContentPane().setBackground(new Color(245, 222, 179));

        JTextField nombreField = new JTextField(15);
        JTextArea descripcionField = new JTextArea(3, 15);
        descripcionField.setLineWrap(true);
        descripcionField.setWrapStyleWord(true);
        if (receta != null) {
            nombreField.setText(receta.getNombre());
            descripcionField.setText(receta.getDescripcion());
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

            if (!Validator.isValidMateriaPrimaNombre(nombre)) {
                showCustomDialog("El nombre de la receta debe tener al menos 2 caracteres.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (receta != null) {
                String result = controller.updateReceta(receta.getId(), nombre, descripcion);
                if (result == null) {
                    showCustomDialog("Receta actualizada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    loadRecetas();
                    dialog.dispose();
                } else {
                    showCustomDialog(result, "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                String result = controller.createReceta(nombre, descripcion);
                if (result == null) {
                    showCustomDialog("Receta creada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    loadRecetas();
                    dialog.dispose();
                } else {
                    showCustomDialog(result, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void loadRecetas() {
        tableModel.setRowCount(0);
        List<Receta> recetas = controller.getAllRecetas();
        for (Receta receta : recetas) {
            List<DetalleReceta> detalles = detalleRecetaRepository.findByRecetaId(receta.getId());
            String ingredientes = detalles.stream()
                    .map(d -> {
                        MateriaPrima materia = materiaPrimaRepository.findById(d.getIdMateria());
                        return (materia != null ? materia.getNombre() : d.getIdMateria()) + ": " + d.getCantidad() + " " + d.getUnidadMedida();
                    })
                    .collect(Collectors.joining(", "));
            tableModel.addRow(new Object[]{
                    receta.getId(),
                    receta.getNombre(),
                    receta.getDescripcion(),
                    ingredientes
            });
        }
    }

    private void filterRecetas() {
        String searchText = searchField.getText().toLowerCase();
        List<Receta> recetas = controller.getAllRecetas();
        List<Receta> filtered = recetas.stream()
                .filter(r -> r.getNombre().toLowerCase().contains(searchText) ||
                        r.getDescripcion().toLowerCase().contains(searchText))
                .collect(Collectors.toList());

        tableModel.setRowCount(0);
        for (Receta receta : filtered) {
            List<DetalleReceta> detalles = detalleRecetaRepository.findByRecetaId(receta.getId());
            String ingredientes = detalles.stream()
                    .map(d -> {
                        MateriaPrima materia = materiaPrimaRepository.findById(d.getIdMateria());
                        return (materia != null ? materia.getNombre() : d.getIdMateria()) + ": " + d.getCantidad() + " " + d.getUnidadMedida();
                    })
                    .collect(Collectors.joining(", "));
            tableModel.addRow(new Object[]{
                    receta.getId(),
                    receta.getNombre(),
                    receta.getDescripcion(),
                    ingredientes
            });
        }
    }

    private void adjustLayoutForScreenSize() {
        int width = getWidth();
        if (width < 600) {
            recetaTable.setFont(new Font("Serif", Font.PLAIN, 12));
            recetaTable.getTableHeader().setFont(new Font("Serif", Font.BOLD, 12));
            recetaTable.setRowHeight(25);
        } else {
            recetaTable.setFont(new Font("Serif", Font.PLAIN, 14));
            recetaTable.getTableHeader().setFont(new Font("Serif", Font.BOLD, 14));
            recetaTable.setRowHeight(30);
        }
    }
}