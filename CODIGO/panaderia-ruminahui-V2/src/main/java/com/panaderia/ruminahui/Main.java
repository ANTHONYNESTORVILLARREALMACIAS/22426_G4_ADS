package com.panaderia.ruminahui;

import com.formdev.flatlaf.FlatLightLaf;
import com.panaderia.ruminahui.util.DatabaseInitializer;
import com.panaderia.ruminahui.util.MongoDBConfig;
import com.panaderia.ruminahui.view.LoginView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Set FlatLaf look-and-feel
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            System.err.println("Error setting FlatLaf: " + e.getMessage());
        }

        // Initialize MongoDB connection and database
        try {
            MongoDBConfig.getDatabase();
            // Uncomment the following line to reset admin user for debugging login issues
            // DatabaseInitializer.resetAdminUser();
            DatabaseInitializer.initialize();
        } catch (Exception e) {
            System.err.println("Error connecting to MongoDB or initializing database: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "No se pudo conectar a la base de datos o inicializarla.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // Launch LoginView on EDT
        SwingUtilities.invokeLater(() -> {
            new LoginView().setVisible(true);
        });

        // Add shutdown hook to close MongoDB connection
        Runtime.getRuntime().addShutdownHook(new Thread(MongoDBConfig::close));
    }
}