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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ProjectsTest {
    private ChromeDriver webDriver;

    private final static String PROJECT_URL = UtilTest.URL + "index.php?module=items/items&path=21";

    @Before
    public void init() {
        webDriver = UtilTest.init();
    }

    @After
    public void tearDown() {
        webDriver.close();
    }

    @Test
    public void given_projectPage_when_userCreateProjectWithEmptyName_then_projectIsNotCreated() {
        //Given
        UtilTest.login(webDriver, "rukovoditel", "vse456ru");
        webDriver.get(PROJECT_URL);

        //When
        UtilTest.openCreateModal(webDriver);
        WebElement nameInput = webDriver.findElement(By.id("fields_158"));
        nameInput.clear();
        webDriver.findElement(By.className("btn-primary-modal-action")).click();

        //Then
        WebElement errorTextElement = webDriver.findElement(By.id("fields_158-error"));
        Assert.assertEquals(errorTextElement.getText(), "This field is required!");
        WebElement alert = webDriver.findElement(By.cssSelector(".modal-footer .alert-danger"));
        Assert.assertEquals(alert.getText(), "Some fields are required. They have been highlighted above.");
    }

    @Test
    public void given_projectPage_when_userCreateProjectWithValidFields_then_projectIsCreated() {
        //Given
        UtilTest.login(webDriver, "rukovoditel", "vse456ru");
        webDriver.get(PROJECT_URL);

        //WHEN
        UtilTest.openCreateModal(webDriver);

        //Set value to selects
        Select prioritySelect = new Select(webDriver.findElement(By.id("fields_156")));
        Select statusSelect = new Select(webDriver.findElement(By.id("fields_157")));
        prioritySelect.selectByVisibleText("High");
        statusSelect.selectByVisibleText("New");

        //Prepare date
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd");
        String dateInputText = formatter.format(date);

        //Fill Project name
        String projectName = "xmass07" + date.getTime();
        WebElement nameInput = webDriver.findElement(By.id("fields_158"));
        nameInput.clear();
        nameInput.sendKeys(projectName);

        //Fill Date input
        WebElement dateInput = webDriver.findElement(By.id("fields_159"));
        dateInput.clear();
        dateInput.sendKeys(dateInputText);

        //Send form
        webDriver.findElement(By.className("btn-primary-modal-action")).click();

        //Then
        webDriver.get(PROJECT_URL);

        //Reset filter
        UtilTest.resetFilter(webDriver);

        //Check
        WebDriverWait wait = new WebDriverWait(webDriver, 3);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#slimScroll > table")));
        List<WebElement> rows = webDriver.findElements(By.cssSelector("#slimScroll > table > tbody > tr"));
        Optional<WebElement> optionalRow = rows.stream().filter(row -> row.findElement(By.cssSelector("td.field-158-td > a")).getText().equals(projectName)).findFirst();
        Assert.assertTrue(optionalRow.isPresent());
        WebElement tableRow = optionalRow.get();
        Assert.assertEquals(tableRow.findElement(By.cssSelector("td.field-158-td > a")).getText(), projectName);

        //Delete created project
        deleteProject(tableRow);
    }

    private void deleteProject(WebElement tableRow) {
        tableRow.findElement(By.className("fa-trash-o")).click();
        WebDriverWait modalWait = new WebDriverWait(webDriver, 3);
        modalWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("ajax-modal")));
        webDriver.findElement(By.id("delete_confirm")).click();
        webDriver.findElement(By.className("btn-primary-modal-action")).click();
    }
}