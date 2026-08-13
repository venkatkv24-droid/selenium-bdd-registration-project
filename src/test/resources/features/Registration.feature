Feature: User Registration on Rahul Shetty Academy Client App

  As a new visitor to the site
  I want to register new user accounts from the login page
  So that I can access the application with valid credentials

  Background:
    Given the user is on the login page

  @registration @dataDriven
  Scenario Outline: Register a new user using data from Excel
    When the user clicks on the "Register here" link
    And the user fills the registration form with details for "<TestCaseId>" from Excel
    And the user submits the registration form
    Then the registration should be successful for "<TestCaseId>"

    Examples:
      | TestCaseId |
      | User1      |
      | User2      |
