package com.qa.automation.stepdefinitions;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.qa.automation.config.ConfigReader;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.AfterAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;

public class Hooks {
    private static final Logger logger = LoggerFactory.getLogger(Hooks.class);

    private static Playwright playwright;
    private static com.microsoft.playwright.Browser browser;
    private static ConfigReader config;

    private BrowserContext context;
    private Page page;

    // ThreadLocal for parallel execution support
    private static ThreadLocal<Page> tlPage = new ThreadLocal<>();
    private static ThreadLocal<BrowserContext> tlContext = new ThreadLocal<>();

    @BeforeAll
    public static void setUp() {
        config = ConfigReader.getInstance();
        playwright = Playwright.create();

        browser = playwright.chromium().launch(new com.microsoft.playwright.BrowserType.LaunchOptions()
                .setHeadless(config.isHeadless())
                .setSlowMo(config.getIntProperty("browser.slow.mo", 0))
                .setArgs(java.util.Arrays.asList("--start-maximized")));

        logger.info("Browser launched");
    }

    @Before
    public void beforeEachScenario() {
        context = browser.newContext(new Browser.NewContextOptions()
                .setLocale("en-IN")
                .setRecordVideoDir(Paths.get("test-results/videos"))
                .setRecordVideoSize(1920, 1080));

        page = context.newPage();
        page.setDefaultTimeout(config.getDefaultTimeout());
        page.setDefaultNavigationTimeout(config.getNavigationTimeout());

        // Setup API listeners for debugging
        setupNetworkListeners(page);

        tlPage.set(page);
        tlContext.set(context);

        logger.info("New browser context and page created");

        // Clear cart by navigating to cart page and removing all items
        clearCartIfExists();
    }

    private void clearCartIfExists() {
        try {
            logger.info("Checking if cart needs to be cleared...");
            page.navigate("https://watchstudio.in/cart/", new Page.NavigateOptions().setTimeout(5000));
            page.waitForTimeout(1000);

            // Check if there are any items in cart
            var cartItems = page.locator(".cart_item, [data-cart-item], .woocommerce-cart-form__cart-item");
            if (cartItems.count() > 0) {
                logger.info("Found {} items in cart, clearing them...", cartItems.count());
                // Remove all items by clicking remove buttons
                var removeButtons = page.locator(".remove, .product-remove a, [data-remove-item]");
                int count = removeButtons.count();
                for (int i = 0; i < count; i++) {
                    try {
                        removeButtons.nth(0).click();
                        page.waitForTimeout(500);
                    } catch (Exception e) {
                        logger.debug("Could not remove item: {}", e.getMessage());
                    }
                }
                logger.info("Cart cleared");
            } else {
                logger.info("Cart is already empty");
            }
        } catch (Exception e) {
            logger.debug("Could not clear cart (may not exist or be accessible): {}", e.getMessage());
        }
    }

    private void setupNetworkListeners(Page page) {
        // Log all API requests
        page.onRequest(request -> {
            String url = request.url();
            if (url.contains("/api/") || url.contains("/wp-admin/admin-ajax.php")) {
                logger.info(">> API Request: {} {}", request.method(), url);
                logger.debug(">> Headers: {}", request.headers());
                if (request.postData() != null) {
                    logger.debug(">> Payload: {}", request.postData());
                }
            }
        });

        // Log all API responses
        page.onResponse(response -> {
            String url = response.url();
            if (url.contains("/api/") || url.contains("/wp-admin/admin-ajax.php")) {
                logger.info("<< API Response: {} {} ({})", response.status(), url, response.headers().get("content-type"));
                try {
                    logger.debug("<< Response body: {}", response.text());
                } catch (Exception e) {
                    logger.debug("<< Could not read response body: {}", e.getMessage());
                }
            }
        });

        // Log failed requests (ignore fonts and static resources)
        page.onRequestFailed(request -> {
            String url = request.url();
            // Skip font and static resource failures to reduce log noise
            if (!url.contains(".woff2") && !url.contains(".woff") && !url.contains(".ttf") &&
                !url.contains(".eot") && !url.contains(".otf") && !url.contains("fonts/")) {
                logger.error("!! Request failed: {} {} - {}", request.method(), url, request.failure());
            }
        });
    }

    @After
    public void afterEachScenario() {
        if (context != null) {
            context.close();
        }
        tlPage.remove();
        tlContext.remove();
        logger.info("Browser context closed");
    }

    @AfterAll
    public static void tearDown() {
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
        logger.info("Playwright cleanup complete");
    }

    public static Page getPage() {
        return tlPage.get();
    }
}
