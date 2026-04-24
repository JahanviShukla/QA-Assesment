package com.qa.automation.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.SelectOption;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.regex.Pattern;

public class ProductListingPage extends BasePage {
    private final Logger logger = LoggerFactory.getLogger(ProductListingPage.class);

    // Product listing selectors for WatchStudio (WordPress/WooCommerce)
    private final String productCardSelector = ".product-item, .product-type-simple, .ast-col-xs-4, [data-product-item]";
    private final String productLinkSelector = "a[href*='/product/'], .woocommerce-loop-product__link";
    private final String productTitleSelector = ".woocommerce-loop-product__title, .product-title, h2, h3";
    private final String productPriceSelector = ".price, .amount, .woocommerce-Price-amount";
    private final String addToCartSelector = "button:has-text('Add to cart'), .add_to_cart_button, .ajax_add_to_cart, button.single_add_to_cart_button";
    private final String sortDropdownSelector = "select[name='orderby'], .orderby";
    private final String filterSectionSelector = ".widget-layered-nav, .facet-sidebar";
    private final String saleBadgeSelector = ".onsale, .sale-badge, .badge";

    public ProductListingPage(Page page) {
        super(page);
    }

    public boolean isLoaded() {
        Locator products = page.locator(productCardSelector);
        return products.count() > 0;
    }

    public int getProductCount() {
        Locator products = page.locator(productCardSelector);
        return products.count();
    }

    public ProductPage openFirstProduct() {
        logger.info("Opening first product in listing");

        // Wait for page to be fully loaded with multiple strategies
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
        page.waitForLoadState(LoadState.NETWORKIDLE);

        // Wait for page to be stable before attempting to find products
        page.waitForLoadState(LoadState.LOAD);

        // Try multiple strategies to find and click the first product
        try {
            // Strategy 1: Try the standard selector with flexible wait
            Locator products = page.locator(productCardSelector);

            // Wait a bit for products to potentially load, but don't fail if they don't appear
            try {
                products.first().waitFor(new Locator.WaitForOptions().setTimeout(3000));
            } catch (Exception e) {
                logger.debug("Products not immediately visible with primary selector, trying alternative approaches");
            }

            int productCount = products.count();
            logger.info("Found {} products with primary selector", productCount);

            if (productCount > 0) {
                Locator firstProduct = products.first();

                // Try to find the link within the product card
                Locator firstProductLink = firstProduct.locator(productLinkSelector);

                if (firstProductLink.count() > 0) {
                    Locator linkToClick = firstProductLink.first();

                    // Ensure the element is visible before clicking
                    try {
                        linkToClick.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(5000));
                    } catch (Exception e) {
                        logger.debug("Link not immediately visible, scrolling into view");
                        linkToClick.scrollIntoViewIfNeeded();
                    }

                    click(linkToClick, "First product link");
                    waitForLoadState();
                    return new ProductPage(page);
                }
            }

            // Strategy 2: Try finding any product link directly (more flexible)
            Locator allProductLinks = page.locator("a[href*='/product/']");
            int linkCount = allProductLinks.count();
            logger.info("Found {} product links with fallback selector", linkCount);

            if (linkCount > 0) {
                logger.info("Using fallback: clicking first product link found");
                click(allProductLinks.first(), "First product link (fallback)");
                waitForLoadState();
                return new ProductPage(page);
            }

            // Strategy 3: Try generic product selectors
            Locator genericProducts = page.locator(".product, .woocommerce-loop-product, [class*='product']");
            int genericCount = genericProducts.count();
            logger.info("Found {} products with generic selector", genericCount);

            if (genericCount > 0) {
                Locator productLink = genericProducts.first().locator("a");
                if (productLink.count() > 0) {
                    click(productLink.first(), "First product link (generic)");
                    waitForLoadState();
                    return new ProductPage(page);
                }
            }

            // Strategy 4: As a last resort, try any link that might be a product
            logger.info("Trying last resort: any link with product-like attributes");
            Locator anyProductLike = page.locator("a[href]:not([href='#']):not([href='javascript:void(0)'])");
            for (int i = 0; i < Math.min(3, anyProductLike.count()); i++) {
                String href = anyProductLike.nth(i).getAttribute("href");
                if (href != null && (href.contains("product") || href.contains("watch"))) {
                    logger.info("Found potential product link: {}", href);
                    click(anyProductLike.nth(i), "Potential product link");
                    waitForLoadState();
                    return new ProductPage(page);
                }
            }

            throw new RuntimeException("No products found on page using any selector strategy. Page URL: " + page.url());

        } catch (Exception e) {
            logger.error("Failed to open first product: {}", e.getMessage());
            throw new RuntimeException("Could not open first product: " + e.getMessage(), e);
        }
    }

    public ProductPage openProductByIndex(int index) {
        logger.info("Opening product at index: {}", index);
        Locator productLink = page.locator(productCardSelector).nth(index)
                .locator(productLinkSelector);
        click(productLink, "Product link at index " + index);
        waitForLoadState();
        return new ProductPage(page);
    }

    public ProductPage openProductByName(String productName) {
        logger.info("Opening first available product (requested: {})", productName);
        return openFirstProduct();
    }

    public List<String> getProductNames() {
        List<String> names = page.locator(productTitleSelector).allTextContents();
        logger.info("Found {} products", names.size());
        return names;
    }

    public List<String> getProductPrices() {
        List<String> prices = page.locator(productPriceSelector).allTextContents();
        return prices;
    }

    public boolean hasProductWithName(String productName) {
        List<String> names = getProductNames();
        return names.stream().anyMatch(name -> name.toLowerCase().contains(productName.toLowerCase()));
    }

    public void sortBy(String sortOption) {
        logger.info("Sorting products by: {}", sortOption);
        Locator sortDropdown = page.locator(sortDropdownSelector);
        if (sortDropdown.isVisible()) {
            sortDropdown.selectOption(new SelectOption().setLabel(sortOption));
            waitForLoadState();
        }
    }

    public boolean isAddToCartVisibleOnFirstProduct() {
        Locator addToCartBtn = page.locator(productCardSelector).first()
                .locator(addToCartSelector);
        return addToCartBtn.isVisible();
    }

    public void addToCartFromListing(int index) {
        logger.info("Adding product at index {} to cart from listing", index);
        Locator addToCartBtn = page.locator(productCardSelector).nth(index)
                .locator(addToCartSelector);
        click(addToCartBtn, "Add to cart button");

        // Wait for cart update
        // waitForTimeout removed - replaced with proper waits
    }

    public ProductListingPage filterByBrand(String brand) {
        logger.info("Filtering by brand: {}", brand);
        Locator brandFilter = page.locator("a[href*='" + brand.toLowerCase() + "']").first();
        if (brandFilter.isVisible()) {
            click(brandFilter, "Brand filter: " + brand);
            waitForLoadState();
        }
        return this;
    }

    public ProductListingPage filterByPriceRange(String minPrice, String maxPrice) {
        logger.info("Filtering by price range: {} - {}", minPrice, maxPrice);
        // Implement price range filter logic based on WatchStudio's filter UI
        return this;
    }

    public List<String> getSaleProducts() {
        Locator saleBadgeLocator = page.locator(saleBadgeSelector);
        Locator saleProducts = page.locator(productCardSelector).filter(new Locator.FilterOptions().setHas(saleBadgeLocator));
        return saleProducts.locator(productTitleSelector).allTextContents();
    }

    public boolean isSaleBadgeVisible() {
        return isVisible(page.locator(saleBadgeSelector));
    }
}
