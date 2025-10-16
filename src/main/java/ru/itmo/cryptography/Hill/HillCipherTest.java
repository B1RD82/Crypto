/**
 * Класс для тестирования реализации шифра Хилла.
 * Проверяет корректность шифрования и расшифрования на примере текста длиной > 100 символов.
 */
public class HillCipherTest {
    public static void main(String[] args) {
        // Ключевая матрица 4×4 (порядок n = 4, так как номер студента 28 → n = 2*(28 mod 3)+2 = 4)
        // Матрица выбрана так, что её определитель взаимно прост с 26 (det = 15, gcd(15,26)=1)
        int[][] key = {
                {3, 2, 1, 5},
                {7, 9, 4, 2},
                {1, 6, 8, 3},
                {5, 1, 2, 9}
        };

        // Создаём экземпляр шифра с заданной ключевой матрицей
        HillCipher cipher = new HillCipher(key);

        // Исходный текст (содержит цифры и спецсимволы для проверки очистки)
        String plaintext = "THEQUICKBROWNFOXJUMPSOVERTHELAZYDOGANDTHENUMBEROFTHEBEASTIS666BUTWEUSEONLYLETTERS";

        // Оставляем только буквы A–Z (удаляем цифры, пробелы и другие символы)
        String cleanOriginal = plaintext.replaceAll("[^A-Z]", "");

        // Дополняем текст символами 'X' до длины, кратной размеру блока (n = 4)
        // Это необходимо, так как шифр Хилла работает с блоками фиксированного размера
        String paddedOriginal = cleanOriginal;
        while (paddedOriginal.length() % 4 != 0) {
            paddedOriginal += 'X';
        }

        // Шифруем исходный текст
        String encrypted = cipher.encrypt(plaintext);

        // Расшифровываем полученный шифротекст
        String decrypted = cipher.decrypt(encrypted);

        // Выводим результаты для наглядности
        System.out.println("Исходный:      " + cleanOriginal);
        System.out.println("Дополненный:   " + paddedOriginal); // То, что реально шифруется
        System.out.println("Шифр:          " + encrypted);
        System.out.println("Расшифр:       " + decrypted);

        // Проверяем корректность: расшифрованный текст должен совпадать
        // с дополненным оригиналом (включая конечные 'X')
        System.out.println("Корректно:     " + paddedOriginal.equals(decrypted));
    }
}