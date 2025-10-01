package ru.itmo.cryptography;

public class HillCipher {

    private static final String ALPHABET = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";
    private static final int MOD = 33;
    private static final int N = 4;

    // Ключевая матрица 4x4, обратима по модулю 33 (det = 127, gcd(127,33)=1)
    private static final int[][] KEY = {
            {2, 5, 1, 8},
            {3, 7, 4, 10},
            {6, 1, 9, 12},
            {1, 4, 3, 7}
    };

    public static String encrypt(String text) {
        StringBuilder clean = new StringBuilder();
        for (char c : text.toLowerCase().toCharArray()) {
            if (ALPHABET.indexOf(c) != -1) {
                clean.append(c);
            }
        }
        while (clean.length() % N != 0) {
            clean.append('я'); // паддинг
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < clean.length(); i += N) {
            int[] block = new int[N];
            for (int j = 0; j < N; j++) {
                block[j] = ALPHABET.indexOf(clean.charAt(i + j));
            }
            int[] encrypted = multiply(KEY, block);
            for (int val : encrypted) {
                result.append(ALPHABET.charAt(val));
            }
        }
        return result.toString();
    }

    public static String decrypt(String cipher) {
        int[][] invKey = inverseMatrix(KEY);
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < cipher.length(); i += N) {
            int[] block = new int[N];
            for (int j = 0; j < N; j++) {
                block[j] = ALPHABET.indexOf(cipher.charAt(i + j));
            }
            int[] decrypted = multiply(invKey, block);
            for (int val : decrypted) {
                result.append(ALPHABET.charAt(val));
            }
        }
        return result.toString();
    }

    private static int[] multiply(int[][] mat, int[] vec) {
        int[] res = new int[N];
        for (int i = 0; i < N; i++) {
            int sum = 0;
            for (int j = 0; j < N; j++) {
                sum += mat[i][j] * vec[j];
            }
            res[i] = Math.floorMod(sum, MOD);
        }
        return res;
    }

    // --- Вспомогательные методы для обратной матрицы (mod 33) ---

    private static int det(int[][] m) {
        int n = m.length;
        if (n == 1) return m[0][0];
        if (n == 2) return m[0][0] * m[1][1] - m[0][1] * m[1][0];
        int d = 0;
        for (int j = 0; j < n; j++) {
            int[][] minor = new int[n-1][n-1];
            for (int i = 1; i < n; i++) {
                for (int k = 0, col = 0; k < n; k++) {
                    if (k == j) continue;
                    minor[i-1][col++] = m[i][k];
                }
            }
            int sign = (j % 2 == 0) ? 1 : -1;
            d += sign * m[0][j] * det(minor);
        }
        return d;
    }

    private static int[][] adjugate(int[][] m) {
        int n = m.length;
        int[][] adj = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int[][] minor = new int[n-1][n-1];
                for (int ii = 0, r = 0; ii < n; ii++) {
                    if (ii == i) continue;
                    for (int jj = 0, c = 0; jj < n; jj++) {
                        if (jj == j) continue;
                        minor[r][c++] = m[ii][jj];
                    }
                    r++;
                }
                int cof = (int) Math.pow(-1, i + j) * det(minor);
                adj[j][i] = cof; // транспонируем
            }
        }
        return adj;
    }

    private static int gcd(int a, int b) {
        while (b != 0) {
            int t = b;
            b = a % b;
            a = t;
        }
        return Math.abs(a);
    }

    private static int modInv(int a, int m) {
        a = Math.floorMod(a, m);
        for (int x = 1; x < m; x++) {
            if (Math.floorMod(a * x, m) == 1) return x;
        }
        throw new RuntimeException("Нет обратного по модулю " + m);
    }

    private static int[][] inverseMatrix(int[][] m) {
        int d = det(m);
        d = Math.floorMod(d, MOD);
        if (gcd(d, MOD) != 1) {
            throw new RuntimeException("Матрица необратима (det=" + d + ")");
        }
        int invDet = modInv(d, MOD);
        int[][] adj = adjugate(m);
        int[][] inv = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                inv[i][j] = Math.floorMod(invDet * adj[i][j], MOD);
            }
        }
        return inv;
    }
}