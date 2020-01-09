package cz.vse.xmass07.rukovoditel.selenium;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class TaskTest {/*

    private ChromeDriver driver;
    private final static String PROJECT_DETAIL_URL = UtilTest.URL + "index.php?module=items/items&path=21-509/22";
    private final static List<String> STATUSES = Arrays.asList("New", "Open", "Waiting", "Done", "Closed", "Paid", "Canceled");

    @Before
    public void init() {
        driver = UtilTest.initChrome();
    }

    @After
    public void tearDown() {
        driver.close();
    }

    @Test
    public void task1() {
        //Given
        UtilTest.login(driver, "rukovoditel", "vse456ru");
        driver.get(PROJECT_DETAIL_URL);
        UtilTest.resetFilter(driver);
        deleteTasks();
        //When
        UtilTest.openCreateModal(driver);
        String taskName = createTask("Task", "New", "Medium");//Set value to selects
        //Then
        WebDriverWait wait = new WebDriverWait(driver, 3);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#slimScroll > table")));
        List<WebElement> rows = driver.findElements(By.cssSelector("#slimScroll > table > tbody > tr"));
        Optional<WebElement> optionalRow = rows.stream().filter(row -> row.findElement(By.cssSelector("td.field-168-td > a")).getText().equals(taskName)).findFirst();
        Assert.assertTrue(optionalRow.isPresent());
        WebElement tableRow = optionalRow.get();
        tableRow.findElement(By.cssSelector("td.field-163-td > a > i.fa-info")).click();
        Assert.assertEquals(driver.findElement(By.cssSelector("div.portlet-title > div.caption")).getText(), taskName);
        Assert.assertEquals(driver.findElement(By.cssSelector("tr.form-group-167 > td")).getText(), "Task");
        Assert.assertEquals(driver.findElement(By.cssSelector("tr.form-group-169 > td")).getText(), "New");
        Assert.assertEquals(driver.findElement(By.cssSelector("tr.form-group-170 > td")).getText(), "Medium");
        Assert.assertEquals(driver.findElement(By.cssSelector(".content_box_content.fieldtype_textarea_wysiwyg")).getText(), "Default description");
        deleteTasks();
    }

    @Test
    public void task2() {
        //Given
        UtilTest.login(driver, "rukovoditel", "vse456ru");
        driver.get(PROJECT_DETAIL_URL);
        UtilTest.resetFilter(driver);
        deleteTasks();
        //When
        STATUSES.stream().forEach(status -> {
            UtilTest.openCreateModal(driver);
            createTask("Task", status, "Medium");
        });
        WebDriverWait wait = new WebDriverWait(driver, 3);
        UtilTest.setFilter(driver, Arrays.asList("New", "Open", "Waiting"));//Use Filter 1
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#slimScroll > table")));
        int result1 = driver.findElements(By.cssSelector("#slimScroll > table > tbody > tr")).size();
        Assert.assertEquals(result1, 3);//Use Filter 2
        UtilTest.setFilter(driver, Arrays.asList("New", "Waiting"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#slimScroll > table")));
        int result2 = driver.findElements(By.cssSelector("#slimScroll > table > tbody > tr")).size();
        Assert.assertEquals(result2, 2);//Reset Filter
        UtilTest.resetFilter(driver);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#slimScroll > table")));
        int result3 = driver.findElements(By.cssSelector("#slimScroll > table > tbody > tr")).size();
        Assert.assertEquals(result3, 7);
        deleteTasks();
    }

    private String createTask(String type, String status, String priority) {
        Select typeSelect = new Select(driver.findElement(By.id("fields_167")));
        Select statusSelect = new Select(driver.findElement(By.id("fields_169")));
        Select priorityStatus = new Select(driver.findElement(By.id("fields_170")));
        typeSelect.selectByVisibleText(type);
        statusSelect.selectByVisibleText(status);
        priorityStatus.selectByVisibleText(priority);
        Date date = new Date();
        String taskName = "Task" + date.getTime();
        WebElement nameInput = driver.findElement(By.id("fields_168"));
        nameInput.clear();
        nameInput.sendKeys(taskName);
        WebDriverWait wait = new WebDriverWait(driver, 2);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("iframe")));
        driver.switchTo().frame(driver.findElement(By.tagName("iframe")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("cke_contents_ltr")));
        WebElement descriptionInput = driver.findElement(By.className("cke_contents_ltr"));
        descriptionInput.clear();
        descriptionInput.sendKeys("Default description");
        driver.switchTo().defaultContent();
        driver.findElement(By.className("btn-primary-modal-action")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#slimScroll > table")));
        return taskName;
    }

    public void deleteTasks() {
        driver.get(PROJECT_DETAIL_URL);
        WebDriverWait wait = new WebDriverWait(driver, 2);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#slimScroll > table")));
        if (driver.findElements(By.cssSelector("#slimScroll > table > tbody > tr > td")).size() > 1) {
            driver.findElement(By.id("select_all_items")).click();
            driver.findElement(By.cssSelector("[class='btn btn-default dropdown-toggle']")).click();
            driver.findElement(By.cssSelector("[class='btn btn-default dropdown-toggle']")).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Delete")));
            driver.findElement(By.linkText("Delete")).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("btn-primary-modal-action")));
            driver.findElement(By.className("btn-primary-modal-action")).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#slimScroll > table")));
        }
    }*/
}