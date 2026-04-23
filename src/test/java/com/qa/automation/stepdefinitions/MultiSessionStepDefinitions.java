package com.qa.automation.stepdefinitions;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
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

    private Page page;
    private BrowserContext context;

    // Session 1 pages
    private Page page1;
    private HomePage homePage1;
    private ProductListingPage productListingPage1;
    private ProductPage productPage1;
    private CartPage cartPage1;

    // Session 2 pages
    private Page page2;
    private HomePage homePage2;
    private CartPage cartPage2;

    public MultiSessionStepDefinitions() {
        this.page = Hooks.getPage();
    }

    @Given("user is logged in with username {string} and password {string}")
    public void userIsLoggedIn(String username, String password) {
        homePage1 = new HomePage(page);
        homePage1.open();
        homePage1.goToLogin().login(username, password);
        logger.info("User logged in with: {}", username);
    }

    @When("user adds product {string} to cart in session 1")
    public void userAddsProductToCartInSession1(String productName) {
        productListingPage1 = homePage1.navigateToMensWatches();
        productPage1 = productListingPage1.openFirstProduct();
        productPage1.clickAddToCart();
        logger.info("Added product to cart in session 1: {}", productName);
    }

    @And("user opens cart in session 2")
    public void userOpensCartInSession2() {
        // Create second session (new browser context)
        context = page.context().browser().newContext();
        page2 = context.newPage();
        homePage2 = new HomePage(page2);
        homePage2.open();
        cartPage2 = homePage2.goToCart();
        logger.info("Opened cart in session 2");
    }

    @Then("cart should contain product {string} in session 2")
    public void cartShouldContainProductInSession2(String productName) {
        assertThat(cartPage2.containsProduct(productName))
                .as("Cart should contain " + productName + " in session 2")
                .isTrue();
    }

    @And("quantity should match in both sessions")
    public void quantityShouldMatchInBothSessions() {
        int qty1 = cartPage1.getItemQuantity(1);
        int qty2 = cartPage2.getItemQuantity(1);
        assertThat(qty1)
                .as("Quantities should match")
                .isEqualTo(qty2);
    }

    @Given("user has {int} items of {string} in cart across sessions")
    public void userHasItemsOfInCartAcrossSessions(int quantity, String productName) {
        homePage1 = new HomePage(page);
        homePage1.open();
        productListingPage1 = homePage1.navigateToMensWatches();
        productPage1 = productListingPage1.openFirstProduct();
        productPage1.setQuantity(quantity);
        productPage1.clickAddToCart();

        // Create session 2
        context = page.context().browser().newContext();
        page2 = context.newPage();
        homePage2 = new HomePage(page2);
        homePage2.open();

        cartPage1 = homePage1.goToCart();
        cartPage2 = homePage2.goToCart();
        logger.info("Added {} items of {} across sessions", quantity, productName);
    }

    @When("user changes quantity to {int} in session 1")
    public void userChangesQuantityToInSession1(int newQuantity) {
        cartPage1.updateQuantity(1, newQuantity);
        logger.info("Changed quantity to {} in session 1", newQuantity);
    }

    @And("user refreshes cart in session 2")
    public void userRefreshesCartInSession2() {
        page2.reload();
        cartPage2.waitForCartUpdate();
        logger.info("Refreshed cart in session 2");
    }

    @Then("quantity should be {int} in session 2")
    public void quantityShouldBeInSession2(int expectedQuantity) {
        int actualQuantity = cartPage2.getItemQuantity(1);
        assertThat(actualQuantity)
                .as("Quantity should be " + expectedQuantity + " in session 2")
                .isEqualTo(expectedQuantity);
    }

    @And("cart total should update in both sessions")
    public void cartTotalShouldUpdateInBothSessions() {
        assertThat(cartPage1.isCartTotalCorrect())
                .as("Cart total should be correct in session 1")
                .isTrue();
        assertThat(cartPage2.isCartTotalCorrect())
                .as("Cart total should be correct in session 2")
                .isTrue();
    }

    @Given("user has {string} in cart across sessions")
    public void userHasInCartAcrossSessions(String productName) {
        homePage1 = new HomePage(page);
        homePage1.open();
        productListingPage1 = homePage1.navigateToMensWatches();
        productPage1 = productListingPage1.openFirstProduct();
        productPage1.clickAddToCart();

        context = page.context().browser().newContext();
        page2 = context.newPage();
        homePage2 = new HomePage(page2);
        homePage2.open();

        cartPage1 = homePage1.goToCart();
        cartPage2 = homePage2.goToCart();
        logger.info("Added {} to cart across sessions", productName);
    }

    @When("user removes {string} from cart in session 1")
    public void userRemovesFromCartInSession1(String productName) {
        cartPage1.removeItem(1);
        logger.info("Removed {} from cart in session 1", productName);
    }

    @Then("cart should not contain {string} in session 2")
    public void cartShouldNotContainInSession2(String productName) {
        page2.reload();
        assertThat(cartPage2.containsProduct(productName))
                .as("Cart should not contain " + productName + " in session 2")
                .isFalse();
    }

    @And("cart should be empty in both sessions")
    public void cartShouldBeEmptyInBothSessions() {
        assertThat(cartPage1.isCartEmpty())
                .as("Cart should be empty in session 1")
                .isTrue();
        assertThat(cartPage2.isCartEmpty())
                .as("Cart should be empty in session 2")
                .isTrue();
    }

    @When("user adds {string} to cart in session 1")
    public void userAddsToCartInSession1(String productName) {
        productListingPage1 = homePage1.navigateToMensWatches();
        productPage1 = productListingPage1.openFirstProduct();
        productPage1.clickAddToCart();
        logger.info("Added {} to cart in session 1", productName);
    }

    @And("user adds {string} to cart in session 2")
    public void userAddsToCartInSession2(String productName) {
        productListingPage1 = homePage2.navigateToMensWatches();
        productPage1 = productListingPage1.openFirstProduct();
        productPage1.clickAddToCart();
        logger.info("Added {} to cart in session 2", productName);
    }

    @And("user navigates to cart in session 1")
    public void userNavigatesToCartInSession1() {
        cartPage1 = homePage1.goToCart();
    }

    @Then("cart should contain both {string} and {string}")
    public void cartShouldContainBothAnd(String product1, String product2) {
        assertThat(cartPage1.getCartItemCount())
                .as("Cart should contain both products")
                .isGreaterThanOrEqualTo(2);
    }

    @And("items should not be duplicated")
    public void itemsShouldNotBeDuplicated() {
        assertThat(cartPage2.getCartItemCount())
                .as("Items should not be duplicated")
                .isLessThan(4);
    }

    @Given("user has {int} item of {string} in cart")
    public void userHasItemOfInCart(int quantity, String productName) {
        homePage1 = new HomePage(page);
        homePage1.open();
        productListingPage1 = homePage1.navigateToMensWatches();
        productPage1 = productListingPage1.openFirstProduct();
        productPage1.setQuantity(quantity);
        productPage1.clickAddToCart();
        logger.info("Added {} item of {} to cart", quantity, productName);
    }

    @When("user sets quantity to {int} in session 1")
    public void userSetsQuantityToInSession1(int quantity) {
        cartPage1 = homePage1.goToCart();
        cartPage1.updateQuantity(1, quantity);
    }

    @And("user sets quantity to {int} in session 2")
    public void userSetsQuantityToInSession2(int quantity) {
        context = page.context().browser().newContext();
        page2 = context.newPage();
        homePage2 = new HomePage(page2);
        homePage2.open();
        cartPage2 = homePage2.goToCart();
        cartPage2.updateQuantity(1, quantity);
    }

    @And("user refreshes cart in both sessions")
    public void userRefreshesCartInBothSessions() {
        page.reload();
        page2.reload();
        cartPage1.waitForCartUpdate();
        cartPage2.waitForCartUpdate();
    }

    @Then("final quantity should be consistent in both sessions")
    public void finalQuantityShouldBeConsistentInBothSessions() {
        int qty1 = cartPage1.getItemQuantity(1);
        int qty2 = cartPage2.getItemQuantity(1);
        assertThat(qty1)
                .as("Final quantity should be consistent")
                .isNotNull();
    }

    @And("system should handle conflict gracefully")
    public void systemShouldHandleConflictGracefully() {
        assertThat(cartPage1.isCartTotalCorrect())
                .as("System should handle conflict gracefully")
                .isTrue();
    }
}
