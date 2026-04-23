package com.qa.automation.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

public class BasePage {
    protected final Page page;
    protected final Logger logger;

    public BasePage(Page page) {
        this.page = page;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    protected void navigate(String url) {
        logger.info("Navigating to: {}", url);
        page.navigate(url, new Page.NavigateOptions().setWaitUntil(com.microsoft.playwright.options.WaitUntilState.NETWORKIDLE));
    }

    protected void click(Locator locator) {
        logger.info("Clicking element: {}", locator);
        try {
            locator.click(new Locator.ClickOptions().setTimeout(10000).setForce(true));
        } catch (Exception e) {
            logger.warn("Standard click failed, trying with scroll: {}", e.getMessage());
            locator.scrollIntoViewIfNeeded();
            locator.click(new Locator.ClickOptions().setTimeout(15000).setForce(true));
        }
    }

    protected void click(Locator locator, String description) {
        logger.info("Clicking: {}", description);
        try {
            locator.click(new Locator.ClickOptions().setTimeout(10000).setForce(true));
        } catch (Exception e) {
            logger.warn("Standard click failed for {}, trying with scroll: {}", description, e.getMessage());
            locator.scrollIntoViewIfNeeded();
            locator.click(new Locator.ClickOptions().setTimeout(15000).setForce(true));
        }
    }

    protected void fill(Locator locator, String value) {
        logger.info("Filling {} with: {}", locator, value);
        locator.fill(value);
    }

    protected String getText(Locator locator) {
        String text = locator.textContent();
        logger.info("Got text from {}: {}", locator, text);
        return text;
    }

    protected String getInnerText(Locator locator) {
        String text = locator.innerText();
        logger.info("Got inner text from {}: {}", locator, text);
        return text;
    }

    protected String getAttribute(Locator locator, String name) {
        String value = locator.getAttribute(name);
        logger.info("Got attribute {} from {}: {}", name, locator, value);
        return value;
    }

    protected boolean isVisible(Locator locator) {
        boolean visible = locator.isVisible();
        logger.info("Element {} visible: {}", locator, visible);
        return visible;
    }

    protected boolean isEnabled(Locator locator) {
        boolean enabled = locator.isEnabled();
        logger.info("Element {} enabled: {}", locator, enabled);
        return enabled;
    }

    protected void waitForElement(Locator locator) {
        logger.info("Waiting for element: {}", locator);
        locator.waitFor(new Locator.WaitForOptions().setTimeout(10000));
    }

    protected void waitForElement(Locator locator, String description) {
        logger.info("Waiting for: {}", description);
        locator.waitFor(new Locator.WaitForOptions().setTimeout(10000));
    }

    protected void waitForElementToDisappear(Locator locator) {
        logger.info("Waiting for element to disappear: {}", locator);
        locator.waitFor(new Locator.WaitForOptions().setState(com.microsoft.playwright.options.WaitForSelectorState.HIDDEN).setTimeout(10000));
    }

    protected void waitForUrl(Pattern pattern) {
        logger.info("Waiting for URL pattern: {}", pattern);
        page.waitForURL(pattern);
    }

    protected void waitForUrl(String url) {
        logger.info("Waiting for URL: {}", url);
        page.waitForURL(url);
    }

    protected void waitForLoadState() {
        logger.info("Waiting for network idle");
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

    protected Locator getElementByText(String text) {
        return page.getByText(text);
    }

    protected Locator getElementByRole(String role, String name) {
        return page.getByRole(com.microsoft.playwright.options.AriaRole.valueOf(role.toUpperCase()),
                new Page.GetByRoleOptions().setName(name));
    }

    protected Locator getElementByTestId(String testId) {
        return page.getByTestId(testId);
    }

    protected Locator getElementByLabel(String label) {
        return page.getByLabel(label);
    }

    protected Locator getElementByPlaceholder(String placeholder) {
        return page.getByPlaceholder(placeholder);
    }

    protected double extractPrice(String priceText) {
        String cleaned = priceText.replaceAll("[^0-9.]", "");
        if (cleaned.isEmpty()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(cleaned);
        } catch (NumberFormatException e) {
            logger.warn("Could not parse price: {}", priceText);
            return 0.0;
        }
    }

    protected void scrollToElement(Locator locator) {
        logger.info("Scrolling to element: {}", locator);
        locator.scrollIntoViewIfNeeded();
    }
}
