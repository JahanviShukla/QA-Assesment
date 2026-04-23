package com.qa.automation.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

public class HomePage extends BasePage {
    private final Logger logger = LoggerFactory.getLogger(HomePage.class);

    // Selectors for WatchStudio.in (WordPress/WooCommerce/Elementor) - ROBUST SELECTORS
    private final String loginButtonSelector = "//a[.//i[@class='far fa-user' and @aria-hidden='true']], //a[contains(@href, '/my-account/') and .//i[@class='far fa-user']]";
    private final String searchIconSelector = ".search-trigger, [aria-label*='search'], .ajax-search-toggle, .search-icon";
    private final String searchInputSelector = "input[type='search'], .search-input, [name='s'], #search-products-form-input, .ajax-search-input";
    private final String cartIconSelector = "header #hfe-menu-cart__toggle_button, .site-header a.elementor-icon[href*='/cart'], .ast-cart-menu-wrap";
    private final String cartCountSelector = ".ast-cart-menu-wrap .count, .cart-count, .astra-cart-drawer .count, .shopping-cart .count, header .count, .cart-icon .count, .cart-count-badge, [data-cart-count]";
    private final String menuSelector = ".menu-toggle, .ast-menu-toggle, [aria-label*='menu'], .elementor-menu-toggle";
    private final String categorySelector = "nav a[href*='product-category'], .category-link, .menu-item a[href*='product']";

    public HomePage(Page page) {
        super(page);
    }

    public void open() {
        String url = System.getProperty("base.url", "https://watchstudio.in/");
        logger.info("Opening WatchStudio homepage: {}", url);
        navigate(url);
        waitForLoadState();
    }

