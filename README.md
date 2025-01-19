# Sele_3

Build a framework with Selenide

## Features and User Cases

| Feature/User Case            | Status |
|------------------------------|--------|
| Selenide FW                  | ✔️     |
| Reports                      | ✔️     |
| Test retry                   | ✔️     |
| Parallel/distributed testing | ✔️     |
| Cross browsers testing       | ✔️     |
| Selenium Grid/Shard          |        |
| CI                           | ✔️     |
| Content testing              |        |
| Multiple languages testing   | ✔️     |
| Group tests by purposes      | ✔️     |
| Source control practice      | ✔️     |
| Switch test environment      |        |
| Wrap custom controls         | ✔️     

## Project Overview

This project is a test automation framework built using Selenide, a powerful library for browser automation in Java. The
framework is designed to run tests in parallel across multiple browsers and generate detailed test reports using Allure
and the default TestNG reporting mechanisms.

## Prerequisites

- Java 21 or higher
- Maven 3.9.6 or higher
- Allure Report 2.32.0 or higher

## Project Structure

- `src/main/java`: Contains the main Java source code.
- `src/test/java`: Contains the test classes.
- `src/test/resources`: Contains the test resources, including:
    - `selenide.properties` file for selenide configuration.
    - `TBD`
- `target/surefire-reports`: Contains the default TestNG/JUnit test reports.
- `allure-results`: Contains the Allure test results.

## Properties can be configured

- Selenide properties.
- TestNG properties.
- `language`: The language to use for the tests.
- `retryType`: The type of retry mechanism to use.
- `retryCount`: The number of times to retry a failed test.

## Install dependencies

    ```shell
    mvn clean install
    ```

## Run tests

    ```shell
    mvn clean test
    ```

or with full parameters

    ```shell
    mvn clean test "-Dselenide.browser=<browserName>" "-DsuiteXmlFile=<path to testng.xml>" -Dlanguage=<language> -DretryType=<retryType> -DretryCount=<retryNumber> -Dparallel=<type> -DthreadCount=<number>
    ```

**Notes**:

- **`selenide.browser`**: The browser to use for testing (e.g., "chrome", "firefox").
- **`suiteXmlFile`**: The path to the `testng.xml` file.
- **`language`**: The language to use for the tests.
- **`retryType`**: The type of retry mechanism to use.
- **`retryCount`**: The number of times to retry a failed test.
- **`parallel`**: Type of parallel execution (e.g., "tests" or "methods").
- **`threadCount`**: Number of threads to use for parallel execution.

## Generate Allure report

    ```shell 
    allure serve target/allure-results
    ```