package com.panaderia.ruminahui.view;

import com.panaderia.ruminahui.controller.RecetaController;
import com.panaderia.ruminahui.model.Receta;
import com.panaderia.ruminahui.model.RecetaRepository;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RecetaView extends JFrame {
    private final RecetaController recetaController;
    private JTable recetaTable;
    private JTextField nombreField;
    private JTextArea descripcionArea;
    private JButton addButton, editButton, deleteButton, backButton;

    public RecetaView() {
        recetaController = new RecetaController(new RecetaRepository());
        initializeUI();
        loadRecetas();
    }

    private void initializeUI() {
        setTitle("Panadería Rumiñahui - Gestión de Recetas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JLabel headerLabel = new JLabel("Gestión de Recetas", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Serif", Font.BOLD, 24));
        headerLabel.setForeground(new Color(139, 69, 19));
        add(headerLabel, BorderLayout.NORTH);

        // Table
        recetaTable = new JTable(new String[][]{}, new String[]{"ID", "Nombre", "Descripción"});
        recetaTable.setBackground(new Color(245, 222, 179));
        add(new JScrollPane(recetaTable), BorderLayout.CENTER);

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
        List<Receta> recetas = recetaController.getAllRecetas();
        String[][] data = new String[recetas.size()][3];
        for (int i = 0; i < recetas.size(); i++) {
            Receta receta = recetas.get(i);
            data[i][0] = receta.getId();
            data[i][1] = receta.getNombre();
            data[i][2] = receta.getDescripcion();
        }
        recetaTable.setModel(new javax.swing.table.DefaultTableModel(data, new String[]{"ID", "Nombre", "Descripción"}));
    }

    private void handleAdd() {
        String result = recetaController.createReceta(nombreField.getText(), descripcionArea.getText());
        if (result == null) {
            loadRecetas();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleEdit() {
        int selectedRow = recetaTable.getSelectedRow();
        if (selectedRow >= 0) {
            String id = (String) recetaTable.getValueAt(selectedRow, 0);
            String result = recetaController.updateReceta(id, nombreField.getText(), descripcionArea.getText());
            if (result == null) {
                loadRecetas();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una receta.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDelete() {
        int selectedRow = recetaTable.getSelectedRow();
        if (selectedRow >= 0) {
            int option = JOptionPane.showConfirmDialog(this, "¿Confirmar eliminación?", "Eliminar Receta", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                String id = (String) recetaTable.getValueAt(selectedRow, 0);
                String result = recetaController.deleteReceta(id);
                if (result == null) {
                    loadRecetas();
                } else {
                    JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una receta.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        nombreField.setText("");
        descripcionArea.setText("");
    }
}