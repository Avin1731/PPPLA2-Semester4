import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HerokuappActionsTest {

    @Test
    public void hoverTest() {
        System.out.println("=== Memulai Test Hover Herokuapp ===");
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://the-internet.herokuapp.com/hovers");

        Actions action = new Actions(driver);
        WebElement firstImage = driver.findElement(By.cssSelector(".figure:nth-child(3) img"));

        // Memindahkan kursor/mouse ke elemen
        action.moveToElement(firstImage).perform(); //
        System.out.println("-> [Step 1] Berhasil hover pada gambar pertama.");

        WebElement nameText = driver.findElement(By.cssSelector(".figure:nth-child(3) h5"));
        assertTrue(nameText.isDisplayed(), "Teks nama user tidak muncul!");
        assertEquals("name: user1", nameText.getText());
        System.out.println("-> [Step 2] Assertion Sukses: 'name: user1' tampil di bawah kursor.");

        driver.quit();
    }

    @Test
    public void keyPressesTest() {
        System.out.println("\n=== Memulai Test Key Presses ===");
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://the-internet.herokuapp.com/key_presses");

        Actions action = new Actions(driver);
        WebElement inputField = driver.findElement(By.id("target"));

        // Interaksi Keyboard menahan dan melepas tombol SHIFT
        action.click(inputField).keyDown(Keys.SHIFT).keyUp(Keys.SHIFT).perform();
        System.out.println("-> [Step 1] Berhasil mengirimkan keyboard input (SHIFT) ke kolom input.");

        WebElement resultText = driver.findElement(By.id("result"));
        assertEquals("You entered: SHIFT", resultText.getText());
        System.out.println("-> [Step 2] Assertion Sukses: Menampilkan indikator 'You entered: SHIFT'.");

        driver.quit();
    }

    @Test
    public void dragAndDropTest() throws InterruptedException {
        System.out.println("\n=== Memulai Test Drag and Drop ===");
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://the-internet.herokuapp.com/drag_and_drop");

        Actions action = new Actions(driver);
        WebElement boxA = driver.findElement(By.id("column-a"));
        WebElement boxB = driver.findElement(By.id("column-b"));

        // Menggunakan method bawaan untuk operasi Drag And Drop
        // *Catatan: Beberapa halaman HTML5 memiliki isu dengan native selenium dnd, 
        // namun instruksi modul menetapkan implementasi Actions secara standar.
        action.dragAndDrop(boxA, boxB).perform();
        System.out.println("-> [Step 1] Perintah drag_and_drop Box A ke Box B terkirim.");

        Thread.sleep(1000); // Jedah visual animasi DOM sesaat

        assertEquals("B", driver.findElement(By.id("column-a")).getText());
        assertEquals("A", driver.findElement(By.id("column-b")).getText());
        System.out.println("-> [Step 2] Assertion Sukses: Header dari Box bertukar posisi.");

        driver.quit();
    }
}