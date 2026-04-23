Feature: WatchStudio Duplicate Action Handling
  As a user
  I want the system to handle duplicate actions gracefully
  So that I don't get unintended duplicate items or errors

  Background:
    Given user is on the WatchStudio homepage
    And user is on a product details page

@d1
  Scenario: Double click on "Add to Cart" button
    When user double clicks on "Add to Cart" button rapidly
    Then system should process both requests and update quantity correctly
    And cart should show correct quantity
    And no duplicate entries should exist

@d2
  Scenario: Click "Add to Cart" multiple times rapidly
    When user clicks "Add to Cart" button 3 times rapidly
    Then cart should contain 1 item with quantity 3
    And cart state should be consistent
    And no errors should be displayed

  # Scenario: Add same product to cart twice from different pages
  #   Given user has added "Rolex Daytona" to cart
  #   When user navigates to "Rolex Daytona" product page again
  #   And user clicks "Add to Cart" button
  #   Then system should increase quantity to 2
  #   And no duplicate entry should be created
  #   And cart should show 1 item with quantity 2

  # Scenario: Rapid cart quantity changes
  #   Given user has 1 item in cart with quantity 1
  #   When user increases quantity to 5 rapidly
  #   Then final quantity should be 5
  #   And cart total should be correct
  #   And no intermediate states should cause errors

  # Scenario: Click remove button multiple times
  #   Given user has 1 item in cart
  #   When user clicks remove button 2 times rapidly
  #   Then item should be removed from cart
  #   And system should not show any errors
  #   And cart should be empty

  # Scenario: Retry failed add to cart action
  #   When user clicks "Add to Cart" during network delay
  #   And action fails with timeout
  #   And user retries clicking "Add to Cart"
  #   Then system should process the retry successfully
  #   And cart should show correct state
  #   And no duplicate should be created

  # Scenario: Simultaneous add to cart from two tabs
  #   Given user has product page open in two tabs
  #   When user clicks "Add to Cart" in tab 1
  #   And user clicks "Add to Cart" in tab 2
  #   Then final cart state should be consistent
  #   And system should handle race condition gracefully
