package com.qa.stepdefinitions;

import com.qa.pages.LoginPage;
import com.qa.utils.ConfigReader;
import com.qa.utils.DriverManager;
import com.qa.utils.ExcelUtils;
import com.qa.utils.ScreenshotUtils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.util.Map;

public class LoginSteps {

    private WebDriver driver;
    private LoginPage loginPage;
    private Map<String, String> loginData;

    @Given("the user is on the application login page")
    public void the_user_is_on_the_application_login_page() {
        driver = DriverManager.getDriver();
        loginPage = new LoginPage(driver);
        Assert.assertTrue(loginPage.isLoginPageDisplayed(), "Login page did not load as expected");
    }

    @When("the user attempts to login with invalid credentials {string} from Excel")
    public void the_user_attempts_to_login_with_invalid_credentials_from_excel(String testCaseId) {
        ExcelUtils excelUtils = new ExcelUtils(ConfigReader.getExcelPath(), "NegativeLoginData");
        loginData = excelUtils.getUserData(testCaseId);

        loginPage.attemptLoginWithInvalidCredentials(loginData.get("Email"), loginData.get("Password"));
    }

    @Then("an invalid login error message should be displayed")
    public void an_invalid_login_error_message_should_be_displayed() {
        Assert.assertTrue(loginPage.isLoginErrorDisplayed(),
                "Expected an error message for invalid login but none was shown (email used: "
                        + loginData.get("Email") + ")");
    }

    @And("the user takes a screenshot as evidence")
    public void the_user_takes_a_screenshot_as_evidence() {
        ScreenshotUtils.attach(driver, Hooks.getScenario(), "Invalid login attempt");
    }
}
