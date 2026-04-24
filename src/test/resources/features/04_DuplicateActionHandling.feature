@d1
Feature: WatchStudio Duplicate Action Handling
  As a user
  I want the system to handle duplicate actions gracefully
  So that I don't get unintended duplicate items or errors

  Background:
    Given user is on the WatchStudio homepage


  Scenario: Double click on "Add to Cart" button
    When user navigates to first product via menu
    And user double clicks on "Add to Cart" button rapidly
    Then cart should show quantity as 1 instead of 2
    And no duplicate entries should exist
    And system should prevent duplicate add to cart actions

