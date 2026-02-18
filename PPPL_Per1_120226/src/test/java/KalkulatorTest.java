import org.junit.jupiter.api.Assertions;import org.junit.jupiter.api.BeforeEach;import org.junit.jupiter.api.Test;

public class KalkulatorTest {
    private Kalkulator calc;

    @BeforeEach
    public void setUp() {calc = new Kalkulator();}

    @Test
    public void testPenjumlahan() {
        System.out.println("\n=== Test Penjumlahan Normal ===");
        calc.setNumber1(5);calc.setNumber2(3);

        int expected = 8;int actual = calc.penjumlahan();

        // Logging
        System.out.println("Input    : 5 + 3");System.out.println("Expected : " + expected);System.out.println("Actual   : " + actual);

        // Assert dengan Alert Message
        Assertions.assertNotNull(calc.getNumber1(), "ALERT: Number 1 tidak boleh null!");
        Assertions.assertNotNull(calc.getNumber2(), "ALERT: Number 2 tidak boleh null!");
        Assertions.assertEquals(expected, actual, "ALERT: Hasil penjumlahan tidak sesuai dengan yang diharapkan!");
    }

    @Test
    public void testPenjumlahanNegative() {
        System.out.println("\n=== Test Penjumlahan Negatif ===");
        calc.setNumber1(-10);calc.setNumber2(-5);

        int expected = -15;int actual = calc.penjumlahan();

        // Logging
        System.out.println("Input    : -10 + -5");System.out.println("Expected : " + expected);System.out.println("Actual   : " + actual);

        // Assert
        Assertions.assertEquals(expected, actual, "ALERT: Hasil penjumlahan negatif salah!");
    }

    @Test
    public void testPenjumlahanOverflowError() {
        System.out.println("\n=== Test Error: Integer Overflow ===");

        // Memasukkan nilai batas maksimal tipe data integer di Java (2,147,483,647)
        int maxValue = Integer.MAX_VALUE;
        calc.setNumber1(maxValue);calc.setNumber2(1);

        // Jika terjadi overflow, Java akan memutarnya menjadi nilai minimum negatif
        int expected = Integer.MIN_VALUE;int actual = calc.penjumlahan();

        // Logging
        System.out.println("Input    : " + maxValue + " + 1");
        System.out.println("Expected : " + expected + " (Nilai Minimum karena Overflow)");
        System.out.println("Actual   : " + actual);

        // Assert
        Assertions.assertEquals(expected, actual, "ALERT: Perilaku Integer Overflow tidak terdeteksi dengan benar!");
    }

    @Test
    public void testInputExceptionError() {
        System.out.println("\n=== Test Error: Input Exception ===");
        System.out.println("Mencoba memasukkan nilai ilegal (contoh: angka 0)...");

        /* * Catatan: Test ini mengasumsikan method setNumber1 di class Kalkulator
         * memiliki validasi yang melempar IllegalArgumentException jika diisi 0.
         * Jika belum ada validasinya di source code, test ini akan gagal/merah.
         */
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            calc.setNumber1(0);
        }, "ALERT: Method tidak melempar IllegalArgumentException saat diberi input ilegal!");

        // Logging hasil tangkapan error
        System.out.println("Error tertangkap : " + exception.getMessage());
        Assertions.assertNotNull(exception.getMessage(), "ALERT: Pesan error dari Exception tidak boleh kosong!");
    }
}