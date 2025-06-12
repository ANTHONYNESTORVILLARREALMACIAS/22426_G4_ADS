package org.example.presentacion;

import org.example.modelo.Estudiante;
import org.example.servicio.EstudianteServicio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 * Clase que implementa una interfaz gráfica para gestionar estudiantes utilizando Swing.
 * Proporciona un formulario para ingresar datos de estudiantes y una tabla para visualizarlos,
 * permitiendo operaciones CRUD (Crear, Leer, Actualizar, Eliminar).
 */
public class EstudiantesUI extends JFrame {

    /** Campo de texto para el identificador único del estudiante. */
    private JTextField campoId;

    /** Campo de texto para el nombre del estudiante. */
    private JTextField campoNombre;

    /** Campo de texto para la edad del estudiante. */
    private JTextField campoEdad;

    /** Botón para crear un nuevo estudiante. */
    private JButton btnCrear;

    /** Botón para leer y mostrar todos los estudiantes en la tabla. */
    private JButton btnLeer;

    /** Botón para actualizar los datos de un estudiante existente. */
    private JButton btnActualizar;

    /** Botón para eliminar un estudiante por su ID. */
    private JButton btnEliminar;

    /** Tabla que muestra la lista de estudiantes. */
    private JTable tablaEstudiantes;

    /** Modelo de datos para la tabla de estudiantes. */
    private DefaultTableModel modeloTabla;

    /** Servicio que maneja la lógica de negocio para los estudiantes. */
    private EstudianteServicio servicio;

    /**
     * Constructor que inicializa la interfaz gráfica y configura los componentes.
     */
    public EstudiantesUI() {
        servicio = new EstudianteServicio();

        // Configurar la ventana principal
        setTitle("Gestión de Estudiantes");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);
        setLocationRelativeTo(null);

        // Panel de formulario
        JPanel panelFormulario = new JPanel(new GridLayout(2, 3, 10, 10));
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Crear campos de entrada
        campoId = new JTextField();
        campoNombre = new JTextField();
        campoEdad = new JTextField();

        // Añadir etiquetas y campos al panel
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

        // Añadir botones al panel
        panelBotones.add(btnCrear);
        panelBotones.add(btnLeer);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);

        // Agrupar formulario y botones
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(panelFormulario, BorderLayout.CENTER);
        panelSuperior.add(panelBotones, BorderLayout.SOUTH);

        // Configurar tabla
        String[] columnas = {"ID", "Nombre", "Edad"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaEstudiantes = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaEstudiantes);

        // Layout principal
        setLayout(new BorderLayout(10, 10));
        add(panelSuperior, BorderLayout.NORTH);
        add(scrollTabla, BorderLayout.CENTER);

        // Configurar eventos de los botones
        btnCrear.addActionListener(this::crearEstudiante);
        btnLeer.addActionListener(e -> cargarTabla());
        btnActualizar.addActionListener(this::actualizarEstudiante);
        btnEliminar.addActionListener(this::eliminarEstudiante);

        // Cargar datos iniciales en la tabla
        cargarTabla();
    }

    /**
     * Crea un nuevo estudiante y lo agrega al sistema, actualizando la tabla.
     * @param e Evento de acción generado por el botón "Crear".
     */
    private void crearEstudiante(ActionEvent e) {
        try {
            int id = Integer.parseInt(campoId.getText());
            String nombre = campoNombre.getText();
            int edad = Integer.parseInt(campoEdad.getText());
            servicio.agregarEstudiante(id, nombre, edad);
            JOptionPane.showMessageDialog(this, "Estudiante agregado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
            cargarTabla();
        } catch (NumberFormatException ex) {
            mostrarError("Ingrese valores numéricos válidos para ID y edad.");
        } catch (IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        }
    }

    /**
     * Actualiza los datos de un estudiante existente y refresca la tabla.
     * @param e Evento de acción generado por el botón "Actualizar".
     */
    private void actualizarEstudiante(ActionEvent e) {
        try {
            int id = Integer.parseInt(campoId.getText());
            String nombre = campoNombre.getText();
            int edad = Integer.parseInt(campoEdad.getText());
            servicio.actualizarEstudiante(id, nombre, edad);
            JOptionPane.showMessageDialog(this, "Estudiante actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
            cargarTabla();
        } catch (NumberFormatException ex) {
            mostrarError("Ingrese valores numéricos válidos para ID y edad.");
        } catch (IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        }
    }

    /**
     * Elimina un estudiante por su ID y actualiza la tabla.
     * @param e Evento de acción generado por el botón "Eliminar".
     */
    private void eliminarEstudiante(ActionEvent e) {
        try {
            int id = Integer.parseInt(campoId.getText());
            servicio.eliminarEstudiante(id);
            JOptionPane.showMessageDialog(this, "Estudiante eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            limpiarCampos();
            cargarTabla();
        } catch (NumberFormatException ex) {
            mostrarError("Ingrese un valor numérico válido para el ID.");
        } catch (IllegalArgumentException ex) {
            mostrarError(ex.getMessage());
        }
    }

    /**
     * Carga la lista de estudiantes en la tabla.
     */
    private void cargarTabla() {
        modeloTabla.setRowCount(0);
        List<Estudiante> estudiantes = servicio.listarEstudiantes();
        for (Estudiante est : estudiantes) {
            modeloTabla.addRow(new Object[]{est.getOrden(), est.getNombre(), est.getEdad()});
        }
    }

    /**
     * Limpia los campos de texto del formulario.
     */
    private void limpiarCampos() {
        campoId.setText("");
        campoNombre.setText("");
        campoEdad.setText("");
    }

    /**
     * Muestra un mensaje de error en una ventana emergente.
     * @param mensaje El mensaje de error a mostrar.
     */
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}