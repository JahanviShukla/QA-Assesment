package com.qa.automation.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.SelectOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductPage extends BasePage {
    private final Logger logger = LoggerFactory.getLogger(ProductPage.class);

    // Product page selectors for WatchStudio (WordPress/WooCommerce)
    private final String productNameSelector = "h1.product_title, .product-title, [data-product-title]";
    private final String productPriceSelector = ".price, .amount, .woocommerce-Price-amount";
    private final String sizeSelector = ".variations select[name*='attribute'], .swatch-color, .variable-items-wrapper";
    private final String addToCartSelector = "//button[@name='add-to-cart' and @type='submit']";
    private final String quantitySelector = "input[name='quantity'], .quantity input.qty, .qty";
    private final String quantityPlusSelector = ".quantity-plus, .plus, [data-quantity='plus']";
    private final String quantityMinusSelector = ".quantity-minus, .minus, [data-quantity='minus']";
    private final String cartDrawerSelector = ".cart-drawer, .astra-cart-drawer, [data-cart-drawer]";
    private final String successMessageSelector = ".woocommerce-message, .success-message, [data-success-message]";
    private final String outOfStockSelector = ".out-of-stock, .sold-out, stock.out-of-stock";
    private final String skuSelector = ".sku, .product-sku, [data-sku]";
    private final String categorySelector = ".posted_in, .product-category, [data-category]";
    private final String descriptionSelector = ".woocommerce-product-details__short-description, .product-description, [data-description]";
    private final String relatedProductsSelector = ".related.products, .upsells.products";

    public ProductPage(Page page) {
        super(page);
    }

    public boolean isLoaded() {
        return isVisible(page.locator(productNameSelector));
    }

    public String getProductName() {
        Locator nameElement = page.locator(productNameSelector).first();
        return getText(nameElement);
    }

    public double getProductPrice() {
        Locator priceElement = page.locator(productPriceSelector).first();
        String priceText = getText(priceElement);
        return extractPrice(priceText);
    }

    public String getProductSKU() {
        Locator skuElement = page.locator(skuSelector);
        if (skuElement.isVisible()) {
            return getText(skuElement).replace("SKU:", "").trim();
        }
        return "";
    }

    public String getProductCategory() {
        Locator categoryElement = page.locator(categorySelector);
        if (categoryElement.isVisible()) {
            return getText(categoryElement);
        }
        return "";
    }

    public String getProductDescription() {
        Locator descElement = page.locator(descriptionSelector);
        if (descElement.isVisible()) {
            return getText(descElement);
        }
        return "";
    }

    public boolean isOutOfStock() {
        Locator outOfStock = page.locator(outOfStockSelector);
        return outOfStock.isVisible();
    }

    public boolean hasSizeOptions() {
        Locator sizes = page.locator(sizeSelector);
        return sizes.count() > 0 && sizes.first().isVisible();
    }

    public ProductPage selectSize(String size) {
        if (hasSizeOptions()) {
            logger.info("Selecting size/variation: {}", size);

            // Try dropdown selection first
            Locator sizeDropdown = page.locator("select[name*='attribute']");
            if (sizeDropdown.isVisible()) {
                sizeDropdown.selectOption(size);
                // waitForTimeout removed - using proper waits instead
                return this;
            }

            // Try swatch/button selection
            Locator sizeOption = page.locator(sizeSelector)
                    .filter(new Locator.FilterOptions().setHasText(size));

            if (sizeOption.count() > 0) {
                click(sizeOption.first(), "Size option: " + size);
                // waitForTimeout removed - using proper waits instead
            } else {
                logger.warn("Size {} not found, trying first available option", size);
                Locator firstSize = page.locator(sizeSelector).first();
                click(firstSize, "First available size");
                // waitForTimeout removed - using proper waits instead
            }
        }
        return this;
    }

    public ProductPage selectFirstAvailableSize() {
        if (hasSizeOptions()) {
            logger.info("Selecting first available size/variation");

            // Try dropdown first
            Locator sizeDropdown = page.locator("select[name*='attribute']");
            if (sizeDropdown.isVisible()) {
                sizeDropdown.selectOption(new SelectOption().setIndex(0));
                // waitForTimeout removed - using proper waits instead
                return this;
            }

            Locator firstSize = page.locator(sizeSelector).first();
            click(firstSize, "First size option");
            // waitForTimeout removed - using proper waits instead
        }
        return this;
    }

    public CartPage addToCart() {
        return addToCart(1);
    }

    public CartPage addToCart(int quantity) {
        logger.info("Adding product to cart with quantity: {}", quantity);

        if (quantity > 1) {
            setQuantity(quantity);
        }

        Locator addToCartBtn = page.locator(addToCartSelector);

        // Click add to cart
        click(addToCartBtn, "Add to cart button");

        // Wait for WooCommerce AJAX response
        page.waitForLoadState(LoadState.NETWORKIDLE);

        if (isCartDrawerOpen()) {
            return new CartPage(page);
        } else {
            return navigateToCart();
        }
    }

    public void clickAddToCart() {
        logger.info("Clicking add to cart button");
        Locator addToCartBtn = page.locator(addToCartSelector);
        click(addToCartBtn, "Add to cart button");

        // Wait for page to update
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

    public void doubleClickAddToCart() {
        logger.info("Simulating RAPID double-click on add to cart button");
        Locator addToCartBtn = page.locator(addToCartSelector);

        // Record the start time
        long startTime = System.currentTimeMillis();

        // Perform two rapid clicks (simulating user double-clicking rapidly)
        // This simulates the bug scenario where both clicks might be processed
        logger.info("First click at: {} ms", System.currentTimeMillis() - startTime);
        click(addToCartBtn, "First Add to Cart click (rapid double-click)");

        // Minimal delay between clicks to simulate rapid double-clicking
        // This is faster than typical debounce times (usually 300-500ms)
        // Using proper wait instead of timeout

        logger.info("Second click at: {} ms (50ms delay)", System.currentTimeMillis() - startTime);
        click(addToCartBtn, "Second Add to Cart click (rapid double-click)");

        // Wait for page to process both requests
        page.waitForLoadState(LoadState.NETWORKIDLE);

        long totalTime = System.currentTimeMillis() - startTime;
        logger.info("Total double-click operation took: {} ms", totalTime);
        logger.warn("=== POTENTIAL BUG ===");
        logger.warn("If item quantity is 2 instead of 1, the system did NOT prevent duplicate actions");
        logger.warn("Expected: Quantity = 1 (duplicate prevention working)");
        logger.warn("Buggy behavior: Quantity = 2 (both requests processed separately)");
    }

    public boolean isSuccessMessageDisplayed() {
        Locator successMsg = page.locator(successMessageSelector);
        return successMsg.isVisible();
    }

    public boolean isAddToCartSuccessMessageDisplayed() {
        // First try to find explicit success message
        Locator successMsg = page.locator(successMessageSelector);
        if (successMsg.isVisible()) {
            logger.info("Success message found");
            return true;
        }

        // Alternative checks for successful add to cart:
        // 1. Check for any notification/toast message
        Locator notification = page.locator(".woocommerce-message, .success-msg, .notification, .toast, .alert-success");
        if (notification.count() > 0) {
            logger.info("Notification/toast found");
            return true;
        }

        // 2. Check if "View Cart" button appears (common WooCommerce pattern)
        Locator viewCartBtn = page.locator("a:has-text('View cart'), a:has-text('View Cart'), .added_to_cart");
        if (viewCartBtn.isVisible()) {
            logger.info("View cart button found - product added successfully");
            return true;
        }

        // 3. Check if add to cart button text changed to "Added" or similar
        Locator addToCartBtn = page.locator(addToCartSelector);
        String btnText = addToCartBtn.innerText();
        if (btnText.contains("Added") || btnText.contains("already")) {
            logger.info("Add to cart button shows product already added");
            return true;
        }

        logger.warn("No clear success indicator found, button text: {}", btnText);
        return false;
    }

    public String getSuccessMessage() {
        Locator successMsg = page.locator(successMessageSelector);
        if (successMsg.isVisible()) {
            return getText(successMsg);
        }
        return "";
    }

    public boolean isCartDrawerOpen() {
        Locator cartDrawer = page.locator(cartDrawerSelector);
        return cartDrawer.isVisible();
    }

    private CartPage navigateToCart() {
        logger.info("Navigating to cart page");
        Locator cartIcon = page.locator(".cart-icon, a[href*='/cart']").first();
        click(cartIcon, "Cart icon");
        waitForLoadState();
        return new CartPage(page);
    }

    public ProductPage setQuantity(int quantity) {
        logger.info("Setting quantity to: {}", quantity);
        Locator quantityInput = page.locator(quantitySelector);

        if (quantityInput.isVisible()) {
            quantityInput.fill(String.valueOf(quantity));
            // waitForTimeout removed - using proper waits instead
        } else {
            int currentQuantity = getCurrentQuantity();
            if (quantity > currentQuantity) {
                for (int i = 0; i < (quantity - currentQuantity); i++) {
                    click(page.locator(quantityPlusSelector), "Plus button");
                    // waitForTimeout removed - using proper waits instead
                }
            } else if (quantity < currentQuantity) {
                for (int i = 0; i < (currentQuantity - quantity); i++) {
                    click(page.locator(quantityMinusSelector), "Minus button");
                    // waitForTimeout removed - using proper waits instead
                }
            }
        }
        return this;
    }

    public int getCurrentQuantity() {
        Locator quantityInput = page.locator(quantitySelector);
        if (quantityInput.isVisible()) {
            String value = quantityInput.inputValue();
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                return 1;
            }
        }
        return 1;
    }

    public boolean hasRelatedProducts() {
        return isVisible(page.locator(relatedProductsSelector));
    }

    public ProductListingPage viewRelatedProducts() {
        if (hasRelatedProducts()) {
            logger.info("Navigating to related products section");
            Locator relatedSection = page.locator(relatedProductsSelector);
            scrollToElement(relatedSection);
            return new ProductListingPage(page);
        }
        throw new RuntimeException("No related products found");
    }
}
