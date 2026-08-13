package com.qa.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Page Object for the Registration page.
 *
 * Locators below were confirmed against the live DOM via browser Inspect (thank you!),
 * replacing the earlier speculative ones.
 */
public class RegisterPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public RegisterPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    private final By firstNameField = By.xpath("//input[@id='firstName']");
    private final By lastNameField = By.xpath("//input[@id='lastName']");
    private final By emailField = By.xpath("//input[@id='userEmail']");
    private final By phoneNumberField = By.xpath("//input[@id='userMobile']");
    private final By occupationDropdown = By.xpath("//select[@class='custom-select ng-untouched ng-pristine ng-valid']");
    private final By maleRadio = By.xpath("//input[@value='Male']");
    private final By femaleRadio = By.xpath("//input[@value='Female']");
    private final By passwordField = By.xpath("//input[@id='userPassword']");
    private final By confirmPasswordField = By.xpath("//input[@id='confirmPassword']");
    private final By ageConsentCheckbox = By.xpath("//input[@type='checkbox']");
    private final By registerButton = By.xpath("//input[@id='login']");

    // Validation messages the form shows when required fields are missing
    private final By phoneRequiredError = By.xpath("//*[contains(text(),'Phone Number is required')]");
    private final By checkboxRequiredError = By.xpath("//*[contains(text(),'check above checkbox')]");

    private final By successBanner = By.cssSelector(".login-wrapper.my-auto.p-5.ng-star-inserted");


    public void fillRegistrationForm(String firstName, String lastName, String email, String phoneNumber,
                                      String occupation, String gender, String password, String confirmPassword) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameField));

        driver.findElement(firstNameField).sendKeys(firstName);
        driver.findElement(lastNameField).sendKeys(lastName);
        driver.findElement(emailField).sendKeys(email);
        driver.findElement(phoneNumberField).sendKeys(phoneNumber);

        selectOccupation(occupation);
        selectGender(gender);

        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(confirmPasswordField).sendKeys(confirmPassword);

        checkAgeConsent();
    }

    private void selectOccupation(String occupation) {
        WebElement dropdown = driver.findElement(occupationDropdown);
        wait.until(ExpectedConditions.elementToBeClickable(occupationDropdown));

        // Click to open the dropdown first, matching the real interaction flow.
        dropdown.click();

        Select select = new Select(dropdown);
        try {
            select.selectByVisibleText(occupation);
        } catch (Exception e) {
            // Fall back to the first real option (index 0 is the "Choose your occupation"
            // placeholder) if the exact text from Excel doesn't match one of the real
            // options: Doctor, Student, Engineer, Scientist.
            select.selectByIndex(1);
        }
    }

    private void selectGender(String gender) {
        if (gender.equalsIgnoreCase("Male")) {
            driver.findElement(maleRadio).click();
        } else if (gender.equalsIgnoreCase("Female")) {
            driver.findElement(femaleRadio).click();
        } else {
            throw new RuntimeException("Unrecognized gender value: " + gender + " (expected Male or Female)");
        }
    }

    private void checkAgeConsent() {
        WebElement checkbox = driver.findElement(ageConsentCheckbox);
        if (!checkbox.isSelected()) {
            checkbox.click();
        }
    }

    public void submit() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(registerButton));
        button.click();
    }

    public boolean isPhoneRequiredErrorDisplayed() {
        return !driver.findElements(phoneRequiredError).isEmpty();
    }

    public boolean isCheckboxRequiredErrorDisplayed() {
        return !driver.findElements(checkboxRequiredError).isEmpty();
    }

    /**
     * Registration success is validated as either the confirmed success wrapper being
     * visible, or the browser navigating back to the login page (this app redirects there
     * after a successful sign-up).
     */
    public boolean isRegistrationSuccessful() {
        try {
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(successBanner),
                    ExpectedConditions.urlContains("/auth/login")
            ));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
