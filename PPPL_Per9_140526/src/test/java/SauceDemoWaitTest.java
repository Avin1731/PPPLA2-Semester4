import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SauceDemoWaitTest {

    @Test
    public void loginFailedWaitStrategyTest() {
        System.out.println("=== Memulai Test SauceDemo Wait Strategy ===");
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();

        // 1. Implicit Wait: Ditetapkan secara global sebelum driver navigasi. 
        // Driver akan otomatis menunggu hingga durasi terpenuhi sebelum mengembalikan error.
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5)); //
        System.out.println("-> [Step 1] Implicit wait 5 detik berhasil di-set secara global.");

        driver.get("https://www.saucedemo.com/"); //
        System.out.println("-> [Step 2] Berhasil membuka halaman SauceDemo.");

        // Input data login yang salah
        driver.findElement(By.id("user-name")).sendKeys("locked_out_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();
        System.out.println("-> [Step 3] Berhasil submit login dengan kredensial yang salah.");

        // 2. Explicit Wait: Menunggu spesifik sampai elemen error menjadi visible.
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); //
        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h3[data-test='error']")));

        assertTrue(errorMessage.isDisplayed());
        System.out.println("-> [Step 4] Explicit wait berhasil menemukan error message: " + errorMessage.getText());

        System.out.println("=== Eksekusi Test Selesai ===");
        driver.quit();
    }
}