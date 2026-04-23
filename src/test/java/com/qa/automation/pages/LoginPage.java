package com.qa.automation.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

public class LoginPage extends BasePage {
    private final Logger logger = LoggerFactory.getLogger(LoginPage.class);

    // Login form selectors (LEFT side)
    private final String loginEmailSelector = "//input[@id='username']";
    private final String loginPasswordSelector = "//input[@id='password']";
    private final String loginButtonSelector = "button[name='login'], .woocommerce-form-login__submit, .u-column1 button[name='login']";

    // Registration form selectors (RIGHT side)
    private final String regEmailSelector = "input[name='email'], #reg_email, .u-column2 input[name='email']";
    private final String regPasswordSelector = "input[name='password'], #reg_password, .u-column2 input[name='password']";
    private final String regButtonSelector = "button[name='register'], .woocommerce-Button--register, .u-column2 button[name='register']";

    // Common selectors
    private final String errorMessageSelector = ".woocommerce-error, .woocommerce-message, .error, [role='alert']";
    private final String logoutLinkSelector = "a[href*='customer-logout'], .woocommerce-MyAccount-navigation-link--woocommerce-logout";
    private final String successMessageSelector = ".woocommerce-message, .success-message";

    public LoginPage(Page page) {
        super(page);
    }

    public boolean isLoaded() {
        // Check if either login or registration form is visible
        return isVisible(page.locator(loginEmailSelector)) ||
               isVisible(page.locator(regEmailSelector));
    }

    // ========== LOGIN METHODS ==========

    // ========== REGISTRATION METHODS ==========

    public HomePage register(String email, String password) {
        logger.info("Attempting registration with email: {}", email);

        // Enter email in registration form
        Locator regEmail = page.locator(regEmailSelector);
        waitForElement(regEmail, "Registration email input");
        fill(regEmail, email);

        // Enter password in registration form
        Locator regPassword = page.locator(regPasswordSelector);
        fill(regPassword, password);

        // Click register button
        Locator regBtn = page.locator(regButtonSelector);
        click(regBtn, "Register button");

        // Wait for registration to complete
        page.waitForTimeout(3000);

        if (isRegistrationSuccess()) {
            logger.info("Registration successful");
            return new HomePage(page);
        } else {
            logger.error("Registration failed");
            throw new RuntimeException("Registration failed - Check error messages");
        }
    }

    public boolean isRegistrationSuccess() {
        Locator successMsg = page.locator(successMessageSelector);
        return successMsg.isVisible() &&
               successMsg.textContent().toLowerCase().contains("registration");
    }

    // ========== VALIDATION METHODS ==========

    public boolean isErrorPresent() {
        Locator error = page.locator(errorMessageSelector);
        return error.isVisible();
    }

    public String getErrorMessage() {
        Locator error = page.locator(errorMessageSelector);
        if (error.isVisible()) {
            return getText(error);
        }
        return "";
    }

    public String getSuccessMessage() {
        Locator success = page.locator(successMessageSelector);
        if (success.isVisible()) {
            return getText(success);
        }
        return "";
    }

    // ========== LOGOUT METHOD ==========

    public HomePage logout() {
        logger.info("Logging out");
        Locator logoutLink = page.locator(logoutLinkSelector);
        if (logoutLink.isVisible()) {
            click(logoutLink, "Logout link");
            waitForLoadState();
        }
        return new HomePage(page);
    }

    // ========== UTILITY METHODS ==========

    public void waitForLoginPageLoad() {
        waitForElement(page.locator(loginEmailSelector), "Login email input field");
    }

    public boolean isLoginFormVisible() {
        return isVisible(page.locator(loginEmailSelector));
    }

    public boolean isRegistrationFormVisible() {
        return isVisible(page.locator(regEmailSelector));
    }

    public String getPageTitle() {
        return page.title();
    }

    public boolean isUserLoggedIn() {
        // Check if we're on the my-account page and logged in
        return page.url().contains("my-account") &&
               !isLoginFormVisible();
    }

    // ========== CUCUMBER-FRIENDLY METHODS ==========

    public void enterEmail(String email) {
        Locator loginEmail = page.locator(loginEmailSelector);
        waitForElement(loginEmail, "Login email input");
        fill(loginEmail, email);
    }

    public void enterPassword(String password) {
        Locator loginPassword = page.locator(loginPasswordSelector);
        fill(loginPassword, password);
    }

    public HomePage clickLoginButton() {
        Locator loginBtn = page.locator(loginButtonSelector);
        click(loginBtn, "Login button");
        // Wait for navigation to complete after login
        page.waitForLoadState(com.microsoft.playwright.options.LoadState.NETWORKIDLE);
        return new HomePage(page);
    }

    public boolean isErrorMessageDisplayed() {
        return isErrorPresent();
    }

    public boolean isLoginPage() {
        return isLoginFormVisible();
    }

    public boolean isEmailFieldVisible() {
        return isVisible(page.locator(loginEmailSelector));
    }

    public boolean isPasswordFieldVisible() {
        return isVisible(page.locator(loginPasswordSelector));
    }

    public boolean isLoginButtonVisible() {
        return isVisible(page.locator(loginButtonSelector));
    }

    public HomePage login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        return clickLoginButton();
    }
}
