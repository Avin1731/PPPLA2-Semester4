import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class SauceDemoLoginTest {

    @Test
    public void testAllUsersLogin() throws InterruptedException {
        // Menyimpan daftar username ke dalam Array
        String[] usernames = {
                "standard_user",
                "locked_out_user",
                "problem_user",
                "performance_glitch_user",
                "error_user",
                "visual_user"
        };

        // Melakukan iterasi untuk setiap username yang ada di dalam array
        for (String user : usernames) {
            System.out.println("Mencoba login dengan username: " + user);

            // 1. Instantiate WebDriver diletakkan di dalam loop agar browser membuka sesi (window) baru yang bersih untuk setiap user
            WebDriver driver = new ChromeDriver();
            driver.manage().window().maximize();

            try {
                // 2. Navigate to the web page
                driver.get("https://www.saucedemo.com/");
                Thread.sleep(3000);

                // 3. Locate the element & Perform action (Send Keys)
                WebElement usernameField = driver.findElement(By.id("user-name"));
                usernameField.sendKeys(user);
                Thread.sleep(3000);

                WebElement passwordField = driver.findElement(By.id("password"));
                passwordField.sendKeys("secret_sauce");
                Thread.sleep(3000);

                // 4. Locate the element & Perform action (Click)
                WebElement loginButton = driver.findElement(By.id("login-button"));
                loginButton.click();
                Thread.sleep(3000);

                // 5. Record result (Assertion)
                String expectedUrlSnippet = "inventory.html";
                String actualUrl = driver.getCurrentUrl();

                // Penanganan khusus: locked_out_user tidak akan bisa masuk ke inventory
                if (user.equals("locked_out_user")) {
                    Assertions.assertFalse(actualUrl.contains(expectedUrlSnippet),
                            "Gagal! User " + user + " seharusnya diblokir dan tidak masuk ke inventory.");
                    System.out.println("-> Sesuai Ekspektasi: " + user + " tertahan di halaman login.\n");
                } else {
                    Assertions.assertTrue(actualUrl.contains(expectedUrlSnippet),
                            "Gagal Login! URL tidak mengarah ke inventory untuk: " + user);
                    System.out.println("-> Sesuai Ekspektasi: " + user + " berhasil login ke inventory.\n");
                }

            } finally {
                // 6. Close Session
                driver.quit();
                Thread.sleep(1500);
            }
        }
    }
}