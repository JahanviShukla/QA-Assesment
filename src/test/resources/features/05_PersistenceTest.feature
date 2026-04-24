@p1
Feature: WatchStudio Cart Persistence Test
  As a registered user
  I want my cart to persist across sessions
  So that I don't lose my items when I logout and login again

  Background:
    Given user is on the WatchStudio homepage

  Scenario: Cart persists after logout and login
    When user logs in with "jhanvishukla24@gmail.com" and "janvi@124"
    And user adds first product to cart with quantity 1
    And user clicks on login button
    And user clicks on logout button
    And user logs in again with "jhanvishukla24@gmail.com" and "janvi@124"
    Then cart should contain at least 1 item
    And quantity should be 1
    And cart contains only the added product
