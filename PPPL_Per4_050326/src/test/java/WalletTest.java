import org.example.InsufficientFundsException;
import org.example.Owner;
import org.junit.jupiter.api.*;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.stream.Stream;

@DisplayName("Pengujian Utama: Wallet (Parent)")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WalletTest {

    private static Wallet wallet;

    // -----------------------------------------------------------
    // 1. Initialization (Class Level - PARENT)
    // -----------------------------------------------------------
    @BeforeAll
    static void initClass() {
        System.out.println("=========================================================");
        System.out.println("--- @BeforeAll [PARENT]: Membuat Object Wallet SEKALI ---");
        System.out.println("=========================================================");
        wallet = new Wallet();
    }

    // -----------------------------------------------------------
    // 2. Cleanup (Class Level - PARENT)
    // -----------------------------------------------------------
    @AfterAll
    static void cleanClass() {
        System.out.println("\n=========================================================");
        System.out.println("--- @AfterAll [PARENT]: Menghapus Object Wallet SEKALI ---");
        System.out.println("=========================================================");
        wallet = null;
    }

    // -----------------------------------------------------------
    // 3. Initialization (Method Level - PARENT)
    // -----------------------------------------------------------
    @BeforeEach
    void initMethod() {
        System.out.println("\n--- @BeforeEach [PARENT]: Reset Saldo & Owner sebelum test dimulai ---");
        // Reset saldo ke 0 setiap sebelum method test dijalankan
        double currentCash = wallet.getCashBalance();
        if (currentCash > 0) {
            wallet.withdrawCash(currentCash);
        }
        wallet.setOwner((String) null);
        wallet.setOwner((Owner) null);
    }

    // -----------------------------------------------------------
    // 4. Cleanup (Method Level - PARENT)
    // -----------------------------------------------------------
    @AfterEach
    void cleanMethod() {
        System.out.println("--- @AfterEach [PARENT]: Test selesai, membersihkan state ---");
    }


    // ===========================================================
    // NESTED CLASS: TUGAS PARAMETERIZED TEST
    // ===========================================================
    @Nested
    @DisplayName("Skenario Nested: Parameterized Test Tugas Praktikum")
    class ParameterizedTestSkenario {

        // ---------------------------------------------------------
        // TUGAS 1: Single-value parameter (Valid & Invalid Nominal)
        // ---------------------------------------------------------
        @ParameterizedTest
        @ValueSource(doubles = {10000, 50000, 100000})
        @DisplayName("Test 1a [NESTED]: Nominal Cash Valid (Angka Positif)")
        void testValidCashInput(double nominal) {
            wallet.addCash(nominal);
            Assertions.assertEquals(nominal, wallet.getCashBalance());
            System.out.println("   -> [Single-Value] Add Cash Valid: " + nominal + " (SUCCESS)");
        }

        @ParameterizedTest
        @ValueSource(doubles = {0, -1000, -50000})
        @DisplayName("Test 1b [NESTED]: Nominal Cash Invalid (Angka Negatif / Nol)")
        void testInvalidCashInput(double nominal) {
            // Mengecek apakah melempar IllegalArgumentException
            Assertions.assertThrows(IllegalArgumentException.class, () -> {
                wallet.addCash(nominal);
            });
            System.out.println("   -> [Single-Value] Add Cash Invalid: " + nominal + " (EXCEPTION THROWN)");
        }

        // ---------------------------------------------------------
        // TUGAS 2: CSV File Source (Valid & Invalid Withdraw)
        // ---------------------------------------------------------
        @ParameterizedTest
        @CsvFileSource(resources = "/valid_withdraw.csv", numLinesToSkip = 1)
        @DisplayName("Test 2a [NESTED]: CsvFileSource - Withdraw Valid")
        void testWithdrawValidCsv(double deposit, double withdraw, double expectedTotal) {
            wallet.addCash(deposit);
            wallet.withdrawCash(withdraw);
            Assertions.assertEquals(expectedTotal, wallet.getCashBalance());
            System.out.println("   -> [CSV Valid] Deposit " + deposit + " - Tarik " + withdraw + " = " + expectedTotal + " (SUCCESS)");
        }

        @ParameterizedTest
        @CsvFileSource(resources = "/invalid_withdraw.csv", numLinesToSkip = 1)
        @DisplayName("Test 2b [NESTED]: CsvFileSource - Withdraw Invalid & Exception Mapping")
        void testWithdrawInvalidCsv(double initialDeposit, double withdraw, String exceptionType) {
            if (initialDeposit > 0) {
                wallet.addCash(initialDeposit);
            }

            // Validasi string exceptionType dari CSV menjadi class Exception asli
            Class<? extends Exception> expectedException = exceptionType.equals("InsufficientFundsException")
                    ? InsufficientFundsException.class
                    : IllegalArgumentException.class;

            Assertions.assertThrows(expectedException, () -> {
                wallet.withdrawCash(withdraw);
            });
            System.out.println("   -> [CSV Invalid] Withdraw " + withdraw + " melempar " + exceptionType + " (SUCCESS)");
        }

        // ---------------------------------------------------------
        // TUGAS 3: MethodSource dan ArgumentProvider (Object Owner)
        // ---------------------------------------------------------

        // Sumber Data menggunakan MethodSource
        static Stream<Arguments> ownerProvider() {
            return Stream.of(
                    Arguments.of(new Owner("ID-01", "Budi Santoso", "budi@mail.com")),
                    Arguments.of(new Owner("ID-02", "Siti Aminah", "siti@mail.com"))
            );
        }

        @ParameterizedTest
        @MethodSource("ownerProvider")
        @DisplayName("Test 3a [NESTED]: MethodSource - Test Object Owner")
        void testSetOwnerWithMethodSource(Owner testOwner) {
            wallet.setOwner(testOwner);

            Owner savedOwner = wallet.getOwnerObject();
            Assertions.assertNotNull(savedOwner);
            Assertions.assertEquals(testOwner.getId(), savedOwner.getId());
            Assertions.assertEquals(testOwner.getName(), savedOwner.getName());

            System.out.println("   -> [MethodSource] Owner di-set: " + savedOwner.getName() + " (SUCCESS)");
        }

        // Sumber Data menggunakan ArgumentsProvider Class (Alternatif / Opsional Sesuai Soal)
        @ParameterizedTest
        @ArgumentsSource(OwnerArgumentsProvider.class)
        @DisplayName("Test 3b [NESTED]: ArgumentProvider - Test Object Owner")
        void testSetOwnerWithArgumentsProvider(Owner testOwner) {
            wallet.setOwner(testOwner);
            Assertions.assertEquals(testOwner.getEmail(), wallet.getOwnerObject().getEmail());
            System.out.println("   -> [ArgumentsProvider] Owner di-set: " + wallet.getOwnerObject().getEmail() + " (SUCCESS)");
        }
    }
}

// -----------------------------------------------------------
// CLASS TAMBAHAN: ArgumentsProvider (Untuk Tugas 3 Poin 2)
// -----------------------------------------------------------
class OwnerArgumentsProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
                Arguments.of(new Owner("ID-99", "Admin", "admin@system.com")),
                Arguments.of(new Owner("ID-88", "User", "user@system.com"))
        );
    }
}