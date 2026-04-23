package com.qa.automation.locators;

/**
 * All locators for WatchStudio Login/Register Page
 */
public class LoginPageLocators {

    // Page verification
    public static final String PAGE_URL = "*/my-account/*";
    public static final String PAGE_TITLE = "My Account";

    // Login Form (Left side)
    public static final String LOGIN_EMAIL_INPUT = "input[name='username'], #username";
    public static final String LOGIN_PASSWORD_INPUT = "input[name='password'], #password";
    public static final String LOGIN_BUTTON = "button[name='login'], .woocommerce-form-login__submit";
    public static final String REMEMBER_ME_CHECKBOX = "input[name='rememberme']";

    // Registration Form (Right side)
    public static final String REG_EMAIL_INPUT = "input[name='email'], #reg_email";
    public static final String REG_PASSWORD_INPUT = "input[name='password'], #reg_password";
    public static final String REG_BUTTON = "button[name='register'], .woocommerce-Button--register";

    // Messages
    public static final String ERROR_MESSAGE = ".woocommerce-error, .error, [role='alert']";
    public static final String SUCCESS_MESSAGE = ".woocommerce-message, .success-message";

    // Logout
    public static final String LOGOUT_LINK = "a[href*='customer-logout'], .woocommerce-MyAccount-navigation-link--woocommerce-logout";

    // User state indicators
    public static final String USER_DISPLAY_NAME = ".user-name, .woocommerce-MyAccount-content [data-user-name]";
    public static final String LOGGED_IN_INDICATOR = "body.logged-in";
}
