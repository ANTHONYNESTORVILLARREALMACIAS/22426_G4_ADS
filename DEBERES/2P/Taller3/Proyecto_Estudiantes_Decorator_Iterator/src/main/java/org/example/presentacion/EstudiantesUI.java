package org.example.presentacion;

import org.example.modelo.Estudiante;
import org.example.repositorio.EstudianteRepositorio;
import org.example.servicio.EstudianteServicio;
import org.example.servicio.DescuentoMatriculaDecorator;

import javax.swing.*;
import java.awt.*;

public class EstudiantesUI extends JFrame {
    private JTextField txtId, txtNombre, txtEdad;
    private JTextArea areaTexto;
    private final EstudianteServicio servicio;

    public EstudiantesUI() {
        super("Gestión de Estudiantes");
        setSize(550, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        servicio = new EstudianteServicio(new EstudianteRepositorio());

        JPanel panel = new JPanel(new GridLayout(4, 2));
        txtId = new JTextField();
        txtNombre = new JTextField();
        txtEdad = new JTextField();

        panel.add(new JLabel("ID:"));
        panel.add(txtId);
        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Edad:"));
        panel.add(txtEdad);

        JButton btnAgregar = new JButton("Agregar");
        btnAgregar.addActionListener(e -> {
            int id = Integer.parseInt(txtId.getText());
            String nombre = txtNombre.getText();
            int edad = Integer.parseInt(txtEdad.getText());

            Estudiante estudiante = new Estudiante(id, nombre, edad);

            // Si edad menor a 20 => aplicar descuento ficticio
            if (edad < 20) {
                estudiante = new DescuentoMatriculaDecorator(estudiante, 15);
            }

            servicio.agregarEstudiante(estudiante);
            mostrarEstudiantes();
        });

        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(e -> {
            servicio.actualizarEstudiante(
                    Integer.parseInt(txtId.getText()),
                    txtNombre.getText(),
                    Integer.parseInt(txtEdad.getText())
            );
            mostrarEstudiantes();
        });

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(e -> {
            servicio.eliminarEstudiante(Integer.parseInt(txtId.getText()));
            mostrarEstudiantes();
        });

        JButton btnDescuentos = new JButton("Mostrar con Descuento");
        btnDescuentos.addActionListener(e -> {
            StringBuilder sb = new StringBuilder("Estudiantes con Descuento:\n\n");
            for (Estudiante est : servicio.obtenerConDescuento()) {
                sb.append(est.getInfo()).append("\n");
            }
            areaTexto.setText(sb.toString());
        });

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnAgregar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnDescuentos);

        areaTexto = new JTextArea();
        areaTexto.setEditable(false);

        add(panel, BorderLayout.NORTH);
        add(panelBotones, BorderLayout.CENTER);
        add(new JScrollPane(areaTexto), BorderLayout.SOUTH);
    }

    private void mostrarEstudiantes() {
        StringBuilder sb = new StringBuilder("Estudiantes registrados:\n\n");
        for (Estudiante e : servicio.obtenerTodos()) {
            sb.append(e.getInfo()).append("\n");
        }
        areaTexto.setText(sb.toString());
    }
}
