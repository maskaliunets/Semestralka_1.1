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
    public void initChrome() {
/**    zakomentujte radek // System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver.exe");
       aby se Selenium nesnazilo pouzit .exe driver, který na linuxu nemuze spustit,
       v .travis.yml je naskriptovano, aby se downloadoval driver pro linux
       (akorat pri teto uprave vam to nepujde spustit lokalne, muselo by se to ošéfovat ....rozlišit prostředí např. "lokál" a "travis" s jinou konfigurací)*/
//        System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver.exe");
//        ChromeDriver service = new ChromeDriver();
//        ChromeOptions cho = new ChromeOptions();
//        cho.addArguments("headless");
//        driver = new ChromeDriver(cho);
//        driver.manage().window().maximize();
        //Send form
        driver = UtilTest.initChrome();
    }

    @After
    public void tearDown() {
        driver.close();
    }

    @Test
    public void given_loginPage_when_userLogsIn_then_userGetWelcomePage() {
        //Given + When
        UtilTest.login(driver, "rukovoditel", "vse456ru");
        //Then
        Assert.assertTrue(driver.getTitle().startsWith("Rukovoditel | Dashboard"));
        Assert.assertFalse(UtilTest.checkDashboard(driver));
    }

    @Test
    public void given_loginPage_when_userUsesEmptyPassword_then_errorMessageShouldDisplayed() {
        //Given + When
        UtilTest.login(driver, "rukovoditel", "");
        //Then
        WebElement label = driver.findElement(By.cssSelector("#password-error"));
        Assert.assertEquals(label.getText(), "This field is required!");
        Assert.assertTrue(UtilTest.checkDashboard(driver));
    }

    @Test
    public void given_loginPage_when_userUsesInvalidPassword_then_alertMessageShouldDisplayed() {
        //Given + When
        UtilTest.login(driver, "rukovoditel", "password");
        //Then
        WebElement label = driver.findElement(By.cssSelector(".alert.alert-danger"));
        Assert.assertTrue(label.getText().contains("No match for Username and/or Password."));
        Assert.assertTrue(UtilTest.checkDashboard(driver));
    }

    @Test
    public void given_userIsLoggedIn_when_userLogsOut_then_userCannotRedirectToDashboard() {
        //Given
        UtilTest.login(driver, "rukovoditel", "vse456ru");
        Assert.assertTrue(driver.getTitle().startsWith("Rukovoditel | Dashboard"));
        //When
        driver.findElement(By.cssSelector(".username")).click();
        driver.findElement(By.cssSelector(".dropdown.user .fa.fa-sign-out")).click();
        WebDriverWait wait = new WebDriverWait(driver, 3);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("form#login_form")));
        //Then
        Assert.assertTrue(UtilTest.checkDashboard(driver));
    }
}