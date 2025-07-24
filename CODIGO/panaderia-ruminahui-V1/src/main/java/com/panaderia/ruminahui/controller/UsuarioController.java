package com.panaderia.ruminahui.controller;

import com.panaderia.ruminahui.model.Usuario;
import com.panaderia.ruminahui.model.UsuarioRepository;
import com.panaderia.ruminahui.util.Validator;

public class UsuarioController {
    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public String updateUsuario(String id, String username, String password, String nombre, String email) {
        if (!Validator.isValidUsername(username)) {
            return "El nombre de usuario debe tener al menos 3 caracteres.";
        }
        if (!Validator.isValidPassword(password)) {
            return "La contraseña debe tener al menos 6 caracteres.";
        }
        if (!Validator.isValidNombre(nombre)) {
            return "El nombre debe tener al menos 2 caracteres.";
        }
        if (!Validator.isValidEmail(email)) {
            return "El email no es válido.";
        }

        Usuario usuario = usuarioRepository.findById(id);
        if (usuario == null) {
            return "Usuario no encontrado.";
        }

        usuario.setUsername(username);
        usuario.setPassword(password);
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuarioRepository.update(usuario);
        return null; // Success
    }
}