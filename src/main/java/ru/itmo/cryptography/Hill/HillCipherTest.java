package ru.itmo.cryptography.Hill;

public class HillCipherTest {
    public static void main(String[] args) {
        String plainText = """
                В криптографии шифр Хилла — это полиграммный шифр подстановки, основанный на линейной алгебре.
                Изобретённый Лестером Хиллом в 1929 году, он стал первым шифром, работающим с блоками букв.
                Эта реализация использует матрицу размером 4 на 4 и работает только с русским алфавитом.
                """;

        System.out.println("\n" + "=".repeat(60));
        System.out.println("ТЕСТ ШИФРА ХИЛЛА , МАТРИЦА 4×4)");
        System.out.println("=".repeat(60));

        String encrypted = HillCipher.encrypt(plainText);
        System.out.println("Зашифрованный текст:");
        System.out.println(encrypted);

        String decrypted = HillCipher.decrypt(encrypted);
        System.out.println("\nРасшифрованный текст:");
        System.out.println(decrypted);

        // Очистка исходного текста: только буквы из алфавита
        StringBuilder cleanOriginal = new StringBuilder();
        for (char c : plainText.toLowerCase().toCharArray()) {
            if ("абвгдеёжзийклмнопрстуфхцчшщъыьэюя".indexOf(c) != -1) {
                cleanOriginal.append(c);
            }
        }
        // Дополнение 'я' до длины, кратной 4 (как в encrypt)
        while (cleanOriginal.length() % 4 != 0) {
            cleanOriginal.append('я');
        }

        boolean check = cleanOriginal.toString().equals(decrypted);
        System.out.println("\n✅ Проверка шифра Хилла (рус.): " + (check ? "УСПЕШНО" : "ОШИБКА!"));
        System.out.println("Длина текста (только буквы): " + cleanOriginal.length());
    }
}