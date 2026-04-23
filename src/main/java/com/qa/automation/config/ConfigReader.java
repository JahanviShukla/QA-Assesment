package com.qa.automation.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static final Logger logger = LoggerFactory.getLogger(ConfigReader.class);
    private static ConfigReader instance;
    private Properties properties;

    private ConfigReader() {
        properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                logger.error("Unable to find config.properties");
                throw new RuntimeException("config.properties not found");
            }
            properties.load(input);
            logger.info("Configuration loaded successfully");
        } catch (IOException e) {
            logger.error("Error loading configuration", e);
            throw new RuntimeException("Failed to load configuration", e);
        }
    }

    public static synchronized ConfigReader getInstance() {
        if (instance == null) {
            instance = new ConfigReader();
        }
        return instance;
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public int getIntProperty(String key, int defaultValue) {
        try {
            return Integer.parseInt(properties.getProperty(key));
        } catch (NumberFormatException e) {
            logger.warn("Invalid integer value for key: {}, using default: {}", key, defaultValue);
            return defaultValue;
        }
    }

    public boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = properties.getProperty(key);
        if (value == null) return defaultValue;
        return Boolean.parseBoolean(value);
    }

    public String getBaseUrl() {
        return getProperty("base.url");
    }

    public String getBrowserType() {
        return getProperty("browser.type", "chromium");
    }

    public boolean isHeadless() {
        return getBooleanProperty("browser.headless", false);
    }

    public int getDefaultTimeout() {
        return getIntProperty("timeout.default", 30000);
    }

    public int getNavigationTimeout() {
        return getIntProperty("timeout.navigation", 30000);
    }

    public int getActionTimeout() {
        return getIntProperty("timeout.action", 10000);
    }

    public String getTestUserEmail() {
        return getProperty("test.user.email");
    }

    public String getTestUserPassword() {
        return getProperty("test.user.password");
    }

    public boolean isScreenshotOnFailure() {
        return getBooleanProperty("screenshot.on.failure", true);
    }

    public boolean isTraceOnFailure() {
        return getBooleanProperty("trace.on.failure", true);
    }
}
