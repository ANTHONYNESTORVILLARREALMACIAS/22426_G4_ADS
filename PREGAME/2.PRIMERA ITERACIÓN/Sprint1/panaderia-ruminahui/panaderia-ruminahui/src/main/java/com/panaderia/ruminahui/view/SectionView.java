package com.panaderia.ruminahui.view;

import com.panaderia.ruminahui.controller.SectionController;
import com.panaderia.ruminahui.model.Section;
import com.panaderia.ruminahui.model.SectionRepository;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SectionView extends JFrame {
    private final SectionController sectionController;
    private JTable sectionTable;
    private JTextField nameField;
    private JTextArea descriptionArea;
    private JButton addButton, editButton, deleteButton;

    public SectionView() {
        sectionController = new SectionController(new SectionRepository());
        initializeUI();
        loadSections();
    }

    private void initializeUI() {
        setTitle("Panadería Rumiñahui - Gestión de Secciones");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Header
        JLabel headerLabel = new JLabel("Gestión de Secciones", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Serif", Font.BOLD, 24));
        headerLabel.setForeground(new Color(139, 69, 19));
        add(headerLabel, BorderLayout.NORTH);

        // Table
        sectionTable = new JTable(new String[][]{}, new String[]{"ID", "Nombre", "Descripción"});
        sectionTable.setBackground(new Color(245, 222, 179));
        add(new JScrollPane(sectionTable), BorderLayout.CENTER);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(245, 222, 179));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Nombre:"), gbc);

        gbc.gridx = 1;
        nameField = new JTextField(15);
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Descripción:"), gbc);

        gbc.gridx = 1;
        descriptionArea = new JTextArea(3, 15);
        formPanel.add(new JScrollPane(descriptionArea), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 222, 179));
        addButton = new JButton("Agregar");
        editButton = new JButton("Editar");
        deleteButton = new JButton("Eliminar");
        addButton.setBackground(new Color(210, 105, 30));
        editButton.setBackground(new Color(210, 105, 30));
        deleteButton.setBackground(new Color(210, 105, 30));
        addButton.setForeground(Color.WHITE);
        editButton.setForeground(Color.WHITE);
        deleteButton.setForeground(Color.WHITE);
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        formPanel.add(buttonPanel, gbc);

        add(formPanel, BorderLayout.SOUTH);

        // Action listeners
        addButton.addActionListener(e -> handleAdd());
        editButton.addActionListener(e -> handleEdit());
        deleteButton.addActionListener(e -> handleDelete());
    }

    private void loadSections() {
        List<Section> sections = sectionController.getAllSections();
        String[][] data = new String[sections.size()][3];
        for (int i = 0; i < sections.size(); i++) {
            Section section = sections.get(i);
            data[i][0] = section.getId();
            data[i][1] = section.getName();
            data[i][2] = section.getDescription();
        }
        sectionTable.setModel(new javax.swing.table.DefaultTableModel(data, new String[]{"ID", "Nombre", "Descripción"}));
    }

    private void handleAdd() {
        String result = sectionController.createSection(nameField.getText(), descriptionArea.getText());
        if (result == null) {
            loadSections();
            nameField.setText("");
            descriptionArea.setText("");
        } else {
            JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleEdit() {
        int selectedRow = sectionTable.getSelectedRow();
        if (selectedRow >= 0) {
            String id = (String) sectionTable.getValueAt(selectedRow, 0);
            String result = sectionController.updateSection(id, nameField.getText(), descriptionArea.getText());
            if (result == null) {
                loadSections();
                nameField.setText("");
                descriptionArea.setText("");
            } else {
                JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una sección.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDelete() {
        int selectedRow = sectionTable.getSelectedRow();
        if (selectedRow >= 0) {
            int option = JOptionPane.showConfirmDialog(this, "¿Confirmar eliminación?", "Eliminar Sección", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                String id = (String) sectionTable.getValueAt(selectedRow, 0);
                String result = sectionController.deleteSection(id);
                if (result == null) {
                    loadSections();
                } else {
                    JOptionPane.showMessageDialog(this, result, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una sección.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}