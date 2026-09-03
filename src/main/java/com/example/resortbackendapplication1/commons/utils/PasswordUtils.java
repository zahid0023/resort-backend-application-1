package com.example.resortbackendapplication1.commons.utils;

import lombok.experimental.UtilityClass;

import java.security.SecureRandom;
import java.util.stream.Collectors;

@UtilityClass
public class PasswordUtils {

    private final String PASSWORD_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private final int DEFAULT_PASSWORD_LENGTH = 7;
    private final SecureRandom PASSWORD_RANDOM = new SecureRandom();

    public String generateRandomPassword() {
        return generateRandomPassword(DEFAULT_PASSWORD_LENGTH);
    }

    public String generateRandomPassword(int length) {
        return PASSWORD_RANDOM.ints(length, 0, PASSWORD_CHARACTERS.length())
                .mapToObj(PASSWORD_CHARACTERS::charAt)
                .map(String::valueOf)
                .collect(Collectors.joining());
    }
}
