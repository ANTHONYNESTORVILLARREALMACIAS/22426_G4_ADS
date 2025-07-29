package com.panaderia.ruminahui.view;

import com.panaderia.ruminahui.controller.MateriaPrimaController;
import com.panaderia.ruminahui.model.MateriaPrima;
import com.panaderia.ruminahui.model.MateriaPrimaRepository;
import com.panaderia.ruminahui.model.Seccion;
import com.panaderia.ruminahui.model.SeccionRepository;
import com.panaderia.ruminahui.util.SessionManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class MateriaPrimaView extends JFrame {
    private JTextField searchField;
    private JComboBox<String> filterSeccionCombo;
    private DefaultTableModel tableModel;
    private JTable materiaTable;
    private MateriaPrimaController controller;
    private SeccionRepository seccionRepository;

    public MateriaPrimaView() {
        if (!SessionManager.isLoggedIn()) {
            dispose();
            new LoginView().setVisible(true);
            return;
        }
        controller = new MateriaPrimaController(new MateriaPrimaRepository(), new SeccionRepository());
        seccionRepository = new SeccionRepository();
        initializeUI();
        loadSeccionesForFilter();
        loadMateriasPrimas();
    }

    private void initializeUI() {
        setTitle("Panadería Rumiñahui - Gestión de Materias Primas");
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
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterMaterias(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterMaterias(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterMaterias(); }
        });

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        headerPanel.add(searchPanel, BorderLayout.NORTH);

        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.setBackground(new Color(139, 69, 19));

        JLabel filterSeccionLabel = new JLabel("Filtrar por Sección:");
        filterSeccionLabel.setForeground(Color.WHITE);
        filterSeccionLabel.setFont(new Font("Serif", Font.BOLD, 14));

        filterSeccionCombo = new JComboBox<>();
        filterSeccionCombo.addItem("Todas las secciones");
        filterSeccionCombo.setFont(new Font("Serif", Font.PLAIN, 14));
        filterSeccionCombo.addActionListener(e -> filterMaterias());

        filterPanel.add(filterSeccionLabel);
        filterPanel.add(filterSeccionCombo);
        headerPanel.add(filterPanel, BorderLayout.CENTER);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Nombre", "Descripción", "Stock Mínimo", "Unidad", "Sección"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        materiaTable = new JTable(tableModel);
        materiaTable.setFont(new Font("Serif", Font.PLAIN, 14));
        materiaTable.getTableHeader().setFont(new Font("Serif", Font.BOLD, 14));
        materiaTable.setRowHeight(30);
        materiaTable.setSelectionBackground(new Color(210, 105, 30));
        materiaTable.setSelectionForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(materiaTable);
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
            int selectedRow = materiaTable.getSelectedRow();
            if (selectedRow >= 0) {
                String id = (String) tableModel.getValueAt(selectedRow, 0);
                MateriaPrima materia = controller.getAllMateriasPrimas().stream()
                        .filter(m -> m.getId().equals(id))
                        .findFirst()
                        .orElse(null);
                showAddEditDialog(materia);
            } else {
                showCustomDialog("Seleccione una materia prima para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = materiaTable.getSelectedRow();
            if (selectedRow >= 0) {
                String id = (String) tableModel.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(this,
                        "¿Está seguro de que desea eliminar esta materia prima?",
                        "Confirmar Eliminación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    String result = controller.deleteMateriaPrima(id);
                    if (result == null) {
                        showCustomDialog("Materia prima eliminada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        loadMateriasPrimas();
                    } else {
                        showCustomDialog(result, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                showCustomDialog("Seleccione una materia prima para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
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

    private void showAddEditDialog(MateriaPrima materia) {
        JDialog dialog = new JDialog(this, materia == null ? "Agregar Materia Prima" : "Editar Materia Prima", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        dialog.getContentPane().setBackground(new Color(245, 222, 179));

        JTextField nombreField = new JTextField(15);
        JTextArea descripcionField = new JTextArea(3, 15);
        descripcionField.setLineWrap(true);
        descripcionField.setWrapStyleWord(true);
        JTextField stockMinimoField = new JTextField(10);
        JTextField unidadMedidaField = new JTextField(10);
        JComboBox<String> seccionCombo = new JComboBox<>();

        for (Seccion seccion : seccionRepository.findAll()) {
            seccionCombo.addItem(seccion.getNombre());
        }

        if (materia != null) {
            nombreField.setText(materia.getNombre());
            descripcionField.setText(materia.getDescripcion());
            stockMinimoField.setText(String.valueOf(materia.getStockMinimo()));
            unidadMedidaField.setText(materia.getUnidadMedida());
            Seccion seccion = seccionRepository.findById(materia.getIdSeccion());
            seccionCombo.setSelectedItem(seccion != null ? seccion.getNombre() : null);
        }

        // Form layout
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

        gbc.gridx = 0;
        gbc.gridy = 2;
        dialog.add(new JLabel("Stock Mínimo:"), gbc);
        gbc.gridx = 1;
        dialog.add(stockMinimoField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        dialog.add(new JLabel("Unidad de Medida:"), gbc);
        gbc.gridx = 1;
        dialog.add(unidadMedidaField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        dialog.add(new JLabel("Sección:"), gbc);
        gbc.gridx = 1;
        dialog.add(seccionCombo, gbc);

        JButton saveButton = createStyledButton("Guardar");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        dialog.add(saveButton, gbc);

        saveButton.addActionListener(e -> {
            try {
                String nombre = nombreField.getText();
                String descripcion = descripcionField.getText();
                double stockMinimo = Double.parseDouble(stockMinimoField.getText());
                String unidadMedida = unidadMedidaField.getText();
                String idSeccion = seccionRepository.findAll().stream()
                        .filter(s -> s.getNombre().equals(seccionCombo.getSelectedItem()))
                        .findFirst()
                        .map(Seccion::getId)
                        .orElse(null);

                if (materia != null) {
                    String result = controller.updateMateriaPrima(materia.getId(), nombre, descripcion, stockMinimo, unidadMedida, idSeccion);
                    if (result == null) {
                        showCustomDialog("Materia prima actualizada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        loadMateriasPrimas();
                        dialog.dispose();
                    } else {
                        showCustomDialog(result, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    String result = controller.createMateriaPrima(nombre, descripcion, stockMinimo, unidadMedida, idSeccion);
                    if (result == null) {
                        showCustomDialog("Materia prima creada con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        loadMateriasPrimas();
                        dialog.dispose();
                    } else {
                        showCustomDialog(result, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (NumberFormatException ex) {
                showCustomDialog("El stock mínimo debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void loadMateriasPrimas() {
        tableModel.setRowCount(0);
        List<MateriaPrima> materias = controller.getAllMateriasPrimas();
        for (MateriaPrima materia : materias) {
            Seccion seccion = seccionRepository.findById(materia.getIdSeccion());
            tableModel.addRow(new Object[]{
                    materia.getId(),
                    materia.getNombre(),
                    materia.getDescripcion(),
                    materia.getStockMinimo(),
                    materia.getUnidadMedida(),
                    seccion != null ? seccion.getNombre() : materia.getIdSeccion()
            });
        }
    }

    private void loadSeccionesForFilter() {
        filterSeccionCombo.removeAllItems();
        filterSeccionCombo.addItem("Todas las secciones");
        for (Seccion seccion : seccionRepository.findAll()) {
            filterSeccionCombo.addItem(seccion.getNombre());
        }
    }

    private void filterMaterias() {
        String searchText = searchField.getText().toLowerCase();
        String seccionFilter = (String) filterSeccionCombo.getSelectedItem();

        // Verificar si seccionFilter es null y asignar un valor por defecto
        if (seccionFilter == null) {
            seccionFilter = "Todas las secciones";
        }

        List<MateriaPrima> materias = controller.getAllMateriasPrimas();
        String finalSeccionFilter = seccionFilter;
        List<MateriaPrima> filtered = materias.stream()
                .filter(m -> {
                    Seccion seccion = seccionRepository.findById(m.getIdSeccion());

                    // Apply seccion filter
                    boolean seccionMatch = finalSeccionFilter.equals("Todas las secciones") ||
                            (seccion != null && seccion.getNombre().equals(finalSeccionFilter));

                    // Apply search text
                    boolean searchMatch = searchText.isEmpty() ||
                            m.getNombre().toLowerCase().contains(searchText) ||
                            m.getDescripcion().toLowerCase().contains(searchText) ||
                            String.valueOf(m.getStockMinimo()).contains(searchText) ||
                            m.getUnidadMedida().toLowerCase().contains(searchText);

                    return seccionMatch && searchMatch;
                })
                .collect(Collectors.toList());

        tableModel.setRowCount(0);
        for (MateriaPrima materia : filtered) {
            Seccion seccion = seccionRepository.findById(materia.getIdSeccion());
            tableModel.addRow(new Object[]{
                    materia.getId(),
                    materia.getNombre(),
                    materia.getDescripcion(),
                    materia.getStockMinimo(),
                    materia.getUnidadMedida(),
                    seccion != null ? seccion.getNombre() : materia.getIdSeccion()
            });
        }
    }

    private void adjustLayoutForScreenSize() {
        int width = getWidth();
        if (width < 800) {
            materiaTable.setFont(new Font("Serif", Font.PLAIN, 12));
            materiaTable.getTableHeader().setFont(new Font("Serif", Font.BOLD, 12));
            materiaTable.setRowHeight(25);
        } else {
            materiaTable.setFont(new Font("Serif", Font.PLAIN, 14));
            materiaTable.getTableHeader().setFont(new Font("Serif", Font.BOLD, 14));
            materiaTable.setRowHeight(30);
        }
    }
}