Feature: Login with invalid credentials on Rahul Shetty Academy Client App

  As a user
  I want the application to reject login attempts with wrong credentials
  So that unauthorized access is prevented

  Background:
    Given the user is on the application login page

  @negative @login
  Scenario: Login fails with an incorrect username and password
    When the user attempts to login with invalid credentials "InvalidLogin1" from Excel
    Then an invalid login error message should be displayed
    And the user takes a screenshot as evidence
