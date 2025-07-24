package com.panaderia.ruminahui.view;

import com.panaderia.ruminahui.controller.MateriaPrimaController;
import com.panaderia.ruminahui.model.MateriaPrima;
import com.panaderia.ruminahui.model.MateriaPrimaRepository;
import com.panaderia.ruminahui.model.Seccion;
import com.panaderia.ruminahui.model.SeccionRepository;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MateriaPrimaView extends JFrame {
    private final MateriaPrimaController materiaPrimaController;
    private JTable materiaTable;
    private JTextField nombreField, stockMinimoField, unidadMedidaField;
    private JTextArea descripcionArea;
    private JComboBox<String> seccionComboBox;
    private JButton addButton, editButton, deleteButton, backButton;

    public MateriaPrimaView() {
        materiaPrimaController = new MateriaPrimaController(new MateriaPrimaRepository(), new SeccionRepository());
        initializeUI();
        loadMateriasPrimas();
    }

    private void initializeUI() {
        setTitle("Panadería Rumiñahui - Gestión de Materias Primas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JLabel headerLabel = new JLabel("Gestión de Materias Primas", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Serif", Font.BOLD, 24));
        headerLabel.setForeground(new Color(139, 69, 19));
        add(headerLabel, BorderLayout.NORTH);

        // Table
        materiaTable = new JTable(new String[][]{}, new String[]{"ID", "Nombre", "Descripción", "Stock Mínimo", "Unidad", "Sección"});
        materiaTable.setBackground(new Color(245, 222, 179));
        add(new JScrollPane(materiaTable), BorderLayout.CENTER);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(245, 222, 179));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Nombre:"), gbc);

        gbc.gridx = 1;
        nombreField = new JTextField(15);
        formPanel.add(nombreField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Descripción:"), gbc);

        gbc.gridx = 1;
        descripcionArea = new JTextArea(3, 15);
        formPanel.add(new JScrollPane(descripcionArea), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Stock Mínimo:"), gbc);

        gbc.gridx = 1;
        stockMinimoField = new JTextField(15);
        formPanel.add(stockMinimoField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Unidad de Medida:"), gbc);

        gbc.gridx = 1;
        unidadMedidaField = new JTextField(15);
        formPanel.add(unidadMedidaField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Sección:"), gbc);

        gbc.gridx = 1;
        seccionComboBox = new JComboBox<>();
        loadSecciones();
        formPanel.add(seccionComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
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

    private void loadSecciones() {
        seccionComboBox.removeAllItems();
        List<Seccion> secciones = new SeccionRepository().findAll();
        for (Seccion seccion : secciones) {
            seccionComboBox.addItem(seccion.getId() + ": " + seccion.getNombre());
        }
    }

    private void loadMateriasPrimas() {
        List<MateriaPrima> materias = materiaPrimaController.getAllMateriasPrimas();
        String[][] data = new String[materias.size()][6];
        for (int i = 0; i < materias.size(); i++) {
            MateriaPrima materia = materias.get(i);
            data[i][0] = materia.getId();
            data[i][1] = materia.getNombre();
            data[i][2] = materia.getDescripcion();
            data[i][3] = String.valueOf(materia.getStockMinimo());
            data[i][4] = materia.getUnidadMedida();
            data[i][5] = materia.getIdSeccion();
        }
        materiaTable.setModel(new javax.swing.table.DefaultTableModel(data, new String[]{"ID", "Nombre", "Descripción", "Stock Mínimo", "Unidad", "Sección"}));
    }

    private void handleAdd() {
        try {
            String idSeccion = seccionComboBox.getSelectedItem() != null ? seccionComboBox.getSelectedItem().toString().split(":")[0] : "";
            double stockMinimo = Double.parseDouble(stockMinimoField.getText());
            String result = materiaPrimaController.createMateriaPrima(
                    nombreField.getText(),
                    descripcionArea.getText(),
                    stockMinimo,
                    unidadMedidaField.getText(),
                    idSeccion
            );
            if (result == null) {
                loadMateriasPrimas();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Stock mínimo debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleEdit() {
        int selectedRow = materiaTable.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                String id = (String) materiaTable.getValueAt(selectedRow, 0);
                String idSeccion = seccionComboBox.getSelectedItem() != null ? seccionComboBox.getSelectedItem().toString().split(":")[0] : "";
                double stockMinimo = Double.parseDouble(stockMinimoField.getText());
                String result = materiaPrimaController.updateMateriaPrima(
                        id,
                        nombreField.getText(),
                        descripcionArea.getText(),
                        stockMinimo,
                        unidadMedidaField.getText(),
                        idSeccion
                );
                if (result == null) {
                    loadMateriasPrimas();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Stock mínimo debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una materia prima.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDelete() {
        int selectedRow = materiaTable.getSelectedRow();
        if (selectedRow >= 0) {
            int option = JOptionPane.showConfirmDialog(this, "¿Confirmar eliminación?", "Eliminar Materia Prima", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                String id = (String) materiaTable.getValueAt(selectedRow, 0);
                String result = materiaPrimaController.deleteMateriaPrima(id);
                if (result == null) {
                    loadMateriasPrimas();
                } else {
                    JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una materia prima.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        nombreField.setText("");
        descripcionArea.setText("");
        stockMinimoField.setText("");
        unidadMedidaField.setText("");
        seccionComboBox.setSelectedIndex(-1);
    }
}