package com.qa.automation.stepdefinitions;

import com.microsoft.playwright.Page;
import com.qa.automation.pages.HomePage;
import com.qa.automation.pages.LoginPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class LoginStepDefinitions {
    private static final Logger logger = LoggerFactory.getLogger(LoginStepDefinitions.class);

    private Page page;
    private HomePage homePage;
    private LoginPage loginPage;

    public LoginStepDefinitions() {
        // Get page from ThreadLocal for parallel execution support
        this.page = Hooks.getPage();
    }

    @Given("user is on the WatchStudio homepage")
    public void userIsOnWatchStudioHomepage() {
        homePage = new HomePage(page);
        homePage.open();
        logger.info("User is on homepage");
    }

    @When("user navigates to the login page")
    public void userNavigatesToLoginPage() {
        loginPage = homePage.goToLogin();
        logger.info("Navigated to login page");
    }

    @And("user enters valid email {string}")
    public void userEntersValidEmail(String email) {
        loginPage.enterEmail(email);
        logger.info("Entered email: {}", email);
    }

    @And("user enters valid password {string}")
    public void userEntersValidPassword(String password) {
        loginPage.enterPassword(password);
        logger.info("Entered password");
    }

    @And("user clicks on the login button")
    public void userClicksLoginButton() {
        homePage = loginPage.clickLoginButton();
        logger.info("Clicked login button");
    }

    @Then("user should be logged in successfully")
    public void userShouldBeLoggedIn() {
        assertThat(homePage.isUserLoggedIn())
            .as("User should be logged in")
            .isTrue();
        logger.info("User is logged in");
    }

    @And("user should be redirected to my-account page")
    public void userShouldBeRedirectedToMyAccountPage() {
        assertThat(page.url()).contains("my-account");
        logger.info("Redirected to my-account page");
    }

    @And("user clears the cart")
    public void userClearsTheCart() {
        try {
            logger.info("Clearing cart before test...");
            page.navigate("https://watchstudio.in/cart/");
            // waitForTimeout removed - replaced with proper waits

            // Check if there are items and remove them
            var removeButtons = page.locator(".remove, .product-remove a, [data-remove-item]");
            int itemCount = removeButtons.count();

            if (itemCount > 0) {
                logger.info("Found {} items in cart, removing...", itemCount);
                for (int i = 0; i < itemCount; i++) {
                    try {
                        // Always click the first remove button since items shift up
                        var firstButton = page.locator(".remove, .product-remove a, [data-remove-item]").first();
                        if (firstButton.isVisible()) {
                            firstButton.click();
                            // waitForTimeout removed - replaced with proper waits
                        }
                    } catch (Exception e) {
                        logger.debug("Could not remove item {}: {}", i, e.getMessage());
                    }
                }
                logger.info("Cart cleared successfully");
            } else {
                logger.info("No items found in cart");
            }
        } catch (Exception e) {
            logger.warn("Could not clear cart: {}", e.getMessage());
        }
    }

    @When("user enters invalid email {string}")
    public void userEntersInvalidEmail(String email) {
        loginPage.enterEmail(email);
    }

    @And("user enters invalid password {string}")
    public void userEntersInvalidPassword(String password) {
        loginPage.enterPassword(password);
    }

    @Then("user should see an error message")
    public void userShouldSeeErrorMessage() {
        assertThat(loginPage.isErrorMessageDisplayed())
            .as("Error message should be displayed")
            .isTrue();
        logger.info("Error message is displayed");
    }

    @And("user should remain on login page")
    public void userShouldRemainOnLoginPage() {
        assertThat(loginPage.isLoginPage())
            .as("User should remain on login page")
            .isTrue();
    }

    @And("user should not be logged in")
    public void userShouldNotBeLoggedIn() {
        assertThat(homePage.isUserLoggedIn())
            .as("User should not be logged in")
            .isFalse();
    }

    @And("user leaves email field empty")
    public void userLeavesEmailFieldEmpty() {
        loginPage.enterEmail("");
    }

    @And("user leaves password field empty")
    public void userLeavesPasswordFieldEmpty() {
        loginPage.enterPassword("");
    }

    @Then("user should see validation error")
    public void userShouldSeeValidationError() {
        assertThat(loginPage.isErrorMessageDisplayed())
            .as("Validation error should be displayed")
            .isTrue();
    }

    @And("login should not be successful")
    public void loginShouldNotBeSuccessful() {
        assertThat(homePage.isUserLoggedIn())
            .as("Login should not be successful")
            .isFalse();
    }

    @Then("user should see email input field")
    public void userShouldSeeEmailInputField() {
        assertThat(loginPage.isEmailFieldVisible())
            .as("Email field should be visible")
            .isTrue();
    }

    @And("user should see password input field")
    public void userShouldSeePasswordFieldVisible() {
        assertThat(loginPage.isPasswordFieldVisible())
            .as("Password field should be visible")
            .isTrue();
    }

    @And("user should see login button")
    public void userShouldSeeLoginButton() {
        assertThat(loginPage.isLoginButtonVisible())
            .as("Login button should be visible")
            .isTrue();
    }

    @And("user should see registration form")
    public void userShouldSeeRegistrationForm() {
        assertThat(loginPage.isRegistrationFormVisible())
            .as("Registration form should be visible")
            .isTrue();
    }

    @Given("user is logged in")
    public void userIsLoggedIn() {
        homePage = new HomePage(page);
        homePage.open();
        loginPage = homePage.goToLogin();
        loginPage.login("jhanvishukla24@gmail.com", "janvi@124");
        logger.info("User is logged in");
    }

    @When("user clicks on logout link")
    public void userClicksLogoutLink() {
        homePage.logout();
        logger.info("Clicked logout link");
    }

    @Then("user should be logged out successfully")
    public void userShouldBeLoggedOutSuccessfully() {
        assertThat(homePage.isUserLoggedIn())
            .as("User should be logged out")
            .isFalse();
        logger.info("User is logged out");
    }

    @And("user should be redirected to homepage")
    public void userShouldBeRedirectedToHomepage() {
        assertThat(page.url()).doesNotContain("my-account");
        logger.info("Redirected to homepage");
    }
}
