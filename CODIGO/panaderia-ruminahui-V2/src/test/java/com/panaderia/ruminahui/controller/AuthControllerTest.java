package com.panaderia.ruminahui.controller;

import com.panaderia.ruminahui.model.Usuario;
import com.panaderia.ruminahui.model.UsuarioRepository;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class AuthControllerTest {

    @Test
    public void testLoginWithValidCredentials() {
        // Arrange
        UsuarioRepository mockRepo = mock(UsuarioRepository.class);
        Usuario usuario = new Usuario("1", "admin", "admin123", "Administrador");
        when(mockRepo.findByUsername("admin")).thenReturn(usuario);

        AuthController controller = new AuthController(mockRepo);

        // Act
        String result = controller.login("admin", "admin123");

        // Assert
        assertNull(result); // Login exitoso retorna null
    }

    @Test
    public void testLoginWithInvalidPassword() {
        UsuarioRepository mockRepo = mock(UsuarioRepository.class);
        Usuario usuario = new Usuario("1", "admin", "admin123", "Administrador");
        when(mockRepo.findByUsername("admin")).thenReturn(usuario);

        AuthController controller = new AuthController(mockRepo);

        String result = controller.login("admin", "wrongpassword");

        assertEquals("Credenciales inválidas.", result);
    }

    @Test
    public void testLoginWithUnknownUser() {
        UsuarioRepository mockRepo = mock(UsuarioRepository.class);
        when(mockRepo.findByUsername("no_existe")).thenReturn(null);

        AuthController controller = new AuthController(mockRepo);

        String result = controller.login("no_existe", "any");

        assertEquals("Credenciales inválidas.", result);
    }

    @Test
    public void testLoginWithInvalidUsernameFormat() {
        UsuarioRepository mockRepo = mock(UsuarioRepository.class);
        AuthController controller = new AuthController(mockRepo);

        String result = controller.login("ab", "admin123"); // Menos de 3 caracteres

        assertEquals("El nombre de usuario debe tener al menos 3 caracteres.", result);
    }

    @Test
    public void testLoginWithInvalidPasswordFormat() {
        UsuarioRepository mockRepo = mock(UsuarioRepository.class);
        AuthController controller = new AuthController(mockRepo);

        String result = controller.login("admin", "123"); // Menos de 6 caracteres

        assertEquals("La contraseña debe tener al menos 6 caracteres.", result);
    }
}