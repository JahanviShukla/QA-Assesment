package com.qa.automation.locators;

/**
 * Common locators used across multiple pages
 */
public class CommonLocators {

    // Loading indicators
    public static final String LOADING_SPINNER = ".loading, .spinner, .loader";
    public static final String AJAX_LOADER = ".blockUI, .woocommerce-ajax-loader";

    // Success/Error messages
    public static final String SUCCESS_MESSAGE = ".woocommerce-message, .success-message, .notice-success";
    public static final String ERROR_MESSAGE = ".woocommerce-error, .error, .notice-error, [role='alert']";
    public static final String WARNING_MESSAGE = ".woocommerce-warning, .warning, .notice-warning";

    // Modals/Popups
    public static final String MODAL_OVERLAY = ".modal-overlay, .blockUI";
    public static final String MODAL_CLOSE = ".modal-close, .close, button:has-text('Close')";

    // Common buttons
    public static final String CONTINUE_SHOPPING = "a[href*='shop'], .continue-shopping";
    public static final String VIEW_CART = "a[href*='cart'], .view-cart";

    // Product listing
    public static final String PRODUCT_CARD = ".product-item, .type-product, .product";
    public static final String PRODUCT_LINK = "a[href*='/product/']";
    public static final String PRODUCT_TITLE = ".woocommerce-loop-product__title, .product-title";
    public static final String PRODUCT_PRICE = ".price, .amount";

    // Search
    public static final String SEARCH_RESULTS = ".products, .product-list";
    public static final String NO_RESULTS = ".woocommerce-info, .no-products-found";

    // Pagination
    public static final String NEXT_PAGE = "a.next, .pagination .next";
    public static final String PREVIOUS_PAGE = "a.prev, .pagination .prev";

    // Footer elements
    public static final String FOOTER = "footer, .site-footer";
    public static final String SOCIAL_LINKS = ".social-icons, .social-links";
}
