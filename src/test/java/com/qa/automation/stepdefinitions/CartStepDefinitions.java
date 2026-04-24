package com.qa.automation.stepdefinitions;

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

public class CartStepDefinitions {
    private static final Logger logger = LoggerFactory.getLogger(CartStepDefinitions.class);

    private Page page;
    private HomePage homePage;
    private ProductListingPage productListingPage;
    private ProductPage productPage;
    private CartPage cartPage;

    public CartStepDefinitions() {
        this.page = Hooks.getPage();
        this.homePage = new HomePage(page);
    }

    @When("user navigates to {string} category")
    public void userNavigatesToCategory(String category) {
        // After login, we need to click the menu button first to access navigation
        logger.info("Attempting to click menu button to access navigation");

        try {
            // Wait a bit for page to stabilize after login
            page.waitForLoadState(com.microsoft.playwright.options.LoadState.DOMCONTENTLOADED);

            // Try multiple strategies to find and click the menu button
            Locator menuButton = null;

            // Strategy 1: Try the primary selector
            menuButton = page.locator("//div[contains(@class,'hfe-nav-menu-icon')]//i[@tabindex='0']").first();
            if (menuButton.isVisible()) {
                menuButton.click();
                logger.info("Clicked menu button using primary selector");
            } else {
                // Strategy 2: Try alternative menu button selectors
                Locator altMenuButton = page.locator(".hfe-nav-menu-icon, [class*='menu-icon'], [class*='hamburger'], button:has-text('Menu')").first();
                if (altMenuButton.isVisible()) {
                    altMenuButton.click();
                    logger.info("Clicked menu button using alternative selector");
                } else {
                    logger.info("Menu button not visible, attempting direct navigation");
                }
            }

            // Small wait for menu to expand if clicked
            if (menuButton != null && menuButton.isVisible()) {
                page.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);
            }
        } catch (Exception e) {
            logger.warn("Could not click menu button, proceeding directly: {}", e.getMessage());
        }

