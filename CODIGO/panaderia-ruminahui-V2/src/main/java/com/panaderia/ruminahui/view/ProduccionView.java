package com.panaderia.ruminahui.view;

import com.panaderia.ruminahui.controller.ProduccionController;
import com.panaderia.ruminahui.model.Produccion;
import com.panaderia.ruminahui.model.ProduccionRepository;
import com.panaderia.ruminahui.model.Receta;
import com.panaderia.ruminahui.model.RecetaRepository;
import com.panaderia.ruminahui.util.SessionManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class ProduccionView extends JFrame {
    private JTextField searchField;
    private JComboBox<String> filterRecetaCombo;
    private JComboBox<String> filterFechaCombo;
    private DefaultTableModel tableModel;
    private JTable produccionTable;
    private ProduccionController controller;
    private RecetaRepository recetaRepository;

    public ProduccionView() {
        if (!SessionManager.isLoggedIn()) {
            dispose();
            new LoginView().setVisible(true);
            return;
        }
        controller = new ProduccionController(new ProduccionRepository(), new RecetaRepository());
        recetaRepository = new RecetaRepository();
        initializeUI();
        loadRecetasForFilter();
        loadProducciones();
    }

    private void initializeUI() {
        setTitle("Panadería Rumiñahui - Gestión de Producción");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Main panel with custom background
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(245, 222, 179));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Header panel with search and filters
        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        headerPanel.setBackground(new Color(139, 69, 19));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setBackground(new Color(139, 69, 19));

        JLabel searchLabel = new JLabel("Buscar:");
        searchLabel.setForeground(Color.WHITE);
        searchLabel.setFont(new Font("Serif", Font.BOLD, 14));

        searchField = new JTextField(20);
        searchField.setFont(new Font("Serif", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 105, 30), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterProducciones(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterProducciones(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterProducciones(); }
        });

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        headerPanel.add(searchPanel, BorderLayout.NORTH);

        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.setBackground(new Color(139, 69, 19));

        JLabel filterRecetaLabel = new JLabel("Filtrar por Receta:");
        filterRecetaLabel.setForeground(Color.WHITE);
        filterRecetaLabel.setFont(new Font("Serif", Font.BOLD, 14));

        filterRecetaCombo = new JComboBox<>();
        filterRecetaCombo.addItem("Todas las recetas");
        filterRecetaCombo.setFont(new Font("Serif", Font.PLAIN, 14));
        filterRecetaCombo.addActionListener(e -> filterProducciones());

        JLabel filterFechaLabe = new JLabel("Filtrar por Fecha:");
        filterFechaLabe.setForeground(Color.WHITE);
        filterFechaLabe.setFont(new Font("Serif", Font.BOLD, 14));

        filterFechaCombo = new JComboBox<>();
        filterFechaCombo.addItem("Todas las fechas");
        filterFechaCombo.setFont(new Font("Serif", Font.PLAIN, 14));
        filterFechaCombo.addActionListener(e -> filterProducciones());

        filterPanel.add(filterRecetaLabel);
        filterPanel.add(filterRecetaCombo);
        filterPanel.add(Box.createHorizontalStrut(20));
        filterPanel.add(filterFechaLabe);
        filterPanel.add(filterFechaCombo);
        headerPanel.add(filterPanel, BorderLayout.CENTER);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Receta", "Cantidad", "Fecha"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        produccionTable = new JTable(tableModel);
        produccionTable.setFont(new Font("Serif", Font.PLAIN, 14));
        produccionTable.getTableHeader().setFont(new Font("Serif", Font.BOLD, 14));
        produccionTable.setRowHeight(30);
        produccionTable.setSelectionBackground(new Color(210, 105, 30));
        produccionTable.setSelectionForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(produccionTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(139, 69, 19), 1));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Button panel
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

        // Action listeners
        addButton.addActionListener(e -> showAddEditDialog(null));
        editButton.addActionListener(e -> {
            int selectedRow = produccionTable.getSelectedRow();
            if (selectedRow >= 0) {
                String id = (String) tableModel.getValueAt(selectedRow, 0);
                Produccion produccion = controller.getAllProducciones().stream()
                        .filter(p -> p.getId().equals(id))
                        .findFirst()
                        .orElse(null);
                showAddEditDialog(produccion);
            } else {
                showCustomDialog("Seleccione una producción para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = produccionTable.getSelectedRow();
            if (selectedRow >= 0) {
                String id = (String) tableModel.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(this,
                        "¿Está seguro de que desea eliminar esta producción?",
                        "Confirmar Eliminación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    String result = controller.deleteProduccion(id);
                    if (result == null) {
                        showCustomDialog("Producción eliminada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        loadProducciones();
                    } else {
                        showCustomDialog(result, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                showCustomDialog("Seleccione una producción para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        });

        backButton.addActionListener(e -> {
            dispose();
            new MainView().setVisible(true);
        });

        // Responsive design
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent e) {
                adjustLayoutForScreenSize();
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

    private void showAddEditDialog(Produccion produccion) {
        JDialog dialog = new JDialog(this, produccion == null ? "Agregar Producción" : "Editar Producción", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        dialog.getContentPane().setBackground(new Color(245, 222, 179));

        // Receta combo
        JComboBox<String> recetaCombo = new JComboBox<>();
        for (Receta receta : recetaRepository.findAll()) {
            recetaCombo.addItem(receta.getNombre());
        }

        JTextField cantidadField = new JTextField(15);
        JTextField fechaField = new JTextField(15);

        if (produccion != null) {
            Receta receta = recetaRepository.findById(produccion.getIdReceta());
            recetaCombo.setSelectedItem(receta != null ? receta.getNombre() : null);
            cantidadField.setText(String.valueOf(produccion.getCantidad()));
            fechaField.setText(produccion.getFecha());
        }

        // Form layout
        gbc.gridx = 0;
        gbc.gridy = 0;
        dialog.add(new JLabel("Receta:"), gbc);
        gbc.gridx = 1;
        dialog.add(recetaCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        dialog.add(new JLabel("Cantidad:"), gbc);
        gbc.gridx = 1;
        dialog.add(cantidadField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        dialog.add(new JLabel("Fecha (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        dialog.add(fechaField, gbc);

        JButton saveButton = createStyledButton("Guardar");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        dialog.add(saveButton, gbc);

        saveButton.addActionListener(e -> {
            try {
                String recetaNombre = (String) recetaCombo.getSelectedItem();
                double cantidad = Double.parseDouble(cantidadField.getText());
                String fecha = fechaField.getText();

                String idReceta = recetaRepository.findAll().stream()
                        .filter(r -> r.getNombre().equals(recetaNombre))
                        .findFirst()
                        .map(Receta::getId)
                        .orElse(null);

                if (produccion != null) {
                    String result = controller.updateProduccion(produccion.getId(), idReceta, cantidad, fecha);
                    if (result == null) {
                        showCustomDialog("Producción actualizada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        loadProducciones();
                        dialog.dispose();
                    } else {
                        showCustomDialog(result, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    String result = controller.createProduccion(idReceta, cantidad, fecha);
                    if (result == null) {
                        showCustomDialog("Producción creada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        loadProducciones();
                        dialog.dispose();
                    } else {
                        showCustomDialog(result, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (NumberFormatException ex) {
                showCustomDialog("La cantidad debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void loadProducciones() {
        tableModel.setRowCount(0);
        List<Produccion> producciones = controller.getAllProducciones();
        for (Produccion produccion : producciones) {
            Receta receta = recetaRepository.findById(produccion.getIdReceta());
            tableModel.addRow(new Object[]{
                    produccion.getId(),
                    receta != null ? receta.getNombre() : produccion.getIdReceta(),
                    produccion.getCantidad(),
                    produccion.getFecha()
            });
        }
        // Update fecha filter
        updateFechaFilter();
    }

    private void loadRecetasForFilter() {
        filterRecetaCombo.removeAllItems();
        filterRecetaCombo.addItem("Todas las recetas");
        for (Receta receta : recetaRepository.findAll()) {
            filterRecetaCombo.addItem(receta.getNombre());
        }
    }

    private void updateFechaFilter() {
        String currentSelection = (String) filterFechaCombo.getSelectedItem();
        filterFechaCombo.removeAllItems();
        filterFechaCombo.addItem("Todas las fechas");

        List<String> fechasUnicas = controller.getAllProducciones().stream()
                .map(Produccion::getFecha)
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        for (String fecha : fechasUnicas) {
            filterFechaCombo.addItem(fecha);
        }

        if (currentSelection != null && fechasUnicas.contains(currentSelection)) {
            filterFechaCombo.setSelectedItem(currentSelection);
        }
    }

    private void filterProducciones() {
        String searchText = searchField.getText().toLowerCase();
        String recetaFilter = (String) filterRecetaCombo.getSelectedItem();
        String fechaFilter = (String) filterFechaCombo.getSelectedItem();

        // Verificación de null para evitar NullPointerException
        if (recetaFilter == null || fechaFilter == null) {
            return;
        }

        List<Produccion> producciones = controller.getAllProducciones();
        List<Produccion> filtered = producciones.stream()
                .filter(p -> {
                    Receta receta = recetaRepository.findById(p.getIdReceta());

                    // Apply receta filter
                    boolean recetaMatch = recetaFilter.equals("Todas las recetas") ||
                            (receta != null && receta.getNombre().equals(recetaFilter));

                    // Apply fecha filter
                    boolean fechaMatch = fechaFilter.equals("Todas las fechas") ||
                            p.getFecha().equals(fechaFilter);

                    // Apply search text
                    boolean searchMatch = searchText.isEmpty() ||
                            (receta != null && receta.getNombre().toLowerCase().contains(searchText)) ||
                            String.valueOf(p.getCantidad()).contains(searchText) ||
                            p.getFecha().toLowerCase().contains(searchText);

                    return recetaMatch && fechaMatch && searchMatch;
                })
                .collect(Collectors.toList());

        tableModel.setRowCount(0);
        for (Produccion produccion : filtered) {
            Receta receta = recetaRepository.findById(produccion.getIdReceta());
            tableModel.addRow(new Object[]{
                    produccion.getId(),
                    receta != null ? receta.getNombre() : produccion.getIdReceta(),
                    produccion.getCantidad(),
                    produccion.getFecha()
            });
        }
    }

    private void adjustLayoutForScreenSize() {
        int width = getWidth();
        if (width < 800) {
            produccionTable.setFont(new Font("Serif", Font.PLAIN, 12));
            produccionTable.getTableHeader().setFont(new Font("Serif", Font.BOLD, 12));
            produccionTable.setRowHeight(25);
        } else {
            produccionTable.setFont(new Font("Serif", Font.PLAIN, 14));
            produccionTable.getTableHeader().setFont(new Font("Serif", Font.BOLD, 14));
            produccionTable.setRowHeight(30);
        }
    }
}