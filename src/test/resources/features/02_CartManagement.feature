Feature: WatchStudio Cart Management Flow
  As a customer
  I want to manage items in my cart
  So that I can purchase products with correct quantities and prices

  Background:
    Given user is on the WatchStudio homepage

@test1
  Scenario: Add single product to cart
    When user navigates to "Men's Watches" category
    And user opens the first product
    And user clicks on "Add to Cart" button
    Then product should be added to cart successfully
    And cart count should be 1

  # Scenario: Add multiple quantities to cart
  #   When user navigates to "Men's Watches" category
  #   And user opens the first product
  #   And user sets quantity to 3
  #   And user clicks on "Add to Cart" button
  #   Then 3 items should be added to cart
  #   And cart total should reflect quantity × price

@test12
  Scenario: Increase product quantity in cart
    Given user has 1 item in cart with quantity 1
    When user navigates to cart page
    And user increases quantity to 2
    Then cart quantity should update to 2
    And item subtotal should double
    And cart total should update correctly

  # Scenario: Decrease product quantity in cart
  #   Given user has 1 item in cart with quantity 3
  #   When user navigates to cart page
  #   And user decreases quantity to 1
  #   Then cart quantity should update to 1
  #   And item subtotal should reduce
  #   And cart total should update correctly

@test13
  Scenario: Remove product from cart
    Given user has items in cart
    When user navigates to cart page
    And user removes the first item
    Then item should be removed from cart
    And cart should be empty
    And empty cart message should be displayed

  # Scenario: Verify cart price accuracy
  #   Given user has added a product with price 5000 to cart
  #   And user sets quantity to 2
  #   When user navigates to cart page
  #   Then cart subtotal should be 10000
  #   And calculations should be accurate

  # Scenario: Navigate to cart and verify contents
  #   Given user has added products to cart
  #   When user navigates to cart page
  #   Then cart page should display all items
  #   And each item should show name, price, quantity, and subtotal
  #   And cart total should be displayed correctly
