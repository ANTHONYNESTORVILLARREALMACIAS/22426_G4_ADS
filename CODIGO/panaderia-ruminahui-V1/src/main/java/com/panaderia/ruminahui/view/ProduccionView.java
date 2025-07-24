package com.panaderia.ruminahui.view;

import com.panaderia.ruminahui.controller.ProduccionController;
import com.panaderia.ruminahui.model.Produccion;
import com.panaderia.ruminahui.model.ProduccionRepository;
import com.panaderia.ruminahui.model.Receta;
import com.panaderia.ruminahui.model.RecetaRepository;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ProduccionView extends JFrame {
    private final ProduccionController produccionController;
    private JTable produccionTable;
    private JComboBox<String> recetaComboBox;
    private JTextField cantidadField, fechaField;
    private JButton addButton, editButton, deleteButton, backButton;

    public ProduccionView() {
        produccionController = new ProduccionController(new ProduccionRepository(), new RecetaRepository());
        initializeUI();
        loadProducciones();
    }

    private void initializeUI() {
        setTitle("Panadería Rumiñahui - Gestión de Producciones");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JLabel headerLabel = new JLabel("Gestión de Producciones", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Serif", Font.BOLD, 24));
        headerLabel.setForeground(new Color(139, 69, 19));
        add(headerLabel, BorderLayout.NORTH);

        // Table
        produccionTable = new JTable(new String[][]{}, new String[]{"ID", "Receta", "Cantidad", "Fecha"});
        produccionTable.setBackground(new Color(245, 222, 179));
        add(new JScrollPane(produccionTable), BorderLayout.CENTER);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(245, 222, 179));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Receta:"), gbc);

        gbc.gridx = 1;
        recetaComboBox = new JComboBox<>();
        loadRecetas();
        formPanel.add(recetaComboBox, gbc);

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

    private void loadRecetas() {
        recetaComboBox.removeAllItems();
        List<Receta> recetas = new RecetaRepository().findAll();
        for (Receta receta : recetas) {
            recetaComboBox.addItem(receta.getId() + ": " + receta.getNombre());
        }
    }

    private void loadProducciones() {
        List<Produccion> producciones = produccionController.getAllProducciones();
        String[][] data = new String[producciones.size()][4];
        RecetaRepository recetaRepo = new RecetaRepository();
        for (int i = 0; i < producciones.size(); i++) {
            Produccion produccion = producciones.get(i);
            Receta receta = recetaRepo.findById(produccion.getIdReceta());
            data[i][0] = produccion.getId();
            data[i][1] = receta != null ? receta.getNombre() : produccion.getIdReceta();
            data[i][2] = String.valueOf(produccion.getCantidad());
            data[i][3] = produccion.getFecha();
        }
        produccionTable.setModel(new javax.swing.table.DefaultTableModel(data, new String[]{"ID", "Receta", "Cantidad", "Fecha"}));
    }

    private void handleAdd() {
        try {
            String idReceta = recetaComboBox.getSelectedItem() != null ? recetaComboBox.getSelectedItem().toString().split(":")[0] : "";
            double cantidad = Double.parseDouble(cantidadField.getText());
            String result = produccionController.createProduccion(idReceta, cantidad, fechaField.getText());
            if (result == null) {
                loadProducciones();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Cantidad debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleEdit() {
        int selectedRow = produccionTable.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                String id = (String) produccionTable.getValueAt(selectedRow, 0);
                String idReceta = recetaComboBox.getSelectedItem() != null ? recetaComboBox.getSelectedItem().toString().split(":")[0] : "";
                double cantidad = Double.parseDouble(cantidadField.getText());
                String result = produccionController.updateProduccion(id, idReceta, cantidad, fechaField.getText());
                if (result == null) {
                    loadProducciones();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Cantidad debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una producción.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDelete() {
        int selectedRow = produccionTable.getSelectedRow();
        if (selectedRow >= 0) {
            int option = JOptionPane.showConfirmDialog(this, "¿Confirmar eliminación?", "Eliminar Producción", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                String id = (String) produccionTable.getValueAt(selectedRow, 0);
                String result = produccionController.deleteProduccion(id);
                if (result == null) {
                    loadProducciones();
                } else {
                    JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una producción.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        recetaComboBox.setSelectedIndex(-1);
        cantidadField.setText("");
        fechaField.setText("");
    }
}