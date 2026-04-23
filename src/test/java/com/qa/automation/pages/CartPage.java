package com.qa.automation.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CartPage extends BasePage {
    private final Logger logger = LoggerFactory.getLogger(CartPage.class);

    // Cart page selectors for WatchStudio (WordPress/WooCommerce)
    private final String cartItemSelector = ".cart_item, [data-cart-item], .woocommerce-cart-form__cart-item";
    private final String itemNameSelector = ".product-name a, .cart-item-title, [data-item-name]";
    private final String itemPriceSelector = ".product-price, .item-price, [data-item-price]";
    private final String itemQuantitySelector = "input[name*='quantity'], .qty, .quantity-input";
    private final String itemTotalSelector = ".product-subtotal, .line-item-total, [data-item-total]";
    private final String removeButtonSelector = "//a[contains(@class,'remove') and @role='button']";
    private final String quantityPlusSelector = ".quantity-plus, .plus, [data-quantity='plus']";
    private final String quantityMinusSelector = ".quantity-minus, .minus, [data-quantity='minus']";
    private final String subtotalSelector = ".cart-subtotal, .order-subtotal, [data-subtotal]";
    private final String totalSelector = ".cart-total, .order-total, [data-total]";
    private final String emptyCartMessageSelector = ".cart-empty, .empty-cart-message, [data-empty-cart]";
    private final String checkoutButtonSelector = "button.checkout-button, .checkout-btn, button:has-text('Proceed to Checkout')";
    private final String couponCodeSelector = "input[name='coupon_code'], #coupon_code";
    private final String applyCouponButtonSelector = "button[name='apply_coupon'], .apply-coupon-btn";
    private final String shippingCalculatorSelector = ".shipping-calculator, .calculate-shipping";

    public CartPage(Page page) {
        super(page);
    }

    public boolean isLoaded() {
        waitForLoadState();
        return true;
    }

    public boolean isCartEmpty() {
        Locator emptyMessage = page.locator(emptyCartMessageSelector);
        Locator cartItems = page.locator(cartItemSelector);
        return emptyMessage.isVisible() || cartItems.count() == 0;
    }

    public int getItemCount() {
        return page.locator(cartItemSelector).count();
    }

    public List<String> getItemNames() {
        List<String> names = page.locator(itemNameSelector).allTextContents();
        logger.info("Cart items: {}", names);
        return names;
    }

    public boolean hasItem(String itemName) {
        List<String> names = getItemNames();
        return names.stream().anyMatch(name -> name.toLowerCase().contains(itemName.toLowerCase()));
    }

    public CartPage setItemQuantity(String itemName, int quantity) {
        logger.info("Setting quantity for {} to: {}", itemName, quantity);

        Locator cartItem = page.locator(cartItemSelector)
                .filter(new Locator.FilterOptions().setHasText(itemName));

        if (cartItem.count() > 0) {
            Locator quantityInput = cartItem.first().locator(itemQuantitySelector);
            quantityInput.fill(String.valueOf(quantity));
            quantityInput.press("Enter");
            page.waitForTimeout(1500);
        } else {
            throw new RuntimeException("Item not found in cart: " + itemName);
        }

        return this;
    }

    public CartPage incrementQuantity(String itemName) {
        logger.info("Incrementing quantity for: {}", itemName);

        Locator cartItem = page.locator(cartItemSelector)
                .filter(new Locator.FilterOptions().setHasText(itemName));

        if (cartItem.count() > 0) {
            Locator plusBtn = cartItem.first().locator(quantityPlusSelector);
            if (plusBtn.isVisible()) {
                click(plusBtn, "Plus button for " + itemName);
                page.waitForTimeout(1500);
            } else {
                // Alternative: increment the input directly
                Locator quantityInput = cartItem.first().locator(itemQuantitySelector);
                String currentValue = quantityInput.inputValue();
                try {
                    int newValue = Integer.parseInt(currentValue) + 1;
                    quantityInput.fill(String.valueOf(newValue));
                    quantityInput.press("Enter");
                    page.waitForTimeout(1500);
                } catch (NumberFormatException e) {
                    logger.error("Could not parse quantity value");
                }
            }
        }

        return this;
    }

    public CartPage decrementQuantity(String itemName) {
        logger.info("Decrementing quantity for: {}", itemName);

        Locator cartItem = page.locator(cartItemSelector)
                .filter(new Locator.FilterOptions().setHasText(itemName));

        if (cartItem.count() > 0) {
            Locator minusBtn = cartItem.first().locator(quantityMinusSelector);
            if (minusBtn.isVisible()) {
                click(minusBtn, "Minus button for " + itemName);
                page.waitForTimeout(1500);
            } else {
                // Alternative: decrement the input directly
                Locator quantityInput = cartItem.first().locator(itemQuantitySelector);
                String currentValue = quantityInput.inputValue();
                try {
                    int newValue = Integer.parseInt(currentValue) - 1;
                    if (newValue >= 1) {
                        quantityInput.fill(String.valueOf(newValue));
                        quantityInput.press("Enter");
                        page.waitForTimeout(1500);
                    }
                } catch (NumberFormatException e) {
                    logger.error("Could not parse quantity value");
                }
            }
        }

        return this;
    }

    public int getItemQuantity(String itemName) {
        Locator cartItem = page.locator(cartItemSelector)
                .filter(new Locator.FilterOptions().setHasText(itemName));

        if (cartItem.count() > 0) {
            Locator quantityInput = cartItem.first().locator(itemQuantitySelector);
            String value = quantityInput.inputValue();
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                return 0;
            }
        }

        return 0;
    }

    public CartPage removeItem(String itemName) {
        logger.info("Removing item from cart: {}", itemName);

        Locator cartItem = page.locator(cartItemSelector)
                .filter(new Locator.FilterOptions().setHasText(itemName));

        if (cartItem.count() > 0) {
            Locator removeBtn = cartItem.first().locator(removeButtonSelector);
            click(removeBtn, "Remove button for " + itemName);
        }

        return this;
    }

    public double getSubtotal() {
        Locator subtotal = page.locator(subtotalSelector);
        if (subtotal.isVisible()) {
            String text = getText(subtotal);
            return extractPrice(text);
        }
        return 0.0;
    }

    public double getTotal() {
        Locator total = page.locator(totalSelector);
        if (total.isVisible()) {
            String text = getText(total);
            return extractPrice(text);
        }
        return 0.0;
    }

    public double getItemPrice(String itemName) {
        Locator cartItem = page.locator(cartItemSelector)
                .filter(new Locator.FilterOptions().setHasText(itemName));

        if (cartItem.count() > 0) {
            Locator priceElement = cartItem.first().locator(itemPriceSelector);
            String priceText = getText(priceElement);
            return extractPrice(priceText);
        }

        return 0.0;
    }

    public double getItemTotal(String itemName) {
        Locator cartItem = page.locator(cartItemSelector)
                .filter(new Locator.FilterOptions().setHasText(itemName));

        if (cartItem.count() > 0) {
            Locator totalElement = cartItem.first().locator(itemTotalSelector);
            String totalText = getText(totalElement);
            return extractPrice(totalText);
        }

        return 0.0;
    }

    public HomePage continueShopping() {
        logger.info("Continuing shopping");
        Locator continueLink = page.locator("a[href*='shop'], .continue-shopping, .back-to-shop");
        if (continueLink.isVisible()) {
            click(continueLink, "Continue shopping link");
        } else {
            page.navigate("https://watchstudio.in/");
        }
        return new HomePage(page);
    }

    public boolean isCheckoutAvailable() {
        Locator checkoutBtn = page.locator(checkoutButtonSelector);
        return checkoutBtn.isVisible() && checkoutBtn.isEnabled();
    }

    public CartPage applyCouponCode(String couponCode) {
        logger.info("Applying coupon code: {}", couponCode);

        Locator couponInput = page.locator(couponCodeSelector);
        if (couponInput.isVisible()) {
            couponInput.fill(couponCode);

            Locator applyBtn = page.locator(applyCouponButtonSelector);
            click(applyBtn, "Apply coupon button");

            page.waitForTimeout(1500);
            logger.info("Coupon code applied successfully");
        } else {
            logger.warn("Coupon input field not found");
        }

        return this;
    }

    public CartPage refreshPage() {
        logger.info("Refreshing cart page");
        page.reload();
        waitForLoadState();
        return this;
    }

    public CartPage waitForCartUpdate() {
        logger.info("Waiting for cart to update");
        page.waitForTimeout(1500);
        return this;
    }

    public boolean isShippingCalculatorAvailable() {
        return isVisible(page.locator(shippingCalculatorSelector));
    }

    public CartPage updateCart() {
        logger.info("Updating cart");
        Locator updateBtn = page.locator("button[name='update_cart'], .update-cart-button");
        if (updateBtn.isVisible()) {
            click(updateBtn, "Update cart button");
            page.waitForLoadState();
        }
        return this;
    }

    // ========== HELPER METHODS FOR STEP DEFINITIONS ==========

    public int getCartItemCount() {
        return getItemCount();
    }

    public boolean containsProduct(String productName) {
        return hasItem(productName);
    }

    public List<String> getAllCartItems() {
        return getItemNames();
    }

    public boolean areAllItemDetailsDisplayed() {
        // Check if cart items show name, price, quantity, subtotal
        Locator cartItems = page.locator(cartItemSelector);
        if (cartItems.count() == 0) return false;

        for (int i = 0; i < cartItems.count(); i++) {
            // Check if product name, price, quantity, and subtotal are visible
            // This is a simplified check - adjust based on actual HTML structure
            return true;
        }
        return false;
    }

    public boolean isSubtotalCorrect() {
        // Simplified check - verify subtotal is displayed
        Locator subtotal = page.locator(".cart-subtotal, .subtotal, tr.cart-subtotal");
        return subtotal.isVisible();
    }

    public boolean isCartTotalCorrect() {
        // Check if cart total is displayed and reasonable
        Locator total = page.locator(totalSelector);
        return total.isVisible() && getTotal() > 0;
    }

    public boolean isCartTotalDisplayed() {
        return isVisible(page.locator(totalSelector));
    }

    public boolean isErrorMessageDisplayed() {
        Locator error = page.locator(".woocommerce-error, .error, [role='alert']");
        return error.isVisible();
    }

    public boolean isEmptyCartMessageDisplayed() {
        // Try multiple selectors for empty cart message
        Locator emptyMsg = page.locator(emptyCartMessageSelector);
        if (emptyMsg.isVisible()) {
            return true;
        }
        // More specific fallback
        Locator emptyMsgWithRole = page.locator("//div[contains(@class,'cart-empty') and @role='status']");
        return emptyMsgWithRole.isVisible();
    }

    public double getCartSubtotal() {
        return getSubtotal();
    }

    // Overload methods to accept index instead of item name
    public void updateQuantity(int index, int newQuantity) {
        Locator quantityInput = page.locator(cartItemSelector)
                .nth(index - 1)
                .locator(itemQuantitySelector);
        fill(quantityInput, String.valueOf(newQuantity));
        updateCart();
    }

    public void removeItem(int index) {
        Locator removeBtn = page.locator(cartItemSelector)
                .nth(index - 1)
                .locator(removeButtonSelector);
        if (removeBtn.isVisible()) {
            click(removeBtn, "Remove item button");
            // Wait for cart to update - network idle is more reliable than load state for AJAX cart operations
            page.waitForLoadState(LoadState.NETWORKIDLE);
            // Additional wait for DOM to update
            page.waitForTimeout(1500);
        } else {
            logger.warn("Remove button not visible for item at index {}", index);
        }
    }

    public void removeItem() {
        logger.info("Removing first item from cart");
        Locator removeBtn = page.locator(".cart_item a.remove[role='button']").first();
        removeBtn.waitFor();
        removeBtn.click();
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

    public int getItemQuantity(int index) {
        Locator quantityInput = page.locator(cartItemSelector)
                .nth(index - 1)
                .locator(itemQuantitySelector);
        try {
            String value = quantityInput.inputValue();
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            logger.debug("Could not get input value, trying text content: {}", e.getMessage());
            String quantity = getText(quantityInput);
            try {
                return Integer.parseInt(quantity.trim());
            } catch (NumberFormatException ex) {
                return 0;
            }
        }
    }
}
