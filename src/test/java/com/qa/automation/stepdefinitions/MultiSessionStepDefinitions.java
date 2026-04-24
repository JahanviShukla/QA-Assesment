package com.qa.automation.stepdefinitions;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.qa.automation.pages.CartPage;
import com.qa.automation.pages.HomePage;
import com.qa.automation.pages.ProductListingPage;
import com.qa.automation.pages.ProductPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class MultiSessionStepDefinitions {
    private static final Logger logger = LoggerFactory.getLogger(MultiSessionStepDefinitions.class);

    // Session 1 pages and context
    private BrowserContext context1;
    private Page page1;
    private HomePage homePage1;
    private ProductListingPage productListingPage1;
    private ProductPage productPage1;
    private CartPage cartPage1;

    // Session 2 pages and context
    private BrowserContext context2;
    private Page page2;
    private HomePage homePage2;
    private CartPage cartPage2;

    @When("user logs in and adds first product to cart in session 1 with username {string} and password {string}")
    public void userLogsInAndAddsFirstProductToCartInSession1(String username, String password) {
        // Create session 1
        context1 = Hooks.getBrowser().newContext();
        page1 = context1.newPage();
        homePage1 = new HomePage(page1);
        homePage1.open();

        // Login
        homePage1.goToLogin().login(username, password);
        logger.info("User logged in session 1 with: {}", username);

        // Navigate to men's watches and add first product
        try {
            Locator menuButton = page1.locator("//div[contains(@class,'hfe-nav-menu-icon')]//i[@tabindex='0']").first();
            if (menuButton.isVisible()) {
                menuButton.click();
                logger.info("Clicked menu button in session 1");
            }
        } catch (Exception e) {
            logger.warn("Could not click menu button in session 1, proceeding directly: {}", e.getMessage());
        }

        productListingPage1 = homePage1.navigateToMensWatches();
        productPage1 = productListingPage1.openFirstProduct();
        productPage1.clickAddToCart();
        logger.info("Added first product to cart in session 1");
    }

    @And("user logs in with same credentials in session 2")
    public void userLogsInWithSameCredentialsInSession2() {
        // Create session 2 as a separate browser context
        context2 = Hooks.getBrowser().newContext();
        page2 = context2.newPage();
        homePage2 = new HomePage(page2);
        homePage2.open();

        // Login with same credentials
        homePage2.goToLogin().login("jhanvishukla24@gmail.com", "janvi@124");
        logger.info("User logged in session 2 with same credentials");
    }

    @And("user opens cart in session 2")
    public void userOpensCartInSession2() {
        cartPage2 = homePage2.goToCart();
        logger.info("Opened cart in session 2");
    }

    @Then("cart should contain items in session 2")
    public void cartShouldContainItemsInSession2() {
        assertThat(cartPage2.getCartItemCount())
                .as("Cart should contain items in session 2")
                .isGreaterThan(0);
        logger.info("Verified cart contains items in session 2");
    }

    @And("cart should not be empty in session 2")
    public void cartShouldNotBeEmptyInSession2() {
        assertThat(cartPage2.isCartEmpty())
                .as("Cart should not be empty in session 2")
                .isFalse();
        logger.info("Verified cart is not empty in session 2");
    }

    @And("user removes all items from cart in session 2")
    public void userRemovesAllItemsFromCartInSession2() {
        int itemCount = cartPage2.getCartItemCount();
        logger.info("Removing {} items from cart in session 2", itemCount);

        // Remove all items one by one
        for (int i = 1; i <= itemCount; i++) {
            cartPage2.removeItem(1);
            page1.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);
        }

        logger.info("All items removed from cart in session 2");
    }

    @And("user opens cart in session 1")
    public void userOpensCartInSession1() {
        cartPage1 = homePage1.goToCart();
        logger.info("Opened cart in session 1");
    }

    @Then("cart should be empty in session 1")
    public void cartShouldBeEmptyInSession1() {
        assertThat(cartPage1.isCartEmpty())
                .as("Cart should be empty in session 1")
                .isTrue();
        logger.info("Verified cart is empty in session 1");
    }
}
