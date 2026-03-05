import org.example.WalletNest;
import org.junit.jupiter.api.*;
import java.util.ArrayList;
import java.util.List;

@DisplayName("Pengujian Utama: Wallet (Parent)")
public class WalletNestTest {

    // Variabel Static (Class Level) karena diinisialisasi di @BeforeAll
    private static WalletNest wallet;
    private static List<String> dummyCards;
    private static double initialCash;

    // -----------------------------------------------------------
    // 1. Initialization (Class Level - PARENT)
    // -----------------------------------------------------------
    @BeforeAll
    static void initClass() {
        System.out.println("--- @BeforeAll [PARENT]: Membuat Object Wallet & List (Dijalankan SEKALI) ---");
        wallet = new WalletNest();
        dummyCards = new ArrayList<>();
    }

    // -----------------------------------------------------------
    // 2. Cleanup (Class Level - PARENT)
    // -----------------------------------------------------------
    @AfterAll
    static void cleanClass() {
        System.out.println("\n--- @AfterAll [PARENT]: Menghapus Object Wallet (Dijalankan SEKALI) ---");
        wallet = null;
        dummyCards = null;
    }

    // -----------------------------------------------------------
    // 3. Initialization (Method Level - PARENT)
    // -----------------------------------------------------------
    @BeforeEach
    void initMethod() {
        System.out.println("\n--- @BeforeEach [PARENT]: Menyiapkan Data Kartu & Uang Dummy ---");
        dummyCards.add("KTP");
        dummyCards.add("ATM BCA");
        initialCash = 100000;
        System.out.println("   -> Jumlah kartu dummy disiapkan: " + dummyCards.size());
    }

    // -----------------------------------------------------------
    // 4. Cleanup (Method Level - PARENT)
    // -----------------------------------------------------------
    @AfterEach
    void cleanMethod() {
        System.out.println("--- @AfterEach [PARENT]: Membersihkan List & Reset Wallet ke Kondisi Pabrik ---");
        for (String card : dummyCards) {
            wallet.removeCard(card);
        }
        double currentBalance = wallet.getCashBalance();
        if (currentBalance > 0) {
            wallet.withdrawCash(currentBalance);
        }
        wallet.setOwner(null);
        dummyCards.clear();
    }

    // --- TEST DI LEVEL PARENT ---
    @Test
    @DisplayName("Test 1 [PARENT]: Verifikasi Dompet Masih Kosong")
    void testDompetKosongAwal() {
        System.out.println("   [Eksekusi Test 1 PARENT: Dompet Kosong]");
        Assertions.assertNull(wallet.getOwner());
        Assertions.assertEquals(0, wallet.getCashBalance());
        System.out.println("   -> Sukses! Dompet terverifikasi kosong.");
    }

    // ===========================================================
    // NESTED CLASS: Skenario Khusus Dompet Sudah Terisi
    // ===========================================================
    @Nested
    @DisplayName("Skenario Nested: Dompet Terisi Penuh")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class SkenarioDompetTerisi {

        // Setup tambahan HANYA untuk test di dalam Nested Class ini
        @BeforeEach
        void isiDompet() {
            System.out.println("--- @BeforeEach [NESTED]: Mengisi Dompet dengan Data Dummy secara Otomatis ---");
            wallet.setOwner("Budi Santoso");
            for (String card : dummyCards) {
                wallet.addCard(card);
            }
            wallet.addCash(initialCash);
        }

        @Test
        @Order(1)
        @DisplayName("Test 2 [NESTED]: Verifikasi Isi Dompet")
        void testVerifikasiIsi() {
            System.out.println("   [Eksekusi Test 2 NESTED: Cek Isi Dompet]");

            // Verification langsung karena otomatis diisi oleh @BeforeEach Nested
            Assertions.assertEquals("Budi Santoso", wallet.getOwner(), "Owner harus Budi Santoso");
            Assertions.assertEquals(2, wallet.getCards().size(), "Jumlah kartu di wallet harus 2");
            Assertions.assertEquals(100000, wallet.getCashBalance(), "Saldo harus sesuai pengisian awal");

            System.out.println("   -> Sukses! Wallet berisi " + wallet.getCards().size() + " kartu dan saldo Rp " + wallet.getCashBalance());
        }

        @Test
        @Order(2)
        @DisplayName("Test 3 [NESTED]: Transaksi (Tarik Tunai) & Hapus Kartu")
        void testTransactionAndRemoveCard() {
            System.out.println("   [Eksekusi Test 3 NESTED: Tarik Tunai & Hapus Kartu]");

            // Action 1: Tarik Uang Rp 25.000
            boolean withdrawStatus = wallet.withdrawCash(25000);

            // Action 2: Hapus 1 Kartu (KTP)
            String cardToRemove = dummyCards.get(0);
            boolean removeStatus = wallet.removeCard(cardToRemove);

            // Verification
            Assertions.assertTrue(withdrawStatus, "Tarik tunai harus mengembalikan nilai true");
            Assertions.assertEquals(75000, wallet.getCashBalance(), "Saldo harus tersisa 75000");

            Assertions.assertTrue(removeStatus, "Penghapusan kartu harus mengembalikan nilai true");
            Assertions.assertEquals(1, wallet.getCards().size(), "Jumlah kartu harus sisa 1");

            System.out.println("   -> Sukses! Saldo sisa Rp " + wallet.getCashBalance() + ", Kartu sisa " + wallet.getCards().size());
        }
    }
}