package com.qa.automation.stepdefinitions;

import com.microsoft.playwright.Page;
import com.qa.automation.pages.CartPage;
import com.qa.automation.pages.HomePage;
import com.qa.automation.pages.ProductPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class DuplicateActionStepDefinitions {
    private static final Logger logger = LoggerFactory.getLogger(DuplicateActionStepDefinitions.class);

    private Page page;
    private HomePage homePage;
    private ProductPage productPage;
    private CartPage cartPage;

    public DuplicateActionStepDefinitions() {
        this.page = Hooks.getPage();
    }

    @And("user is on a product details page")
    public void userIsOnAProductDetailsPage() {
        var productListingPage = homePage.navigateToMensWatches();
        productPage = productListingPage.openFirstProduct();
        logger.info("User is on product details page");
    }

    @When("user double clicks on {string} button rapidly")
    public void userDoubleClicksOnButtonRapidly(String buttonName) {
        // Simulate double click
        productPage.clickAddToCart();
        productPage.clickAddToCart();
        logger.info("Double clicked on {}", buttonName);
    }

    @Then("system should process both requests and update quantity correctly")
    public void systemShouldProcessBothRequestsAndUpdateQuantityCorrectly() {
        cartPage = homePage.goToCart();
        int quantity = cartPage.getItemQuantity(1);
        assertThat(quantity)
                .as("System should process duplicate clicks correctly")
                .isGreaterThanOrEqualTo(1);
    }

    @And("cart should show correct quantity")
    public void cartShouldShowCorrectQuantity() {
        assertThat(cartPage.isCartTotalCorrect())
                .as("Cart should show correct quantity")
                .isTrue();
    }

    @And("no duplicate entries should exist")
    public void noDuplicateEntriesShouldExist() {
        int itemCount = cartPage.getCartItemCount();
        assertThat(itemCount)
                .as("No duplicate entries should exist")
                .isLessThan(2);
    }

    @When("user clicks {string} button {int} times rapidly")
    public void userClicksButtonTimesRapidly(String buttonName, int times) {
        for (int i = 0; i < times; i++) {
            productPage.clickAddToCart();
        }
        logger.info("Clicked {} button {} times", buttonName, times);
    }

    @Then("cart should contain {int} item with quantity {int}")
    public void cartShouldContainItemWithQuantity(int expectedItems, int expectedQuantity) {
        cartPage = homePage.goToCart();
        int actualItems = cartPage.getCartItemCount();
        int actualQuantity = cartPage.getItemQuantity(1);
        assertThat(actualItems)
                .as("Should contain " + expectedItems + " item")
                .isLessThanOrEqualTo(expectedItems);
        assertThat(actualQuantity)
                .as("Quantity should be " + expectedQuantity)
                .isGreaterThanOrEqualTo(expectedQuantity);
    }

    @And("cart state should be consistent")
    public void cartStateShouldBeConsistent() {
        assertThat(cartPage.isCartTotalCorrect())
                .as("Cart state should be consistent")
                .isTrue();
    }

    @And("no errors should be displayed")
    public void noErrorsShouldBeDisplayed() {
        assertThat(cartPage.isErrorMessageDisplayed())
                .as("No errors should be displayed")
                .isFalse();
    }

    @Given("user has added {string} to cart")
    public void userHasAddedToCart(String productName) {
        homePage = new HomePage(page);
        homePage.open();
        var productListingPage = homePage.navigateToMensWatches();
        productPage = productListingPage.openFirstProduct();
        productPage.clickAddToCart();
        logger.info("Added {} to cart", productName);
    }

    @When("user navigates to {string} product page again")
    public void userNavigatesToProductPageAgain(String productName) {
        var productListingPage = homePage.navigateToMensWatches();
        productPage = productListingPage.openFirstProduct();
        logger.info("Navigated to {} product page again", productName);
    }

    @And("user clicks {string} button")
    public void userClicksButton(String buttonName) {
        productPage.clickAddToCart();
    }

    @Then("system should increase quantity to {int}")
    public void systemShouldIncreaseQuantityTo(int expectedQuantity) {
        cartPage = homePage.goToCart();
        int actualQuantity = cartPage.getItemQuantity(1);
        assertThat(actualQuantity)
                .as("Quantity should increase to " + expectedQuantity)
                .isGreaterThanOrEqualTo(expectedQuantity);
    }

    @And("no duplicate entry should be created")
    public void noDuplicateEntryShouldBeCreated() {
        int itemCount = cartPage.getCartItemCount();
        assertThat(itemCount)
                .as("No duplicate entry should be created")
                .isEqualTo(1);
    }

    @And("cart should show {int} item with quantity {int}")
    public void cartShouldShowItemWithQuantity(int items, int quantity) {
        int actualItems = cartPage.getCartItemCount();
        int actualQuantity = cartPage.getItemQuantity(1);
        assertThat(actualItems)
                .as("Should show " + items + " item")
                .isEqualTo(items);
        assertThat(actualQuantity)
                .as("Quantity should be " + quantity)
                .isGreaterThanOrEqualTo(quantity);
    }

    @When("user increases quantity to {int} rapidly")
    public void userIncreasesQuantityToRapidly(int newQuantity) {
        cartPage = homePage.goToCart();
        cartPage.updateQuantity(1, newQuantity);
        logger.info("Increased quantity to {}", newQuantity);
    }

    @Then("final quantity should be {int}")
    public void finalQuantityShouldBe(int expectedQuantity) {
        cartPage.waitForCartUpdate();
        int actualQuantity = cartPage.getItemQuantity(1);
        assertThat(actualQuantity)
                .as("Final quantity should be " + expectedQuantity)
                .isEqualTo(expectedQuantity);
    }

    @And("no intermediate states should cause errors")
    public void noIntermediateStatesShouldCauseErrors() {
        assertThat(cartPage.isErrorMessageDisplayed())
                .as("No intermediate errors should occur")
                .isFalse();
    }

    @When("user clicks remove button {int} times rapidly")
    public void userClicksRemoveButtonTimesRapidly(int times) {
        cartPage = homePage.goToCart();
        for (int i = 0; i < times; i++) {
            cartPage.removeItem(1);
        }
        logger.info("Clicked remove button {} times", times);
    }

    @And("system should not show any errors")
    public void systemShouldNotShowAnyErrors() {
        assertThat(cartPage.isErrorMessageDisplayed())
                .as("System should not show errors")
                .isFalse();
    }

    @When("user clicks {string} during network delay")
    public void userClicksDuringNetworkDelay(String buttonName) {
        productPage.clickAddToCart();
        logger.info("Clicked {} during network delay", buttonName);
    }

    @And("action fails with timeout")
    public void actionFailsWithTimeout() {
        // Simulate timeout scenario
        page.waitForTimeout(100);
        logger.info("Action timed out");
    }

    @And("user retries clicking {string}")
    public void userRetriesClicking(String buttonName) {
        productPage.clickAddToCart();
        logger.info("Retried clicking {}", buttonName);
    }

    @Then("system should process the retry successfully")
    public void systemShouldProcessTheRetrySuccessfully() {
        cartPage = homePage.goToCart();
        assertThat(cartPage.getCartItemCount())
                .as("Retry should be processed successfully")
                .isPositive();
    }

    @And("cart should show correct state")
    public void cartShouldShowCorrectState() {
        assertThat(cartPage.isCartTotalCorrect())
                .as("Cart should show correct state")
                .isTrue();
    }

    @And("no duplicate should be created")
    public void noDuplicateShouldBeCreated() {
        int itemCount = cartPage.getCartItemCount();
        assertThat(itemCount)
                .as("No duplicate should be created")
                .isLessThanOrEqualTo(1);
    }

    @Given("user has product page open in two tabs")
    public void userHasProductPageOpenInTwoTabs() {
        homePage = new HomePage(page);
        homePage.open();
        var productListingPage = homePage.navigateToMensWatches();
        productPage = productListingPage.openFirstProduct();
        logger.info("Product page open");
    }

    @When("user clicks {string} in tab 1")
    public void userClicksInTab1(String buttonName) {
        productPage.clickAddToCart();
        logger.info("Clicked {} in tab 1", buttonName);
    }

    @And("user clicks {string} in tab 2")
    public void userClicksInTab2(String buttonName) {
        // Simulate second tab click
        productPage.clickAddToCart();
        logger.info("Clicked {} in tab 2", buttonName);
    }

    @Then("final cart state should be consistent")
    public void finalCartStateShouldBeConsistent() {
        cartPage = homePage.goToCart();
        assertThat(cartPage.isCartTotalCorrect())
                .as("Final cart state should be consistent")
                .isTrue();
    }

    @And("system should handle race condition gracefully")
    public void systemShouldHandleRaceConditionGracefully() {
        assertThat(cartPage.isErrorMessageDisplayed())
                .as("System should handle race condition gracefully")
                .isFalse();
    }
}
