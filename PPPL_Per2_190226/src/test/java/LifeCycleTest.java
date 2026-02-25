import org.junit.jupiter.api.*;
import java.util.ArrayList;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LifeCycleTest {

    // Variabel Static (Class Level) karena diinisialisasi di @BeforeAll
    private static Course course;
    private static List<Student> studentList;

    // -----------------------------------------------------------
    // 1. Initialization (Class Level)
    // -----------------------------------------------------------
    @BeforeAll
    static void initClass() {
        System.out.println("--- @BeforeAll: Membuat Object Course & List (Dijalankan SEKALI di awal) ---");
        course = new Course("CS101", "Pemrograman Java");
        studentList = new ArrayList<>();
    }

    // -----------------------------------------------------------
    // 2. Cleanup (Class Level)
    // -----------------------------------------------------------
    @AfterAll
    static void cleanClass() {
        System.out.println("\n--- @AfterAll: Menghapus Object Course (Dijalankan SEKALI di akhir) ---");
        course = null;
        studentList = null;
    }

    // -----------------------------------------------------------
    // 3. Initialization (Method Level)
    // -----------------------------------------------------------
    @BeforeEach
    void initMethod() {
        System.out.println("\n--- @BeforeEach: Membuat Student baru & Masukkan ke List ---");
        // Membuat object student baru setiap kali test method berjalan
        Student s1 = new Student("M001", "Budi");
        Student s2 = new Student("M002", "Siti");

        // Menambahkan ke list
        studentList.add(s1);
        studentList.add(s2);

        System.out.println("   -> Jumlah siswa di list sementara: " + studentList.size());
    }

    // -----------------------------------------------------------
    // 4. Cleanup (Method Level) - SESUAI REQUEST POIN 2
    // -----------------------------------------------------------
    @AfterEach
    void cleanMethod() {
        System.out.println("--- @AfterEach: Membersihkan List & Reset Course ---");

        // Membersihkan student dari Course agar test selanjutnya mulai dari 0
        // (Kita perlu melakukan ini karena object 'course' bersifat static/shared)
        for (Student s : studentList) {
            course.unenrollStudent(s);
        }

        // Membersihkan list student
        studentList.clear();
    }

    // -----------------------------------------------------------
    // 5. Test Execution
    // -----------------------------------------------------------

    @Test
    @Order(1)
    @DisplayName("Test 1: Enroll Student")
    void testEnrollStudent() {
        System.out.println("   [Eksekusi Test 1: Enroll]");

        // Action: Enroll semua siswa dari list ke course
        for (Student s : studentList) {
            course.enrollStudent(s);
        }

        // Verification / Assertion
        // Kita memasukkan 2 siswa di @BeforeEach
        Assertions.assertEquals(2, course.getStudentCount(), "Jumlah siswa harus 2");
        System.out.println("   -> Sukses! Jumlah siswa di course: " + course.getStudentCount());
    }

    @Test
    @Order(2)
    @DisplayName("Test 2: Unenroll Student")
    void testUnenrollStudent() {
        System.out.println("   [Eksekusi Test 2: Unenroll]");

        // Setup awal untuk test ini: Enroll dulu
        for (Student s : studentList) {
            course.enrollStudent(s);
        }

        // Action: Unenroll 1 siswa (Siswa pertama di list)
        Student studentToRemove = studentList.get(0);
        course.unenrollStudent(studentToRemove);

        // Verification
        // Awal 2, dihapus 1, sisa harus 1
        Assertions.assertEquals(1, course.getStudentCount(), "Jumlah siswa harus 1 setelah penghapusan");
        System.out.println("   -> Sukses! Jumlah siswa di course: " + course.getStudentCount());
    }
}