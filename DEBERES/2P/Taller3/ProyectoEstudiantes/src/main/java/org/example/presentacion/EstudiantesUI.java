package org.example.presentacion;

import org.example.modelo.Estudiante;
import org.example.servicio.EstudianteServicio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class EstudiantesUI extends JFrame {

    private JTextField campoId, campoNombre, campoEdad;
    private JButton btnCrear, btnLeer, btnActualizar, btnEliminar;
    private JTable tablaEstudiantes;
    private DefaultTableModel modeloTabla;

    private EstudianteServicio servicio;

    public EstudiantesUI() {
        servicio = new EstudianteServicio();

        setTitle("Gestión de Estudiantes");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);
        setLocationRelativeTo(null);

        // Panel de formulario
        JPanel panelFormulario = new JPanel(new GridLayout(2, 3, 10, 10));
        campoId = new JTextField();
        campoNombre = new JTextField();
        campoEdad = new JTextField();

        panelFormulario.add(new JLabel("ID:"));
        panelFormulario.add(new JLabel("Nombre:"));
        panelFormulario.add(new JLabel("Edad:"));

        panelFormulario.add(campoId);
        panelFormulario.add(campoNombre);
        panelFormulario.add(campoEdad);

        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnCrear = new JButton("Crear");
        btnLeer = new JButton("Leer");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");

        panelBotones.add(btnCrear);
        panelBotones.add(btnLeer);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);

        // Agrupar formulario y botones
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelFormulario, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        // Tabla
        String[] columnas = {"ID", "Nombre", "Edad"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaEstudiantes = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaEstudiantes);

        // Layout principal
        setLayout(new BorderLayout(10, 10));
        add(panelSuperior, BorderLayout.NORTH);
        add(scrollTabla, BorderLayout.CENTER);

        // Eventos
        btnCrear.addActionListener(this::crearEstudiante);
        btnLeer.addActionListener(e -> cargarTabla());
        btnActualizar.addActionListener(this::actualizarEstudiante);
        btnEliminar.addActionListener(this::eliminarEstudiante);
    }

    private void crearEstudiante(ActionEvent e) {
        try {
            int id = Integer.parseInt(campoId.getText());
            String nombre = campoNombre.getText();
            int edad = Integer.parseInt(campoEdad.getText());

            servicio.agregarEstudiante(id, nombre, edad);
            limpiarCampos();
            cargarTabla();
        } catch (Exception ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void actualizarEstudiante(ActionEvent e) {
        try {
            int id = Integer.parseInt(campoId.getText());
            String nombre = campoNombre.getText();
            int edad = Integer.parseInt(campoEdad.getText());

            servicio.actualizarEstudiante(id, nombre, edad);
            limpiarCampos();
            cargarTabla();
        } catch (Exception ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void eliminarEstudiante(ActionEvent e) {
        try {
            int id = Integer.parseInt(campoId.getText());

            servicio.eliminarEstudiante(id);
            limpiarCampos();
            cargarTabla();
        } catch (Exception ex) {
            mostrarError(ex.getMessage());
        }
    }

    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        List<Estudiante> estudiantes = servicio.listarEstudiantes();
        for (Estudiante est : estudiantes) {
            modeloTabla.addRow(new Object[]{est.getOrden(), est.getNombre(), est.getEdad()});
        }
    }

    private void limpiarCampos() {
        campoId.setText("");
        campoNombre.setText("");
        campoEdad.setText("");
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
