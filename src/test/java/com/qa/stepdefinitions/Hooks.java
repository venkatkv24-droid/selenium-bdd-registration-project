package com.qa.stepdefinitions;

import com.qa.utils.ConfigReader;
import com.qa.utils.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class Hooks {

    // Holds the current Scenario so any step definition class can attach a screenshot to
    // the report on demand (not just automatically on failure in @After).
    private static final ThreadLocal<Scenario> currentScenario = new ThreadLocal<>();

    public static Scenario getScenario() {
        return currentScenario.get();
    }

    @Before
    public void setUp(Scenario scenario) {
        currentScenario.set(scenario);
        WebDriver driver = DriverManager.getDriver();
        driver.get(ConfigReader.getBaseUrl());
    }

    @After
    public void tearDown(Scenario scenario) {
        WebDriver driver = DriverManager.getDriver();

        if (scenario.isFailed()) {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", scenario.getName());
        }

        DriverManager.quitDriver();
        currentScenario.remove();
    }
}
