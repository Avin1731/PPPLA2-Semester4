import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class TestSearchEngine {

    @Test
    public void searchTest() {
        System.out.println("=== Memulai TestSearchEngine ===");

        // 1. Inisialisasi dan Navigasi
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.bing.com/");
        System.out.println("-> [Step 1] Berhasil membuka browser dan navigasi ke Bing.");

        // 2. Locate Element Search Bar
        WebElement search_bar = driver.findElement(By.id("sb_form_q"));
        System.out.println("-> [Step 2] Berhasil menemukan elemen search bar (ID: sb_form_q).");

        // 3. Action: Send Keys
        String query = "Budi";
        search_bar.sendKeys(query);
        System.out.println("-> [Step 3] Berhasil melakukan sendKeys dengan kata kunci '" + query + "'.");

        // 4. Locate Element Form & Submit
        WebElement form = driver.findElement(By.id("sb_form"));
        System.out.println("-> [Step 4] Berhasil menemukan elemen form pencarian (ID: sb_form).");

        form.submit();
        System.out.println("-> [Step 5] Berhasil melakukan submit form.");

        // menerapkan EXPLICIT WAIT
        System.out.println("-> [Step 5.1] Menunggu halaman hasil pencarian dimuat (Maksimal 10 detik)...");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.titleContains(query));

        System.out.println("-> [Step 5.2] Selesai menunggu! Judul halaman sudah berubah.");

        // 5. Assertion
        String actualTitle = driver.getTitle();
        System.out.println("-> [Step 6] Mengambil title halaman saat ini: '" + actualTitle + "'");

        assert actualTitle != null;
        Assertions.assertTrue(actualTitle.contains(query),
                "Title tidak mengandung kata kunci pencarian. Title saat ini: " + actualTitle);

        System.out.println("-> [Step 7] Assertion Sukses! Title halaman terbukti mengandung query '" + query + "'.");
        System.out.println("=== Eksekusi TestSearchEngine Selesai ===");

        driver.quit();
    }
}