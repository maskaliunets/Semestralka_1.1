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

public class TaskTest {

    private ChromeDriver webDriver;

    private final static String PROJECT_DETAIL_URL = TestUtils.URL + "index.php?module=items/items&path=21-509/22";
    private final static List<String> STATUSES = Arrays.asList("New", "Open", "Waiting", "Done", "Closed", "Paid", "Canceled");


    @Before
    public void init() {
        webDriver = TestUtils.init();
    }

    @After
    public void tearDown() {
        webDriver.close();
    }

    @Test
    public void given_detailProjectWithoutTasks_when_userCreatesTask_then_taskIsCreated() {
        //Given
        TestUtils.login(webDriver, "rukovoditel", "vse456ru");
        webDriver.get(PROJECT_DETAIL_URL);
        TestUtils.resetFilter(webDriver);
        deleteTasks();

        //When
        TestUtils.openCreateModal(webDriver);
        //Set value to selects
        String taskName = createTask("Task", "New", "Medium");

        //Then
        WebDriverWait wait = new WebDriverWait(webDriver, 3);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#slimScroll > table")));

        List<WebElement> rows = webDriver.findElements(By.cssSelector("#slimScroll > table > tbody > tr"));
        Optional<WebElement> optionalRow = rows.stream().filter(row -> row.findElement(By.cssSelector("td.field-168-td > a")).getText().equals(taskName)).findFirst();
        Assert.assertTrue(optionalRow.isPresent());
        WebElement tableRow = optionalRow.get();
        tableRow.findElement(By.cssSelector("td.field-163-td > a > i.fa-info")).click();

        Assert.assertEquals(webDriver.findElement(By.cssSelector("div.portlet-title > div.caption")).getText(), taskName);
        Assert.assertEquals(webDriver.findElement(By.cssSelector("tr.form-group-167 > td")).getText(), "Task");
        Assert.assertEquals(webDriver.findElement(By.cssSelector("tr.form-group-169 > td")).getText(), "New");
        Assert.assertEquals(webDriver.findElement(By.cssSelector("tr.form-group-170 > td")).getText(), "Medium");
        Assert.assertEquals(webDriver.findElement(By.cssSelector(".content_box_content.fieldtype_textarea_wysiwyg")).getText(), "Default description");

        //Delete task
        deleteTasks();
    }

    @Test
    public void given_detailProjectWithoutTasks_when_userCreates7TasksAndUseFilter_then_taskIsCreatedAndFilterWorks() {
        //Given
        TestUtils.login(webDriver, "rukovoditel", "vse456ru");
        webDriver.get(PROJECT_DETAIL_URL);
        TestUtils.resetFilter(webDriver);
        deleteTasks();

        //When
        STATUSES.stream().forEach(status -> {
            TestUtils.openCreateModal(webDriver);
            createTask("Task", status, "Medium");
        });


        WebDriverWait wait = new WebDriverWait(webDriver, 3);
        //Use Filter 1
        TestUtils.setFilter(webDriver, Arrays.asList("New", "Open", "Waiting"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#slimScroll > table")));
        int result1 = webDriver.findElements(By.cssSelector("#slimScroll > table > tbody > tr")).size();
        Assert.assertEquals(result1, 3);

        //Use Filter 2
        TestUtils.setFilter(webDriver, Arrays.asList("New", "Waiting"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#slimScroll > table")));
        int result2 = webDriver.findElements(By.cssSelector("#slimScroll > table > tbody > tr")).size();
        Assert.assertEquals(result2, 2);

        //Reset Filter
        TestUtils.resetFilter(webDriver);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#slimScroll > table")));
        int result3 = webDriver.findElements(By.cssSelector("#slimScroll > table > tbody > tr")).size();
        Assert.assertEquals(result3, 7);

        //Delete tasks
        deleteTasks();

    }

    private String createTask(String type, String status, String priority) {
        Select typeSelect = new Select(webDriver.findElement(By.id("fields_167")));
        Select statusSelect = new Select(webDriver.findElement(By.id("fields_169")));
        Select priorityStatus = new Select(webDriver.findElement(By.id("fields_170")));
        typeSelect.selectByVisibleText(type);
        statusSelect.selectByVisibleText(status);
        priorityStatus.selectByVisibleText(priority);

        Date date = new Date();
        String taskName = "Task" + date.getTime();
        WebElement nameInput = webDriver.findElement(By.id("fields_168"));
        nameInput.clear();
        nameInput.sendKeys(taskName);
        WebDriverWait wait = new WebDriverWait(webDriver, 2);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("iframe")));
        webDriver.switchTo().frame(webDriver.findElement(By.tagName("iframe")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("cke_contents_ltr")));
        WebElement descriptionInput = webDriver.findElement(By.className("cke_contents_ltr"));
        descriptionInput.clear();
        descriptionInput.sendKeys("Default description");
        webDriver.switchTo().defaultContent();

        //Send form
        webDriver.findElement(By.className("btn-primary-modal-action")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#slimScroll > table")));
        return taskName;
    }


    public void deleteTasks() {
        webDriver.get(PROJECT_DETAIL_URL);
        WebDriverWait wait = new WebDriverWait(webDriver, 2);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#slimScroll > table")));
        if (webDriver.findElements(By.cssSelector("#slimScroll > table > tbody > tr > td")).size() > 1) {
            webDriver.findElement(By.id("select_all_items")).click();
            webDriver.findElement(By.cssSelector("[class='btn btn-default dropdown-toggle']")).click();
            webDriver.findElement(By.cssSelector("[class='btn btn-default dropdown-toggle']")).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("Delete")));
            webDriver.findElement(By.linkText("Delete")).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("btn-primary-modal-action")));
            webDriver.findElement(By.className("btn-primary-modal-action")).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#slimScroll > table")));
        }
    }

}