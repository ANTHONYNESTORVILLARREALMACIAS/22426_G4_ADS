package com.panaderia.ruminahui.controller;

import com.panaderia.ruminahui.model.Usuario;
import com.panaderia.ruminahui.model.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mindrot.jbcrypt.BCrypt;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioControllerTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioController usuarioController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void updateUsuario_SuccessWithPasswordChange() {
        // Arrange
        String id = "1";
        String nombre = "Nuevo Nombre";
        String password = "nuevaContraseña";

        Usuario existing = new Usuario(id, "admin", "Nombre Antiguo", "hashAntiguo");

        when(usuarioRepository.findById(id)).thenReturn(existing);
        when(usuarioRepository.update(any(Usuario.class))).thenReturn(null);

        // Act
        String result = usuarioController.updateUsuario(id, nombre, password);

        // Assert
        assertNull(result);
        verify(usuarioRepository).update(argThat(u ->
                u.getId().equals(id) &&
                        u.getNombre().equals(nombre) &&
                        !u.getPassword().equals("hashAntiguo") && // Verifica que el password cambió
                        u.getUsername().equals("admin") // Verifica que el username no cambió
        ));
    }

    @Test
    void updateUsuario_SuccessWithoutPasswordChange() {
        // Arrange
        String id = "1";
        String nombre = "Nuevo Nombre";
        String password = "";

        Usuario existing = new Usuario(id, "admin", "Nombre Antiguo", "hashAntiguo");

        when(usuarioRepository.findById(id)).thenReturn(existing);
        when(usuarioRepository.update(any(Usuario.class))).thenReturn(null);

        // Act
        String result = usuarioController.updateUsuario(id, nombre, password);

        // Assert
        assertNull(result);
        verify(usuarioRepository).update(argThat(u ->
                u.getId().equals(id) &&
                        u.getNombre().equals(nombre) &&
                        u.getPassword().equals("hashAntiguo") // Verifica que el password no cambió
        ));
    }

    @Test
    void updateUsuario_EmptyId() {
        // Act
        String result = usuarioController.updateUsuario("", "Nombre", "password");

        // Assert
        assertEquals("El ID del usuario es requerido.", result);
        verify(usuarioRepository, never()).update(any(Usuario.class));
    }

    @Test
    void updateUsuario_EmptyNombre() {
        // Act
        String result = usuarioController.updateUsuario("1", "", "password");

        // Assert
        assertEquals("El nombre es requerido.", result);
        verify(usuarioRepository, never()).update(any(Usuario.class));
    }

    @Test
    void updateUsuario_UserNotFound() {
        // Arrange
        when(usuarioRepository.findById("1")).thenReturn(null);

        // Act
        String result = usuarioController.updateUsuario("1", "Nombre", "password");

        // Assert
        assertEquals("Usuario no encontrado.", result);
        verify(usuarioRepository, never()).update(any(Usuario.class));
    }

    @Test
    void updateUsuario_RepositoryError() {
        // Arrange
        String id = "1";
        Usuario existing = new Usuario(id, "admin", "Nombre Antiguo", "hashAntiguo");

        when(usuarioRepository.findById(id)).thenReturn(existing);
        when(usuarioRepository.update(any(Usuario.class))).thenReturn("Error de base de datos");

        // Act
        String result = usuarioController.updateUsuario(id, "Nuevo Nombre", "nuevaContraseña");

        // Assert
        assertEquals("Error al actualizar el usuario: Error de base de datos", result);
    }

    @Test
    void getUsuarioById_Success() {
        // Arrange
        String id = "1";
        Usuario expected = new Usuario(id, "admin", "Administrador", "hash");

        when(usuarioRepository.findById(id)).thenReturn(expected);

        // Act
        Usuario result = usuarioController.getUsuarioById(id);

        // Assert
        assertEquals(expected, result);
        verify(usuarioRepository).findById(id);
    }

    @Test
    void getUsuarioById_NotFound() {
        // Arrange
        when(usuarioRepository.findById("1")).thenReturn(null);

        // Act
        Usuario result = usuarioController.getUsuarioById("1");

        // Assert
        assertNull(result);
    }
}