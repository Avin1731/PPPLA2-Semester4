import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class TestSearchEngine {

    @Test
    public void searchTest() throws InterruptedException {
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
        search_bar.sendKeys("Budi");
        System.out.println("-> [Step 3] Berhasil melakukan sendKeys dengan kata kunci 'Budi'.");

        // 4. Locate Element Form & Submit
        WebElement form = driver.findElement(By.id("sb_form"));
        System.out.println("-> [Step 4] Berhasil menemukan elemen form pencarian (ID: sb_form).");

        form.submit();
        System.out.println("-> [Step 5] Berhasil melakukan submit form.");

        // Jeda sebentar menunggu halaman hasil pencarian dimuat sempurna sebelum mengecek title
        Thread.sleep(2000);

        // 5. Assertion
        String query = "Budi";
        String actualTitle = driver.getTitle();
        System.out.println("-> [Step 6] Mengambil title halaman saat ini: '" + actualTitle + "'");

        assert actualTitle != null;
        Assertions.assertTrue(actualTitle.contains(query),
                "Title tidak mengandung kata kunci pencarian. Title saat ini: " + actualTitle);

        System.out.println("-> [Step 7] Assertion Sukses! Title halaman terbukti mengandung query '" + query + "'.");
        System.out.println("=== Eksekusi TestSearchEngine Selesai ===");

        // driver.quit();
    }
}