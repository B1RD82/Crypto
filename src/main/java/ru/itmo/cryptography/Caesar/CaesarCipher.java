package ru.itmo.cryptography.Caesar;

public class CaesarCipher {
    private static final String ALPHABET = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";
    private static final int SHIFT = 30;

    public static String encrypt(String text) {
        StringBuilder encrypted = new StringBuilder();
        for (char c : text.toCharArray()) {
            int index = ALPHABET.indexOf(Character.toLowerCase(c));
            if (index != -1) {
                int newIndex = (index + SHIFT) % ALPHABET.length();
                char shiftedChar = ALPHABET.charAt(newIndex);
                encrypted.append(Character.isUpperCase(c) ? Character.toUpperCase(shiftedChar) : shiftedChar);
            } else {
                encrypted.append(c);
            }
        }

        return encrypted.toString();

    }
    public static String decrypt(String text) {
        StringBuilder decrypted = new StringBuilder();
        for (char c : text.toCharArray()) {
            int index = ALPHABET.indexOf(Character.toLowerCase(c));
            if (index != -1) {
                int newIndex = (index - SHIFT + ALPHABET.length()) % ALPHABET.length();
                char shiftedChar = ALPHABET.charAt(newIndex);
                decrypted.append(Character.isUpperCase(c) ? Character.toUpperCase(shiftedChar): shiftedChar);
            } else {
                decrypted.append(c);
            }
        }
        return decrypted.toString();
    }
}
