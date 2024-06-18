package br.com.judev.simpletwitter.config.security;

import java.security.SecureRandom;
import java.util.Base64;

public class SecretKeyGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int SECRET_KEY_LENGTH = 32;

    public static void main(String[] args) {
        SecureRandom random = new SecureRandom();
        StringBuilder secretKey = new StringBuilder(SECRET_KEY_LENGTH);

        for (int i = 0; i < SECRET_KEY_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            secretKey.append(CHARACTERS.charAt(index));
        }

        System.out.println("Generated Secret Key: " + secretKey.toString());
    }
}
