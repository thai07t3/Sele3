package utils;

import org.testng.annotations.Parameters;

public class RetryConfig {
    @Parameters("retryType")
    public static String getRetryType() {
        return System.getProperty("retryType");
    }

    @Parameters("retryCount")
    public static int getRetryCount() {
        return Integer.parseInt(System.getProperty("retryCount"));
    }
}
