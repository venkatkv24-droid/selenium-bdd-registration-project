package com.qa.stepdefinitions;

import com.qa.pages.LoginPage;
import com.qa.pages.RegisterPage;
import com.qa.utils.ConfigReader;
import com.qa.utils.DriverManager;
import com.qa.utils.ExcelUtils;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.util.Map;

public class RegistrationSteps {

    private WebDriver driver;
    private LoginPage loginPage;
    private RegisterPage registerPage;
    private Map<String, String> userData;

    @Given("the user is on the login page")
    public void the_user_is_on_the_login_page() {
        driver = DriverManager.getDriver();
        loginPage = new LoginPage(driver);
        Assert.assertTrue(loginPage.isLoginPageDisplayed(), "Login page did not load as expected");
    }

    @When("the user clicks on the {string} link")
    public void the_user_clicks_on_the_link(String linkText) {
        registerPage = loginPage.clickRegisterHere();
    }

    @And("the user fills the registration form with details for {string} from Excel")
    public void the_user_fills_the_registration_form_with_details_from_excel(String testCaseId) {
        ExcelUtils excelUtils = new ExcelUtils(ConfigReader.getExcelPath(), "UserData");
        userData = excelUtils.getUserData(testCaseId);

     // The demo site persists registered accounts across runs and rejects a repeat
        // registration with the same email (silently - no redirect/success banner, which
        // isRegistrationSuccessful() then correctly reads as a failure). Excel keeps the
        // human-readable base email; we append a per-run timestamp so every execution
        // registers a fresh, never-seen-before address.
        String uniqueEmail = makeEmailUnique(userData.get("Email"));
        userData.put("Email", uniqueEmail);
        
        registerPage.fillRegistrationForm(
                userData.get("FirstName"),
                userData.get("LastName"),
                userData.get("Email"),
                userData.get("PhoneNumber"),
                userData.get("Occupation"),
                userData.get("Gender"),
                userData.get("Password"),
                userData.get("ConfirmPassword")
        );
    }

    @And("the user submits the registration form")
    public void the_user_submits_the_registration_form() {
        registerPage.submit();
    }

    @Then("the registration should be successful for {string}")
    public void the_registration_should_be_successful_for(String testCaseId) {
        Assert.assertTrue(registerPage.isRegistrationSuccessful(),
                "Registration did not succeed for " + testCaseId
                        + " (email used: " + userData.get("Email") + ")");
    }
    
    
    /**
     * Inserts a timestamp before the "@" so each run registers a brand-new address,
     * e.g. pushpa.allu1944@testmail.com -> pushpa.allu1944.1755085123456@testmail.com
     */
    private String makeEmailUnique(String baseEmail) {
        int atIndex = baseEmail.indexOf('@');
        if (atIndex == -1) {
            return baseEmail + System.currentTimeMillis();
        }
        String localPart = baseEmail.substring(0, atIndex);
        String domainPart = baseEmail.substring(atIndex);
        return localPart + "." + System.currentTimeMillis() + domainPart;
    }  
    
}







