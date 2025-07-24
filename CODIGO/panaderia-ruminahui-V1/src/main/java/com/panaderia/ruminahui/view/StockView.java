package com.panaderia.ruminahui.view;

import com.panaderia.ruminahui.controller.StockController;
import com.panaderia.ruminahui.model.MateriaPrima;
import com.panaderia.ruminahui.model.MateriaPrimaRepository;
import com.panaderia.ruminahui.model.Stock;
import com.panaderia.ruminahui.model.StockRepository;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class StockView extends JFrame {
    private final StockController stockController;
    private JTable stockTable;
    private JTextField cantidadField, fechaField;
    private JComboBox<String> materiaComboBox;
    private JButton addButton, editButton, deleteButton, backButton;

    public StockView() {
        stockController = new StockController(new StockRepository(), new MateriaPrimaRepository());
        initializeUI();
        loadStocks();
    }

    private void initializeUI() {
        setTitle("Panadería Rumiñahui - Gestión de Stock");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JLabel headerLabel = new JLabel("Gestión de Stock", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Serif", Font.BOLD, 24));
        headerLabel.setForeground(new Color(139, 69, 19));
        add(headerLabel, BorderLayout.NORTH);

        // Table
        stockTable = new JTable(new String[][]{}, new String[]{"ID", "Materia Prima", "Cantidad", "Fecha"});
        stockTable.setBackground(new Color(245, 222, 179));
        add(new JScrollPane(stockTable), BorderLayout.CENTER);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(245, 222, 179));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Materia Prima:"), gbc);

        gbc.gridx = 1;
        materiaComboBox = new JComboBox<>();
        loadMateriasPrimas();
        formPanel.add(materiaComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Cantidad:"), gbc);

        gbc.gridx = 1;
        cantidadField = new JTextField(15);
        formPanel.add(cantidadField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Fecha (AAAA-MM-DD):"), gbc);

        gbc.gridx = 1;
        fechaField = new JTextField(15);
        formPanel.add(fechaField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 222, 179));
        addButton = new JButton("Agregar");
        editButton = new JButton("Editar");
        deleteButton = new JButton("Eliminar");
        backButton = new JButton("Volver");
        addButton.setBackground(new Color(210, 105, 30));
        editButton.setBackground(new Color(210, 105, 30));
        deleteButton.setBackground(new Color(210, 105, 30));
        backButton.setBackground(new Color(210, 105, 30));
        addButton.setForeground(Color.WHITE);
        editButton.setForeground(Color.WHITE);
        deleteButton.setForeground(Color.WHITE);
        backButton.setForeground(Color.WHITE);
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);
        formPanel.add(buttonPanel, gbc);

        add(formPanel, BorderLayout.SOUTH);

        // Action listeners
        addButton.addActionListener(e -> handleAdd());
        editButton.addActionListener(e -> handleEdit());
        deleteButton.addActionListener(e -> handleDelete());
        backButton.addActionListener(e -> {
            dispose();
            new MainView().setVisible(true);
        });
    }

    private void loadMateriasPrimas() {
        materiaComboBox.removeAllItems();
        List<MateriaPrima> materias = new MateriaPrimaRepository().findAll();
        for (MateriaPrima materia : materias) {
            materiaComboBox.addItem(materia.getId() + ": " + materia.getNombre());
        }
    }

    private void loadStocks() {
        List<Stock> stocks = stockController.getAllStocks();
        String[][] data = new String[stocks.size()][4];
        MateriaPrimaRepository materiaRepo = new MateriaPrimaRepository();
        for (int i = 0; i < stocks.size(); i++) {
            Stock stock = stocks.get(i);
            MateriaPrima materia = materiaRepo.findById(stock.getIdMateria());
            data[i][0] = stock.getId();
            data[i][1] = materia != null ? materia.getNombre() : stock.getIdMateria();
            data[i][2] = String.valueOf(stock.getCantidad());
            data[i][3] = stock.getFecha();
        }
        stockTable.setModel(new javax.swing.table.DefaultTableModel(data, new String[]{"ID", "Materia Prima", "Cantidad", "Fecha"}));
    }

    private void handleAdd() {
        try {
            String idMateria = materiaComboBox.getSelectedItem() != null ? materiaComboBox.getSelectedItem().toString().split(":")[0] : "";
            double cantidad = Double.parseDouble(cantidadField.getText());
            String result = stockController.createStock(idMateria, cantidad, fechaField.getText());
            if (result == null) {
                loadStocks();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Cantidad debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleEdit() {
        int selectedRow = stockTable.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                String id = (String) stockTable.getValueAt(selectedRow, 0);
                String idMateria = materiaComboBox.getSelectedItem() != null ? materiaComboBox.getSelectedItem().toString().split(":")[0] : "";
                double cantidad = Double.parseDouble(cantidadField.getText());
                String result = stockController.updateStock(id, idMateria, cantidad, fechaField.getText());
                if (result == null) {
                    loadStocks();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Cantidad debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un stock.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDelete() {
        int selectedRow = stockTable.getSelectedRow();
        if (selectedRow >= 0) {
            int option = JOptionPane.showConfirmDialog(this, "¿Confirmar eliminación?", "Eliminar Stock", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                String id = (String) stockTable.getValueAt(selectedRow, 0);
                String result = stockController.deleteStock(id);
                if (result == null) {
                    loadStocks();
                } else {
                    JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un stock.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        cantidadField.setText("");
        fechaField.setText("");
        materiaComboBox.setSelectedIndex(-1);
    }
}