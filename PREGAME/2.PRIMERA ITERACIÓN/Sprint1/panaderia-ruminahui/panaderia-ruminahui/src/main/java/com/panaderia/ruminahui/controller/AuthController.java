package com.panaderia.ruminahui.controller;

import com.panaderia.ruminahui.model.User;
import com.panaderia.ruminahui.model.UserRepository;
import com.panaderia.ruminahui.util.Validator;

public class AuthController {
    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String login(String username, String password) {
        if (!Validator.isValidUsername(username)) {
            return "El nombre de usuario debe tener al menos 3 caracteres.";
        }
        if (!Validator.isValidPassword(password)) {
            return "La contraseña debe tener al menos 6 caracteres.";
        }

        User user = userRepository.findByUsername(username);
        if (user == null || !user.getPassword().equals(password)) {
            return "Credenciales inválidas.";
        }

        return null; // Success
    }
}