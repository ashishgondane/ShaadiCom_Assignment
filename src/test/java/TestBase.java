import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class TestBase {
    static WebDriver driver;
    static String email, password, community;

    @BeforeTest
    public void beforeStart() {
        System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "\\resources\\drivers\\geckodriver.exe");
        driver = new FirefoxDriver();
        driver.manage().window().maximize();
        driver.get("http://marathishaadi.com/");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @Test(description="Registration to Marathi Shaadi Website")
    public void regiserToWebsite() throws IOException, ParseException {
        // User lands on Homepage
        WebElement letsBeginButton = driver.findElement(By.xpath("//button[@data-testid='lets_begin']"));
        Assert.assertTrue(letsBeginButton.isDisplayed());
        letsBeginButton.click();
        Assert.assertTrue(driver.findElement(By.xpath("//h2[@data-testid='signup_page_1_title']")).isDisplayed());
        readWriteJSON();
        panel1(email, password);
        panel2(community);
    }

    @SuppressWarnings("unchecked")
    public void readWriteJSON() throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        try {
            FileReader reader = new FileReader(System.getProperty("user.dir") + "\\utility\\testData.json");
            // Read JSON file
            JSONObject testData = (JSONObject) jsonParser.parse(reader);

            email = (String) testData.get("email");
            password = (String) testData.get("password");
            community = (String) testData.get("community");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void panel1(String email, String password) {
        // Fill Email field
        driver.findElement(By.name("email")).sendKeys(email);

        // Fill Password field
        driver.findElement(By.name("password1")).sendKeys(password);

        // Here I am selecting Brother option from dropdown
        driver.findElement(By.xpath("//div[@class='Dropdown-control postedby_selector']")).click();
        driver.findElement(By.xpath("//div[@class='Dropdown-menu postedby_options']/div[4]")).click();

        driver.findElement(By.xpath("//button[@data-testid='next_button']")).click();
    }

    public void panel2(String community) {
        Assert.assertEquals(community, driver.findElement(By.xpath("//div[@class='Dropdown-control mother_tongue_selector Dropdown-disabled']/div[1]")).getText());
    }

    @AfterTest
    public void afterStart() {
        driver.quit();
    }
}