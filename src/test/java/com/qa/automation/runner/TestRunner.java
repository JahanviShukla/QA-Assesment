package com.qa.automation.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * Test Runner for Cucumber + Playwright
 *
 * RUN SPECIFIC TAG: Just change the tags value below
 * Examples:
 *   tags = "@loginpage"           - Run only login tests
 *   tags = "@S-4089.10"           - Run specific test case
 *   tags = "@loginpage or @cart"  - Run multiple tags
 *   tags = ""                     - Run all tests
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"src/test/resources/features"},
        glue = {"com.qa.automation.stepdefinitions"},
        plugin = {
                "pretty",
                "html:target/cucumber-reports/cucumber-pretty.html",
                "json:target/cucumber-reports/Cucumber.json"
        },
        monochrome = true,
        tags = "@loginpage"  // ← PUT YOUR TAG HERE to run specific tests
)
public class TestRunner {
}
