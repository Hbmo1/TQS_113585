import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeDriver;

public class Hooks {

    @BeforeAll
    public static void setupAll() {
        WebDriverManager.chromedriver().setup();
        DriverManager.driver = new ChromeDriver();
        DriverManager.driver.manage().window().maximize();
    }

    @AfterAll
    public static void teardownAll() {
        if (DriverManager.driver != null) {
            DriverManager.driver.quit();
        }
    }
}
