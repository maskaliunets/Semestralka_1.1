package cz.vse.xmass07.rukovoditel.selenium;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.List;
import java.util.UUID;

/**
 * Unit test for simple App.
 */
public class LoginTest {
    private ChromeDriver driver;

    @Before
    public void init() {
/**    zakomentujte radek // System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver.exe");
       aby se Selenium nesnazilo pouzit .exe driver, který na linuxu nemuze spustit,
       v .travis.yml je naskriptovano, aby se downloadoval driver pro linux
       (akorat pri teto uprave vam to nepujde spustit lokalne, muselo by se to ošéfovat ....rozlišit prostředí např. "lokál" a "travis" s jinou konfigurací)*/
//        System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver.exe");
//        ChromeDriver service = new ChromeDriver();
        ChromeOptions cho = new ChromeOptions();
        cho.addArguments("headless");
        driver = new ChromeDriver(cho);
        driver.manage().window().maximize();
    }

    @After
    public void tearDown() {
//       driver.close();
    }

    @Test
    public void google1_should_pass() {
        driver.get("https://www.google.com/");
        WebElement searchInput = driver.findElement(By.name("q"));
        searchInput.sendKeys("travis");
        searchInput.sendKeys(Keys.ENTER);
        Assert.assertTrue(driver.getTitle().startsWith("travis - "));
        driver.quit();
    }

    @Test
    public void google2_should_fail() {
        driver.get("https://www.google.com/");
        // WebElement searchInputNotExisting = driver.findElement(By.name("kdsfkladsjfas"));
        driver.quit();
    }

    @Test
    public void google3_should_fail() {
        driver.get("https://www.google.com/");
        Assert.assertEquals("one", "one");
        driver.quit();
    }

    public void shouldNotLoginUsingInvalidPassword() {
        /**given*/
        driver.get("https://opensource-demo.orangehrmlive.com/");

        /**when*/
        WebElement usernameInput = driver.findElement(By.id("txtUsername"));
        usernameInput.sendKeys("admin");
        WebElement passwordInput = driver.findElement(By.id("txtPassword"));
        passwordInput.sendKeys("invalidPassssssssword");
        WebElement loginButton = driver.findElement(By.id("btnLogin"));
        loginButton.click();

        /** then*/
        WebElement errorMessageSpan = driver.findElement(By.id("spanMessage"));
        Assert.assertEquals("Invalid credentials", errorMessageSpan.getText());

        /** validation error exists
        url changed to https://opensource-demo.orangehrmlive.com/index.php/auth/validateCredentials
        there is no menu*/
    }

    @Test
    public void shouldLoginUsingValidCredentials() {
        /** given*/
        //driver.get("http://demo.churchcrm.io/master/");
        driver.get("http://digitalnizena.cz/church/");

        // when
        WebElement usernameInput = driver.findElement(By.id("UserBox"));
        usernameInput.sendKeys("church");
        WebElement passwordInput = driver.findElement(By.id("PasswordBox"));
        passwordInput.sendKeys("church12345");
        WebElement loginButton = driver.findElement(By.className("btn-primary"));
        loginButton.click();
    }

    public void shouldCreateNewUser() throws InterruptedException {
        /** Given*/
        shouldLoginUsingValidCredentials();

        /** When*/
        driver.get("http://digitalnizena.cz/church/PersonEditor.php");

        WebElement genderSelectElement = driver.findElement(By.name("Gender"));
        Select genderSelect = new Select(genderSelectElement);
        genderSelect.selectByVisibleText("Male");

        WebElement firstNameInput = driver.findElement(By.id("FirstName"));
        firstNameInput.sendKeys("John");
        WebElement lastNameInput = driver.findElement(By.id("LastName"));
        String uuid = UUID.randomUUID().toString();
        lastNameInput.sendKeys("Doe " + uuid);
        WebElement emailInput = driver.findElement(By.name("Email"));
        emailInput.sendKeys("john.doe@gmail.com");

        WebElement classificationSelectElement = driver.findElement(By.name("Classification"));
        Select classificationSelect = new Select(classificationSelectElement);
        classificationSelect.selectByIndex(4);

        WebElement personSaveButton = driver.findElement(By.id("PersonSaveButton"));
        personSaveButton.click();

        /** Then*/
        driver.get("http://digitalnizena.cz/church/v2/people");

        WebElement searchInput = driver.findElement(By.cssSelector("#members_filter input"));
        searchInput.sendKeys(uuid);
        Thread.sleep(500);

        /** to verify if record is shown in table grid, we first filter the whole table to get exactly one data row
        that row should contain previously generated UUID value (in last name
        UKOL...opravit, doplnit tak, aby se provedla verifikace ze kontakt, ktery jsme vytvorili opravdu existuje
        (jde vyhledat a zobrazi se v tabulce)
        doporucuji radek tabulky s danou osobou projit (traverzovat), nebo jinym zpusobem v nem najit retezec UUID, ktery jednoznacne identifikuje pridanou osobu*/
        List<WebElement> elements = driver.findElements(By.cssSelector("table#members tr"));
        Assert.assertEquals(2, elements.size());

        /** data row is at index 0, header row is at index 1  (because in ChurchCRM html code there is tbody before thead)*/
        WebElement personTableRow = elements.get(0);

        /**option1*/
        Assert.assertTrue(personTableRow.getText().contains(uuid));
        /** option2 - traverse all cells in table grid*/
        List<WebElement> cells = personTableRow.findElements(By.tagName("td"));
        final int EXPECTED_COLUMNS = 9;
        Assert.assertEquals(EXPECTED_COLUMNS, cells.size());
        for (int i = 0; i < cells.size(); i++) {
            WebElement cell = cells.get(i);
            if (cell.getText().contains(uuid)) {
                //
            }
            System.out.println(cells.get(i).getText());
        }
    }


}