package com.qa.automation.locators;

/**
 * All locators for WatchStudio Cart Page
 */
public class CartPageLocators {

    // Page verification
    public static final String PAGE_URL = "*/cart/*";
    public static final String PAGE_TITLE = "Cart";

    // Cart items
    public static final String CART_ITEMS = ".cart_item, .woocommerce-cart-form__cart-item";
    public static final String CART_ITEM = ".cart_item";
    public static final String ITEM_NAME = ".product-name a, .cart-item-title";
    public static final String ITEM_PRICE = ".product-price, .item-price";
    public static final String ITEM_QUANTITY = "input[name*='quantity'], .qty";
    public static final String ITEM_SUBTOTAL = ".product-subtotal, .line-item-total";
    public static final String REMOVE_BUTTON = ".remove, .product-remove a";

    // Cart totals
    public static final String CART_SUBTOTAL = ".cart-subtotal .amount";
    public static final String CART_TOTAL = ".cart-total .amount";
    public static final String CART_TOTAL_WRAPPER = ".cart-total";

    // Quantity controls
    public static final String QUANTITY_PLUS = ".quantity-plus, .plus";
    public static final String QUANTITY_MINUS = ".quantity-minus, .minus";
    public static final String UPDATE_CART_BUTTON = "button[name='update_cart']";

    // Empty cart
    public static final String EMPTY_CART_MESSAGE = ".cart-empty, .empty-cart-message";
    public static final String RETURN_TO_SHOP_LINK = "a[href*='shop'], .return-to-shop";

    // Checkout
    public static final String CHECKOUT_BUTTON = "button.checkout-button, .checkout-btn";
    public static final String PROCEED_TO_CHECKOUT = "a[href*='checkout']";

    // Coupon
    public static final String COUPON_CODE_INPUT = "input[name='coupon_code'], #coupon_code";
    public static final String APPLY_COUPON_BUTTON = "button[name='apply_coupon']";

    // Cart icon/badge
    public static final String HEADER_CART_COUNT = ".cart-count, .hfe-cart-count";
}
