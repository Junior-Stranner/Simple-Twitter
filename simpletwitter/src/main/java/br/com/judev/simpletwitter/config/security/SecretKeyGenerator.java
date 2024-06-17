package br.com.judev.simpletwitter.config.security;

import java.security.SecureRandom;
import java.util.Base64;

public class SecretKeyGenerator {
    public static void main(String[] args) {
        // Gerar uma chave secreta aleatória com 32 bytes (256 bits)
        byte[] secretKey = generateSecretKey(32);

        // Gerar uma chave secreta aleatória com 32 bytes (256 bits)
        String base64Key = Base64.getEncoder().encodeToString(secretKey);
        System.out.println("Chave secreta gerada: "+base64Key);
    }

    private static byte[] generateSecretKey(int keyLenght) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[keyLenght];
        secureRandom.nextBytes(key);
        return key;

    }
}
