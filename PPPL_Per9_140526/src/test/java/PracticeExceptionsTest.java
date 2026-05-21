import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PracticeExceptionsTest {

    @Test
    public void testCase1_NoSuchElementFix() {
        System.out.println("\n=== Test Case 1: Handle NoSuchElementException ===");
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();

        // Memanfaatkan Implicit Wait agar elemen di Row 2 ditunggu otomatis
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        driver.get("https://practicetestautomation.com/practice-test-exceptions/");
        driver.findElement(By.id("add_btn")).click();

        // Ini akan menyebabkan NoSuchElementException jika tidak ada Wait Strategy 
        // karena loading DOM memerlukan waktu sebelum input muncul.
        WebElement row2Input = driver.findElement(By.xpath("//div[@id='row2']/input"));
        assertTrue(row2Input.isDisplayed());
        System.out.println("-> [Step 1] Row 2 (Input) berhasil ditemukan melalui verifikasi implicit wait.");

        driver.quit();
    }

    @Test
    public void testCase5_TimeoutExceptionFix() {
        System.out.println("\n=== Test Case 5: Handle TimeoutException ===");
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://practicetestautomation.com/practice-test-exceptions/");
        driver.findElement(By.id("add_btn")).click();

        // Ubah argumen Duration ke 6 detik untuk mencegah TimeoutException 
        // karena rendering pada test ini memakan waktu minimal 5 detik.
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(6));
        WebElement row2Input = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@id='row2']/input")));

        assertTrue(row2Input.isDisplayed());
        System.out.println("-> [Step 1] Explicit wait selesai memproses, elemen tampil aman tanpa TimeoutException.");
        driver.quit();
    }

    // ====== JAWABAN BONUS: TEST CASE 2, 3, & 4 ====== //

    @Test
    public void testCase2_ElementNotInteractableFix() {
        System.out.println("\n=== Bonus Test Case 2: Handle ElementNotInteractableException ===");
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://practicetestautomation.com/practice-test-exceptions/");
        driver.findElement(By.id("add_btn")).click();

        WebElement row2Input = driver.findElement(By.xpath("//div[@id='row2']/input"));
        row2Input.sendKeys("Pizza");

        // Menggunakan By.name("Save") me-return elemen Save milik Row 1 yang 'hidden'.
        // Kita menggunakan spesifik Xpath agar klik jatuh pada elemen Row 2 yang *interactable*.
        WebElement saveBtnRow2 = driver.findElement(By.xpath("//div[@id='row2']/button[@name='Save']"));
        saveBtnRow2.click();
        System.out.println("-> [Step 1] Teks telah disimpan tanpa mengenai ElementNotInteractableException.");
        driver.quit();
    }

    @Test
    public void testCase3_InvalidElementStateFix() {
        System.out.println("\n=== Bonus Test Case 3: Handle InvalidElementStateException ===");
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://practicetestautomation.com/practice-test-exceptions/");

        WebElement row1Input = driver.findElement(By.xpath("//div[@id='row1']/input"));
        WebElement editBtn = driver.findElement(By.id("edit_btn"));

        // Harus diklik terlebih dahulu, karena mencoba clear text pada field disabled 
        // akan langsung throw InvalidElementStateException.
        editBtn.click();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(row1Input)).clear();
        row1Input.sendKeys("Pizza");

        System.out.println("-> [Step 1] Data berhasil dimodifikasi di form input setelah state di-unlock.");
        driver.quit();
    }

    @Test
    public void testCase4_StaleElementReferenceFix() {
        System.out.println("\n=== Bonus Test Case 4: Handle StaleElementReferenceException ===");
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://practicetestautomation.com/practice-test-exceptions/");

        WebElement instructionsText = driver.findElement(By.id("instructions"));
        driver.findElement(By.id("add_btn")).click();

        // Begitu add diklik, tag <p id="instructions"> dicabut paksa dari struktur HTML.
        // Jika kita memanggil instructionsText.getText(), akan kena StaleElementReferenceException.
        // Solusinya adalah memverifikasi element tersebut menghilang.
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        assertTrue(wait.until(ExpectedConditions.invisibilityOf(instructionsText)));

        System.out.println("-> [Step 1] Instruksi lawas berhasil diverifikasi menghilang dari DOM.");
        driver.quit();
    }
}