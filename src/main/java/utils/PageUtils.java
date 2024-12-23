package utils;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import org.openqa.selenium.JavascriptExecutor;

public class PageUtils {

    // Chờ jQuery không còn hoạt động
    public static void waitForJQuery() {
        if (isJQueryDefined()) {
            Selenide.Wait().until(driver -> (Boolean) Selenide.executeJavaScript("return jQuery.active === 0"));
        }
    }

    // Chờ trạng thái tài liệu là "complete"
    public static void waitForPageLoad() {
        Selenide.Wait().until(driver -> "complete".equals(Selenide.executeJavaScript("return document.readyState")));
    }

    // Chờ tất cả các yêu cầu AJAX hoàn thành
    public static void waitForAjax() {
        if (isJQueryDefined()) {
            Selenide.Wait().until(driver -> (Boolean) Selenide.executeJavaScript("return jQuery.active === 0"));
        } else {
            System.out.println("jQuery không được định nghĩa trên trang, bỏ qua chờ AJAX.");
        }
    }

    // Kiểm tra nếu jQuery được định nghĩa trên trang
    private static boolean isJQueryDefined() {
        try {
            return Boolean.TRUE.equals(Selenide.executeJavaScript("return typeof jQuery !== 'undefined'"));
        } catch (Exception e) {
            return false;
        }
    }

    // Chờ tất cả các yêu cầu fetch (bao gồm cả fetch API)
    public static void waitForFetchRequests() {
        Selenide.Wait().until(driver -> {
            JavascriptExecutor jsExecutor = (JavascriptExecutor) WebDriverRunner.getWebDriver();
            return (Boolean) jsExecutor.executeScript(
                    "return window.fetch ? (window.activeFetchRequests || 0) === 0 : true;"
            );
        });
    }

    // Chờ trạng thái của một phần tử cụ thể (nếu cần)
    public static void waitForElementVisibility(String cssSelector) {
        Selenide.Wait().until(driver -> {
            JavascriptExecutor jsExecutor = (JavascriptExecutor) WebDriverRunner.getWebDriver();
            return (Boolean) jsExecutor.executeScript(
                    "return document.querySelector(arguments[0]) !== null && document.querySelector(arguments[0]).offsetParent !== null;",
                    cssSelector
            );
        });
    }

    // Chờ tổng hợp các điều kiện để đảm bảo trang tải hoàn toàn
    public static void waitForPageFullyLoaded() {
        waitForPageLoad();
        waitForJQuery();
        waitForAjax();
        waitForJavaScript();
    }

    // Chờ JavaScript chạy xong
    public static void waitForJavaScript() {
        Selenide.Wait().until(driver -> {
            JavascriptExecutor jsExecutor = (JavascriptExecutor) WebDriverRunner.getWebDriver();
            return (Boolean) jsExecutor.executeScript(
                    "return window.activeJavaScriptExecutions ? window.activeJavaScriptExecutions === 0 : true;"
            );
        });
    }
}
