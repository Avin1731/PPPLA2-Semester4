import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

public class DemoQATest {

    @Test
    public void searchTest() throws InterruptedException {
        System.out.println("=== Memulai Test Hover Menu DemoQA ===");

        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://demoqa.com/menu/");
        System.out.println("-> [Step 1] Berhasil membuka browser dan navigasi ke web DemoQA.");

        // Tambahkan jeda awal agar elemen termuat
        Thread.sleep(2000);

        Actions action = new Actions(driver);

        WebElement menu2 = driver.findElement(By.partialLinkText("Main Item 2"));
        action.moveToElement(menu2).perform();
        System.out.println("-> [Step 2] Berhasil menemukan dan hover pada menu 'Main Item 2'.");

        Thread.sleep(1000);

        WebElement menu_sub = driver.findElement(By.partialLinkText("SUB SUB LIST"));
        action.moveToElement(menu_sub).perform();
        System.out.println("-> [Step 3] Berhasil menemukan dan hover pada sub-menu 'SUB SUB LIST'.");

        Thread.sleep(1000);

        WebElement menu_sub_sub = driver.findElement(By.partialLinkText("Sub Sub Item 2"));
        action.moveToElement(menu_sub_sub).click().perform();
        System.out.println("-> [Step 4] Berhasil menemukan dan mengeklik 'Sub Sub Item 2'.");

        System.out.println("-> Assertion/Pesan Sukses: Seluruh alur navigasi menu bertingkat berhasil dieksekusi tanpa error.");
        System.out.println("=== Eksekusi Test Selesai ===");

        Thread.sleep(2000);
        driver.quit();
    }
}