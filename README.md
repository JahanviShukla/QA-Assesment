# WatchStudio E-commerce Automation - Playwright Java

**Assignment:** QA Engineer Assessment - Playwright Automation
**Application:** https://watchstudio.in/
**Framework:** Playwright with Java + JUnit 5
**Author:** QA Engineer
**Date:** 2026-04-23

---

## Table of Contents
1. [Project Overview](#project-overview)
2. [Prerequisites](#prerequisites)
3. [Project Structure](#project-structure)
4. [Setup Instructions](#setup-instructions)
5. [Running Tests](#running-tests)
6. [Test Coverage](#test-coverage)
7. [Configuration](#configuration)
8. [Reports & Artifacts](#reports--artifacts)
9. [Troubleshooting](#troubleshooting)

---

## Project Overview

This is a comprehensive test automation framework for the **WatchStudio** e-commerce platform. The framework demonstrates:

- ✅ Clean Page Object Model architecture
- ✅ No fixed waits (proper async handling)
- ✅ Robust selector strategies for WordPress/WooCommerce
- ✅ Comprehensive edge case coverage
- ✅ Multi-session testing
- ✅ API awareness and monitoring
- ✅ Detailed logging and debugging
- ✅ Screenshot and trace capture on failure
- ✅ **NEW: WatchStudio-specific features and selectors**

---

## Prerequisites

### Required Software:
1. **Java JDK 11 or higher**
   ```bash
   java -version
   ```

2. **Apache Maven 3.6+**
   ```bash
   mvn -version
   ```

3. **Git** (for cloning)

### Optional but Recommended:
- IDE: IntelliJ IDEA or Eclipse
- Browser: Chrome/Chromium (for visual verification)

---

## Project Structure

```
QA-Assesment/
├── pom.xml                                 # Maven dependencies
├── README.md                               # This file
├── Test_Scenarios_Document.md             # Test case documentation
├── AI_Usage_Documentation.md              # AI usage details
├── src/
│   ├── main/
│   │   ├── java/com/qa/automation/
│   │   │   └── config/
│   │   │       └── ConfigReader.java      # Configuration management
│   │   └── resources/
│   │       ├── config.properties          # Test configuration
│   │       └── logback.xml                # Logging configuration
│   └── test/
│       └── java/com/qa/automation/
│           ├── base/
│           │   └── BaseTest.java          # Test base class
│           ├── pages/                     # Page Object Model
│           │   ├── BasePage.java
│           │   ├── HomePage.java
│           │   ├── LoginPage.java
│           │   ├── ProductPage.java
│           │   ├── CartPage.java
│           │   ├── ProductListingPage.java
│           │   └── SearchResultsPage.java
│           ├── tests/                     # Test classes
│           │   ├── WatchStudioSmokeTest.java  # NEW: WatchStudio specific tests
│           │   ├── TC001_LoginFlowTest.java
│           │   ├── TC002_CartManagementTest.java
│           │   ├── TC003_MultiSessionTest.java
│           │   ├── TC004_DuplicateActionTest.java
│           │   ├── TC005_PersistenceTest.java
│           │   └── TC006_ApiAwarenessTest.java
│           └── utils/
│               ├── ScreenshotUtils.java
│               └── TraceUtils.java
└── test-results/                          # Generated test outputs
    ├── screenshots/
    ├── traces/
    ├── videos/
    └── logs/
```

---

## Setup Instructions

### Step 1: Verify Environment

Check if Maven is installed:
```bash
mvn -version
```

Expected output should show Maven version 3.6+

### Step 2: Install Dependencies & Browsers

Navigate to the project directory:
```bash
cd /home/greycell-labs-inc/Desktop/QA-Assesment
```

Download dependencies and install Playwright browsers:
```bash
mvn clean install
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"
```

Or use the profile:
```bash
mvn clean install -Pinstall-playwright-browsers
```

### Step 3: Configure Test Credentials (Optional)

Edit `src/main/resources/config.properties`:

```properties
# Update these with actual test credentials
test.user.email=your-email@example.com
test.user.password=your-password
```

**Note:** Tests can run in guest mode without credentials, but some scenarios will be skipped.

---

## Running Tests

### Run All Tests
```bash
mvn clean test
```

### Run WatchStudio Smoke Tests
```bash
mvn test -Dtest=WatchStudioSmokeTest
```

### Run Specific Test Class
```bash
# Login flow tests
mvn test -Dtest=TC001_LoginFlowTest

# Cart management tests
mvn test -Dtest=TC002_CartManagementTest

# Multi-session tests
mvn test -Dtest=TC003_MultiSessionTest

# Edge case tests
mvn test -Dtest=TC004_DuplicateActionTest

# Persistence tests
mvn test -Dtest=TC005_PersistenceTest

# API awareness tests
mvn test -Dtest=TC006_ApiAwarenessTest
```

### Run with Specific Browser
Edit `config.properties`:
```properties
browser.type=chromium  # Options: chromium, firefox, webkit
```

### Run in Headless Mode
Edit `config.properties`:
```properties
browser.headless=true
```

### Run with Parallel Execution
```bash
mvn test -Djunit.jupiter.execution.parallel.enabled=true
```

---

## Test Coverage

### WatchStudio Specific Tests
| Test ID | Description | Status |
|---------|-------------|--------|
| WS-001 | Verify WatchStudio homepage loads | ✅ Implemented |
| WS-002 | Search for Rolex watches | ✅ Implemented |
| WS-003 | Navigate to Men's Watches category | ✅ Implemented |
| WS-004 | Open first product from listing | ✅ Implemented |
| WS-005 | Add product to cart | ✅ Implemented |
| WS-006 | Verify cart contents | ✅ Implemented |
| WS-007 | Navigate to brands section | ✅ Implemented |
| WS-008 | Verify sale products are displayed | ✅ Implemented |
| WS-009 | Test cart item quantity modification | ✅ Implemented |
| WS-010 | Verify responsive design elements | ✅ Implemented |

### Core Flow Tests
| Test ID | Description | Status |
|---------|-------------|--------|
| TC-001 | Navigate to login page | ✅ Implemented |
| TC-002 | Login with valid credentials | ✅ Implemented |
| TC-003 | Logout functionality | ✅ Implemented |
| TC-004 | Browse and select product | ✅ Implemented |
| TC-005 | Add item to cart | ✅ Implemented |
| TC-006 | Increase quantity | ✅ Implemented |
| TC-007 | Decrease quantity | ✅ Implemented |
| TC-008 | Remove item from cart | ✅ Implemented |
| TC-009 | Cart state persistence | ✅ Implemented |

### Edge Case Tests
| Test ID | Description | Status |
|---------|-------------|--------|
| TC-EDGE-001 | Double-click Add to Cart | ✅ Implemented |
| TC-EDGE-002 | Page refresh during action | ✅ Implemented |
| TC-EDGE-003 | Delayed UI updates | ✅ Implemented |
| TC-EDGE-004 | Session expiry handling | ✅ Implemented |
| TC-EDGE-005 | Add same item multiple times | ✅ Implemented |
| TC-EDGE-006 | Multi-session cart sync | ✅ Implemented |
| TC-EDGE-007 | Multi-session cart modification | ✅ Implemented |

### API Awareness Tests
| Test ID | Description | Status |
|---------|-------------|--------|
| TC-API-001 | Add to Cart API validation | ✅ Implemented |
| TC-API-002 | Cart Update API validation | ✅ Implemented |
| TC-API-003 | Remove Item API validation | ✅ Implemented |
| TC-API-004 | Product Listing API validation | ✅ Implemented |

---

## Configuration

### Application Settings
```properties
# WatchStudio URL
base.url=https://watchstudio.in

# Browser Configuration
browser.type=chromium          # chromium, firefox, webkit
browser.headless=false         # true or false
browser.slow.mo=0              # Slow motion in ms (for debugging)

# Timeout Settings (in milliseconds)
timeout.default=30000          # Default timeout (ms)
timeout.navigation=30000       # Navigation timeout (ms)
timeout.action=10000           # Action timeout (ms)
```

### Test Data Settings
```properties
# Test Data
test.product.category=watches
test.product.min.price=5000
test.product.max.price=100000
test.product.brand=rolex
```

### Logging & Debugging
```properties
# Logging
screenshot.on.failure=true     # Capture screenshots on failure
trace.on.failure=true          # Capture traces on failure
log.level=INFO                 # Log level
```

---

## Reports & Artifacts

After test execution, find artifacts in:

| Location | Contents |
|----------|----------|
| `test-results/screenshots/` | Failure screenshots |
| `test-results/traces/` | Playwright trace files |
| `test-results/videos/` | Browser session recordings |
| `test-results/logs/` | Execution logs |

### Viewing Playwright Traces

Install Playwright CLI globally:
```bash
npm install -g @playwright/test
```

View trace:
```bash
playwright show-trace test-results/traces/trace-file.zip
```

---

## WatchStudio Specific Features

### WordPress/WooCommerce Platform
The framework is optimized for WatchStudio's WordPress/WooCommerce platform:

- **Standard WooCommerce Selectors**: Uses WooCommerce-specific CSS classes
- **Variable Product Support**: Handles size/brand options properly
- **Coupon Code Functionality**: Can test discount codes
- **Shipping Calculator**: Can test shipping cost calculation
- **AJAX Add to Cart**: Handles WooCommerce AJAX responses

### Key Selectors Used

#### Product Pages
```java
// WooCommerce product selectors
private final String productCardSelector = ".product-item, .product-type-simple, .ast-col-xs-4";
private final String addToCartSelector = "button.single_add_to_cart_button, .add_to_cart_button";
private final String priceSelector = ".price, .amount, .woocommerce-Price-amount";
```

#### Cart Pages
```java
// WooCommerce cart selectors
private final String cartItemSelector = ".cart_item, .woocommerce-cart-form__cart-item";
private final String couponCodeSelector = "input[name='coupon_code'], #coupon_code";
private final String checkoutButtonSelector = "button.checkout-button, .checkout-btn";
```

### Special Features
1. **Brand Navigation**: Dedicated methods for navigating to watch brands
2. **Category Filtering**: Men's/Women's watches specific navigation
3. **Sale Products**: Verification of sale badges and pricing
4. **COD Support**: Can test Cash on Delivery flow

---

## Troubleshooting

### Issue: "Browser window not maximized properly"
**Solution:** The framework now uses `--start-maximized` and `--kiosk` flags. If still having issues, check `BaseTest.java` launch options.

### Issue: "command not found: mvn"
**Solution:** Install Maven
```bash
sudo apt-get install maven  # Linux
brew install maven           # macOS
```

### Issue: "Playwright browsers not installed"
**Solution:** Install browsers
```bash
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"
```

### Issue: Tests fail with selector errors
**Solution:** WatchStudio website may have changed. Update selectors in Page classes:
- `HomePage.java` - Main navigation selectors
- `ProductPage.java` - Product detail selectors
- `CartPage.java` - Cart page selectors

### Issue: "Connection refused" or "Network error"
**Solution:** Check internet connection and WatchStudio website availability.

### Issue: Tests timeout
**Solution:** Increase timeout in `config.properties`

---

## Key Features Demonstrated

### 1. No Fixed Waits ❌
All waits use proper conditions:
```java
// ❌ NOT used
page.waitForTimeout(5000);

// ✅ Used instead
page.waitForResponse(response -> response.url().contains("cart"));
page.waitForLoadState(LoadState.NETWORKIDLE);
```

### 2. Robust Selectors
Multiple fallback strategies:
```java
private final String loginSelector = "a[href*='my-account'], .my-account-link, [data-testid='account-link']";
```

### 3. Page Object Model
Clean separation of pages and test logic.

### 4. Comprehensive Logging
Every action logged for debugging.

### 5. API Monitoring
Request/response captured and validated.

---

## Notes

- **Website Accessibility:** Tests assume https://watchstudio.in/ is accessible
- **Rate Limiting:** Add delays if tests trigger rate limiting
- **Test Data:** Currently uses any available products. Can be made specific.
- **Credentials:** Guest mode works for most tests. Configure credentials for full coverage.
- **Platform Specific:** Optimized for WordPress/WooCommerce platform

---

## Future Enhancements

1. Add checkout flow testing
2. Implement data-driven testing with CSV/Excel
3. Implement CI/CD integration
4. Add Allure reporting
5. Parallel execution with Selenium Grid
6. Mobile viewport testing
7. Performance testing integration
8. Visual regression testing

---

## Contact & Support

For questions or issues:
- Review test logs in `test-results/logs/`
- Check screenshots/trace files for failures
- Review selector strategies if website changed

---

**Framework Version:** 2.0 (WatchStudio Edition)
**Last Updated:** 2026-04-23
**Status:** ✅ Ready for Execution
