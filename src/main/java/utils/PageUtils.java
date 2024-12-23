package utils;

import com.codeborne.selenide.Selenide;

public class PageUtils {
    public static void waitForJQuery() {
        Selenide.executeJavaScript("return jQuery.active === 0 ? true : false");
        Selenide.Wait().until(driver -> (Boolean) Selenide.executeJavaScript("return jQuery.active === 0"));
    }

    public static void waitForPageLoad() {
        Selenide.Wait().until(driver -> "complete".equals(Selenide.executeJavaScript("return document.readyState")));
    }
}
