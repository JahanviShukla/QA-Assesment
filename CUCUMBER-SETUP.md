# Simple Cucumber + Playwright Setup

## Your Simple Structure

```
┌─────────────────────────────────────────────┐
│  Feature Files (*.feature)                  │
│  Your tests in plain English                │
│  Location: src/test/resources/features/     │
└─────────────────┬───────────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────────┐
│  Step Definitions                           │
│  Connect English to Java code               │
│  Location: src/test/java/.../stepdefinitions/│
└─────────────────┬───────────────────────────┘
                  │
                  ▼
┌─────────────────────────────────────────────┐
│  Page Objects                               │
│  Your Playwright code                       │
│  Location: src/test/java/.../pages/         │
└─────────────────────────────────────────────┘
```

## Run Your Tests

```bash
# Run all Cucumber tests
mvn test

# Run specific feature file
mvn test -Dcucumber.options="src/test/resources/features/01_LoginFlow.feature"

# Run scenarios with specific tag
mvn test -Dcucumber.options="--tags @loginpage"
```

## About JUnit

**JUnit is only used as the invisible runner for Cucumber.** You don't write any JUnit code!

The `@RunWith(Cucumber.class)` annotation in TestRunner.java tells JUnit to run Cucumber, which then runs your feature files.

## Created Files

- `src/test/java/com/qa/automation/runner/TestRunner.java` - Runs Cucumber tests
- `src/test/java/com/qa/automation/stepdefinitions/Hooks.java` - Browser setup/teardown
- `src/test/java/com/qa/automation/stepdefinitions/LoginStepDefinitions.java` - Login test steps
