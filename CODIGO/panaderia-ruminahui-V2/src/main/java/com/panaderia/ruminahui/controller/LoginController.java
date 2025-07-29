package com.panaderia.ruminahui.controller;

import com.panaderia.ruminahui.model.Usuario;
import com.panaderia.ruminahui.model.UsuarioRepository;
import com.panaderia.ruminahui.util.SessionManager;
import org.mindrot.jbcrypt.BCrypt;

public class LoginController {
    private final UsuarioRepository usuarioRepository;

    public LoginController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public String authenticate(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return "Usuario y contraseña son requeridos.";
        }

        // Enforce admin-only login
        if (!username.equals("admin")) {
            return "Solo el administrador puede iniciar sesión.";
        }

        Usuario usuario = usuarioRepository.findByUsername(username);
        if (usuario == null) {
            return "Usuario no encontrado. Verifique que el usuario 'admin' esté registrado.";
        }

        if (!BCrypt.checkpw(password, usuario.getPassword())) {
            return "Contraseña incorrecta. Intente de nuevo.";
        }

        String token = SessionManager.generateToken(usuario);
        if (SessionManager.login(usuario, token)) {
            return null; // Success
        } else {
            return "Error al iniciar sesión. Intente de nuevo.";
        }
    }
}