    public LoginPage goToLogin() {
        logger.info("Navigating to login page");

        // Wait for page load
        page.waitForLoadState();

        // Strategy 1: Try to find and click visible login button
        try {
            Locator allLoginLinks = page.locator("a[href*='my-account']");

            // Find the first visible one
            Locator loginLinks = null;
            for (int i = 0; i < allLoginLinks.count(); i++) {
                if (allLoginLinks.nth(i).isVisible()) {
                    loginLinks = allLoginLinks.nth(i);
                    break;
                }
            }

            if (loginLinks != null) {
                logger.info("Found visible login link");

                // Scroll into view and wait for it to be ready
                loginLinks.scrollIntoViewIfNeeded();
                loginLinks.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.ATTACHED));

                // Click with force option
                loginLinks.click(new Locator.ClickOptions()
                    .setForce(true)
                    .setTimeout(5000)
                );

                // Wait for navigation
                page.waitForTimeout(2000);

                if (page.url().contains("my-account")) {
                    logger.info("Successfully navigated to login page via click");
                    return new LoginPage(page);
                }
            }
        } catch (Exception e) {
            logger.warn("Click strategy failed: {}", e.getMessage());
        }

        // Strategy 2: Direct navigation fallback
        logger.info("Using direct navigation fallback");
        page.navigate("https://watchstudio.in/my-account/");
        page.waitForLoadState();

        return new LoginPage(page);
    }

    public CartPage goToCart() {
        logger.info("Navigating to cart");
        Locator cartIcon = page.locator(cartIconSelector).first();
        click(cartIcon, "Cart icon");
        waitForUrl(Pattern.compile(".*cart.*"));
        return new CartPage(page);
    }

    public SearchResultsPage searchProduct(String searchTerm) {
        logger.info("Searching for product: {}", searchTerm);

        // Click search icon if needed
        Locator searchIcon = page.locator(searchIconSelector);
        if (searchIcon.isVisible()) {
            click(searchIcon, "Search icon");
        }

        // Wait for search input and enter term
        Locator searchInput = page.locator(searchInputSelector);
        waitForElement(searchInput, "Search input");
        fill(searchInput, searchTerm);

        // Submit search (Enter key or search button)
        searchInput.press("Enter");
        waitForLoadState();

        return new SearchResultsPage(page);
    }

    public int getCartItemCount() {
        // Try multiple strategies to find the cart count
        try {
            // Strategy 1: Look for cart count badge in header (element count, not price)
            Locator cartCount = page.locator(cartCountSelector);
            if (cartCount.count() > 0) {
                for (int i = 0; i < cartCount.count(); i++) {
                    Locator element = cartCount.nth(i);
                    if (element.isVisible()) {
                        String count = getText(element).trim();
                        logger.info("Found cart count element with text: {}", count);

                        // Only parse if it's a small number (count), not a price
                        // Prices are typically > 100, counts are usually < 100
                        count = count.replaceAll("[^0-9]", "");
                        if (!count.isEmpty()) {
                            int countValue = Integer.parseInt(count);
                            if (countValue < 100) { // It's a count, not a price
                                return countValue;
                            }
                        }
                    }
                }
            }

            // Strategy 2: Check if cart drawer/slideout is open and get quantity from there
            Locator cartDrawerQuantity = page.locator(
                "//div[contains(@class, 'cart-drawer')]//input[contains(@name, 'qty') or contains(@class, 'qty')]"
            );
            if (cartDrawerQuantity.isVisible()) {
                String qty = cartDrawerQuantity.getAttribute("value");
                logger.info("Cart drawer quantity value: {}", qty);
                if (qty != null && !qty.isEmpty()) {
                    return Integer.parseInt(qty);
                }
            }

            // Strategy 3: Click cart icon to open drawer and check quantity
            try {
                Locator cartIcon = page.locator(cartIconSelector).first();
                if (cartIcon.isVisible()) {
                    cartIcon.click();
                    page.waitForTimeout(1000); // Wait for drawer to open

                    Locator quantityInput = page.locator(
                        "//input[contains(@name, 'qty') or contains(@class, 'qty')]"
                    ).first();

                    if (quantityInput.isVisible()) {
                        String qty = quantityInput.getAttribute("value");
                        logger.info("Quantity input value after opening cart: {}", qty);
                        if (qty != null && !qty.isEmpty()) {
                            return Integer.parseInt(qty);
                        }
                    }

                    // Close drawer by clicking outside or escape
                    page.keyboard().press("Escape");
                }
            } catch (Exception e) {
                logger.debug("Could not open cart drawer: {}", e.getMessage());
            }

            logger.info("No cart count found, assuming 0");
            return 0;

        } catch (Exception e) {
            logger.warn("Error getting cart count: {}", e.getMessage());
            return 0;
        }
    }

    public boolean isCartIconVisible() {
        return isVisible(page.locator(cartIconSelector));
    }

    public boolean isUserLoggedIn() {
        // Check for logged-in state indicators
        Locator loggedInIndicator = page.locator("a[href*='customer-logout'], .user-name, [data-logged-in='true']");
        return loggedInIndicator.isVisible();
    }

    public ProductListingPage navigateToCategory(String categoryUrl) {
        logger.info("Navigating to category: {}", categoryUrl);
        page.navigate(categoryUrl);
        waitForLoadState();
        return new ProductListingPage(page);
    }

    public ProductListingPage navigateToBrands() {
        logger.info("Navigating to brands section");
        Locator brandsLink = page.locator("a[href*='brands'], .brands-link").first();
        click(brandsLink, "Brands link");
        waitForLoadState();
        return new ProductListingPage(page);
    }

    public ProductListingPage navigateToMensWatches() {
        logger.info("Navigating to men's watches");
        try {
            // Ensure we're on a valid page state
            page.waitForLoadState();

            Locator mensWatchesLink = page.locator("a.hfe-menu-item[href*='for-him']").first();
            if (mensWatchesLink.isVisible()) {
                click(mensWatchesLink, "Men's Watches link");
            } else {
                logger.info("Men's watches link not visible, using direct navigation");
                page.navigate("https://watchstudio.in/product-category/mens-watches/");
            }
            waitForLoadState();
        } catch (Exception e) {
            logger.warn("Error navigating to men's watches, using direct navigation: {}", e.getMessage());
            page.navigate("https://watchstudio.in/product-category/mens-watches/");
            waitForLoadState();
        }
        return new ProductListingPage(page);
    }

    public ProductListingPage navigateToWomensWatches() {
        logger.info("Navigating to women's watches");
        Locator womensWatchesLink = page.locator("a[href*='womens-watches'], .womens-watches-link").first();
        if (womensWatchesLink.isVisible()) {
            click(womensWatchesLink, "Women's Watches link");
        } else {
            page.navigate("https://watchstudio.in/product-category/womens-watches/");
        }
        waitForLoadState();
        return new ProductListingPage(page);
    }

    public LoginPage logout() {
        logger.info("Logging out from home page");
        page.navigate("https://watchstudio.in/my-account/customer-logout/");
        page.waitForLoadState();
        return new LoginPage(page);
    }
}
