package listener.retry;

import org.testng.*;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlInclude;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;
import utils.RetryConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RetryAfterExecutionListener implements ITestListener {

    private final List<ITestResult> failedTests = new ArrayList<>();
    private final int retryCount = RetryConfig.getRetryCount();

    @Override
    public void onTestFailure(ITestResult result) {
        // Record failed tests for retry
        if (RetryConfig.getRetryType().equals("after")) {
            failedTests.add(result);
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        if (RetryConfig.getRetryType().equals("after") && retryCount > 0 && !failedTests.isEmpty()) {
            retryFailedTests(context);
        }
    }

    private void retryFailedTests(ITestContext context) {
        TestNG testNG = new TestNG(); // Create a new TestNG instance
        XmlSuite suite = new XmlSuite(); // Create a new XmlSuite instance
        suite.setName("RetryFailedTests");

        XmlTest test = new XmlTest(suite); // Create a new XmlTest instance
        test.setName("RetryFailedTestMethods");

        List<XmlClass> classes = new ArrayList<>();

        for (ITestResult failedTest : failedTests) { // Iterate through failed tests
            Class<?> testClass = failedTest.getMethod().getTestClass().getRealClass();
            XmlClass xmlClass = new XmlClass(testClass);

            XmlInclude include = new XmlInclude(failedTest.getMethod().getMethodName()); // Include failed test method
            xmlClass.setIncludedMethods(Collections.singletonList(include)); // Add included method to class

            classes.add(xmlClass); // Add class to list
        }

        test.setXmlClasses(classes); // Set classes to test
        testNG.setXmlSuites(Collections.singletonList(suite)); // Set suites to TestNG
        testNG.run(); // Run TestNG
    }
}
