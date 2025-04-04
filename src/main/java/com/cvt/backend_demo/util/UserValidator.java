package com.cvt.backend_demo.util;

import java.util.regex.Pattern;

public class UserValidator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty();
    }

    public static boolean isValidPassword(String password) {
        // At least 8 characters, one upper, one lower, one digit, one special char
        return password != null && password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$");
    }

    // ✅ THIS is the method you need to make sure is present
    public static boolean isValidUser(String username, String email, String firstName,
                                      String lastName, String organization, String password) {
        return isValidName(username) &&
                isValidEmail(email) &&
                isValidName(firstName) &&
                isValidName(lastName) &&
                isValidName(organization) &&
                isValidPassword(password);
    }
}
