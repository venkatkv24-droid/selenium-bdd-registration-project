# Selenium BDD Registration Automation

## Update log (latest changes)

1. **Registration page rebuilt to match the real form** (from your screenshot): First Name,
   Last Name, Email, Phone Number, Occupation (dropdown), Gender (radio), Password, Confirm
   Password, and the mandatory "I am 18 year or Older" checkbox are all now filled and
   submitted via the **Register** button (previously assumed "Sign Up" — fixed).
   `Excel data now has matching columns: PhoneNumber, Occupation, Gender, ConfirmPassword.`
2. **`UserRegistrationData.xlsx` missing under `testdata` in Eclipse** — the file is
   confirmed present in this project/zip. If Eclipse's Project Explorer still shows the
   `testdata` folder empty after importing: select the project → press **F5** (Refresh).
   If it's still not visible, right-click `testdata` → **Import > File System** and point it
   at the same file, or just re-extract the zip fresh and re-import.
3. **New negative test case added**: `Login.feature` attempts login with an invalid
   email/password (from a new `NegativeLoginData` sheet in the same Excel file), asserts an
   error message is shown, and **explicitly captures a screenshot** (via `ScreenshotUtils`)
   regardless of pass/fail, attached to both the Cucumber and ExtentReports output.

Automates new-user registration on `https://rahulshettyacademy.com/client/#/auth/login`
(click **Register here**, fill the form, submit) for 2 users, using:

- **Selenium WebDriver 4** (browser automation)
- **Cucumber (BDD)** with a `Scenario Outline` in `Registration.feature`
- **TestNG** as the runner engine (`TestRunner.java` + `testng.xml`)
- **Apache POI** to pull each user's data from an Excel sheet
- **ExtentReports** (Spark HTML report), auto-generated via the Cucumber adapter
- **WebDriverManager** so you don't need to manually download chromedriver

## Project layout

```
selenium-bdd-project/
├── pom.xml
├── testng.xml
├── src/main/java/com/qa/
│   ├── pages/LoginPage.java
│   ├── pages/RegisterPage.java
│   └── utils/ (ConfigReader, ExcelUtils, DriverManager)
├── src/test/java/com/qa/
│   ├── runners/TestRunner.java
│   └── stepdefinitions/ (Hooks, RegistrationSteps)
└── src/test/resources/
    ├── config.properties
    ├── extent.properties / extent-config.xml
    ├── features/Registration.feature
    └── testdata/UserRegistrationData.xlsx   <-- 2 users: User1, User2
```

## How to run

```bash
mvn clean test
```

Reports land in `test-output/`:
- `test-output/SparkReport/Spark.html` — ExtentReports dashboard
- `test-output/cucumber-report.html` — plain Cucumber HTML report

## ⚠️ Before your first run — verify the locators

I built this project in a sandboxed environment **with no network access**, so I could not
launch a real browser against the live site to confirm every element locator. The locators in
`LoginPage.java` and `RegisterPage.java` are based on this site's well-documented, commonly
used structure (`name="userEmail"`, `name="userPassword"`, `name="firstName"`, etc.), but the
app can change. Before running:

1. Open `https://rahulshettyacademy.com/client/#/auth/login` in Chrome.
2. Right-click each field (email, password, "Register here" link) → **Inspect**.
3. Confirm the `name`/`id`/link text still matches what's in `LoginPage.java` / `RegisterPage.java`.
4. Adjust the `By` locators if anything differs — they're all defined near the top of each page
   class, so changes are isolated to one place.
5. Do the same for the registration form's success indicator (`successBanner` in
   `RegisterPage.java`) — confirm whether success shows a toast, an alert, or a redirect.

## Test data

`UserRegistrationData.xlsx` has 2 rows (`User1`, `User2`) with FirstName/LastName/Email/Password.
Emails include a random numeric suffix so re-running the suite doesn't collide with
"already registered" errors — regenerate or edit the sheet for fresh runs if needed.

## If your Claude session hits a usage/length limit while you're extending this project

1. Ask Claude: **"Summarize this conversation and the current state of the project so I can
   continue it in a new chat."**
2. Copy that summary.
3. Start a **new chat**, and as your first message paste the summary **plus** re-upload (or
   paste) whichever project files are relevant to what you want to do next (e.g. just the
   feature file and step defs if you're adding a new scenario).
4. Keep this project folder saved locally / in version control as you go — that's your source
   of truth regardless of any chat limit, so you never depend on chat history to recover code.
