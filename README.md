# Sele_3

Build a framework with Selenide

## Features and User Cases

| Feature/User Case                | Status                                      |
|----------------------------------|---------------------------------------------|
| Selenide FW                      | ✔️                                           |
| Reports                          | ✔️                                           |
| Test retry                       | ✔️                                           |
| Parallel/distributed testing     | ✔️                                          |
| Cross browsers testing           | ✔️                                           |
| Selenium Grid/Shard              |                                            |
| CI                               | ✔️                                          |
| Content testing                  |                                            |
| Multiple languages testing       | ✔️                                           |
| Group tests by purposes          |                                            |
| Source control practice          | ✔️                                           |
| Switch test environment          |                                            |
| Wrap custom controls             | ✔️               

## Project Overview

This project is a test automation framework built using Selenide, a powerful library for browser automation in Java. The
framework is designed to run tests in parallel across multiple browsers and generate detailed test reports using Allure
and the default TestNG reporting mechanisms.

## Prerequisites

- Java 17 or higher
- Maven 3.13.0 or higher
- Allure Commandline (for generating Allure reports)

## Project Structure

- `src/main/java`: Contains the main Java source code.
- `src/test/java`: Contains the test classes.
- `src/test/resources`: Contains the test resources, including the `browser-config.properties` file for browser
  configurations.
- `target/surefire-reports`: Contains the default TestNG/JUnit test reports.
- `allure-results`: Contains the Allure test results.

## Configuration

### Browser Configuration

Browser settings can be configured in the `src/test/resources/browser-config.properties` file. Example:

```ini
# Chrome
chrome.browser=chrome
chrome.isMaximized=false
chrome.headless=false
chrome.gridUrl=

# Firefox
firefox.browser=firefox
firefox.isMaximized=true
firefox.headless=false
firefox.gridUrl=