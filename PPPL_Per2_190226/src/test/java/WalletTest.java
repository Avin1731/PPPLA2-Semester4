import org.junit.jupiter.api.*;
import java.util.ArrayList;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WalletTest {

    // Variabel Static (Class Level) karena diinisialisasi di @BeforeAll
    private static Wallet wallet;
    private static List<String> dummyCards;
    private static double initialCash;

    // -----------------------------------------------------------
    // 1. Initialization (Class Level)
    // -----------------------------------------------------------
    @BeforeAll
    static void initClass() {
        System.out.println("--- @BeforeAll: Membuat Object Wallet & List (Dijalankan SEKALI di awal) ---");
        wallet = new Wallet();
        dummyCards = new ArrayList<>();
    }

    // -----------------------------------------------------------
    // 2. Cleanup (Class Level)
    // -----------------------------------------------------------
    @AfterAll
    static void cleanClass() {
        System.out.println("\n--- @AfterAll: Menghapus Object Wallet (Dijalankan SEKALI di akhir) ---");
        wallet = null;
        dummyCards = null;
    }

    // -----------------------------------------------------------
    // 3. Initialization (Method Level)
    // -----------------------------------------------------------
    @BeforeEach
    void initMethod() {
        System.out.println("\n--- @BeforeEach: Menyiapkan Data Kartu & Uang Dummy ---");

        // Menyiapkan data dummy yang siap dimasukkan ke dompet tiap test berjalan
        dummyCards.add("KTP");
        dummyCards.add("ATM BCA");
        initialCash = 100000;

        System.out.println("   -> Jumlah kartu di list dummy sementara: " + dummyCards.size());
    }

    // -----------------------------------------------------------
    // 4. Cleanup (Method Level)
    // -----------------------------------------------------------
    @AfterEach
    void cleanMethod() {
        System.out.println("--- @AfterEach: Membersihkan List & Reset Wallet ke Kondisi Pabrik ---");

        // 1. Mengambil/mengeluarkan kartu dari Wallet (seperti unenroll student)
        for (String card : dummyCards) {
            wallet.removeCard(card);
        }

        // 2. Menguras seluruh uang yang tersisa di Wallet
        double currentBalance = wallet.getCashBalance();
        if (currentBalance > 0) {
            wallet.withdrawCash(currentBalance);
        }

        // 3. Menghapus identitas Owner
        wallet.setOwner(null);

        // 4. Membersihkan list dummy untuk test berikutnya
        dummyCards.clear();
    }

    // -----------------------------------------------------------
    // 5. Test Execution
    // -----------------------------------------------------------

    @Test
    @Order(1)
    @DisplayName("Test 1: Set Owner & Tambah Isi Dompet")
    void testAddItemsToWallet() {
        System.out.println("   [Eksekusi Test 1: Tambah Item]");

        // Action: Set Owner
        wallet.setOwner("Budi Santoso");

        // Action: Masukkan kartu dari list dummy ke wallet
        for (String card : dummyCards) {
            wallet.addCard(card);
        }

        // Action: Masukkan uang
        wallet.addCash(initialCash);

        // Verification / Assertion
        Assertions.assertEquals("Budi Santoso", wallet.getOwner(), "Owner harus Budi Santoso");
        Assertions.assertEquals(2, wallet.getCards().size(), "Jumlah kartu di wallet harus 2");
        Assertions.assertEquals(100000, wallet.getCashBalance(), "Saldo harus sesuai pengisian awal");

        System.out.println("   -> Sukses! Wallet berisi " + wallet.getCards().size() + " kartu dan saldo Rp " + wallet.getCashBalance());
    }

    @Test
    @Order(2)
    @DisplayName("Test 2: Transaksi (Tarik Tunai) & Hapus Kartu")
    void testTransactionAndRemoveCard() {
        System.out.println("   [Eksekusi Test 2: Tarik Tunai & Hapus Kartu]");

        // Setup mandiri untuk test ini: Isi dompet terlebih dahulu
        wallet.setOwner("Budi Santoso");
        for (String card : dummyCards) {
            wallet.addCard(card);
        }
        wallet.addCash(initialCash);

        // Action 1: Tarik Uang Rp 25.000
        boolean withdrawStatus = wallet.withdrawCash(25000);

        // Action 2: Hapus 1 Kartu (KTP dari index ke-0 list dummy)
        String cardToRemove = dummyCards.get(0);
        boolean removeStatus = wallet.removeCard(cardToRemove);

        // Verification
        Assertions.assertTrue(withdrawStatus, "Tarik tunai harus mengembalikan nilai true");
        Assertions.assertEquals(75000, wallet.getCashBalance(), "Saldo harus tersisa 75000");

        Assertions.assertTrue(removeStatus, "Penghapusan kartu harus mengembalikan nilai true");
        Assertions.assertEquals(1, wallet.getCards().size(), "Jumlah kartu harus sisa 1 di dalam dompet");

        System.out.println("   -> Sukses! Saldo sisa Rp " + wallet.getCashBalance() + ", Kartu sisa " + wallet.getCards().size());
    }
}