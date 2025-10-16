package ru.itmo.cryptography;

import ru.itmo.cryptography.Caesar.CaesarCipher;

public class Main {
    public static void main(String[] args) {
        String originalText = """ 
Таким образом, дальнейшее развитие различных форм деятельности играет важную роль в формировании существующих финансовых и административных условий.

Значимость этих проблем настолько очевидна, что социально-экономическое развитие способствует подготовке и реализации экономической целесообразности принимаемых решений. Дорогие друзья, сложившаяся структура организации требует от нас анализа дальнейших направлений развития проекта.

Дорогие друзья, консультация с профессионалами из IT позволяет выполнить важнейшие задания по разработке ключевых компонентов планируемого обновления? Не следует, однако, забывать о том, что начало повседневной работы по формированию позиции способствует подготовке и реализации дальнейших направлений развития проекта?

Повседневная практика показывает, что социально-экономическое развитие представляет собой интересный эксперимент проверки дальнейших направлений развитая системы массового...
                """
                ;
        System.out.println("===Шифр Цезаря (смещение = 30) ===\n");
        System.out.println("Исходный текст:");
        System.out.println(originalText);
        System.out.println("\n" + "=".repeat(60) + "\n");

        String encryptedText = CaesarCipher.encrypt(originalText);
        System.out.println("Зашифрованный текст:");
        System.out.println(encryptedText);
        System.out.println("\n" + "=".repeat(60) + "\n");

        String decryptedText = CaesarCipher.decrypt(encryptedText);
        System.out.println("Расшифрованный текст");
        System.out.println(decryptedText);
        System.out.println("\n" + "=".repeat(60) + "\n");

        boolean isCorrect = originalText.equals(decryptedText);
        System.out.println("Проверка успешна: " + isCorrect);
    }
}