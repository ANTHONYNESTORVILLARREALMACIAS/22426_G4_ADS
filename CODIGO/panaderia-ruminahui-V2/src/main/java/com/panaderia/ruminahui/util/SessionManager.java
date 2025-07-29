package com.panaderia.ruminahui.util;

import com.panaderia.ruminahui.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class SessionManager {
    private static final String SECRET_KEY = "panaderia_ruminahui_secret_key_2025";
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour
    private static Usuario loggedInUser;
    private static String currentToken;

    public static String generateToken(Usuario usuario) {
        String token = Jwts.builder()
                .setSubject(usuario.getId())
                .claim("username", usuario.getUsername())
                .claim("nombre", usuario.getNombre())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
        currentToken = token;
        loggedInUser = usuario; // Ensure user is set in session
        return token;
    }

    public static boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            loggedInUser = new Usuario(
                    claims.getSubject(),
                    claims.get("username", String.class),
                    claims.get("nombre", String.class),
                    null
            );
            currentToken = token;
            return true;
        } catch (Exception e) {
            loggedInUser = null;
            currentToken = null;
            return false;
        }
    }

    public static boolean login(Usuario usuario, String token) {
        if (validateToken(token)) {
            loggedInUser = usuario;
            currentToken = token;
            return true;
        }
        return false;
    }

    public static boolean isLoggedIn() {
        return loggedInUser != null && currentToken != null && validateToken(currentToken);
    }

    public static Usuario getLoggedInUser() {
        return loggedInUser;
    }

    public static void setLoggedInUser(Usuario usuario) {
        loggedInUser = usuario;
        // Regenerate token to reflect updated user data
        if (usuario != null) {
            generateToken(usuario);
        }
    }

    public static void logout() {
        loggedInUser = null;
        currentToken = null;
    }
}