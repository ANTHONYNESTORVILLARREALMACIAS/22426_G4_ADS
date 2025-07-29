package com.panaderia.ruminahui.util;

import com.mongodb.client.MongoDatabase;
import com.panaderia.ruminahui.model.Usuario;
import com.panaderia.ruminahui.model.UsuarioRepository;
import org.mindrot.jbcrypt.BCrypt;

public class DatabaseInitializer {
    public static void initialize() {
        MongoDatabase database = MongoDBConfig.getDatabase();
        UsuarioRepository usuarioRepository = new UsuarioRepository();

        // Check if admin user exists
        if (usuarioRepository.findByUsername("admin") == null) {
            String hashedPassword = BCrypt.hashpw("admin123", BCrypt.gensalt());
            Usuario admin = new Usuario(IDGenerator.generateID("usuarios"), "admin", "Administrador", hashedPassword);
            String result = usuarioRepository.create(admin);
            if (result == null) {
                System.out.println("Admin user created successfully.");
            } else {
                System.err.println("Error creating admin user: " + result);
            }
        } else {
            System.out.println("Admin user already exists.");
        }
    }

    public static void resetAdminUser() {
        MongoDatabase database = MongoDBConfig.getDatabase();
        UsuarioRepository usuarioRepository = new UsuarioRepository();

        // Delete existing admin user
        Usuario existingAdmin = usuarioRepository.findByUsername("admin");
        if (existingAdmin != null) {
            usuarioRepository.delete(existingAdmin.getId());
            System.out.println("Existing admin user deleted.");
        }

        // Create fresh admin user
        String hashedPassword = BCrypt.hashpw("admin123", BCrypt.gensalt());
        Usuario admin = new Usuario(IDGenerator.generateID("usuarios"), "admin", "Administrador", hashedPassword);
        String result = usuarioRepository.create(admin);
        if (result == null) {
            System.out.println("Admin user reset successfully.");
        } else {
            System.err.println("Error resetting admin user: " + result);
        }
    }
}