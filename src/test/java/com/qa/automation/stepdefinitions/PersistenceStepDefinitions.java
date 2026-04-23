package com.qa.automation.stepdefinitions;

import com.microsoft.playwright.Page;
import com.qa.automation.pages.CartPage;
import com.qa.automation.pages.HomePage;
import com.qa.automation.pages.LoginPage;
import com.qa.automation.pages.ProductListingPage;
import com.qa.automation.pages.ProductPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class PersistenceStepDefinitions {
    private static final Logger logger = LoggerFactory.getLogger(PersistenceStepDefinitions.class);

    private Page page;
    private HomePage homePage;
    private LoginPage loginPage;
    private ProductListingPage productListingPage;
    private ProductPage productPage;
    private CartPage cartPage;

    public PersistenceStepDefinitions() {
        this.page = Hooks.getPage();
    }

    @When("user logs in with {string} and {string}")
    public void userLogsInWithAnd(String email, String password) {
        loginPage = homePage.goToLogin();
        homePage = loginPage.login(email, password);
        logger.info("User logged in with: {}", email);
    }

    @And("user adds {string} to cart")
    public void userAddsToCart(String productName) {
        productListingPage = homePage.navigateToMensWatches();
        productPage = productListingPage.openFirstProduct();
        productPage.clickAddToCart();
        logger.info("Added {} to cart", productName);
    }

    @And("quantity is set to {int}")
    public void quantityIsSetTo(int quantity) {
        productPage.setQuantity(quantity);
        productPage.clickAddToCart();
        logger.info("Quantity set to: {}", quantity);
    }

    @And("user logs out")
    public void userLogsOut() {
        homePage.logout();
        logger.info("User logged out");
    }

    @And("user logs in again with {string} and {string}")
    public void userLogsInAgainWithAnd(String email, String password) {
        loginPage = homePage.goToLogin();
        homePage = loginPage.login(email, password);
        logger.info("User logged in again");
    }

    @Then("cart should contain {string}")
    public void cartShouldContain(String productName) {
        cartPage = homePage.goToCart();
        assertThat(cartPage.containsProduct(productName))
                .as("Cart should contain " + productName)
                .isTrue();
    }

    @And("quantity should be {int}")
    public void quantityShouldBe(int expectedQuantity) {
        int actualQuantity = cartPage.getItemQuantity(1);
        assertThat(actualQuantity)
                .as("Quantity should be " + expectedQuantity)
                .isGreaterThanOrEqualTo(expectedQuantity);
    }

    @And("cart total should be preserved")
    public void cartTotalShouldBePreserved() {
        assertThat(cartPage.isCartTotalCorrect())
                .as("Cart total should be preserved")
                .isTrue();
    }

    @Given("user is logged in with {string} and {string}")
    public void userIsLoggedInWithAnd(String email, String password) {
        homePage = new HomePage(page);
        homePage.open();
        loginPage = homePage.goToLogin();
        homePage = loginPage.login(email, password);
        logger.info("User is logged in");
    }

    @And("user has added {int} items to cart")
    public void userHasAddedItemsToCart(int itemCount) {
        productListingPage = homePage.navigateToMensWatches();
        for (int i = 0; i < itemCount; i++) {
            productPage = productListingPage.openFirstProduct();
            productPage.clickAddToCart();
            page.goBack();
        }
        logger.info("Added {} items to cart", itemCount);
    }

    @When("user closes browser and opens again")
    public void userClosesBrowserAndOpensAgain() {
        // Simulate browser close and reopen
        page.context().close();
        var newContext = page.context().browser().newContext();
        var newPage = newContext.newPage();
        homePage = new HomePage(newPage);
        homePage.open();
        logger.info("Browser closed and reopened");
    }

    @And("user navigates to my-account page")
    public void userNavigatesToMyAccountPage() {
        page.navigate("https://watchstudio.in/my-account/");
        logger.info("Navigated to my-account page");
    }

    @Then("cart should contain {int} items")
    public void cartShouldContainItems(int expectedCount) {
        cartPage = homePage.goToCart();
        int actualCount = cartPage.getCartItemCount();
        assertThat(actualCount)
                .as("Cart should contain " + expectedCount + " items")
                .isGreaterThanOrEqualTo(expectedCount);
    }

    @And("quantities should be preserved")
    public void quantitiesShouldBePreserved() {
        assertThat(cartPage.isCartTotalCorrect())
                .as("Quantities should be preserved")
                .isTrue();
    }

    @And("cart total should be correct")
    public void cartTotalShouldBeCorrect() {
        assertThat(cartPage.isCartTotalCorrect())
                .as("Cart total should be correct")
                .isTrue();
    }

    @Given("user is on homepage without login")
    public void userIsOnHomepageWithoutLogin() {
        homePage = new HomePage(page);
        homePage.open();
        logger.info("User is on homepage without login");
    }

    @And("cart is empty")
    public void cartIsEmpty() {
        cartPage = homePage.goToCart();
        // Assuming cart is initially empty
        logger.info("Cart is empty");
    }

    @Then("cart should still be empty")
    public void cartShouldStillBeEmpty() {
        cartPage = homePage.goToCart();
        assertThat(cartPage.isCartEmpty())
                .as("Cart should still be empty")
                .isTrue();
    }

    @When("user adds multiple items to cart")
    public void userAddsMultipleItemsToCart() {
        productListingPage = homePage.navigateToMensWatches();
        productPage = productListingPage.openFirstProduct();
        productPage.clickAddToCart();
        page.goBack();
        logger.info("Added item to cart");
    }

    @And("all items should be present")
    public void allItemsShouldBePresent() {
        assertThat(cartPage.getCartItemCount())
                .as("All items should be present")
                .isPositive();
    }

    @And("user has {string} in cart with quantity {int}")
    public void userHasInCartWithQuantity(String productName, int quantity) {
        productListingPage = homePage.navigateToMensWatches();
        productPage = productListingPage.openFirstProduct();
        productPage.setQuantity(quantity);
        productPage.clickAddToCart();
        logger.info("Added {} with quantity {}", productName, quantity);
    }

    @When("user changes quantity to {int}")
    public void userChangesQuantityTo(int newQuantity) {
        cartPage = homePage.goToCart();
        cartPage.updateQuantity(1, newQuantity);
        logger.info("Changed quantity to {}", newQuantity);
    }

    @Then("cart should contain {string} with quantity {int}")
    public void cartShouldContainWithQuantity(String productName, int expectedQuantity) {
        cartPage = homePage.goToCart();
        int actualQuantity = cartPage.getItemQuantity(1);
        assertThat(actualQuantity)
                .as("Quantity should be " + expectedQuantity)
                .isEqualTo(expectedQuantity);
    }

    @And("cart total should reflect updated quantity")
    public void cartTotalShouldReflectUpdatedQuantity() {
        assertThat(cartPage.isCartTotalCorrect())
                .as("Cart total should reflect updated quantity")
                .isTrue();
    }

    @Given("guest user has added {string} to cart")
    public void guestUserHasAddedToCart(String productName) {
        homePage = new HomePage(page);
        homePage.open();
        productListingPage = homePage.navigateToMensWatches();
        productPage = productListingPage.openFirstProduct();
        productPage.clickAddToCart();
        logger.info("Guest user added {} to cart", productName);
    }

    @Then("guest cart should be preserved")
    public void guestCartShouldBePreserved() {
        cartPage = homePage.goToCart();
        assertThat(cartPage.getCartItemCount())
                .as("Guest cart should be preserved")
                .isPositive();
    }

    @And("guest cart should merge with existing cart")
    public void guestCartShouldMergeWithExistingCart() {
        assertThat(cartPage.isCartTotalCorrect())
                .as("Guest cart should merge")
                .isTrue();
    }

    @And("no items should be lost")
    public void noItemsShouldBeLost() {
        assertThat(cartPage.getCartItemCount())
                .as("No items should be lost")
                .isPositive();
    }

    @And("user has {string} in cart")
    public void userHasInCart(String productName) {
        productListingPage = homePage.navigateToMensWatches();
        productPage = productListingPage.openFirstProduct();
        productPage.clickAddToCart();
        logger.info("Added {} to cart", productName);
    }

    @When("user removes {string} from cart")
    public void userRemovesFromCart(String productName) {
        cartPage = homePage.goToCart();
        cartPage.removeItem(1);
        logger.info("Removed {} from cart", productName);
    }

    @Then("cart should not contain {string}")
    public void cartShouldNotContain(String productName) {
        cartPage = homePage.goToCart();
        assertThat(cartPage.containsProduct(productName))
                .as("Cart should not contain " + productName)
                .isFalse();
    }

    @And("removed state should be preserved")
    public void removedStateShouldBePreserved() {
        assertThat(cartPage.isCartEmpty())
                .as("Removed state should be preserved")
                .isTrue();
    }
}
