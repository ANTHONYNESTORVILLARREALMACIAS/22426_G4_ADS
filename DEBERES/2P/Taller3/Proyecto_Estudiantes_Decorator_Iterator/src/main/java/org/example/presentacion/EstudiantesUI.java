package org.example.presentacion;

import org.example.modelo.Estudiante;
import org.example.repositorio.EstudianteRepositorio;
import org.example.servicio.EstudianteServicio;
import org.example.servicio.DescuentoMatriculaDecorator;

import javax.swing.*;
import java.awt.*;

/**
 * Clase que representa la interfaz gráfica de usuario (GUI) para la gestión de estudiantes.
 * Utiliza Swing para crear un formulario interactivo y aplica el patrón Decorator
 * para estudiantes con descuento.
 */
public class EstudiantesUI extends JFrame {
    // Campos de texto para ingresar datos del estudiante
    private JTextField txtId, txtNombre, txtEdad;
    // Área de texto para mostrar la lista de estudiantes
    private JTextArea areaTexto;
    // Servicio que maneja la lógica de negocio
    private final EstudianteServicio servicio;

    /**
     * Constructor que inicializa la ventana y sus componentes.
     */
    public EstudiantesUI() {
        super("Gestión de Estudiantes");
        setSize(550, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Inicializa el servicio con un repositorio
        servicio = new EstudianteServicio(new EstudianteRepositorio());

        // Panel para los campos de entrada
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

        // Botón para agregar un estudiante
        JButton btnAgregar = new JButton("Agregar");
        btnAgregar.addActionListener(e -> {
            int id = Integer.parseInt(txtId.getText());
            String nombre = txtNombre.getText();
            int edad = Integer.parseInt(txtEdad.getText());

            Estudiante estudiante = new Estudiante(id, nombre, edad);

            // Aplica el decorador si el estudiante tiene menos de 20 años
            if (edad < 20) {
                estudiante = new DescuentoMatriculaDecorator(estudiante, 20);
            }

            servicio.agregarEstudiante(estudiante);
            mostrarEstudiantes();
        });

        // Botón para actualizar un estudiante
        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(e -> {
            servicio.actualizarEstudiante(
                    Integer.parseInt(txtId.getText()),
                    txtNombre.getText(),
                    Integer.parseInt(txtEdad.getText())
            );
            mostrarEstudiantes();
        });

        // Botón para eliminar un estudiante
        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(e -> {
            servicio.eliminarEstudiante(Integer.parseInt(txtId.getText()));
            mostrarEstudiantes();
        });

        // Botón para mostrar estudiantes con descuento
        JButton btnDescuentos = new JButton("Mostrar con Descuento");
        btnDescuentos.addActionListener(e -> {
            StringBuilder sb = new StringBuilder("Estudiantes con Descuento:\n\n");
            for (Estudiante est : servicio.obtenerConDescuento()) {
                sb.append(est.getInfo()).append("\n");
            }
            areaTexto.setText(sb.toString());
        });

        // Panel para los botones
        JPanel panelBotones = new JPanel();
        panelBotones.add(btnAgregar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnDescuentos);

        // Área de texto para mostrar resultados
        areaTexto = new JTextArea();
        areaTexto.setEditable(false);

        // Agrega los componentes a la ventana
        add(panel, BorderLayout.NORTH);
        add(panelBotones, BorderLayout.CENTER);
        add(new JScrollPane(areaTexto), BorderLayout.SOUTH);
    }

    /**
     * Actualiza el área de texto con la lista de estudiantes registrados.
     */
    private void mostrarEstudiantes() {
        StringBuilder sb = new StringBuilder("Estudiantes registrados:\n\n");
        for (Estudiante e : servicio.obtenerTodos()) {
            sb.append(e.getInfo()).append("\n");
        }
        areaTexto.setText(sb.toString());
    }
}