import java.util.Arrays;

/**
 * Класс для шифрования и расшифрования текста по системе Хилла (mod 26).
 * Работает только с английскими буквами A–Z.
 */
public class HillCipher {
    private final int[][] keyMatrix;      // Ключевая матрица шифрования (n×n)
    private final int[][] invKeyMatrix;   // Обратная матрица по модулю 26 (для расшифровки)
    private final int n;                  // Размер матрицы (порядок)

    /**
     * Конструктор: принимает ключевую матрицу.
     * Проверяет, что она квадратная и обратима по модулю 26.
     */
    public HillCipher(int[][] key) {
        this.n = key.length;
        this.keyMatrix = copyMatrix(key); // Сохраняем копию, чтобы избежать внешних изменений
        int det = mod(determinant(keyMatrix), 26);
        if (gcd(det, 26) != 1) {
            throw new IllegalArgumentException("Определитель матрицы должен быть взаимно прост с 26.");
        }
        this.invKeyMatrix = modMatrixInverse(keyMatrix, 26); // Вычисляем обратную матрицу
    }

    /**
     * Шифрует переданный текст.
     * Удаляет все символы, кроме букв A–Z, приводит к верхнему регистру,
     * дополняет текст символами 'X' до длины, кратной n.
     */
    public String encrypt(String plaintext) {
        String clean = cleanInput(plaintext); // Оставляем только A–Z
        // Дополняем текст до длины, кратной размеру блока
        while (clean.length() % n != 0) clean += 'X';

        StringBuilder res = new StringBuilder();
        // Обрабатываем текст блоками по n символов
        for (int i = 0; i < clean.length(); i += n) {
            int[] block = new int[n];
            // Преобразуем символы блока в числа (A=0, B=1, ..., Z=25)
            for (int j = 0; j < n; j++) block[j] = clean.charAt(i + j) - 'A';
            // Умножаем ключевую матрицу на вектор-блок
            int[] enc = multiplyMatrixVector(keyMatrix, block);
            // Преобразуем числа обратно в буквы
            for (int val : enc) res.append((char)(mod(val, 26) + 'A'));
        }
        return res.toString();
    }

    /**
     * Расшифровывает шифротекст.
     * Входная строка должна быть кратна n (иначе — ошибка).
     */
    public String decrypt(String ciphertext) {
        String clean = cleanInput(ciphertext);
        if (clean.length() % n != 0)
            throw new IllegalArgumentException("Длина шифротекста должна быть кратна размеру блока.");

        StringBuilder res = new StringBuilder();
        for (int i = 0; i < clean.length(); i += n) {
            int[] block = new int[n];
            for (int j = 0; j < n; j++) block[j] = clean.charAt(i + j) - 'A';
            // Умножаем обратную матрицу на вектор-блок
            int[] dec = multiplyMatrixVector(invKeyMatrix, block);
            for (int val : dec) res.append((char)(mod(val, 26) + 'A'));
        }
        return res.toString();
    }

    /**
     * Оставляет только буквы A–Z и приводит строку к верхнему регистру.
     */
    private String cleanInput(String s) {
        return s.toUpperCase().replaceAll("[^A-Z]", "");
    }

    /**
     * Умножает матрицу (n×n) на вектор (длины n) по модулю 26.
     */
    private int[] multiplyMatrixVector(int[][] mat, int[] vec) {
        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) res[i] += mat[i][j] * vec[j];
            res[i] = mod(res[i], 26); // Приводим результат по модулю 26
        }
        return res;
    }

    /**
     * Корректная операция взятия остатка по модулю (работает с отрицательными числами).
     */
    private int mod(int a, int m) {
        return ((a % m) + m) % m;
    }

    /**
     * Находит наибольший общий делитель (НОД) двух чисел (алгоритм Евклида).
     */
    private int gcd(int a, int b) {
        while (b != 0) {
            int t = b;
            b = a % b;
            a = t;
        }
        return a;
    }

    /**
     * Создаёт глубокую копию двумерного массива.
     */
    private int[][] copyMatrix(int[][] src) {
        return Arrays.stream(src).map(int[]::clone).toArray(int[][]::new);
    }

    /**
     * Вычисляет обратную матрицу по модулю mod.
     * Использует формулу: A⁻¹ = det(A)⁻¹ * adj(A) (mod mod)
     */
    private int[][] modMatrixInverse(int[][] mat, int mod) {
        int det = mod(determinant(mat), mod);
        int detInv = modInverse(det, mod); // Обратный элемент определителя
        if (detInv == -1) throw new ArithmeticException("Обратная матрица не существует.");
        int[][] adj = adjugateMatrix(mat); // Союзная (адъюнкт) матрица
        int[][] inv = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                inv[i][j] = mod(adj[i][j] * detInv, mod);
        return inv;
    }

    /**
     * Находит обратный элемент a⁻¹ по модулю m (т.е. a * a⁻¹ ≡ 1 (mod m)).
     * Используется простой перебор (допустимо, так как m = 26 — мало).
     */
    private int modInverse(int a, int m) {
        for (int x = 1; x < m; x++)
            if (mod(a * x, m) == 1) return x;
        return -1; // Не существует
    }

    /**
     * Рекурсивное вычисление определителя матрицы.
     */
    private int determinant(int[][] mat) {
        int n = mat.length;
        if (n == 1) return mat[0][0];
        if (n == 2) return mat[0][0] * mat[1][1] - mat[0][1] * mat[1][0];
        int det = 0;
        // Разложение по первой строке
        for (int col = 0; col < n; col++)
            det += ((col % 2 == 0) ? 1 : -1) * mat[0][col] * determinant(minor(mat, 0, col));
        return det;
    }

    /**
     * Возвращает минор матрицы — матрицу без указанной строки и столбца.
     */
    private int[][] minor(int[][] mat, int r, int c) {
        int n = mat.length - 1;
        int[][] m = new int[n][n];
        for (int i = 0, ri = 0; i < mat.length; i++) {
            if (i == r) continue; // Пропускаем удаляемую строку
            for (int j = 0, cj = 0; j < mat[i].length; j++) {
                if (j == c) continue; // Пропускаем удаляемый столбец
                m[ri][cj++] = mat[i][j];
            }
            ri++;
        }
        return m;
    }

    /**
     * Вычисляет союзную (адъюнкт) матрицу — транспонированную матрицу алгебраических дополнений.
     */
    private int[][] adjugateMatrix(int[][] mat) {
        int n = mat.length;
        int[][] cof = new int[n][n];
        // Строим матрицу кофакторов (алгебраических дополнений)
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                cof[i][j] = ((i + j) % 2 == 0 ? 1 : -1) * determinant(minor(mat, i, j));
        // Транспонируем её
        int[][] adj = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                adj[i][j] = cof[j][i];
        return adj;
    }
}