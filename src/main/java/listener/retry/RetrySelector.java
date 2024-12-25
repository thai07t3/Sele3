package listener.retry;

import org.testng.IRetryAnalyzer;
import org.testng.ITestNGListener;
import utils.RetryConfig;

public class RetrySelector {
    public static Class<? extends IRetryAnalyzer> getRetryAnalyzer() {
        String retryType = RetryConfig.getRetryType();
        if ("immediate".equalsIgnoreCase(retryType)) {
            return ImmediateRetry.class;
        }
        return null; // Không áp dụng retry
    }

    public static Class<? extends ITestNGListener> getRetryListener() {
        String retryType = RetryConfig.getRetryType();
        if ("after".equalsIgnoreCase(retryType)) {
            return RetryAfterExecutionListener.class;
        }
        return null;
    }
}