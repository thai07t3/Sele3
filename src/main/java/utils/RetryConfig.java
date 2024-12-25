package utils;

public class RetryConfig {
    public static String getRetryType() {
        return System.getProperty("retry.type", "immediate");
    }

    public static int getRetryCount() {
        return Integer.parseInt(System.getProperty("retry.count", "2"));
    }
}
