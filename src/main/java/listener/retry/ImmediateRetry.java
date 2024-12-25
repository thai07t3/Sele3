package listener.retry;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import utils.RetryConfig;

public class ImmediateRetry implements IRetryAnalyzer {
    private int retryCount = 0;
    private final int maxRetryCount = RetryConfig.getRetryCount();

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < maxRetryCount) {
            retryCount++;
            return true;
        }
        return false;
    }
}