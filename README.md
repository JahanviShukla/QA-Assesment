# WatchStudio E-commerce Automation - Playwright Java

**Assignment:** QA Engineer Assessment - Playwright Automation
**Application:** https://watchstudio.in/
**Framework:** Playwright with Java + Cucumber + JUnit 5
**Author:** QA Engineer
**Date:** 2026-04-24

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
- ✅ **No fixed waits** - All `waitForTimeout()` calls replaced with proper conditional waits
- ✅ Robust multi-strategy selectors for WordPress/WooCommerce
- ✅ Comprehensive edge case coverage
- ✅ Multi-session testing
- ✅ API awareness and monitoring
- ✅ Detailed logging and debugging
- ✅ Screenshot and trace capture on failure
- ✅ **NEW: WatchStudio-specific features and selectors**
- ✅ **NEW: AI-assisted development and debugging**

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
├── pom.xml                                    # Maven dependencies
├── README.md                                  # This file
├── AI_USAGE_DOCUMENTATION.md                   # AI usage details (NEW)
├── Test_Scenarios_Document.md                 # Test case documentation
├── src/
│   ├── main/
│   │   ├── java/com/qa/automation/
│   │   │   └── config/
│   │   │       └── ConfigReader.java          # Configuration management
│   │   └── resources/
│   │       ├── config.properties              # Test configuration
│   │       └── logback.xml                    # Logging configuration
│   └── test/
│       ├── java/com/qa/automation/
│       │   ├── pages/                         # Page Object Model
│       │   │   ├── BasePage.java
│       │   │   ├── HomePage.java
│       │   │   ├── LoginPage.java
│       │   │   ├── ProductPage.java
│       │   │   ├── CartPage.java
│       │   │   ├── ProductListingPage.java
│       │   │   └── SearchResultsPage.java
│       │   ├── runner/
│       │   │   └── TestRunner.java            # Cucumber test runner
│       │   └── stepdefinitions/               # Cucumber step definitions
│       │       ├── Hooks.java                  # Test setup/teardown
│       │       ├── LoginStepDefinitions.java
│       │       ├── CartStepDefinitions.java
│       │       ├── MultiSessionStepDefinitions.java
│       │       ├── DuplicateActionStepDefinitions.java
│       │       └── PersistenceStepDefinitions.java
│       └── resources/
│           └── features/                      # Cucumber feature files
│               ├── 01_LoginFlow.feature       # Login + Cart management (combined)
│               ├── 03_MultiSessionTest.feature
│               ├── 04_DuplicateActionHandling.feature
│               └── 05_PersistenceTest.feature
└── test-results/                              # Generated test outputs
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

### Run All Cucumber Tests
```bash
mvn test
```

