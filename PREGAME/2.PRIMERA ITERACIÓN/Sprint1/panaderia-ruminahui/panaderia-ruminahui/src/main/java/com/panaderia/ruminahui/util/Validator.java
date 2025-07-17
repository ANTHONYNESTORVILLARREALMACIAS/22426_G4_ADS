package com.panaderia.ruminahui.util;

public class Validator {
    public static boolean isValidUsername(String username) {
        return username != null && !username.trim().isEmpty() && username.length() >= 3;
    }

    public static boolean isValidPassword(String password) {
        return password != null && !password.trim().isEmpty() && password.length() >= 6;
    }

    public static boolean isValidSectionName(String name) {
        return name != null && !name.trim().isEmpty() && name.length() >= 2;
    }

    public static boolean isValidSectionDescription(String description) {
        return description != null && !description.trim().isEmpty();
    }
}