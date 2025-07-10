package org.example;

import org.example.repository.EstudianteDAO;
import org.example.repository.EstudianteRepository;
import org.example.service.EstudianteService;
import org.example.view.EstudiantePrinter;
import org.example.view.Menu;

public class Main {
    public static void main(String[] args) {
        // Configuración de dependencias
        EstudianteRepository repository = new EstudianteDAO();
        EstudianteService service = new EstudianteService(repository);
        EstudiantePrinter printer = new EstudiantePrinter();

        // Crear e iniciar el menú
        Menu menu = new Menu(service, printer);
        menu.iniciar();
    }
}