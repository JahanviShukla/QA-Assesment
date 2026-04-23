# Quick Start Guide - SuperKicks Automation

## 🚀 Get Started in 3 Steps

### Step 1: Navigate to Project
```bash
cd /home/greycell-labs-inc/Desktop/QA-Assesment
```

### Step 2: Run Tests
```bash
# Run all tests
mvn clean test

# Or run specific test
mvn test -Dtest=TC002_CartManagementTest
```

### Step 3: View Results
```bash
# View logs
cat test-results/logs/automation.log

# View screenshots
ls test-results/screenshots/
```

---

## 📋 Test Classes Quick Reference

| Test Class | Description | Command |
|------------|-------------|---------|
| TC001_LoginFlowTest | Login, logout functionality | `mvn test -Dtest=TC001_LoginFlowTest` |
| TC002_CartManagementTest | Add, update, remove cart items | `mvn test -Dtest=TC002_CartManagementTest` |
| TC003_MultiSessionTest | Cart sync across sessions | `mvn test -Dtest=TC003_MultiSessionTest` |
| TC004_DuplicateActionTest | Double-click, refresh, edge cases | `mvn test -Dtest=TC004_DuplicateActionTest` |
| TC005_PersistenceTest | Cart state after logout/login | `mvn test -Dtest=TC005_PersistenceTest` |
| TC006_ApiAwarenessTest | API validation | `mvn test -Dtest=TC006_ApiAwarenessTest` |

---

## ⚙️ Configuration

### Edit Test Settings
```bash
nano src/main/resources/config.properties
```

### Key Settings
- `browser.type=chromium` - Change browser (chromium/firefox/webkit)
- `browser.headless=false` - Run headless (true/false)
- `test.user.email` - Your test email
- `test.user.password` - Your test password

---

## 📁 Key Files

| File | Description |
|------|-------------|
| README.md | Full documentation |
| RUN_COMMANDS.md | All commands reference |
| Test_Scenarios_Document.md | Test case documentation |
| AI_Usage_Documentation.md | How AI was used |
| PROJECT_SUMMARY.md | Project overview |
| pom.xml | Maven dependencies |

---

## 🔧 Common Commands

```bash
# Clean build
mvn clean install

# Run tests
mvn test

# Run specific test
mvn test -Dtest=ClassName

# Run in headless mode
# Edit config.properties: browser.headless=true
# Then: mvn test

# View results
cat test-results/logs/automation.log
ls test-results/screenshots/
ls test-results/traces/
```

---

## ✅ Health Check

```bash
# Check Java
java -version

# Check Maven
mvn -version

# Check Playwright browsers
ls ~/.cache/ms-playwright/

# Run quick test
mvn test -Dtest=TC001_LoginFlowTest
```

---

## 🆘 Troubleshooting

| Issue | Solution |
|-------|----------|
| "mvn: command not found" | Install Maven: `sudo apt-get install maven` |
| "Playwright browsers not installed" | Run: `mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"` |
| Tests timeout | Increase `timeout.default` in config.properties |
| Website not accessible | Check internet connection: `curl -I https://www.superkicks.in/` |

---

## 📊 Test Results Location

```
test-results/
├── screenshots/    # Failure screenshots
├── traces/          # Playwright trace files
├── videos/          # Browser recordings
└── logs/            # Execution logs
```

---

**Need More Details?** See [README.md](README.md) or [RUN_COMMANDS.md](RUN_COMMANDS.md)

---

**Ready to Test?** Run: `mvn clean test`
