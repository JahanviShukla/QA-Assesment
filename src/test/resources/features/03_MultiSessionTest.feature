@test124
Feature: WatchStudio Multi-Session Cart Test
  As a user
  I want my cart to sync across multiple sessions
  So that I can see consistent cart data on different devices

@test21
  Scenario: Add item in session 1 and verify in session 2
    When user logs in and adds first product to cart in session 1 with username "jhanvishukla24@gmail.com" and password "janvi@124"
    And user logs in with same credentials in session 2
    And user opens cart in session 2
    Then cart should contain items in session 2
    And cart should not be empty in session 2

@test22
  Scenario: Remove item in session 2 and verify in session 1
    When user logs in and adds first product to cart in session 1 with username "jhanvishukla24@gmail.com" and password "janvi@124"
    And user logs in with same credentials in session 2
    And user opens cart in session 2
    And user removes all items from cart in session 2
    And user opens cart in session 1
    Then cart should be empty in session 1
