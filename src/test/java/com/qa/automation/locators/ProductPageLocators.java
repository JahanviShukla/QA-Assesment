package com.qa.automation.locators;

/**
 * All locators for WatchStudio Product Pages
 */
public class ProductPageLocators {

    // Page verification
    public static final String PAGE_URL = "*/product/*";
    public static final String PRODUCT_TITLE = "h1.product_title, .product-title";

    // Product details
    public static final String PRODUCT_PRICE = ".price, .woocommerce-Price-amount";
    public static final String PRODUCT_DESCRIPTION = ".woocommerce-product-details__short-description, .product-description";
    public static final String PRODUCT_IMAGE = ".woocommerce-product-gallery img, .product-image";
    public static final String PRODUCT_SKU = ".sku, .product-sku";

    // Add to cart
    public static final String ADD_TO_CART_BUTTON = "button.single_add_to_cart_button, .add_to_cart_button";
    public static final String QUANTITY_INPUT = "input[name='quantity'], .qty";

    // Variations (size, color, etc.)
    public static final String VARIATION_SELECT = "select[name*='attribute']";
    public static final String VARIATION_SWATCH = ".swatch-color, .variable-items-wrapper button";

    // Stock status
    public static final String IN_STOCK = ".stock:not(.out-of-stock), .in-stock";
    public static final String OUT_OF_STOCK = ".out-of-stock, stock.out-of-stock";

    // Success message
    public static final String ADDED_TO_CART_MESSAGE = ".woocommerce-message, .success-message";

    // Related products
    public static final String RELATED_PRODUCTS = ".related.products";
    public static final String UPSELL_PRODUCTS = ".upsells.products";
}