### Run Specific Feature
```bash
# Login + Cart management (combined feature)
mvn test -Dcucumber.options="--name 'Successful login with valid credentials'"

# Multi-session tests
mvn test -Dcucumber.features="src/test/resources/features/03_MultiSessionTest.feature"

# Duplicate action tests
mvn test -Dcucumber.features="src/test/resources/features/04_DuplicateActionHandling.feature"

# Persistence tests
mvn test -Dcucumber.features="src/test/resources/features/05_PersistenceTest.feature"
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

### Feature Files Overview
| Feature File | Description | Key Scenarios |
|--------------|-------------|---------------|
| `01_LoginFlow.feature` | **Authentication + Cart Management** | Login, cart operations (add/update/remove), calculations |
| `03_MultiSessionTest.feature` | Multi-session testing | Cart persistence across browser sessions |
| `04_DuplicateActionHandling.feature` | Edge case testing | Double-click prevention, rapid actions |
| `05_PersistenceTest.feature` | Data persistence | Cart state preservation, session handling |

> **Note:** Cart management scenarios are now consolidated in `01_LoginFlow.feature` to avoid duplication and improve test maintainability.

### Core Flow Tests
| Test ID | Description | Feature File | Status |
|---------|-------------|--------------|--------|
| TC-001 | Navigate to login page | 01_LoginFlow.feature | ✅ Implemented |
| TC-002 | Login with valid credentials | 01_LoginFlow.feature | ✅ Implemented |
| TC-003 | Add item to cart | 01_LoginFlow.feature | ✅ Implemented |
| TC-004 | Update item quantity | 01_LoginFlow.feature | ✅ Implemented |
| TC-005 | Remove item from cart | 01_LoginFlow.feature | ✅ Implemented |
| TC-006 | Cart calculations | 01_LoginFlow.feature | ✅ Implemented |
| TC-007 | Cart state persistence | 03_MultiSessionTest.feature | ✅ Implemented |

### Edge Case Tests
| Test ID | Description | Feature File | Status |
|---------|-------------|--------------|--------|
| TC-EDGE-001 | Double-click Add to Cart | 04_DuplicateActionHandling.feature | ✅ Implemented |
| TC-EDGE-002 | Rapid quantity updates | 04_DuplicateActionHandling.feature | ✅ Implemented |
| TC-EDGE-003 | Multi-session cart sync | 03_MultiSessionTest.feature | ✅ Implemented |
| TC-EDGE-004 | Session persistence | 05_PersistenceTest.feature | ✅ Implemented |

### API Awareness Tests
| Test ID | Description | Feature File | Status |
|---------|-------------|--------------|--------|
| TC-API-001 | API request monitoring | Hooks.java | ✅ Implemented |
| TC-API-002 | Response validation | Hooks.java | ✅ Implemented |

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
All waits use proper conditions (no `waitForTimeout()` anywhere in codebase):
```java
// ❌ NOT used anymore (all calls removed)
page.waitForTimeout(5000);

// ✅ Used instead - proper conditional waits
page.waitForLoadState(LoadState.NETWORKIDLE);
page.waitForSelector(selector, new Page.WaitForSelectorOptions()
    .setState(WaitForSelectorState.HIDDEN)
    .setTimeout(5000));
```

### 2. Robust Multi-Strategy Selectors
Multiple fallback strategies for element location:
```java
// Try primary selector
Locator removeBtn = page.locator(".cart_item a.remove[role='button']").first();
if (!removeBtn.isVisible()) {
    // Fallback to XPath selector
    removeBtn = page.locator(cartItemSelector).first().locator(removeButtonSelector);
}
if (!removeBtn.isVisible()) {
    // Try generic remove link selector
    removeBtn = page.locator("a.remove, [class*='remove'], [aria-label*='remove']").first();
}
```

### 3. Page Object Model
Clean separation of pages and test logic with clear responsibilities.

### 4. Comprehensive Logging
Every action logged for debugging with detailed context.

### 5. API Monitoring
Request/response captured and validated via custom Hooks.

### 6. AI-Assisted Development
- Intelligent selector strategies
- Debugging and issue resolution
- Test scenario optimization
- Wait strategy improvements

---

## Notes

- **Website Accessibility:** Tests assume https://watchstudio.in/ is accessible
- **Rate Limiting:** Add delays if tests trigger rate limiting
- **Test Data:** Currently uses any available products. Can be made specific.
- **Credentials:** Guest mode works for most tests. Configure credentials for full coverage.
- **Platform Specific:** Optimized for WordPress/WooCommerce platform

---

## Recent Changes (2026-04-24)

### Framework Improvements
- **Removed all `waitForTimeout()` calls** - Replaced with proper conditional waits for faster, more reliable tests
- **Improved cart removal functionality** - Fixed selector issues with multi-strategy approach
- **Consolidated feature files** - Cart management now part of LoginFlow feature to reduce duplication
- **Enhanced error handling** - Better logging and fallback mechanisms

### Documentation Updates
- **Added AI_USAGE_DOCUMENTATION.md** - Comprehensive guide on AI-assisted development
- **Updated README.md** - Reflects current test structure and improvements

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
9. **AI-powered test maintenance** - Self-healing selectors (planned)

---

## Contact & Support

For questions or issues:
- Review test logs in `test-results/logs/`
- Check screenshots/trace files for failures
- Review selector strategies if website changed

---

**Framework Version:** 2.1 (Enhanced with AI Assistance)
**Last Updated:** 2026-04-24
**Status:** ✅ Ready for Execution
