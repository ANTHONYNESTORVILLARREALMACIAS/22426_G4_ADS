package com.panaderia.ruminahui.view;

import com.panaderia.ruminahui.controller.StockController;
import com.panaderia.ruminahui.model.MateriaPrima;
import com.panaderia.ruminahui.model.MateriaPrimaRepository;
import com.panaderia.ruminahui.model.Seccion;
import com.panaderia.ruminahui.model.SeccionRepository;
import com.panaderia.ruminahui.model.Stock;
import com.panaderia.ruminahui.model.StockRepository;
import com.panaderia.ruminahui.util.SessionManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class StockView extends JFrame {
    private JTextField searchField;
    private JComboBox<String> filterSeccionCombo, filterFechaCombo;
    private DefaultTableModel tableModel;
    private JTable stockTable;
    private StockController controller;
    private MateriaPrimaRepository materiaPrimaRepository;
    private SeccionRepository seccionRepository;

    public StockView() {
        if (!SessionManager.isLoggedIn()) {
            dispose();
            new LoginView().setVisible(true);
            return;
        }
        controller = new StockController(new StockRepository(), new MateriaPrimaRepository());
        materiaPrimaRepository = new MateriaPrimaRepository();
        seccionRepository = new SeccionRepository();
        initializeUI();
        loadSeccionesForFilter();
        loadStocks();
    }

    private void initializeUI() {
        setTitle("Panadería Rumiñahui - Gestión de Stock");
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
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterStocks(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterStocks(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterStocks(); }
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
        filterSeccionCombo.addActionListener(e -> filterStocks());

        JLabel filterFechaLabe = new JLabel("Filtrar por Fecha:");
        filterFechaLabe.setForeground(Color.WHITE);
        filterFechaLabe.setFont(new Font("Serif", Font.BOLD, 14));

        filterFechaCombo = new JComboBox<>();
        filterFechaCombo.addItem("Todas las fechas");
        filterFechaCombo.setFont(new Font("Serif", Font.PLAIN, 14));
        filterFechaCombo.addActionListener(e -> filterStocks());

        filterPanel.add(filterSeccionLabel);
        filterPanel.add(filterSeccionCombo);
        filterPanel.add(Box.createHorizontalStrut(20));
        filterPanel.add(filterFechaLabe);
        filterPanel.add(filterFechaCombo);
        headerPanel.add(filterPanel, BorderLayout.CENTER);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Materia Prima", "Cantidad", "Unidad", "Fecha", "Sección"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        stockTable = new JTable(tableModel);
        stockTable.setFont(new Font("Serif", Font.PLAIN, 14));
        stockTable.getTableHeader().setFont(new Font("Serif", Font.BOLD, 14));
        stockTable.setRowHeight(30);
        stockTable.setSelectionBackground(new Color(210, 105, 30));
        stockTable.setSelectionForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(stockTable);
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
            int selectedRow = stockTable.getSelectedRow();
            if (selectedRow >= 0) {
                String id = (String) tableModel.getValueAt(selectedRow, 0);
                Stock stock = controller.getAllStocks().stream()
                        .filter(s -> s.getId().equals(id))
                        .findFirst()
                        .orElse(null);
                showAddEditDialog(stock);
            } else {
                showCustomDialog("Seleccione un stock para editar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = stockTable.getSelectedRow();
            if (selectedRow >= 0) {
                String id = (String) tableModel.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(this,
                        "¿Está seguro de que desea eliminar este registro de stock?",
                        "Confirmar Eliminación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    String result = controller.deleteStock(id);
                    if (result == null) {
                        showCustomDialog("Stock eliminado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        loadStocks();
                    } else {
                        showCustomDialog(result, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                showCustomDialog("Seleccione un stock para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
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

    private void showAddEditDialog(Stock stock) {
        JDialog dialog = new JDialog(this, stock == null ? "Agregar Stock" : "Editar Stock", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        dialog.getContentPane().setBackground(new Color(245, 222, 179));

        // Materia prima combo
        JComboBox<String> materiaCombo = new JComboBox<>();
        for (MateriaPrima materia : materiaPrimaRepository.findAll()) {
            materiaCombo.addItem(materia.getNombre());
        }

        JTextField cantidadField = new JTextField(15);
        JTextField unidadMedidaField = new JTextField(15);
        JTextField fechaField = new JTextField(15);

        if (stock != null) {
            MateriaPrima materia = materiaPrimaRepository.findById(stock.getIdMateria());
            materiaCombo.setSelectedItem(materia != null ? materia.getNombre() : null);
            cantidadField.setText(String.valueOf(stock.getCantidad()));
            unidadMedidaField.setText(stock.getUnidadMedida());
            fechaField.setText(stock.getFecha());
        }

        // Form layout
        gbc.gridx = 0;
        gbc.gridy = 0;
        dialog.add(new JLabel("Materia Prima:"), gbc);
        gbc.gridx = 1;
        dialog.add(materiaCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        dialog.add(new JLabel("Cantidad:"), gbc);
        gbc.gridx = 1;
        dialog.add(cantidadField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        dialog.add(new JLabel("Unidad de Medida:"), gbc);
        gbc.gridx = 1;
        dialog.add(unidadMedidaField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        dialog.add(new JLabel("Fecha (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        dialog.add(fechaField, gbc);

        JButton saveButton = createStyledButton("Guardar");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        dialog.add(saveButton, gbc);

        saveButton.addActionListener(e -> {
            try {
                String materiaNombre = (String) materiaCombo.getSelectedItem();
                double cantidad = Double.parseDouble(cantidadField.getText());
                String unidadMedida = unidadMedidaField.getText();
                String fecha = fechaField.getText();

                String idMateria = materiaPrimaRepository.findAll().stream()
                        .filter(m -> m.getNombre().equals(materiaNombre))
                        .findFirst()
                        .map(MateriaPrima::getId)
                        .orElse(null);

                if (stock != null) {
                    String result = controller.updateStock(stock.getId(), idMateria, cantidad, unidadMedida, fecha);
                    if (result == null) {
                        showCustomDialog("Stock actualizado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        loadStocks();
                        dialog.dispose();
                    } else {
                        showCustomDialog(result, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    String result = controller.createStock(idMateria, cantidad, unidadMedida, fecha);
                    if (result == null) {
                        showCustomDialog("Stock creado con éxito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        loadStocks();
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

    private void loadStocks() {
        tableModel.setRowCount(0);
        List<Stock> stocks = controller.getAllStocks();
        for (Stock stock : stocks) {
            MateriaPrima materia = materiaPrimaRepository.findById(stock.getIdMateria());
            Seccion seccion = materia != null ? seccionRepository.findById(materia.getIdSeccion()) : null;
            tableModel.addRow(new Object[]{
                    stock.getId(),
                    materia != null ? materia.getNombre() : stock.getIdMateria(),
                    stock.getCantidad(),
                    stock.getUnidadMedida(),
                    stock.getFecha(),
                    seccion != null ? seccion.getNombre() : (materia != null ? materia.getIdSeccion() : "")
            });
        }
        // Update fecha filter
        updateFechaFilter();
    }

    private void loadSeccionesForFilter() {
        filterSeccionCombo.removeAllItems();
        filterSeccionCombo.addItem("Todas las secciones");
        for (Seccion seccion : seccionRepository.findAll()) {
            filterSeccionCombo.addItem(seccion.getNombre());
        }
    }

    private void updateFechaFilter() {
        String currentSelection = (String) filterFechaCombo.getSelectedItem();
        filterFechaCombo.removeAllItems();
        filterFechaCombo.addItem("Todas las fechas");

        List<String> fechasUnicas = controller.getAllStocks().stream()
                .map(Stock::getFecha)
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

    private void filterStocks() {
        String searchText = searchField.getText().toLowerCase();
        String seccionFilter = filterSeccionCombo.getSelectedItem() != null ?
                (String) filterSeccionCombo.getSelectedItem() : "Todas las secciones";
        String fechaFilter = filterFechaCombo.getSelectedItem() != null ?
                (String) filterFechaCombo.getSelectedItem() : "Todas las fechas";


        List<Stock> stocks = controller.getAllStocks();
        List<Stock> filtered = stocks.stream()
                .filter(s -> {
                    MateriaPrima materia = materiaPrimaRepository.findById(s.getIdMateria());
                    Seccion seccion = materia != null ? seccionRepository.findById(materia.getIdSeccion()) : null;

                    // Apply seccion filter
                    boolean seccionMatch = seccionFilter.equals("Todas las secciones") ||
                            (seccion != null && seccion.getNombre().equals(seccionFilter));

                    // Apply fecha filter
                    boolean fechaMatch = fechaFilter.equals("Todas las fechas") ||
                            s.getFecha().equals(fechaFilter);

                    // Apply search text
                    boolean searchMatch = searchText.isEmpty() ||
                            (materia != null && materia.getNombre().toLowerCase().contains(searchText)) ||
                            String.valueOf(s.getCantidad()).contains(searchText) ||
                            s.getUnidadMedida().toLowerCase().contains(searchText) ||
                            s.getFecha().toLowerCase().contains(searchText);

                    return seccionMatch && fechaMatch && searchMatch;
                })
                .collect(Collectors.toList());

        tableModel.setRowCount(0);
        for (Stock stock : filtered) {
            MateriaPrima materia = materiaPrimaRepository.findById(stock.getIdMateria());
            Seccion seccion = materia != null ? seccionRepository.findById(materia.getIdSeccion()) : null;
            tableModel.addRow(new Object[]{
                    stock.getId(),
                    materia != null ? materia.getNombre() : stock.getIdMateria(),
                    stock.getCantidad(),
                    stock.getUnidadMedida(),
                    stock.getFecha(),
                    seccion != null ? seccion.getNombre() : (materia != null ? materia.getIdSeccion() : "")
            });
        }
    }

    private void adjustLayoutForScreenSize() {
        int width = getWidth();
        if (width < 800) {
            stockTable.setFont(new Font("Serif", Font.PLAIN, 12));
            stockTable.getTableHeader().setFont(new Font("Serif", Font.BOLD, 12));
            stockTable.setRowHeight(25);
        } else {
            stockTable.setFont(new Font("Serif", Font.PLAIN, 14));
            stockTable.getTableHeader().setFont(new Font("Serif", Font.BOLD, 14));
            stockTable.setRowHeight(30);
        }
    }
}