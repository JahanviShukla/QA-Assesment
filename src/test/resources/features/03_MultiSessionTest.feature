Feature: WatchStudio Multi-Session Cart Test
  As a user
  I want my cart to sync across multiple sessions
  So that I can see consistent cart data on different devices

  Background:
    Given user is logged in with username "test@example.com" and password "Test@123"

@test21
  Scenario: Add item in one session and verify in another
    When user adds product "Rolex Submariner" to cart in session 1
    And user opens cart in session 2
    Then cart should contain product "Rolex Submariner" in session 2
    And quantity should match in both sessions

@test22
  Scenario: Modify quantity in one session and verify in another
    Given user has 2 items of "Omega Seamaster" in cart across sessions
    When user changes quantity to 3 in session 1
    And user refreshes cart in session 2
    Then quantity should be 3 in session 2
    And cart total should update in both sessions

  # Scenario: Remove item in one session and verify in another
  #   Given user has "TAG Heuer" in cart across sessions
  #   When user removes "TAG Heuer" from cart in session 1
  #   And user refreshes cart in session 2
  #   Then cart should not contain "TAG Heuer" in session 2
  #   And cart should be empty in both sessions

  # Scenario: Add different items in both sessions and verify merge
  #   When user adds "Rolex" to cart in session 1
  #   And user adds "Omega" to cart in session 2
  #   And user navigates to cart in session 1
  #   Then cart should contain both "Rolex" and "Omega"
  #   And items should not be duplicated

  # Scenario: Modify same item in both sessions simultaneously
  #   Given user has 1 item of "Breitling" in cart
  #   When user sets quantity to 3 in session 1
  #   And user sets quantity to 2 in session 2
  #   And user refreshes cart in both sessions
  #   Then final quantity should be consistent in both sessions
  #   And system should handle conflict gracefully
