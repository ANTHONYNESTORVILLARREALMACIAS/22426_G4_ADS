package com.panaderia.ruminahui.controller;

import com.panaderia.ruminahui.model.Usuario;
import com.panaderia.ruminahui.model.UsuarioRepository;
import org.mindrot.jbcrypt.BCrypt;

public class UsuarioController {
    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public String updateUsuario(String id, String nombre, String password) {
        if (id == null || id.isEmpty()) {
            return "El ID del usuario es requerido.";
        }
        if (nombre == null || nombre.isEmpty()) {
            return "El nombre es requerido.";
        }

        Usuario existingUsuario = usuarioRepository.findById(id);
        if (existingUsuario == null) {
            return "Usuario no encontrado.";
        }

        // Update user data, preserving the existing username
        String hashedPassword = password != null && !password.isEmpty()
                ? BCrypt.hashpw(password, BCrypt.gensalt())
                : existingUsuario.getPassword(); // Keep existing password if not provided
        Usuario usuario = new Usuario(id, existingUsuario.getUsername(), nombre, hashedPassword);
        String result = usuarioRepository.update(usuario);
        if (result == null) {
            return null; // Success
        } else {
            return "Error al actualizar el usuario: " + result;
        }
    }

    public Usuario getUsuarioById(String id) {
        return usuarioRepository.findById(id);
    }
}