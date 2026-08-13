package com.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object for https://rahulshettyacademy.com/client/#/auth/login
 *
 * NOTE ON LOCATORS: same caveat as RegisterPage - these reflect the commonly documented
 * structure of this training app. Verify against the live DOM (right-click -> Inspect)
 * before your first run and adjust if the current markup differs.
 */
public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    private final By registerHereLink = By.xpath("//a[@class='text-reset']");
    private final By emailField = By.xpath("//input[@id='userEmail']");
    private final By passwordField = By.xpath("//input[@id='userPassword']");
    private final By loginButton = By.xpath("//input[@id='login']");

    // Shown when login is attempted with an invalid email/password combination.
    private final By loginErrorMessage = By.xpath("//div[@aria-label='Incorrect email or password.']");


    public void open(String url) {
        driver.get(url);
    }

    public RegisterPage clickRegisterHere() {
        WebElement link = wait.until(ExpectedConditions.elementToBeClickable(registerHereLink));
        link.click();
        return new RegisterPage(driver);
    }

    public void login(String email, String password) {
        driver.findElement(emailField).clear();
        driver.findElement(emailField).sendKeys(email);
        driver.findElement(passwordField).clear();
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();
    }

    /**
     * Negative-path login: same action as login(), kept as a separate method name so the
     * intent is clear in step definitions / reports.
     */
    public void attemptLoginWithInvalidCredentials(String email, String password) {
        login(email, password);
    }

    public boolean isLoginErrorDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(loginErrorMessage)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isLoginPageDisplayed() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(emailField)).isDisplayed();
    }
}
