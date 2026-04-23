@loginpage
Feature: WatchStudio Login Flow
  As a user of WatchStudio
  I want to login to my account
  So that I can access my profile and manage my orders

  Background:
    Given user is on the WatchStudio homepage

  @LOGIN-001
  Scenario: Successful login with valid credentials
    When user navigates to the login page
    And user enters valid email "jhanvishukla24@gmail.com"
    And user enters valid password "janvi@124"
    And user clicks on the login button
    Then user should be logged in successfully
    And user should be redirected to my-account page
    And user clears the cart
    When user navigates to "Men's Watches" category
    And user opens the first product
    And user clicks on "Add to Cart" button
    Then product should be added to cart successfully
    And cart count should be 1
    When user increases quantity to 2
    Then cart quantity should update to 2
    And item subtotal should double
    And cart total should update correctly
    And user removes the first item
    Then item should be removed from cart
    And empty cart message should be displayed

 


