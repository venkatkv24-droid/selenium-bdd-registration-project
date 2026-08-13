package com.qa.utils;

import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 * Takes a screenshot on demand (not just on failure) and attaches it to the running
 * Cucumber Scenario, so it shows up embedded in both the Cucumber HTML report and the
 * ExtentReports Spark dashboard.
 */
public class ScreenshotUtils {

    public static void attach(WebDriver driver, Scenario scenario, String label) {
        byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        scenario.attach(screenshot, "image/png", label);
    }
}
