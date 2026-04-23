# Run Commands - SuperKicks E-commerce Automation

This document provides all the commands needed to run the automated tests.

---

## Prerequisites Verification

### Check Java Version
```bash
java -version
```
**Expected:** Java 11 or higher

### Check Maven Version
```bash
mvn -version
```
**Expected:** Maven 3.6 or higher

---

## Initial Setup (One-Time)

### 1. Navigate to Project Directory
```bash
cd /home/greycell-labs-inc/Desktop/QA-Assesment
```

### 2. Install Dependencies
```bash
mvn clean install
```

### 3. Install Playwright Browsers (if not already installed)
```bash
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"
```

---

## Running Tests

### Run All Tests
```bash
mvn clean test
```

### Run Specific Test Classes

#### Login Flow Tests
```bash
mvn test -Dtest=TC001_LoginFlowTest
```

#### Cart Management Tests
```bash
mvn test -Dtest=TC002_CartManagementTest
```

#### Multi-Session Tests
```bash
mvn test -Dtest=TC003_MultiSessionTest
```

#### Edge Case Tests
```bash
mvn test -Dtest=TC004_DuplicateActionTest
```

#### Persistence Tests
```bash
mvn test -Dtest=TC005_PersistenceTest
```

#### API Awareness Tests
```bash
mvn test -Dtest=TC006_ApiAwarenessTest
```

---

## Configuration

### Edit Test Configuration
```bash
nano src/main/resources/config.properties
```

### Configure Test Credentials (Optional)
Update the following in `config.properties`:
```properties
test.user.email=your-email@example.com
test.user.password=your-password
```

### Run in Headless Mode
Edit `config.properties`:
```properties
browser.headless=true
```

### Change Browser
Edit `config.properties`:
```properties
browser.type=chromium  # Options: chromium, firefox, webkit
```

---

## View Test Results

### Test Reports Location
```bash
ls -la test-results/
```

### View Screenshots (if tests failed)
```bash
ls -la test-results/screenshots/
```

### View Traces
```bash
ls -la test-results/traces/
```

### View Logs
```bash
cat test-results/logs/automation.log
```

### View Videos
```bash
ls -la test-results/videos/
```

---

## Play with Playwright Traces

### Install Playwright CLI (if not installed)
```bash
npm install -g @playwright/test
```

### View a Trace File
```bash
playwright show-trace test-results/traces/trace-file.zip
```

---

## Troubleshooting

### Issue: "command not found: mvn"
**Solution:** Install Maven
```bash
sudo apt-get update
sudo apt-get install maven
```

### Issue: "Playwright browsers not installed"
**Solution:** Install browsers
```bash
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"
```

### Issue: Tests timeout
**Solution:** Increase timeout in `config.properties`
```properties
timeout.default=60000
timeout.navigation=60000
```

### Issue: Website not accessible
**Solution:** Check internet connection and website availability
```bash
curl -I https://www.superkicks.in/
```

---

## Clean and Rebuild

### Clean All Generated Files
```bash
mvn clean
```

### Rebuild Project
```bash
mvn clean install
```

### Remove Test Results
```bash
rm -rf test-results/
```

---

## Quick Reference

| Command | Description |
|---------|-------------|
| `mvn clean test` | Run all tests |
| `mvn test -Dtest=ClassName` | Run specific test class |
| `mvn clean install` | Build project |
| `mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"` | Install browsers |
| `cat test-results/logs/automation.log` | View logs |

---

## Test Suite Overview

### Core Flow Tests (TC001)
- Navigate to login page
- Login with valid credentials
- Logout functionality

### Cart Management Tests (TC002)
- Browse and select product
- Add item to cart
- Verify cart contents
- Increase quantity
- Decrease quantity
- Remove item from cart

### Multi-Session Tests (TC003)
- Add item in session 1, verify in session 2
- Modify cart in session 2, verify in session 1

### Edge Case Tests (TC004)
- Double-click on Add to Cart
- Page refresh during action
- Add same item multiple times
- Delayed UI updates

### Persistence Tests (TC005)
- Cart state persistence across logout/login
- Session expiry handling

### API Awareness Tests (TC006)
- Validate Add to Cart API
- Validate Cart Update API
- Validate Remove Item API
- Validate Product Listing API

---

## Notes

- Tests can run in **guest mode** without credentials
- For full functionality, configure test credentials in `config.properties`
- Screenshots and traces are captured automatically on failure
- All tests use proper waits (no fixed sleeps)
- Framework uses Page Object Model for maintainability

---

**Last Updated:** 2025-04-23
