# 🎯 WatchStudio Automation Framework - Complete Guide

## 📁 Project Structure

```
QA-Assesment/
├── src/
│   ├── main/
│   │   ├── java/com/qa/automation/
│   │   │   ├── config/
│   │   │   │   └── ConfigReader.java
│   │   │   └── base/
│   │   │       └── BaseTest.java
│   │   └── resources/
│   │       ├── config.properties          # Test configuration
│   │       └── logback.xml
│   │
│   └── test/
│       ├── java/com/qa/automation/
│       │   ├── base/
│       │   │   └── BaseTest.java           # Test base class
│       │   │
│       │   ├── locators/                  # 🔍 ALL LOCATORS (Centralized)
│       │   │   ├── HomePageLocators.java
│       │   │   ├── LoginPageLocators.java
│       │   │   ├── CartPageLocators.java
│       │   │   ├── ProductPageLocators.java
│       │   │   └── CommonLocators.java
│       │   │
│       │   ├── pages/                     # 📄 Page Object Classes
│       │   │   ├── BasePage.java
│       │   │   ├── HomePage.java
│       │   │   ├── LoginPage.java
│       │   │   ├── ProductPage.java
│       │   │   ├── CartPage.java
│       │   │   ├── ProductListingPage.java
│       │   │   └── SearchResultsPage.java
│       │   │
│       │   ├── stepdefinitions/           # 👣 Step Implementations
│       │   │   ├── LoginStepDefinitions.java
│       │   │   ├── CartStepDefinitions.java
│       │   │   ├── MultiSessionStepDefinitions.java
│       │   │   ├── DuplicateActionStepDefinitions.java
│       │   │   └── PersistenceStepDefinitions.java
│       │   │
│       │   ├── tests/                     # 🧪 Test Classes (Optional)
│       │   │   ├── WatchStudioSmokeTest.java
│       │   │   ├── TC001_LoginFlowTest.java
│       │   │   └── TC002_CartManagementTest.java
│       │   │
│       │   ├── utils/                     # 🛠️ Utilities
│       │   │   ├── ScreenshotUtils.java
│       │   │   └── TraceUtils.java
│       │   │
│       │   └── runner/                    # 🏃 Test Runners
│       │       ├── TestRunner.java          # Run ALL scenarios
│       │       ├── RunLoginTests.java       # Run ONLY login
│       │       ├── RunCartTests.java        # Run ONLY cart
│       │       ├── RunMultiSessionTests.java
│       │       ├── RunDuplicateActionTests.java
│       │       └── RunPersistenceTests.java
│       │
│       └── resources/
│           └── features/                   # 📋 Gherkin Feature Files
│               ├── 01_LoginFlow.feature
│               ├── 02_CartManagement.feature
│               ├── 03_MultiSessionTest.feature
│               ├── 04_DuplicateActionHandling.feature
│               └── 05_PersistenceTest.feature
│
├── pom.xml
├── README.md
└── test-results/                          # Generated reports
    ├── screenshots/
    ├── traces/
    ├── videos/
    ├── logs/
    └── cucumber-reports/
```

---

## 🚀 How to Run Tests

### **Option 1: Run ALL Scenarios**
```bash
cd /home/greycell-labs-inc/Desktop/QA-Assesment
mvn clean test
```

### **Option 2: Run Specific Feature File**
```bash
# Run ONLY Login tests
mvn test -Dtest=RunLoginTests

# Run ONLY Cart tests
mvn test -Dtest=RunCartTests

# Run ONLY Multi-Session tests
mvn test -Dtest=RunMultiSessionTests

# Run ONLY Duplicate Action tests
mvn test -Dtest=RunDuplicateActionTests

# Run ONLY Persistence tests
mvn test -Dtest=RunPersistenceTests
```

### **Option 3: Run Specific Scenario**
```bash
# Run by scenario name (modify TestRunner.java)
# Edit TestRunner.java → add: name = ".*Your Scenario Name.*"
mvn test -Dtest=TestRunner
```

### **Option 4: Run by Tags**
```bash
# Add tags to feature files first: @Smoke, @Regression, etc.
# Then modify TestRunner.java → tags = "@Smoke"
mvn test -Dtest=TestRunner
```

---

## 📝 Test Coverage

### **1. Login Flow** (`01_LoginFlow.feature`)
- ✅ Successful login with valid credentials
- ✅ Login with invalid credentials
- ✅ Login with empty credentials
- ✅ Verify login elements
- ✅ Successful logout

### **2. Cart Management** (`02_CartManagement.feature`)
- ✅ Add single product to cart
- ✅ Add multiple quantities
- ✅ Increase quantity
- ✅ Decrease quantity
- ✅ Remove product
- ✅ Price validation
- ✅ Cart contents verification

### **3. Multi-Session Test** (`03_MultiSessionTest.feature`)
- ✅ Add item in one session, verify in another
- ✅ Modify quantity across sessions
- ✅ Remove item across sessions
- ✅ Add different items in both sessions
- ✅ Simultaneous modification handling

### **4. Duplicate Action Handling** (`04_DuplicateActionHandling.feature`)
- ✅ Double click on "Add to Cart"
- ✅ Multiple rapid clicks
- ✅ Add same product twice
- ✅ Rapid quantity changes
- ✅ Multiple remove clicks
- ✅ Retry failed actions
- ✅ Simultaneous add from two tabs

### **5. Persistence Test** (`05_PersistenceTest.feature`)
- ✅ Cart persists after logout and login
- ✅ Cart persists across browser sessions
- ✅ Empty cart persists
- ✅ Multiple items persist
- ✅ Modified cart persists
- ✅ Guest cart merges with logged-in cart
- ✅ Removed items stay removed

