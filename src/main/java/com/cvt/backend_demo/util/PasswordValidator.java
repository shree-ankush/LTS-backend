package com.cvt.backend_demo.util;

public class PasswordValidator {
    public static boolean isValid(String password) {
        return password != null &&
                password.length() >= 8 &&
                password.matches(".*[A-Z].*") &&       // At least one uppercase
                password.matches(".*[a-z].*") &&       // At least one lowercase
                password.matches(".*\\d.*") &&         // At least one digit
                password.matches(".*[!@#$%^&*()].*");  // At least one special character
    }
}
