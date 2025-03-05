package com.api.global.common.util;

public class XorEncryptUtil {
    public static String xorEncrypt(String plaintext, String key) {
        if (key.isEmpty()) {
            throw new IllegalArgumentException("Key cannot be empty.");
        }

        StringBuilder expandedKey = new StringBuilder();
        while (expandedKey.length() < plaintext.length()) {
            expandedKey.append(key);
        }
        String adjustedKey = expandedKey.substring(0, plaintext.length());

        StringBuilder encryptedText = new StringBuilder();
        for (int i = 0; i < plaintext.length(); i++) {
            char encryptedChar = (char) (plaintext.charAt(i) ^ adjustedKey.charAt(i));
            encryptedText.append(encryptedChar);
        }
        return Base64Util.encode(encryptedText.toString());
    }
}