---

## 🛠️ Framework Architecture

### **1. Feature Files** (Gherkin Format)
- **Location:** `src/test/resources/features/`
- **Purpose:** Test scenarios in plain English
- **Format:** Given-When-Then
- **Benefits:**
  - Non-technical stakeholders can understand
  - Easy to maintain
  - Clear test documentation

### **2. Locator Files** (Centralized Selectors)
- **Location:** `src/test/java/com/qa/automation/locators/`
- **Purpose:** All web element selectors in one place
- **Benefits:**
  - ✅ Easy to update when UI changes
  - ✅ Reusable across tests
  - ✅ No duplicate selectors
  - ✅ Single source of truth

**Example:**
```java
public class HomePageLocators {
    public static final String LOGIN_BUTTON = "//a[.//i[@class='far fa-user']]";
    public static final String CART_ICON = "#hfe-menu-cart__toggle_button";
}
```

### **3. Step Definitions** (Implementation)
- **Location:** `src/test/java/com/qa/automation/stepdefinitions/`
- **Purpose:** Java code implementing Gherkin steps
- **Benefits:**
  - ✅ Clean separation of concerns
  - ✅ Reusable steps
  - ✅ Easy to debug
  - ✅ Proper assertions

### **4. Page Classes** (Page Object Model)
- **Location:** `src/test/java/com/qa/automation/pages/`
- **Purpose:** Interact with web pages
- **Benefits:**
  - ✅ Encapsulates page logic
  - ✅ Reusable methods
  - ✅ Easy to maintain
  - ✅ Clear page structure

### **5. Test Runners** (Execution Control)
- **Location:** `src/test/java/com/qa/automation/runner/`
- **Purpose:** Control which tests run
- **Benefits:**
  - ✅ Run specific test suites
  - ✅ Run individual scenarios
  - ✅ Generate reports
  - ✅ Easy CI/CD integration

---

## 📊 Reporting

### **Generated Reports Location:**
- **HTML Report:** `target/cucumber-reports/cucumber-pretty.html`
- **JSON Report:** `target/cucumber-reports/Cucumber.json`
- **JUnit Report:** `target/cucumber-reports/Cucumber.xml`
- **Screenshots:** `target/screenshots/`
- **Traces:** `target/traces/`
- **Videos:** `test-results/videos/`

### **View HTML Report:**
```bash
# Open in browser
open target/cucumber-reports/cucumber-pretty.html

# Or use Playwright trace viewer
npx playwright show-trace target/traces/trace.zip
```

---

## ⚙️ Configuration

### **Edit `src/main/resources/config.properties`:**
```properties
# Application URL
base.url=https://watchstudio.in

# Browser Configuration
browser.type=chromium
browser.headless=false

# Test User Credentials
test.user.email=your-email@example.com
test.user.password=your-password

# Timeouts (milliseconds)
timeout.default=30000
timeout.navigation=30000
timeout.action=10000
```

---

## 🎯 Best Practices

### **1. Adding New Tests**
1. **Add scenario** to appropriate `.feature` file
2. **Implement steps** in corresponding step definition file
3. **Add locators** (if needed) to locator files
4. **Run test** to verify
5. **Commit changes**

### **2. Updating Locators**
1. **Find the locator** in `src/test/java/com/qa/automation/locators/`
2. **Update selector** string
3. **Run tests** to verify
4. **Commit changes**

### **3. Debugging Failed Tests**
1. **Check screenshots** in `target/screenshots/`
2. **Check traces** in `target/traces/`
3. **Review logs** in `test-results/logs/`
4. **Update selectors** if needed
5. **Re-run tests**

---

## 📋 Quick Reference

### **Run Specific Test Suites:**
```bash
# All tests
mvn clean test

# Login only
mvn test -Dtest=RunLoginTests

# Cart only
mvn test -Dtest=RunCartTests

# Multi-session only
mvn test -Dtest=RunMultiSessionTests

# Duplicate actions only
mvn test -Dtest=RunDuplicateActionTests

# Persistence only
mvn test -Dtest=RunPersistenceTests
```

### **Common Maven Commands:**
```bash
# Clean build
mvn clean

# Compile only
mvn test-compile

# Run with specific browser
mvn test -Dbrowser.type=firefox

# Run in headless mode
mvn test -Dbrowser.headless=true
```

---

## 🔧 Troubleshooting

### **Issue: Tests not running**
**Solution:** Check if Cucumber dependencies are in `pom.xml`

### **Issue: Element not found**
**Solution:** Update locators in corresponding locator file

### **Issue: Timeout errors**
**Solution:** Increase timeouts in `config.properties`

### **Issue: Browser not launching**
**Solution:** Check browser path and Playwright installation

---

## ✅ Framework Benefits

1. **📁 Organized Structure** - Everything in its place
2. **🔍 Centralized Locators** - Easy to update
3. **📋 Gherkin Scenarios** - Clear test documentation
4. **👣 Step Definitions** - Reusable implementation
5. **🏃 Flexible Runners** - Run what you want
6. **📊 Comprehensive Reports** - HTML, JSON, JUnit
7. **🎯 Page Object Model** - Clean page interactions
8. **⚡ Fast Execution** - Parallel execution support
9. **🛠️ Easy Maintenance** - Change selectors in one place
10. **🚀 Production Ready** - Stable and robust

---

**Created:** 2026-04-23
**Framework:** Playwright + Cucumber + Java
**Application:** WatchStudio.in
**Status:** ✅ Ready for Execution
