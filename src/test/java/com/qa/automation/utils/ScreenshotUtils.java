package com.qa.automation.utils;

import com.microsoft.playwright.Page;
import com.qa.automation.config.ConfigReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotUtils {
    private static final Logger logger = LoggerFactory.getLogger(ScreenshotUtils.class);
    private static final ConfigReader config = ConfigReader.getInstance();
    private static final String SCREENSHOT_DIR = "test-results/screenshots";

    static {
        // Create directory if it doesn't exist
        try {
            java.nio.file.Files.createDirectories(Paths.get(SCREENSHOT_DIR));
        } catch (Exception e) {
            logger.error("Failed to create screenshot directory", e);
        }
    }

    public static void captureScreenshot(Page page, String testName) {
        captureScreenshot(page, testName, "info");
    }

    public static void captureScreenshot(Page page, String testName, String status) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss-SSS"));
            String fileName = String.format("%s/%s_%s_%s.png",
                    SCREENSHOT_DIR, testName, status, timestamp);

            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(Paths.get(fileName))
                    .setFullPage(true));

            logger.info("Screenshot saved: {}", fileName);
        } catch (Exception e) {
            logger.error("Failed to capture screenshot", e);
        }
    }

    public static void captureScreenshotOnFailure(Page page, String testName) {
        if (config.isScreenshotOnFailure()) {
            logger.info("Capturing failure screenshot for: {}", testName);
            captureScreenshot(page, testName, "failure");
        }
    }
}
