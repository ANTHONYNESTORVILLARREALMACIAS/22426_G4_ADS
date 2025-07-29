package com.panaderia.ruminahui.view;

import com.panaderia.ruminahui.controller.DetalleRecetaController;
import com.panaderia.ruminahui.model.DetalleReceta;
import com.panaderia.ruminahui.model.DetalleRecetaRepository;
import com.panaderia.ruminahui.model.MateriaPrima;
import com.panaderia.ruminahui.model.MateriaPrimaRepository;
import com.panaderia.ruminahui.model.Receta;
import com.panaderia.ruminahui.model.RecetaRepository;
import com.panaderia.ruminahui.util.SessionManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class DetalleRecetaView extends JFrame {
    private JTextField searchField;
    private JComboBox<String> filterRecetaCombo, filterMateriaCombo;
    private DefaultTableModel tableModel;
    private JTable detalleTable;
    private DetalleRecetaController controller;
    private RecetaRepository recetaRepository;
    private MateriaPrimaRepository materiaPrimaRepository;

    public DetalleRecetaView() {
        if (!SessionManager.isLoggedIn()) {
            dispose();
            new LoginView().setVisible(true);
            return;
        }
        controller = new DetalleRecetaController(new DetalleRecetaRepository(), new RecetaRepository(), new MateriaPrimaRepository());
        recetaRepository = new RecetaRepository();
        materiaPrimaRepository = new MateriaPrimaRepository();
        initializeUI();
        loadRecetasForFilter();
        loadMateriasForFilter();
        loadDetallesReceta();
    }

    private void initializeUI() {
        setTitle("Panadería Rumiñahui - Gestión de Ingredientes de Recetas");
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
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterDetalles(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterDetalles(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterDetalles(); }
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
        filterRecetaCombo.addActionListener(e -> filterDetalles());

        JLabel filterMateriaLabel = new JLabel("Filtrar por Materia Prima:");
        filterMateriaLabel.setForeground(Color.WHITE);
        filterMateriaLabel.setFont(new Font("Serif", Font.BOLD, 14));

        filterMateriaCombo = new JComboBox<>();
        filterMateriaCombo.addItem("Todas las materias");
        filterMateriaCombo.setFont(new Font("Serif", Font.PLAIN, 14));
        filterMateriaCombo.addActionListener(e -> filterDetalles());

        filterPanel.add(filterRecetaLabel);
        filterPanel.add(filterRecetaCombo);
        filterPanel.add(Box.createHorizontalStrut(20));
        filterPanel.add(filterMateriaLabel);
        filterPanel.add(filterMateriaCombo);
        headerPanel.add(filterPanel, BorderLayout.CENTER);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Receta", "Materia Prima", "Cantidad", "Unidad de Medida"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        detalleTable = new JTable(tableModel);
        detalleTable.setFont(new Font("Serif", Font.PLAIN, 14));
        detalleTable.getTableHeader().setFont(new Font("Serif", Font.BOLD, 14));
        detalleTable.setRowHeight(30);
        detalleTable.setSelectionBackground(new Color(210, 105, 30));
        detalleTable.setSelectionForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(detalleTable);
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
            int selectedRow = detalleTable.getSelectedRow();
            if (selectedRow >= 0) {
                String id = (String) tableModel.getValueAt(selectedRow, 0);
                DetalleReceta detalle = controller.getAllDetallesReceta().stream()
                        .filter(d -> d.getId().equals(id))
                        .findFirst()
                        .orElse(null);
                showAddEditDialog(detalle);
            } else {
                showCustomDialog("Seleccione un ingrediente para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = detalleTable.getSelectedRow();
            if (selectedRow >= 0) {
                String id = (String) tableModel.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(this,
                        "¿Está seguro de que desea eliminar este ingrediente de la receta?",
                        "Confirmar Eliminación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    String result = controller.deleteDetalleReceta(id);
                    if (result == null) {
                        showCustomDialog("Ingrediente eliminado de la receta con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        loadDetallesReceta();
                    } else {
                        showCustomDialog(result, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                showCustomDialog("Seleccione un ingrediente para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
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

    private void showAddEditDialog(DetalleReceta detalle) {
        JDialog dialog = new JDialog(this, detalle == null ? "Agregar Ingrediente a Receta" : "Editar Ingrediente de Receta", true);
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

        // Materia prima combo
        JComboBox<String> materiaCombo = new JComboBox<>();
        for (MateriaPrima materia : materiaPrimaRepository.findAll()) {
            materiaCombo.addItem(materia.getNombre());
        }

        JTextField cantidadField = new JTextField(15);
        JTextField unidadMedidaField = new JTextField(15);

        if (detalle != null) {
            Receta receta = recetaRepository.findById(detalle.getIdReceta());
            MateriaPrima materia = materiaPrimaRepository.findById(detalle.getIdMateria());
            recetaCombo.setSelectedItem(receta != null ? receta.getNombre() : null);
            materiaCombo.setSelectedItem(materia != null ? materia.getNombre() : null);
            cantidadField.setText(String.valueOf(detalle.getCantidad()));
            unidadMedidaField.setText(detalle.getUnidadMedida());
        }

        // Form layout
        gbc.gridx = 0;
        gbc.gridy = 0;
        dialog.add(new JLabel("Receta:"), gbc);
        gbc.gridx = 1;
        dialog.add(recetaCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        dialog.add(new JLabel("Materia Prima:"), gbc);
        gbc.gridx = 1;
        dialog.add(materiaCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        dialog.add(new JLabel("Cantidad:"), gbc);
        gbc.gridx = 1;
        dialog.add(cantidadField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        dialog.add(new JLabel("Unidad de Medida:"), gbc);
        gbc.gridx = 1;
        dialog.add(unidadMedidaField, gbc);

        JButton saveButton = createStyledButton("Guardar");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        dialog.add(saveButton, gbc);

        saveButton.addActionListener(e -> {
            try {
                String recetaNombre = (String) recetaCombo.getSelectedItem();
                String materiaNombre = (String) materiaCombo.getSelectedItem();
                double cantidad = Double.parseDouble(cantidadField.getText());
                String unidadMedida = unidadMedidaField.getText();

                String idReceta = recetaRepository.findAll().stream()
                        .filter(r -> r.getNombre().equals(recetaNombre))
                        .findFirst()
                        .map(Receta::getId)
                        .orElse(null);

                String idMateria = materiaPrimaRepository.findAll().stream()
                        .filter(m -> m.getNombre().equals(materiaNombre))
                        .findFirst()
                        .map(MateriaPrima::getId)
                        .orElse(null);

                if (detalle != null) {
                    String result = controller.updateDetalleReceta(detalle.getId(), idReceta, idMateria, cantidad, unidadMedida);
                    if (result == null) {
                        showCustomDialog("Ingrediente actualizado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        loadDetallesReceta();
                        dialog.dispose();
                    } else {
                        showCustomDialog(result, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    String result = controller.createDetalleReceta(idReceta, idMateria, cantidad, unidadMedida);
                    if (result == null) {
                        showCustomDialog("Ingrediente agregado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        loadDetallesReceta();
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

    private void loadDetallesReceta() {
        tableModel.setRowCount(0);
        List<DetalleReceta> detalles = controller.getAllDetallesReceta();
        for (DetalleReceta detalle : detalles) {
            Receta receta = recetaRepository.findById(detalle.getIdReceta());
            MateriaPrima materia = materiaPrimaRepository.findById(detalle.getIdMateria());
            tableModel.addRow(new Object[]{
                    detalle.getId(),
                    receta != null ? receta.getNombre() : detalle.getIdReceta(),
                    materia != null ? materia.getNombre() : detalle.getIdMateria(),
                    detalle.getCantidad(),
                    detalle.getUnidadMedida()
            });
        }
    }

    private void loadRecetasForFilter() {
        filterRecetaCombo.removeAllItems();
        filterRecetaCombo.addItem("Todas las recetas");
        for (Receta receta : recetaRepository.findAll()) {
            filterRecetaCombo.addItem(receta.getNombre());
        }
    }

    private void loadMateriasForFilter() {
        filterMateriaCombo.removeAllItems();
        filterMateriaCombo.addItem("Todas las materias");
        for (MateriaPrima materia : materiaPrimaRepository.findAll()) {
            filterMateriaCombo.addItem(materia.getNombre());
        }
    }


    private void filterDetalles() {
        String searchText = searchField.getText().toLowerCase();
        String recetaFilter = (String) filterRecetaCombo.getSelectedItem();
        String materiaFilter = (String) filterMateriaCombo.getSelectedItem();

        // Si los filtros son null, usar valores por defecto
        if (recetaFilter == null) recetaFilter = "Todas las recetas";
        if (materiaFilter == null) materiaFilter = "Todas las materias";

        List<DetalleReceta> detalles = controller.getAllDetallesReceta();
        String finalRecetaFilter = recetaFilter;
        String finalMateriaFilter = materiaFilter;
        List<DetalleReceta> filtered = detalles.stream()
                .filter(d -> {
                    Receta receta = recetaRepository.findById(d.getIdReceta());
                    MateriaPrima materia = materiaPrimaRepository.findById(d.getIdMateria());

                    // Apply receta filter
                    boolean recetaMatch = finalRecetaFilter.equals("Todas las recetas") ||
                            (receta != null && receta.getNombre().equals(finalRecetaFilter));

                    // Apply materia filter
                    boolean materiaMatch = finalMateriaFilter.equals("Todas las materias") ||
                            (materia != null && materia.getNombre().equals(finalMateriaFilter));

                    // Apply search text
                    boolean searchMatch = searchText.isEmpty() ||
                            (receta != null && receta.getNombre().toLowerCase().contains(searchText)) ||
                            (materia != null && materia.getNombre().toLowerCase().contains(searchText)) ||
                            String.valueOf(d.getCantidad()).contains(searchText) ||
                            d.getUnidadMedida().toLowerCase().contains(searchText);

                    return recetaMatch && materiaMatch && searchMatch;
                })
                .collect(Collectors.toList());

        // Actualizar la tabla
        tableModel.setRowCount(0);
        for (DetalleReceta detalle : filtered) {
            Receta receta = recetaRepository.findById(detalle.getIdReceta());
            MateriaPrima materia = materiaPrimaRepository.findById(detalle.getIdMateria());
            tableModel.addRow(new Object[]{
                    detalle.getId(),
                    receta != null ? receta.getNombre() : detalle.getIdReceta(),
                    materia != null ? materia.getNombre() : detalle.getIdMateria(),
                    detalle.getCantidad(),
                    detalle.getUnidadMedida()
            });
        }
    }

    private void adjustLayoutForScreenSize() {
        int width = getWidth();
        if (width < 800) {
            detalleTable.setFont(new Font("Serif", Font.PLAIN, 12));
            detalleTable.getTableHeader().setFont(new Font("Serif", Font.BOLD, 12));
            detalleTable.setRowHeight(25);
        } else {
            detalleTable.setFont(new Font("Serif", Font.PLAIN, 14));
            detalleTable.getTableHeader().setFont(new Font("Serif", Font.BOLD, 14));
            detalleTable.setRowHeight(30);
        }
    }
}