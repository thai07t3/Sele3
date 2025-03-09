package utils;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.JavascriptExecutor;

import static com.codeborne.selenide.Selenide.executeJavaScript;
import static utils.Constants.DEFAULT_TIMEOUT;

public class PageUtils {

    // Wait for jQuery to be inactive
    public static void waitForJQuery() {
        if (isJQueryDefined()) {
            Selenide.Wait().until(driver -> (Boolean) Selenide.executeJavaScript("return jQuery.active === 0"));
        }
    }

    // Wait for the document to be "complete"
    public static void waitForPageLoad() {
        Selenide.Wait().until(driver -> "complete".equals(Selenide.executeJavaScript("return document.readyState")));
    }

    // Wait for all AJAX requests to complete
    public static void waitForAjax() {
        if (isJQueryDefined()) {
            Selenide.Wait().until(driver -> (Boolean) Selenide.executeJavaScript("return jQuery.active === 0"));
        } else {
            System.out.println("jQuery không được định nghĩa trên trang, bỏ qua chờ AJAX.");
        }
    }

    // Check if jQuery is defined on the page
    private static boolean isJQueryDefined() {
        try {
            return Boolean.TRUE.equals(Selenide.executeJavaScript("return typeof jQuery !== 'undefined'"));
        } catch (Exception e) {
            return false;
        }
    }

    // Wait for all fetch requests (including fetch API)
    public static void waitForFetchRequests() {
        Selenide.Wait().until(driver -> {
            JavascriptExecutor jsExecutor = (JavascriptExecutor) WebDriverRunner.getWebDriver();
            return (Boolean) jsExecutor.executeScript(
                    "return window.fetch ? (window.activeFetchRequests || 0) === 0 : true;"
            );
        });
    }

    // Wait for the visibility of an element
    public static void waitForElementVisibility(String cssSelector) {
        Selenide.Wait().until(driver -> {
            JavascriptExecutor jsExecutor = (JavascriptExecutor) WebDriverRunner.getWebDriver();
            return (Boolean) jsExecutor.executeScript(
                    "return document.querySelector(arguments[0]) !== null && document.querySelector(arguments[0]).offsetParent !== null;",
                    cssSelector
            );
        });
    }

    // Wait for all conditions to ensure the page is fully loaded
    public static void waitForPageFullyLoaded() {
        waitForPageLoad();
        waitForJQuery();
        waitForAjaxComplete();
        waitForJavaScript();
    }

    // Wait for JavaScript to be inactive
    public static void waitForJavaScript() {
        Selenide.Wait().until(driver -> {
            JavascriptExecutor jsExecutor = (JavascriptExecutor) WebDriverRunner.getWebDriver();
            return (Boolean) jsExecutor.executeScript(
                    "return window.activeJavaScriptExecutions ? window.activeJavaScriptExecutions === 0 : true;"
            );
        });
    }

    // Check if jQuery is active
    public static boolean isJQueryActive() {
        try {
            return (Boolean) executeJavaScript("return typeof jQuery !== 'undefined'");
        } catch (Exception e) {
            return false;
        }
    }

    // Wait for all AJAX requests to complete
    public static void waitForAjaxComplete() {
        if (isJQueryActive()) {
            Selenide.Wait()
                    .withTimeout(DEFAULT_TIMEOUT)
                    .until(d -> (Long) executeJavaScript("return jQuery.active") == 0);
        }
    }
}
