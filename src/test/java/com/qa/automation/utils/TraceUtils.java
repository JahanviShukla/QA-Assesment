package com.qa.automation.utils;

import com.microsoft.playwright.BrowserContext;
import com.qa.automation.config.ConfigReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;

public class TraceUtils {
    private static final Logger logger = LoggerFactory.getLogger(TraceUtils.class);
    private static final ConfigReader config = ConfigReader.getInstance();
    private static final String TRACE_DIR = "test-results/traces";

    static {
        try {
            java.nio.file.Files.createDirectories(Paths.get(TRACE_DIR));
        } catch (Exception e) {
            logger.error("Failed to create trace directory", e);
        }
    }

    public static void startTrace(BrowserContext context, String testName) {
        if (config.isTraceOnFailure()) {
            try {
                context.tracing().start();
                logger.debug("Trace started for: {}", testName);
            } catch (Exception e) {
                logger.warn("Failed to start trace: {}", e.getMessage());
            }
        }
    }

    public static void stopTrace(BrowserContext context, String testName) {
        if (config.isTraceOnFailure()) {
            try {
                String tracePath = Paths.get(TRACE_DIR, testName + ".zip").toString();
                context.tracing().stop(
                        new com.microsoft.playwright.Tracing.StopOptions().setPath(Paths.get(tracePath))
                );
                logger.info("Trace saved: {}", tracePath);
            } catch (Exception e) {
                logger.warn("Failed to stop trace: {}", e.getMessage());
            }
        }
    }

    public static void captureTraceOnFailure(BrowserContext context, String testName) {
        if (config.isTraceOnFailure()) {
            try {
                logger.info("Capturing failure trace for: {}", testName);
                String tracePath = Paths.get(TRACE_DIR, testName + "_failure.zip").toString();
                context.tracing().stop(
                        new com.microsoft.playwright.Tracing.StopOptions().setPath(Paths.get(tracePath))
                );
                logger.info("Failure trace saved: {}", tracePath);
            } catch (Exception e) {
                logger.warn("Failed to capture failure trace: {}", e.getMessage());
            }
        }
    }
}
