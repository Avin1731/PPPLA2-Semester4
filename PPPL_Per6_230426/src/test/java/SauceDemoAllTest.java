import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class SauceDemoAllTest {

    @Test
    public void testAllUsersLogin() throws InterruptedException {
        String[] usernames = {
                "standard_user",
                "locked_out_user",
                "problem_user",
                "performance_glitch_user",
                "error_user",
                "visual_user"
        };

        for (String user : usernames) {
            System.out.println("Mencoba login dengan username: " + user);

            WebDriver driver = new ChromeDriver();
            driver.manage().window().maximize();

            try {
                driver.get("https://www.saucedemo.com/");
                Thread.sleep(3000);

                driver.findElement(By.id("user-name")).sendKeys(user);
                Thread.sleep(3000);

                driver.findElement(By.id("password")).sendKeys("secret_sauce");
                Thread.sleep(3000);

                long startTime = System.currentTimeMillis();
                driver.findElement(By.id("login-button")).click();
                long endTime = System.currentTimeMillis();
                Thread.sleep(3000);

                String currentUrl = driver.getCurrentUrl();

                // 1. Assertion Dasar: Cek Status Login Dulu
                if (user.equals("locked_out_user")) {
                    WebElement errorMsg = driver.findElement(By.cssSelector("h3[data-test='error']"));
                    Assertions.assertTrue(errorMsg.getText().contains("locked out"));
                    // Ditambahkan \n di akhir string agar output console memiliki spasi antar iterasi
                    System.out.println("-> Assertion Sukses: locked_out_user gagal login dan tertahan dengan pesan error.\n");
                } else {
                    Assertions.assertTrue(currentUrl.contains("inventory.html"));
                    System.out.println("-> Assertion Sukses: " + user + " berhasil login ke halaman inventory.");

                    // 2. Lanjut ke Assertion Spesifik (Deteksi Bug)
                    switch (user) {
                        case "standard_user":
                            System.out.println("-> Sesuai Ekspektasi: Tidak ada bug spesifik pada standard_user.\n");
                            break;

                        case "problem_user":
                            WebElement problemImg = driver.findElement(By.id("item_4_img_link")).findElement(By.tagName("img"));
                            Assertions.assertTrue(problemImg.getAttribute("src").contains("sl-404"));
                            System.out.println("-> Sesuai Ekspektasi: problem_user menampilkan gambar rusak/anjing (sl-404) pada gambar produk.\n");
                            break;

                        case "performance_glitch_user":
                            Assertions.assertTrue((endTime - startTime) > 2000);
                            System.out.println("-> Sesuai Ekspektasi: performance_glitch_user mengalami delay saat login lebih dari 2 detik.\n");
                            break;

                        case "error_user":
                            driver.findElement(By.className("product_sort_container")).click();
                            driver.findElement(By.cssSelector("option[value='za']")).click();
                            WebElement firstItem = driver.findElement(By.className("inventory_item_name"));
                            Assertions.assertNotEquals("Test.allTheThings() T-Shirt (Red)", firstItem.getText());
                            System.out.println("-> Sesuai Ekspektasi: error_user gagal menggunakan fitur sorting Z to A (urutan produk tidak berubah).\n");
                            break;

                        case "visual_user":
                            WebElement visualImg = driver.findElement(By.id("item_4_img_link")).findElement(By.tagName("img"));
                            // Menggunakan assertFalse untuk memastikan bahwa link gambar BUKAN "backpack" seperti seharusnya
                            Assertions.assertFalse(visualImg.getAttribute("src").contains("backpack"));
                            System.out.println("-> Sesuai Ekspektasi: visual_user menampilkan layout/gambar produk yang salah (visual glitch terdeteksi).\n");
                            break;
                    }
                }

            } catch (Exception e) {
                System.out.println("-> Test Gagal pada " + user + ": " + e.getMessage() + "\n");
            } finally {
                driver.quit();
                Thread.sleep(1500);
            }
        }
    }
}