        if (category.contains("Men")) {
            productListingPage = homePage.navigateToMensWatches();
        } else if (category.contains("Women")) {
            productListingPage = homePage.navigateToWomensWatches();
        }
        logger.info("Navigated to category: {}", category);
    }

    @And("user opens the first product")
    public void userOpensTheFirstProduct() {
        productPage = productListingPage.openFirstProduct();
        logger.info("Opened first product");
    }

    @And("user clicks on {string} button")
    public void userClicksOnButton(String buttonName) {
        if (buttonName.contains("Add to Cart")) {
            productPage.clickAddToCart();
        }
        logger.info("Clicked on button: {}", buttonName);
    }

    @Then("product should be added to cart successfully")
    public void productShouldBeAddedToCartSuccessfully() {
        assertThat(productPage.isAddToCartSuccessMessageDisplayed())
                .as("Success message should be displayed")
                .isTrue();
        logger.info("Product added to cart successfully");
    }

    @And("cart count should be {int}")
    public void cartCountShouldBe(int expectedCount) {
        int actualCount = homePage.getCartItemCount();
        assertThat(actualCount)
                .as("Cart count should be " + expectedCount)
                .isEqualTo(expectedCount);
        logger.info("Cart count is: {}", actualCount);
    }

    @And("success message should be displayed")
    public void successMessageShouldBeDisplayed() {
        assertThat(productPage.isAddToCartSuccessMessageDisplayed())
                .as("Success message should be displayed")
                .isTrue();
    }

    @And("user sets quantity to {int}")
    public void userSetsQuantityTo(int quantity) {
        productPage.setQuantity(quantity);
        logger.info("Set quantity to: {}", quantity);
    }

    @Then("{int} items should be added to cart")
    public void itemsShouldBeAddedToCart(int expectedQuantity) {
        int cartCount = homePage.getCartItemCount();
        assertThat(cartCount)
                .as("Cart should contain " + expectedQuantity + " items")
                .isGreaterThanOrEqualTo(expectedQuantity);
    }

    @And("cart total should reflect quantity × price")
    public void cartTotalShouldReflectQuantityPrice() {
        cartPage = homePage.goToCart();
        assertThat(cartPage.isCartTotalCorrect())
                .as("Cart total should reflect quantity × price")
                .isTrue();
    }

    @Given("user has {int} item in cart with quantity {int}")
    public void userHasItemInCartWithQuantity(int itemCount, int quantity) {
        homePage = new HomePage(page);
        homePage.open();
        productListingPage = homePage.navigateToMensWatches();
        productPage = productListingPage.openFirstProduct();
        if (quantity > 1) {
            productPage.setQuantity(quantity);
        }
        productPage.clickAddToCart();
        logger.info("Added {} item(s) with quantity {}", itemCount, quantity);
    }

    @When("user navigates to cart page")
    public void userNavigatesToCartPage() {
        cartPage = homePage.goToCart();
        logger.info("Navigated to cart page");
    }

    @And("user increases quantity to {int}")
    public void userIncreasesQuantityTo(int newQuantity) {
        // Navigate to cart page first if not already there
        if (cartPage == null) {
            cartPage = homePage.goToCart();
        }
        cartPage.updateQuantity(1, newQuantity);
        logger.info("Increased quantity to: {}", newQuantity);
    }

    @And("user decreases quantity to {int}")
    public void userDecreasesQuantityTo(int newQuantity) {
        cartPage.updateQuantity(1, newQuantity);
        logger.info("Decreased quantity to: {}", newQuantity);
    }

    @Then("cart quantity should update to {int}")
    public void cartQuantityShouldUpdateTo(int expectedQuantity) {
        if (cartPage == null) {
            cartPage = homePage.goToCart();
        }
        cartPage.waitForCartUpdate();
        int actualQuantity = cartPage.getItemQuantity(1);
        assertThat(actualQuantity)
                .as("Cart quantity should be " + expectedQuantity)
                .isEqualTo(expectedQuantity);
    }

    @And("item subtotal should double")
    public void itemSubtotalShouldDouble() {
        if (cartPage == null) {
            cartPage = homePage.goToCart();
        }
        assertThat(cartPage.isSubtotalCorrect())
                .as("Item subtotal should be double")
                .isTrue();
    }

    @And("item subtotal should reduce")
    public void itemSubtotalShouldReduce() {
        if (cartPage == null) {
            cartPage = homePage.goToCart();
        }
        assertThat(cartPage.isSubtotalCorrect())
                .as("Item subtotal should reduce")
                .isTrue();
    }

    @And("cart total should update correctly")
    public void cartTotalShouldUpdateCorrectly() {
        assertThat(cartPage.isCartTotalCorrect())
                .as("Cart total should update correctly")
                .isTrue();
    }

    @Given("user has items in cart")
    public void userHasItemsInCart() {
        homePage = new HomePage(page);
        homePage.open();
        productListingPage = homePage.navigateToMensWatches();
        productPage = productListingPage.openFirstProduct();
        productPage.clickAddToCart();
        page.waitForLoadState(LoadState.NETWORKIDLE);
        logger.info("User has items in cart");
    }

    @And("user removes the first item")
    public void userRemovesTheFirstItem() {
        cartPage.removeItem();
        logger.info("Removed first item from cart");
    }

    @Then("item should be removed from cart")
    public void itemShouldBeRemovedFromCart() {
        assertThat(cartPage.getCartItemCount())
                .as("Item should be removed")
                .isZero();
    }

    @And("cart should be empty")
    public void cartShouldBeEmpty() {
        assertThat(cartPage.isCartEmpty())
                .as("Cart should be empty")
                .isTrue();
    }

    @And("empty cart message should be displayed")
    public void emptyCartMessageShouldBeDisplayed() {
        assertThat(cartPage.isEmptyCartMessageDisplayed())
                .as("Empty cart message should be displayed")
                .isTrue();
    }

    @Given("user has added a product with price {int} to cart")
    public void userHasAddedAProductWithPriceToCart(int price) {
        homePage = new HomePage(page);
        homePage.open();
        productListingPage = homePage.navigateToMensWatches();
        productPage = productListingPage.openFirstProduct();
        productPage.clickAddToCart();
        logger.info("Added product with price: {}", price);
    }

    @Then("cart subtotal should be {int}")
    public void cartSubtotalShouldBe(int expectedSubtotal) {
        cartPage = homePage.goToCart();
        double actualSubtotal = cartPage.getCartSubtotal();
        assertThat(actualSubtotal)
                .as("Cart subtotal should be " + expectedSubtotal)
                .isEqualTo(expectedSubtotal);
    }

    @And("calculations should be accurate")
    public void calculationsShouldBeAccurate() {
        assertThat(cartPage.isCartTotalCorrect())
                .as("Calculations should be accurate")
                .isTrue();
    }

    @Given("user has added products to cart")
    public void userHasAddedProductsToCart() {
        homePage = new HomePage(page);
        homePage.open();
        productListingPage = homePage.navigateToMensWatches();
        productPage = productListingPage.openFirstProduct();
        productPage.clickAddToCart();
        logger.info("Added products to cart");
    }

    @Then("cart page should display all items")
    public void cartPageShouldDisplayAllItems() {
        assertThat(cartPage.getAllCartItems().size())
                .as("Cart should display all items")
                .isPositive();
    }

    @And("each item should show name, price, quantity, and subtotal")
    public void eachItemShouldShowNamePriceQuantityAndSubtotal() {
        assertThat(cartPage.areAllItemDetailsDisplayed())
                .as("All item details should be displayed")
                .isTrue();
    }

    @And("cart total should be displayed correctly")
    public void cartTotalShouldBeDisplayedCorrectly() {
        assertThat(cartPage.isCartTotalDisplayed())
                .as("Cart total should be displayed")
                .isTrue();
    }
}
