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
    private ChromeDriver webDriver;

    @Before
    public void init() {
        /**    zakomentujte radek // System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver.exe");
         aby se Selenium nesnazilo pouzit .exe driver, který na linuxu nemuze spustit,
         v .travis.yml je naskriptovano, aby se downloadoval driver pro linux
         (akorat pri teto uprave vam to nepujde spustit lokalne, muselo by se to ošéfovat ....rozlišit prostředí např. "lokál" a "travis" s jinou konfigurací)*/
//        System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver.exe");
//        ChromeDriver service = new ChromeDriver();
//        ChromeOptions cho = new ChromeOptions();
//        cho.addArguments("headless");
//        webDriver = new ChromeDriver(cho);
//        webDriver.manage().window().maximize();
        webDriver = UtilTest.init();
    }

    @After
    public void tearDown() {
        webDriver.close();
    }

    @Test
    public void given_loginPage_when_userLogsIn_then_userGetWelcomePage() {
        //Given + When
        UtilTest.login(webDriver, "rukovoditel", "vse456ru");
        //Then
        Assert.assertTrue(webDriver.getTitle().startsWith("Rukovoditel | Dashboard"));
        Assert.assertFalse(UtilTest.checkInaccessiblePage(webDriver));
    }

    @Test
    public void given_loginPage_when_userUsesEmptyPassword_then_errorMessageShouldDisplayed() {
        //Given + When
        UtilTest.login(webDriver, "rukovoditel", "");
        //Then
        WebElement label = webDriver.findElement(By.cssSelector("#password-error"));
        Assert.assertEquals(label.getText(), "This field is required!");
        Assert.assertTrue(UtilTest.checkInaccessiblePage(webDriver));
    }

    @Test
    public void given_loginPage_when_userUsesInvalidPassword_then_alertMessageShouldDisplayed() {
        //Given + When
        UtilTest.login(webDriver, "rukovoditel", "password");
        //Then
        WebElement label = webDriver.findElement(By.cssSelector(".alert.alert-danger"));
        Assert.assertTrue(label.getText().contains("No match for Username and/or Password."));
        Assert.assertTrue(UtilTest.checkInaccessiblePage(webDriver));
    }

    @Test
    public void given_userIsLoggedIn_when_userLogsOut_then_userCannotRedirectToDashboard() {
        //Given
        UtilTest.login(webDriver, "rukovoditel", "vse456ru");
        Assert.assertTrue(webDriver.getTitle().startsWith("Rukovoditel | Dashboard"));

        //When
        webDriver.findElement(By.cssSelector(".username")).click();
        webDriver.findElement(By.cssSelector(".dropdown.user .fa.fa-sign-out")).click();

        WebDriverWait wait = new WebDriverWait(webDriver, 3);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("form#login_form")));

        //Then
        Assert.assertTrue(UtilTest.checkInaccessiblePage(webDriver));
    }
}