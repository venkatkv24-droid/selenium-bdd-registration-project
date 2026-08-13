package com.qa.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * Retries a failed scenario immediately, within the same TestNG run,
 * instead of requiring a separate rerun pass.
 *
 * Applies per-scenario because TestRunner.scenarios() is a DataProvider —
 * each Cucumber scenario is its own TestNG @Test invocation.
 *
 * Max retry count is configurable via -Dretry.count=<n> (default 2).
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private int retryCount = 0;
    private static final int MAX_RETRY_COUNT =
            Integer.parseInt(System.getProperty("retry.count", "2"));

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY_COUNT) {
            retryCount++;
            System.out.println("[RETRY] " + result.getName()
                    + " failed. Retry attempt " + retryCount + " of " + MAX_RETRY_COUNT);
            return true;
        }
        System.out.println("[RETRY] " + result.getName()
                + " exhausted all " + MAX_RETRY_COUNT + " retries. Marking as FAILED.");
        return false;
    }
}
