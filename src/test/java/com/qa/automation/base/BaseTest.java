package com.qa.automation.base;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import com.qa.automation.config.ConfigReader;
import com.qa.automation.utils.ScreenshotUtils;
import com.qa.automation.utils.TraceUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;

public class BaseTest {
    protected static Playwright playwright;
    protected static Browser browser;
    protected static ConfigReader config;

    protected BrowserContext context;
    protected Page page;

    protected static final Logger logger = LoggerFactory.getLogger(BaseTest.class);

    @BeforeAll
    static void setupPlaywright() {
        config = ConfigReader.getInstance();
        playwright = Playwright.create();

        BrowserType browserType = getBrowserType(config);
        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
                .setHeadless(config.isHeadless())
                .setSlowMo(config.getIntProperty("browser.slow.mo", 0))
                .setArgs(java.util.Arrays.asList("--start-maximized", "--kiosk"));

        browser = browserType.launch(launchOptions);
        logger.info("Browser launched: {}", config.getBrowserType());
    }

    @BeforeEach
    void setupTest() {
        context = browser.newContext(new Browser.NewContextOptions()
                .setLocale("en-IN")
                .setRecordVideoDir(Paths.get("test-results/videos"))
                .setRecordVideoSize(1920, 1080));

        page = context.newPage();

        // Maximize the viewport to match available screen space
        page.evaluate("() => { window.screen.width = window.screen.availWidth; window.screen.height = window.screen.availHeight; }");

        page.setDefaultTimeout(config.getDefaultTimeout());
        page.setDefaultNavigationTimeout(config.getNavigationTimeout());

        setupNetworkListeners();
        logger.info("New browser context and page created");
    }

    @AfterEach
    void tearDownTest() {
        if (page != null) {
            ScreenshotUtils.captureScreenshotOnFailure(page, this.getClass().getSimpleName());
            TraceUtils.captureTraceOnFailure(context, this.getClass().getSimpleName());
        }
        if (context != null) {
            context.close();
        }
    }

    @AfterAll
    static void tearDownPlaywright() {
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
        logger.info("Playwright cleanup complete");
    }

    private BrowserType getBrowserType() {
        String browserName = config.getBrowserType().toLowerCase();
        switch (browserName) {
            case "firefox":
                return playwright.firefox();
            case "webkit":
                return playwright.webkit();
            case "chromium":
            default:
                return playwright.chromium();
        }
    }

    private void setupNetworkListeners() {
        page.onRequest(request -> {
            logger.debug(">> Request: {} {}", request.method(), request.url());
        });

        page.onResponse(response -> {
            logger.debug("<< Response: {} {} ({})",
                    response.status(),
                    response.url(),
                    response.headers().get("content-type"));
        });
    }

    protected void waitForNetworkIdle() {
        page.waitForLoadState(LoadState.NETWORKIDLE);
    }

    protected void waitForUrl(String urlPattern) {
        page.waitForURL(urlPattern);
    }

    protected Page createNewPage() {
        return context.newPage();
    }

    protected BrowserContext createNewContext() {
        return browser.newContext(new Browser.NewContextOptions()
                .setViewportSize(1920, 1080)
                .setLocale("en-IN"));
    }

    private static BrowserType getBrowserType(ConfigReader config) {
        String browserName = config.getBrowserType().toLowerCase();
        switch (browserName) {
            case "firefox":
                return playwright.firefox();
            case "webkit":
                return playwright.webkit();
            case "chromium":
            default:
                return playwright.chromium();
        }
    }
}
