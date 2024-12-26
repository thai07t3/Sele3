package listener.retry;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.testng.IRetryAnalyzer;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utils.RetryConfig;

import java.util.ArrayList;
import java.util.List;

public class RetryAfterExecutionListener implements ITestListener {
    private static final List<ITestResult> failedTests = new ArrayList<>();

    @Override
    public void onTestFailure(ITestResult result) {
        failedTests.add(result);
    }

    @Override
    public void onFinish(ITestContext context) {
        String retryType = RetryConfig.getRetryType();
        if (retryType.equalsIgnoreCase("after")) {
            int maxRetryCount = RetryConfig.getRetryCount();
            for (int i = 0; i < maxRetryCount; i++) {
                List<ITestResult> currentFailedTests = new ArrayList<>(failedTests);
                failedTests.clear();
                for (ITestResult failedTest : currentFailedTests) {
                    retryTest(failedTest, i + 1);
                }
            }
        }
    }

    @Step("Retrying test {testName}, attempt {attempt}")
    private void retryTest(ITestResult failedTest, int attempt) {
        String testName = failedTest.getMethod().getMethodName(); // Trích xuất tên test
        retryTestStep(testName, attempt); // Gọi step với tham số tĩnh
        System.out.println("Retrying:" + testName + " (Attempt " + attempt + ")");
        try {
            failedTest.getMethod().getTestClass().getRealClass()
                    .getMethod(failedTest.getMethod().getMethodName())
                    .invoke(failedTest.getInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void retryTestStep(String testName, int attempt) {
    }
}
