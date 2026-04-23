package com.qa.automation.locators;

/**
 * All locators for WatchStudio Home Page
 * Centralized locator management for easy maintenance
 */
public class HomePageLocators {

    // Login Button - Most robust XPath
    public static final String LOGIN_BUTTON = "//a[.//i[@class='far fa-user' and @aria-hidden='true']]";
    public static final String LOGIN_BUTTON_ALT = "//a[contains(@href, '/my-account/') and .//i[@class='far fa-user']]";

    // Search
    public static final String SEARCH_ICON = ".search-trigger, [aria-label*='search'], .search-icon";
    public static final String SEARCH_INPUT = "input[type='search'], .search-input, [name='s']";

    // Cart
    public static final String CART_ICON = "#hfe-menu-cart__toggle_button, .ast-cart-menu-wrap";
    public static final String CART_COUNT = ".cart-count, .hfe-cart-count";

    // Navigation
    public static final String MENS_WATCHES_LINK = "a[href*='mens-watches']";
    public static final String WOMENS_WATCHES_LINK = "a[href*='womens-watches']";
    public static final String BRANDS_LINK = "a[href*='brands']";

    // Page verification
    public static final String PAGE_TITLE = "Watch Studio";
    public static final String LOGO = ".site-logo, img[alt*='Watch Studio']";
}
