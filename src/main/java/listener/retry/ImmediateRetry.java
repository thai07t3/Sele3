package listener.retry;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import utils.RetryConfig;

import static utils.RetryConfig.getRetryType;

public class ImmediateRetry implements IRetryAnalyzer {
    private int retryCount = 0;
    private final int maxRetryCount = RetryConfig.getRetryCount();

    @Override
    public boolean retry(ITestResult result) {
        if (getRetryType().equals("after")) {
            return false;
        }
        if (retryCount < maxRetryCount) {
            logRetry(result);
            retryCount++;
            return true;
        }
        return false;
    }

    @Step("Retrying test {testName}, attempt {retryCount}")
    public void logRetry(ITestResult result) {
        String testName = result.getMethod().getMethodName(); // Trích xuất tên test
        logRetryStep(testName, retryCount + 1); // Gọi step với các tham số tĩnh
        System.out.println("Retry:" + testName + " (" + (retryCount + 1) + "/" + maxRetryCount + ")");
    }

    private void logRetryStep(String testName, int retryCount) {
        // Đây là bước được theo dõi bởi Allure
    }
}