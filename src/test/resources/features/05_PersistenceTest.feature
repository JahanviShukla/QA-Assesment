Feature: WatchStudio Cart Persistence Test
  As a registered user
  I want my cart to persist across sessions
  So that I don't lose my items when I logout and login again

  Background:
    Given user is on the WatchStudio homepage

  Scenario: Cart persists after logout and login
    When user logs in with "test@example.com" and "Test@123"
    And user adds "Rolex Submariner" to cart
    And quantity is set to 2
    And user logs out
    And user logs in again with "test@example.com" and "Test@123"
    Then cart should contain "Rolex Submariner"
    And quantity should be 2
    And cart total should be preserved

  # Scenario: Cart persists across browser sessions
  #   Given user is logged in with "test@example.com" and "Test@123"
  #   And user has added 2 items to cart
  #   When user closes browser and opens again
  #   And user navigates to my-account page
  #   Then cart should contain 2 items
  #   And quantities should be preserved
  #   And cart total should be correct

  # Scenario: Empty cart persists after logout
  #   Given user is logged in
  #   And cart is empty
  #   When user logs out
  #   And user logs in again
  #   Then cart should still be empty
  #   And empty cart message should be displayed

  # Scenario: Cart persists with multiple items
  #   Given user is logged in with "test@example.com" and "Test@123"
  #   When user adds "Rolex" to cart
  #   And user adds "Omega" to cart
  #   And user adds "TAG Heuer" to cart
  #   And user logs out
  #   And user logs in again
  #   Then cart should contain 3 items
  #   And all items should be present
  #   And quantities should be preserved

  # Scenario: Modified cart persists after logout
  #   Given user is logged in with "test@example.com" and "Test@123"
  #   And user has "Breitling" in cart with quantity 3
  #   When user changes quantity to 5
  #   And user logs out
  #   And user logs in again
  #   Then cart should contain "Breitling" with quantity 5
  #   And cart total should reflect updated quantity

  # Scenario: Guest cart merges with logged-in cart
  #   Given guest user has added "Rolex" to cart
  #   When user logs in with "test@example.com" and "Test@123"
  #   Then guest cart should be preserved
  #   And guest cart should merge with existing cart
  #   And no items should be lost

  # Scenario: Removed item stays removed after login
  #   Given user is logged in with "test@example.com" and "Test@123"
  #   And user has "Cartier" in cart
  #   When user removes "Cartier" from cart
  #   And user logs out
  #   And user logs in again
  #   Then cart should not contain "Cartier"
    # And removed state should be preserved
