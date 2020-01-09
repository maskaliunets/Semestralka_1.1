package cz.vse.xmass07.rukovoditel.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.List;
import java.util.stream.Collectors;


public class UtilTest {

    public static final String URL = "https://digitalnizena.cz/rukovoditel/";
    private static final String ADMIN_PAGE = URL + "index.php?module=dashboard/";

    public static ChromeDriver initChrome() {
        ChromeOptions cho = new ChromeOptions();

        boolean runOnTravis = true;
        if (runOnTravis) {
            cho.addArguments("--headless");
            cho.addArguments("start-maximized");
            cho.addArguments("window-size=1200,1100");
            cho.addArguments("--disable-gpu");
            cho.addArguments("--disable-extensions");
        } else {
            System.setProperty("driver.chrome.driver", "src/test/resources/drivers/chromedriver");
        }
        ChromeDriver driver = new ChromeDriver(cho);
        return driver;
    }

    public static void login(WebDriver driver, String jmeno, String heslo) {
        driver.get(URL);
        WebElement searchInput = driver.findElement(By.name("username"));
        searchInput.sendKeys(jmeno);
        searchInput = driver.findElement(By.name("password"));
        searchInput.sendKeys(heslo);
        driver.findElement(By.cssSelector(".btn")).click();
    }

    public static boolean checkDashboard(WebDriver driver) {
        driver.get(ADMIN_PAGE);
        return !driver.getCurrentUrl().equals(ADMIN_PAGE);
    }

    public static void openCreateModal(WebDriver driver) {
        driver.findElement(By.className("btn-primary")).click();
        WebDriverWait wait = new WebDriverWait(driver, 2);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ajax-modal")));
    }

    public static void resetFilter(WebDriver driver) {
        boolean activeFilter = driver.findElements(By.cssSelector("a[title='Remove All Filters'] > .fa-trash-o")).size() > 0;
        if (activeFilter) {
            driver.findElement(By.cssSelector("a[title='Remove All Filters'] > .fa-trash-o")).click();
        }
    }

    public static void setFilter(WebDriver driver, List<String> values) {
        resetFilter(driver);
        WebDriverWait wait = new WebDriverWait(driver, 3);
        driver.findElement(By.className("btn-users-filters")).click();
        driver.findElement(By.className("btn-users-filters")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText("Default Filters")));
        driver.findElement(By.partialLinkText("Default Filters")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".filters-preview-box-heading")));
        driver.findElement(By.cssSelector(".filters-preview-box-heading")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".btn-primary-modal-action")));
        driver.findElements(By.cssSelector(".search-choice-close")).stream().forEach(WebElement::click);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#values_chosen")));
        driver.findElement(By.cssSelector("#values_chosen")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".chosen-results")));

        values.forEach(item -> {
            WebElement element = driver.findElements(By.cssSelector(".chosen-results > li"))
                    .stream().filter(choice -> item.equals(choice.getText())).findFirst().get();
            element.click();
            driver.findElement(By.cssSelector("#values_chosen")).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".chosen-results")));
        });

        driver.findElement(By.className("btn-primary-modal-action")).submit();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#slimScroll > table")));
    }
}