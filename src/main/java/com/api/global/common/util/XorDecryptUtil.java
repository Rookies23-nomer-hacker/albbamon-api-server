package com.api.global.common.util;

public class XorDecryptUtil {
    public static String xorDecrypt(String encryptedText, String key) {
        encryptedText = Base64Util.decode(encryptedText);
        if (key.isEmpty()) {
            throw new IllegalArgumentException("Key cannot be empty.");
        }

        StringBuilder expandedKey = new StringBuilder();
        while (expandedKey.length() < encryptedText.length()) {
            expandedKey.append(key);
        }
        String adjustedKey = expandedKey.substring(0, encryptedText.length());

        StringBuilder decryptedText = new StringBuilder();
        for (int i = 0; i < encryptedText.length(); i++) {
            char decryptedChar = (char) (encryptedText.charAt(i) ^ adjustedKey.charAt(i));
            decryptedText.append(decryptedChar);
        }
        return decryptedText.toString();
    }
}
