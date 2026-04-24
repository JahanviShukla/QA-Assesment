package com.qa.automation.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.SelectOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SearchResultsPage extends BasePage {
    private final Logger logger = LoggerFactory.getLogger(SearchResultsPage.class);

    // Search results selectors for WatchStudio (WordPress/WooCommerce)
    private final String resultItemSelector = ".product-item, .type-product, [data-result-item]";
    private final String productTitleSelector = ".woocommerce-loop-product__title, .product-title, h2, h3";
    private final String productPriceSelector = ".price, .amount, .woocommerce-Price-amount";
    private final String noResultsSelector = ".woocommerce-info, .no-results, .no-products-found";
    private final String resultCountSelector = ".result-count, .items-found, .woocommerce-result-count";
    private final String addToCartSelector = ".add_to_cart_button, .add-to-cart-btn";

    public SearchResultsPage(Page page) {
        super(page);
    }

    public boolean hasResults() {
        Locator results = page.locator(resultItemSelector);
        return results.count() > 0;
    }

    public int getResultCount() {
        if (hasNoResultsMessage()) {
            return 0;
        }

        Locator results = page.locator(resultItemSelector);
        return results.count();
    }

    public String getResultCountText() {
        Locator countElement = page.locator(resultCountSelector);
        if (countElement.isVisible()) {
            return getText(countElement);
        }
        return "";
    }

    public boolean hasNoResultsMessage() {
        Locator noResults = page.locator(noResultsSelector);
        return noResults.isVisible();
    }

    public ProductPage openFirstResult() {
        logger.info("Opening first search result");
        Locator firstResult = page.locator(resultItemSelector).first()
                .locator("a[href*='/product/']");
        click(firstResult, "First search result");
        waitForLoadState();
        return new ProductPage(page);
    }

    public ProductPage openResultByIndex(int index) {
        logger.info("Opening search result at index: {}", index);
        Locator result = page.locator(resultItemSelector).nth(index)
                .locator("a[href*='/product/']");
        click(result, "Search result at index " + index);
        waitForLoadState();
        return new ProductPage(page);
    }

    public ProductPage openResultByName(String productName) {
        logger.info("Opening search result: {}", productName);

        Locator result = page.locator(resultItemSelector)
                .filter(new Locator.FilterOptions().setHasText(productName));

        if (result.count() > 0) {
            Locator resultLink = result.first().locator("a[href*='/product/']");
            click(resultLink, "Search result: " + productName);
            waitForLoadState();
            return new ProductPage(page);
        } else {
            throw new RuntimeException("Product not found in search results: " + productName);
        }
    }

    public List<String> getResultNames() {
        return page.locator(productTitleSelector).allTextContents();
    }

    public List<String> getResultPrices() {
        return page.locator(productPriceSelector).allTextContents();
    }

    public boolean hasProduct(String productName) {
        List<String> names = getResultNames();
        return names.stream().anyMatch(name ->
            name.toLowerCase().contains(productName.toLowerCase())
        );
    }

    public void addToCartFromSearch(int index) {
        logger.info("Adding product at index {} to cart from search results", index);
        Locator addToCartBtn = page.locator(resultItemSelector).nth(index)
                .locator(addToCartSelector);
        click(addToCartBtn, "Add to cart button");
        // waitForTimeout removed - replaced with proper waits
    }

    public SearchResultsPage sortResults(String sortOption) {
        logger.info("Sorting search results by: {}", sortOption);
        Locator sortDropdown = page.locator("select[name='orderby'], .orderby");
        if (sortDropdown.isVisible()) {
            sortDropdown.selectOption(new SelectOption().setLabel(sortOption));
            waitForLoadState();
        }
        return this;
    }

    public ProductListingPage viewAllResults() {
        logger.info("Viewing all search results as product listing");
        return new ProductListingPage(page);
    }
}
