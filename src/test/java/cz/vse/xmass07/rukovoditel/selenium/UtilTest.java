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

    public static ChromeDriver init() {
        ChromeOptions cho = new ChromeOptions();

        boolean runOnTravis = true;
        if (runOnTravis) {
            cho.addArguments("--headless");
            cho.addArguments("start-maximized");
            cho.addArguments("window-size=1200,1100");
            cho.addArguments("--disable-gpu");
            cho.addArguments("--disable-extensions");
        } else {
            System.setProperty("webdriver.chrome.driver", "src/test/resources/drivers/chromedriver");
        }
        ChromeDriver webDriver = new ChromeDriver(cho);
        return webDriver;
    }

    public static void login(WebDriver webDriver, String jmeno, String heslo) {
        webDriver.get(URL);
        WebElement searchInput = webDriver.findElement(By.name("username"));
        searchInput.sendKeys(jmeno);
        searchInput = webDriver.findElement(By.name("password"));
        searchInput.sendKeys(heslo);
        webDriver.findElement(By.cssSelector(".btn")).click();
    }

    public static boolean checkInaccessiblePage(WebDriver webDriver) {
        webDriver.get(ADMIN_PAGE);
        return !webDriver.getCurrentUrl().equals(ADMIN_PAGE);
    }

    public static void openCreateModal(WebDriver webDriver) {
        webDriver.findElement(By.className("btn-primary")).click();
        WebDriverWait wait = new WebDriverWait(webDriver, 2);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ajax-modal")));
    }

    public static void resetFilter(WebDriver webDriver) {
        boolean activeFilter = webDriver.findElements(By.cssSelector("a[title='Remove All Filters'] > .fa-trash-o")).size() > 0;
        if (activeFilter) {
            webDriver.findElement(By.cssSelector("a[title='Remove All Filters'] > .fa-trash-o")).click();
        }
    }

    public static void setFilter(WebDriver webDriver, List<String> values) {
        resetFilter(webDriver);
        WebDriverWait wait = new WebDriverWait(webDriver, 3);
        webDriver.findElement(By.className("btn-users-filters")).click();
        webDriver.findElement(By.className("btn-users-filters")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText("Default Filters")));
        webDriver.findElement(By.partialLinkText("Default Filters")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".filters-preview-box-heading")));
        webDriver.findElement(By.cssSelector(".filters-preview-box-heading")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".btn-primary-modal-action")));
        webDriver.findElements(By.cssSelector(".search-choice-close")).stream().forEach(WebElement::click);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#values_chosen")));
        webDriver.findElement(By.cssSelector("#values_chosen")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".chosen-results")));

        values.forEach(item -> {
            WebElement element = webDriver.findElements(By.cssSelector(".chosen-results > li"))
                    .stream().filter(choice -> item.equals(choice.getText())).findFirst().get();
            element.click();
            webDriver.findElement(By.cssSelector("#values_chosen")).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".chosen-results")));
        });

        webDriver.findElement(By.className("btn-primary-modal-action")).submit();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#slimScroll > table")));
    }
}