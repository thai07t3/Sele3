package listener.retry;

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
        int maxRetryCount = RetryConfig.getRetryCount();
        for (int i = 0; i < maxRetryCount; i++) {
            List<ITestResult> currentFailedTests = new ArrayList<>(failedTests);
            failedTests.clear();
            for (ITestResult failedTest : currentFailedTests) {
                System.out.println("Retrying: " + failedTest.getName() + " (Attempt " + (i + 1) + ")");
                try {
                    failedTest.getMethod().getTestClass().getRealClass()
                            .getMethod(failedTest.getMethod().getMethodName())
                            .invoke(failedTest.getInstance());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
    }

    @Override
    public void onTestSuccess(ITestResult result) {
    }

    @Override
    public void onTestSkipped(ITestResult result) {
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
    }

    @Override
    public void onStart(ITestContext context) {
    }
}
