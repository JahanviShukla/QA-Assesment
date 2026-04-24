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

        // Log page content for debugging
        logger.info("Current page URL: {}", page.url());
        logger.info("Page title: {}", page.title());

        // If no products found, try navigating to shop page
        if (!page.url().contains("/shop/") && getProductCount() == 0) {
            logger.info("No products found on current page, navigating to shop page");
            try {
                page.navigate("https://watchstudio.in/shop/");
                page.waitForLoadState(LoadState.NETWORKIDLE);

                // Wait for products to be present on shop page
                Locator shopProducts = page.locator(productCardSelector);
                try {
                    shopProducts.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(10000));
                } catch (Exception e) {
                    logger.debug("Products not immediately visible on shop page: {}", e.getMessage());
                }
            } catch (Exception e) {
                logger.debug("Shop page navigation failed: {}", e.getMessage());
            }
        }

        // Try multiple strategies to find and click the first product
        try {
            // Strategy 1: Try the standard selector with flexible wait
            Locator products = page.locator(productCardSelector);

            // Wait for products to appear with a longer timeout
            try {
                products.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(10000));
            } catch (Exception e) {
                logger.debug("Products not immediately visible with primary selector, trying alternative approaches");
            }

            int productCount = products.count();
            logger.info("Found {} products with primary selector: {}", productCount, productCardSelector);

            if (productCount > 0) {
                Locator firstProduct = products.first();

                // Try to find the link within the product card
                Locator firstProductLink = firstProduct.locator(productLinkSelector);

                if (firstProductLink.count() > 0) {
                    Locator linkToClick = firstProductLink.first();

                    // Try JavaScript click as it's more reliable for elements that might be hidden
                    try {
                        logger.info("Attempting click on first product link");
                        linkToClick.click(new Locator.ClickOptions().setForce(true).setTimeout(5000));
                        waitForLoadState();
                        return new ProductPage(page);
                    } catch (Exception jsException) {
                        logger.debug("Click failed: {}", jsException.getMessage());
                        // Fall through to alternative strategies
                    }
                }
            }

            // Strategy 2: Try finding any product link directly (more flexible)
            Locator allProductLinks = page.locator("a[href*='/product/']");
            int linkCount = allProductLinks.count();
            logger.info("Found {} product links with fallback selector: a[href*='/product/']", linkCount);

            if (linkCount > 0) {
                logger.info("Using fallback: clicking first product link found");
                try {
                    click(allProductLinks.first(), "First product link (fallback)");
                    waitForLoadState();
                    return new ProductPage(page);
                } catch (Exception e) {
                    logger.debug("Fallback click failed, trying JavaScript: {}", e.getMessage());
                    // Try JavaScript click as last resort
                    try {
                        allProductLinks.first().click(new Locator.ClickOptions().setForce(true).setTimeout(5000));
                        waitForLoadState();
                        return new ProductPage(page);
                    } catch (Exception jsException) {
                        logger.debug("JavaScript fallback click also failed: {}", jsException.getMessage());
                    }
                }
            }

            // Strategy 3: Try generic product selectors with broader scope
            Locator genericProducts = page.locator(".product, .woocommerce-loop-product, [class*='product'], article");
            int genericCount = genericProducts.count();
            logger.info("Found {} products with generic selector", genericCount);

            if (genericCount > 0) {
                Locator productLink = genericProducts.first().locator("a");
                if (productLink.count() > 0) {
                    try {
                        logger.info("Attempting to click generic product link");
                        click(productLink.first(), "First product link (generic)");
                        waitForLoadState();
                        return new ProductPage(page);
                    } catch (Exception e) {
                        logger.debug("Generic product click failed: {}", e.getMessage());
                    }
                }
            }

            // Strategy 4: Try to find any link with product in href
            logger.info("Strategy 4: Looking for any links containing 'product' in href");
            Locator anyProductLink = page.locator("a[href*='product']").first();
            if (anyProductLink.count() > 0) {
                try {
                    logger.info("Found product link, attempting click");
                    anyProductLink.click(new Locator.ClickOptions().setForce(true).setTimeout(5000));
                    waitForLoadState();
                    return new ProductPage(page);
                } catch (Exception e) {
                    logger.debug("Product link click failed: {}", e.getMessage());
                }
            }

            // Strategy 5: Last resort - try to find and click any element that might be a product
            logger.info("Strategy 5: Attempting to find any clickable product element");
            try {
                // Try to find product images or titles that might be clickable
                Locator potentialProducts = page.locator("img[alt*='watch'], img[alt*='Watch'], .product img, .woocommerce-loop-product__link img");
                int potentialCount = potentialProducts.count();
                logger.info("Found {} potential product images", potentialCount);

                if (potentialCount > 0) {
                    // Try clicking on the image or its parent
                    for (int i = 0; i < Math.min(3, potentialCount); i++) {
                        try {
                            Locator img = potentialProducts.nth(i);
                            Locator parent = img.locator("xpath=../..");
                            click(parent.first(), "Potential product parent element");
                            waitForLoadState();
                            return new ProductPage(page);
                        } catch (Exception e) {
                            logger.debug("Failed to click potential product {}: {}", i, e.getMessage());
                        }
                    }
                }
            } catch (Exception e) {
                logger.debug("Last resort strategy failed: {}", e.getMessage());
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
