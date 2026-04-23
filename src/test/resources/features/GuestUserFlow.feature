Feature: Guest User E-commerce Flow
  As a guest user
  I want to browse watches, add to cart, and manage my cart
  So that I can purchase without creating an account

  Background:
    Given user is on the WatchStudio homepage

  Scenario: Browse watches as guest user
    When user navigates to Men's Watches category
    Then products should be displayed
    And user should not be required to login

  Scenario: Search for specific watch brand
    When user searches for "Rolex"
    Then relevant Rolex watches should be displayed
    And results should contain brand name

  Scenario: Navigate by brands
    When user clicks on "Brands" section
    And selects "ROLEX" brand
    Then only Rolex watches should be displayed
    And product count should be visible

  Scenario: View sale products
    Then sale badge products should be visible
    And discounted prices should be displayed
    And original prices should be struck through

  Scenario: Open product from listing
    When user navigates to product listing page
    And user clicks on first product
    Then product details page should open
    And product name should be visible
    And product price should be visible

  Scenario: Check product availability
    Given user is on product details page
    When product is in stock
    Then "Add to Cart" button should be enabled
    And quantity selector should be visible
