import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Skenario Lifecycle Dompet")
public class WalletTest {

    private Wallet wallet;

    @BeforeAll
    public void init() {
        System.out.println("=================================================");
        System.out.println("   M U L A I   P E N G U J I A N   W A L L E T   ");
        System.out.println("=================================================");
        wallet = new Wallet();
    }

    @Test
    @Order(1)
    @DisplayName("Skenario 1: Cek Kondisi Awal (Toko)")
    public void step1_InitialState() {
        System.out.println("\n[STEP 1] Cek Kondisi Dompet Baru (Dari Pabrik)");

        // Assertions
        Assertions.assertNull(wallet.getOwner());
        Assertions.assertTrue(wallet.getCards().isEmpty());
        Assertions.assertEquals(0.0, wallet.getCashBalance());

        // Output Laporan
        System.out.println("   -> Status Owner : " + wallet.getOwner() + " (Sesuai/Null)");
        System.out.println("   -> Jumlah Kartu : " + wallet.getCards().size() + " (Sesuai/Kosong)");
        System.out.println("   -> Total Saldo  : Rp " + wallet.getCashBalance() + " (Sesuai/0)");
        System.out.println("✅ HASIL: Lolos Pengujian Awal.");
    }

    @Test
    @Order(2)
    @DisplayName("Skenario 2: Set Pemilik")
    public void step2_SetOwner() {
        System.out.println("\n[STEP 2] Simulasi Pembelian (Set Owner)");

        String pemilik = "Budi Santoso";
        wallet.setOwner(pemilik);

        // Assertions
        Assertions.assertEquals(pemilik, wallet.getOwner());

        // Output Laporan
        System.out.println("   -> Input Owner  : " + pemilik);
        System.out.println("   -> Data Tersimpan: " + wallet.getOwner());
        System.out.println("✅ HASIL: Owner berhasil diupdate.");
    }

    @Test
    @Order(3)
    @DisplayName("Skenario 3: Menambah Isi Dompet")
    public void step3_AddItems() {
        System.out.println("\n[STEP 3] Owner Mengisi Dompet (Uang & Kartu)");

        // Action
        wallet.addCard("KTP");
        wallet.addCard("ATM BCA");
        wallet.addCash(100000); // Tambah 100rb

        // Assertions
        Assertions.assertEquals(2, wallet.getCards().size());
        Assertions.assertEquals(100000, wallet.getCashBalance());

        // Output Laporan
        System.out.println("   -> Kartu Masuk  : KTP, ATM BCA");
        System.out.println("   -> Uang Masuk   : Rp 100.000");
        System.out.println("   -> Cek Total    : " + wallet.getCards().size() + " Kartu, Rp " + wallet.getCashBalance());
        System.out.println("✅ HASIL: Penambahan data sukses.");
    }

    @Test
    @Order(4)
    @DisplayName("Skenario 4: Transaksi (Tarik Uang)")
    public void step4_Transaction() {
        System.out.println("\n[STEP 4] Simulasi Transaksi (Withdraw)");

        // Action: Ambil 25.000 (Valid)
        System.out.println("   -> Mencoba tarik Rp 25.000...");
        boolean status = wallet.withdrawCash(25000);

        // Assertions
        Assertions.assertTrue(status);
        Assertions.assertEquals(75000, wallet.getCashBalance());

        // Output Laporan
        System.out.println("   -> Status Tarik : " + (status ? "BERHASIL" : "GAGAL"));
        System.out.println("   -> Sisa Saldo   : Rp " + wallet.getCashBalance());
        System.out.println("✅ HASIL: Saldo berkurang sesuai nominal.");
    }

    @Test
    @Order(5)
    @DisplayName("Skenario 5: Hapus Kartu")
    public void step5_RemoveCard() {
        System.out.println("\n[STEP 5] Simulasi Hapus Kartu");

        // Action
        System.out.println("   -> Menghapus 'KTP'...");
        boolean hapus = wallet.removeCard("KTP");

        // Assertions
        Assertions.assertTrue(hapus);
        Assertions.assertEquals(1, wallet.getCards().size());

        // Output Laporan
        System.out.println("   -> Status Hapus : " + (hapus ? "BERHASIL" : "GAGAL"));
        System.out.println("   -> Sisa Kartu   : " + wallet.getCards().size() + " (ATM BCA)");
        System.out.println("✅ HASIL: Kartu terhapus dari list.");
    }

    @Test
    @Order(6)
    @DisplayName("Skenario 6: Jual Dompet (Reset ke Default)")
    public void step6_SellAndReset() {
        System.out.println("\n[STEP 6] Dompet Dijual (Dikosongkan Kembali)");

        // 1. Ambil Sisa Uang (Kuras Habis)
        double sisaSaldo = wallet.getCashBalance();
        wallet.withdrawCash(sisaSaldo);
        System.out.println("   -> Menguras sisa saldo: Rp " + sisaSaldo);

        // 2. Ambil Sisa Kartu
        wallet.removeCard("ATM BCA");
        System.out.println("   -> Mengambil semua kartu...");

        // 3. Hapus Owner
        wallet.setOwner(null);
        System.out.println("   -> Menghapus data pemilik...");

        // Assertions (Pastikan kembali ke kondisi pabrik)
        Assertions.assertAll("Cek Reset",
                () -> Assertions.assertNull(wallet.getOwner()),
                () -> Assertions.assertTrue(wallet.getCards().isEmpty()),
                () -> Assertions.assertEquals(0.0, wallet.getCashBalance())
        );

        System.out.println("✅ HASIL: Dompet kembali bersih seperti baru.");
        System.out.println("\n=================================================");
        System.out.println("   S E M U A   P E N G U J I A N   L O L O S     ");
        System.out.println("=================================================");
    }
}