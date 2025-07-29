package com.panaderia.ruminahui.controller;

import com.panaderia.ruminahui.model.Usuario;
import com.panaderia.ruminahui.model.UsuarioRepository;
import com.panaderia.ruminahui.util.SessionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mindrot.jbcrypt.BCrypt;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoginControllerTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private SessionManager sessionManager;

    @InjectMocks
    private LoginController loginController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void authenticate_SuccessfulAdminLogin() {
        // Arrange
        String username = "admin";
        String password = "admin123";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        Usuario usuario = new Usuario("1", username, "Administrador", hashedPassword);
        String expectedToken = "generated-token";

        when(usuarioRepository.findByUsername(username)).thenReturn(usuario);
        when(sessionManager.generateToken(usuario)).thenReturn(expectedToken);
        when(sessionManager.login(usuario, expectedToken)).thenReturn(true);

        // Act
        String result = loginController.authenticate(username, password);

        // Assert
        assertNull(result);
        verify(usuarioRepository, times(1)).findByUsername(username);
        verify(sessionManager, times(1)).generateToken(usuario);
        verify(sessionManager, times(1)).login(usuario, expectedToken);
    }

    @Test
    void authenticate_EmptyCredentials() {
        // Act
        String result1 = loginController.authenticate("", "password");
        String result2 = loginController.authenticate("admin", "");
        String result3 = loginController.authenticate("", "");
        String result4 = loginController.authenticate(null, null);

        // Assert
        assertEquals("Usuario y contraseña son requeridos.", result1);
        assertEquals("Usuario y contraseña son requeridos.", result2);
        assertEquals("Usuario y contraseña son requeridos.", result3);
        assertEquals("Usuario y contraseña son requeridos.", result4);
        verify(usuarioRepository, never()).findByUsername(anyString());
    }

    @Test
    void authenticate_NonAdminUser() {
        // Arrange
        String username = "user1";
        String password = "user123";

        // Act
        String result = loginController.authenticate(username, password);

        // Assert
        assertEquals("Solo el administrador puede iniciar sesión.", result);
        verify(usuarioRepository, never()).findByUsername(anyString());
    }

    @Test
    void authenticate_UserNotFound() {
        // Arrange
        String username = "admin";
        String password = "admin123";

        when(usuarioRepository.findByUsername(username)).thenReturn(null);

        // Act
        String result = loginController.authenticate(username, password);

        // Assert
        assertEquals("Usuario no encontrado. Verifique que el usuario 'admin' esté registrado.", result);
        verify(usuarioRepository, times(1)).findByUsername(username);
    }

    @Test
    void authenticate_WrongPassword() {
        // Arrange
        String username = "admin";
        String password = "wrongpassword";
        String hashedPassword = BCrypt.hashpw("correctpassword", BCrypt.gensalt());
        Usuario usuario = new Usuario("1", username, "Administrador", hashedPassword);

        when(usuarioRepository.findByUsername(username)).thenReturn(usuario);

        // Act
        String result = loginController.authenticate(username, password);

        // Assert
        assertEquals("Contraseña incorrecta. Intente de nuevo.", result);
        verify(usuarioRepository, times(1)).findByUsername(username);
    }

    @Test
    void authenticate_SessionManagerError() {
        // Arrange
        String username = "admin";
        String password = "admin123";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        Usuario usuario = new Usuario("1", username, "Administrador", hashedPassword);
        String token = "generated-token";

        when(usuarioRepository.findByUsername(username)).thenReturn(usuario);
        when(sessionManager.generateToken(usuario)).thenReturn(token);
        when(sessionManager.login(usuario, token)).thenReturn(false);

        // Act
        String result = loginController.authenticate(username, password);

        // Assert
        assertEquals("Error al iniciar sesión. Intente de nuevo.", result);
        verify(usuarioRepository, times(1)).findByUsername(username);
        verify(sessionManager, times(1)).generateToken(usuario);
        verify(sessionManager, times(1)).login(usuario, token);
    }
}