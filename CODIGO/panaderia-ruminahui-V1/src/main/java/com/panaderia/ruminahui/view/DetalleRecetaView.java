package com.panaderia.ruminahui.view;

import com.panaderia.ruminahui.controller.DetalleRecetaController;
import com.panaderia.ruminahui.model.DetalleReceta;
import com.panaderia.ruminahui.model.MateriaPrima;
import com.panaderia.ruminahui.model.MateriaPrimaRepository;
import com.panaderia.ruminahui.model.Receta;
import com.panaderia.ruminahui.model.RecetaRepository;
import com.panaderia.ruminahui.model.DetalleRecetaRepository;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DetalleRecetaView extends JFrame {
    private final DetalleRecetaController detalleRecetaController;
    private JTable detalleTable;
    private JComboBox<String> recetaComboBox, materiaComboBox;
    private JTextField cantidadField, unidadMedidaField;
    private JButton addButton, editButton, deleteButton, backButton;

    public DetalleRecetaView() {
        detalleRecetaController = new DetalleRecetaController(new DetalleRecetaRepository(), new RecetaRepository(), new MateriaPrimaRepository());
        initializeUI();
        loadDetalles();
    }

    private void initializeUI() {
        setTitle("Panadería Rumiñahui - Gestión de Detalles de Receta");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JLabel headerLabel = new JLabel("Gestión de Detalles de Receta", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Serif", Font.BOLD, 24));
        headerLabel.setForeground(new Color(139, 69, 19));
        add(headerLabel, BorderLayout.NORTH);

        // Table
        detalleTable = new JTable(new String[][]{}, new String[]{"ID", "Receta", "Materia Prima", "Cantidad", "Unidad"});
        detalleTable.setBackground(new Color(245, 222, 179));
        add(new JScrollPane(detalleTable), BorderLayout.CENTER);

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
        formPanel.add(new JLabel("Materia Prima:"), gbc);

        gbc.gridx = 1;
        materiaComboBox = new JComboBox<>();
        loadMateriasPrimas();
        formPanel.add(materiaComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Cantidad:"), gbc);

        gbc.gridx = 1;
        cantidadField = new JTextField(15);
        formPanel.add(cantidadField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Unidad de Medida:"), gbc);

        gbc.gridx = 1;
        unidadMedidaField = new JTextField(15);
        formPanel.add(unidadMedidaField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
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

    private void loadMateriasPrimas() {
        materiaComboBox.removeAllItems();
        List<MateriaPrima> materias = new MateriaPrimaRepository().findAll();
        for (MateriaPrima materia : materias) {
            materiaComboBox.addItem(materia.getId() + ": " + materia.getNombre());
        }
    }

    private void loadDetalles() {
        List<DetalleReceta> detalles = detalleRecetaController.getAllDetallesReceta();
        String[][] data = new String[detalles.size()][5];
        RecetaRepository recetaRepo = new RecetaRepository();
        MateriaPrimaRepository materiaRepo = new MateriaPrimaRepository();
        for (int i = 0; i < detalles.size(); i++) {
            DetalleReceta detalle = detalles.get(i);
            Receta receta = recetaRepo.findById(detalle.getIdReceta());
            MateriaPrima materia = materiaRepo.findById(detalle.getIdMateria());
            data[i][0] = detalle.getId();
            data[i][1] = receta != null ? receta.getNombre() : detalle.getIdReceta();
            data[i][2] = materia != null ? materia.getNombre() : detalle.getIdMateria();
            data[i][3] = String.valueOf(detalle.getCantidad());
            data[i][4] = detalle.getUnidadMedida();
        }
        detalleTable.setModel(new javax.swing.table.DefaultTableModel(data, new String[]{"ID", "Receta", "Materia Prima", "Cantidad", "Unidad"}));
    }

    private void handleAdd() {
        try {
            String idReceta = recetaComboBox.getSelectedItem() != null ? recetaComboBox.getSelectedItem().toString().split(":")[0] : "";
            String idMateria = materiaComboBox.getSelectedItem() != null ? materiaComboBox.getSelectedItem().toString().split(":")[0] : "";
            double cantidad = Double.parseDouble(cantidadField.getText());
            String result = detalleRecetaController.createDetalleReceta(idReceta, idMateria, cantidad, unidadMedidaField.getText());
            if (result == null) {
                loadDetalles();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Cantidad debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleEdit() {
        int selectedRow = detalleTable.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                String id = (String) detalleTable.getValueAt(selectedRow, 0);
                String idReceta = recetaComboBox.getSelectedItem() != null ? recetaComboBox.getSelectedItem().toString().split(":")[0] : "";
                String idMateria = materiaComboBox.getSelectedItem() != null ? materiaComboBox.getSelectedItem().toString().split(":")[0] : "";
                double cantidad = Double.parseDouble(cantidadField.getText());
                String result = detalleRecetaController.updateDetalleReceta(id, idReceta, idMateria, cantidad, unidadMedidaField.getText());
                if (result == null) {
                    loadDetalles();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Cantidad debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un detalle de receta.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDelete() {
        int selectedRow = detalleTable.getSelectedRow();
        if (selectedRow >= 0) {
            int option = JOptionPane.showConfirmDialog(this, "¿Confirmar eliminación?", "Eliminar Detalle de Receta", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                String id = (String) detalleTable.getValueAt(selectedRow, 0);
                String result = detalleRecetaController.deleteDetalleReceta(id);
                if (result == null) {
                    loadDetalles();
                } else {
                    JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un detalle de receta.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        recetaComboBox.setSelectedIndex(-1);
        materiaComboBox.setSelectedIndex(-1);
        cantidadField.setText("");
        unidadMedidaField.setText("");
    }
}