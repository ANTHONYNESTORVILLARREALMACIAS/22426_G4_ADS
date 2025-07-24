package com.panaderia.ruminahui;

import com.formdev.flatlaf.FlatLightLaf;
import com.panaderia.ruminahui.view.LoginView;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new FlatLightLaf());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new LoginView().setVisible(true);
        });
    }
}