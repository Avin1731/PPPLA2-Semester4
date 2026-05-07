import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class SauceDemoLoginXpathTest {

    WebDriver driver;

    @BeforeEach
    public void setUp() throws InterruptedException {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.saucedemo.com/");
        Thread.sleep(3000);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "standard_user",
            "locked_out_user",
            "problem_user",
            "performance_glitch_user",
            "error_user",
            "visual_user"
    })
    public void testAllUsersLoginWithXpath(String user) throws InterruptedException {
        System.out.println("Mencoba login dengan username: " + user);

        // =======================================================
        // 1. Interaksi Elemen Username (Axis: descendant & self)
        // =======================================================
        WebElement usernameField = driver.findElement(
                By.xpath("//div[@class='login-box']/descendant::input[@id='user-name']/self::input")
        );

        // Pastikan elemen tertampil dan aktif
        Assertions.assertTrue(usernameField.isDisplayed() && usernameField.isEnabled(),
                "Elemen Username tidak tertampil atau tidak aktif");

        // Clear elemen dan isi value
        usernameField.clear();
        usernameField.sendKeys(user);
        Thread.sleep(3000);

        // =======================================================
        // 2. Interaksi Elemen Password (Axis: following)
        // =======================================================
        WebElement passwordField = driver.findElement(
                By.xpath("//input[@id='user-name']/following::input[@id='password']")
        );

        // Pastikan elemen tertampil dan aktif
        Assertions.assertTrue(passwordField.isDisplayed() && passwordField.isEnabled(),
                "Elemen Password tidak tertampil atau tidak aktif");

        // Clear elemen dan isi value
        passwordField.clear();
        passwordField.sendKeys("secret_sauce");
        Thread.sleep(3000);

        // =======================================================
        // 3. Interaksi Elemen Login Button (Axis: parent & following-sibling)
        // =======================================================
        WebElement loginButton = driver.findElement(
                By.xpath("//input[@id='password']/parent::div/following-sibling::input[@id='login-button']")
        );

        // Pastikan elemen tertampil dan aktif
        Assertions.assertTrue(loginButton.isDisplayed() && loginButton.isEnabled(),
                "Tombol Login tidak tertampil atau tidak aktif");

        // Eksekusi click
        loginButton.click();
        Thread.sleep(3000);

        // =======================================================
        // 4. Record result (Assertion Dasar)
        // =======================================================
        String expectedUrlSnippet = "inventory.html";
        String actualUrl = driver.getCurrentUrl();

        if (user.equals("locked_out_user")) {
            // Menggunakan Ancestor & Child untuk menangkap error message
            WebElement errorMsg = driver.findElement(
                    By.xpath("//input[@id='login-button']/ancestor::form/child::div[contains(@class, 'error')]/h3")
            );

            // Opsional: Pastikan pesan error juga benar-benar tertampil di layar
            Assertions.assertTrue(errorMsg.isDisplayed(), "Pesan error tidak tertampil");
            Assertions.assertTrue(errorMsg.getText().contains("locked out"));
            System.out.println("-> Sesuai Ekspektasi: " + user + " tertahan di halaman login.\n");
        } else {
            Assertions.assertTrue(actualUrl.contains(expectedUrlSnippet),
                    "Gagal Login! URL tidak mengarah ke inventory untuk: " + user);
            System.out.println("-> Sesuai Ekspektasi: " + user + " berhasil login ke inventory.\n");
        }
    }

    @AfterEach
    public void tearDown() throws InterruptedException {
        if (driver != null) {
            driver.quit();
            Thread.sleep(1500);
        }
    }
}