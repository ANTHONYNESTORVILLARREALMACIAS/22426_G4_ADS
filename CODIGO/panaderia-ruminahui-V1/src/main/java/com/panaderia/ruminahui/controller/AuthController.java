package com.panaderia.ruminahui.controller;

import com.panaderia.ruminahui.model.Usuario;
import com.panaderia.ruminahui.model.UsuarioRepository;
import com.panaderia.ruminahui.util.Validator;

public class AuthController {
    private final UsuarioRepository usuarioRepository;

    public AuthController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public String login(String username, String password) {
        if (!Validator.isValidUsername(username)) {
            return "El nombre de usuario debe tener al menos 3 caracteres.";
        }
        if (!Validator.isValidPassword(password)) {
            return "La contraseña debe tener al menos 6 caracteres.";
        }

        Usuario usuario = usuarioRepository.findByUsername(username);
        if (usuario == null || !usuario.getPassword().equals(password)) {
            return "Credenciales inválidas.";
        }

        return null; // Success
    }
}