import org.example.LibraryController;
import org.example.LibraryModel;
import org.example.NotificationService;
import org.junit.jupiter.api.*;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@DisplayName("Pengujian Utama: Library Controller (Parent)")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LibraryControllerTest {

    // PERBAIKAN: Hapus keyword 'static' pada deklarasi Mock dan InjectMocks
    @Mock
    private LibraryModel libraryModel;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private LibraryController libraryController;

    private AutoCloseable mocksContainer;

    // -----------------------------------------------------------
    // 1. Initialization (Class Level - PARENT)
    // -----------------------------------------------------------
    @BeforeAll
    static void initClass() {
        System.out.println("=========================================================");
        System.out.println("--- @BeforeAll [PARENT]: Memulai Pengujian Library    ---");
        System.out.println("=========================================================");
    }

    // -----------------------------------------------------------
    // 2. Cleanup (Class Level - PARENT)
    // -----------------------------------------------------------
    @AfterAll
    static void cleanClass() {
        System.out.println("\n=========================================================");
        System.out.println("--- @AfterAll [PARENT]: Pengujian Library Selesai     ---");
        System.out.println("=========================================================");
    }

    // -----------------------------------------------------------
    // 3. Initialization (Method Level - PARENT)
    // -----------------------------------------------------------
    @BeforeEach
    void initMethod() {
        System.out.println("\n--- @BeforeEach [PARENT]: Inisialisasi Mock sebelum test dimulai ---");
        // Menginisialisasi objek-objek Mockito secara manual untuk test ini
        mocksContainer = MockitoAnnotations.openMocks(this);
    }

    // -----------------------------------------------------------
    // 4. Cleanup (Method Level - PARENT)
    // -----------------------------------------------------------
    @AfterEach
    void cleanMethod() throws Exception {
        System.out.println("--- @AfterEach [PARENT]: Test selesai, membersihkan state mock ---");
        // Menutup dan membersihkan mock resource setelah setiap test selesai
        if (mocksContainer != null) {
            mocksContainer.close();
        }
    }

    // ===========================================================
    // NESTED CLASS: TUGAS MOCKITO TEST SCENARIO
    // ===========================================================
    @Nested
    @DisplayName("Skenario Nested: Tugas Praktikum Mockito")
    class MockitoTestSkenario {

        // ---------------------------------------------------------
        // Skenario 1: Test ketersediaan buku pada controller
        // ---------------------------------------------------------
        @Test
        @DisplayName("Test 1 [NESTED]: Cek Ketersediaan Buku (Tersedia & Tidak)")
        void testCheckAvailability() {
            // Stubbing
            when(libraryModel.isBookAvailable("B01")).thenReturn(true);
            when(libraryModel.isBookAvailable("B02")).thenReturn(false);

            // Assertions
            assertTrue(libraryController.checkAvailability("B01"));
            assertFalse(libraryController.checkAvailability("B02"));

            System.out.println("   -> [Cek Tersedia] Buku B01 Tersedia, B02 Tidak (SUCCESS)");
        }

        // ---------------------------------------------------------
        // Skenario 2a: Buku tidak sedang dipinjam (Tersedia), fungsi peminjaman dipanggil
        // ---------------------------------------------------------
        @Test
        @DisplayName("Test 2a [NESTED]: Peminjaman Sukses - Fungsi Peminjaman Dipanggil")
        void testBorrowBook_WhenAvailable() {
            // Stubbing
            when(libraryModel.isBookAvailable("B01")).thenReturn(true);

            // Action
            libraryController.processBorrowBook("B01", "Clean Code", "Budi");

            // Verification
            verify(libraryModel, times(1)).borrowBook("B01");

            System.out.println("   -> [Pinjam Sukses] Fungsi peminjaman dipanggil 1 kali (SUCCESS)");
        }

        // ---------------------------------------------------------
        // Skenario 2b: Buku sedang dipinjam (Tidak Tersedia), fungsi peminjaman tidak dipanggil
        // ---------------------------------------------------------
        @Test
        @DisplayName("Test 2b [NESTED]: Peminjaman Gagal - Fungsi Peminjaman Tidak Dipanggil")
        void testBorrowBook_WhenNotAvailable() {
            // Stubbing
            when(libraryModel.isBookAvailable("B01")).thenReturn(false);

            // Action
            libraryController.processBorrowBook("B01", "Clean Code", "Budi");

            // Verification
            verify(libraryModel, never()).borrowBook(anyString());

            System.out.println("   -> [Pinjam Gagal] Buku tidak tersedia, fungsi peminjaman diabaikan (SUCCESS)");
        }

        // ---------------------------------------------------------
        // Skenario 3: Verifikasi urutan eksekusi menggunakan InOrder
        // ---------------------------------------------------------
        @Test
        @DisplayName("Test 3 [NESTED]: Verifikasi Urutan Eksekusi (InOrder)")
        void testBorrowBook_ExecutionOrder() {
            // Stubbing
            when(libraryModel.isBookAvailable("B01")).thenReturn(true);

            // Action
            libraryController.processBorrowBook("B01", "Clean Code", "Budi");

            // Inisialisasi InOrder
            InOrder inOrder = inOrder(libraryModel, notificationService);

            // Verification (Urutan pemanggilan method)
            inOrder.verify(libraryModel).getAllBooks();
            inOrder.verify(libraryModel).Peminjaman("Clean Code", "Budi");
            inOrder.verify(notificationService).sendNotification("Budi", "Clean Code");

            System.out.println("   -> [InOrder] Urutan pemanggilan fungsi getAllBooks -> Peminjaman -> sendNotification (SUCCESS)");
        }
    }
}