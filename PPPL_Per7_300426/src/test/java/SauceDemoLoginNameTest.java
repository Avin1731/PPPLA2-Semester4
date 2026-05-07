import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class SauceDemoLoginNameTest {

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

            // 1. Instantiate WebDriver diletakkan di dalam loop agar browser membuka sesi baru yang bersih
            WebDriver driver = new ChromeDriver();
            driver.manage().window().maximize();

            try {
                // 2. Navigate to the web page (Buka halaman login)
                driver.get("https://www.saucedemo.com/");
                Thread.sleep(3000);

                // =======================================================
                // 3. Interaksi Elemen Username
                // =======================================================
                // a. Cari elemen username menggunakan By.name
                WebElement usernameField = driver.findElement(By.name("user-name"));

                // b. Pastikan elemen tertampil dan aktif
                Assertions.assertTrue(usernameField.isDisplayed() && usernameField.isEnabled(),
                        "Elemen Username tidak tertampil atau tidak aktif");

                // c. Clear elemen dan isi value
                usernameField.clear();
                usernameField.sendKeys(user);
                Thread.sleep(3000);

                // =======================================================
                // 4. Interaksi Elemen Password
                // =======================================================
                // a. Cari elemen password menggunakan By.name
                WebElement passwordField = driver.findElement(By.name("password"));

                // b. Pastikan elemen tertampil dan aktif
                Assertions.assertTrue(passwordField.isDisplayed() && passwordField.isEnabled(),
                        "Elemen Password tidak tertampil atau tidak aktif");

                // c. Clear elemen dan isi value
                passwordField.clear();
                passwordField.sendKeys("secret_sauce");
                Thread.sleep(3000);

                // =======================================================
                // 5. Interaksi Elemen Button Login
                // =======================================================
                // a. Cari elemen button menggunakan By.name
                WebElement loginButton = driver.findElement(By.name("login-button"));

                // b. Pastikan elemen tertampil dan aktif
                Assertions.assertTrue(loginButton.isDisplayed() && loginButton.isEnabled(),
                        "Tombol Login tidak tertampil atau tidak aktif");

                // c. Eksekusi click
                loginButton.click();
                Thread.sleep(3000);

                // =======================================================
                // 6. Record result (Assertion Dasar)
                // =======================================================
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