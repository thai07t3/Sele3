package tests.google;

import base.BaseTest;
import listener.retry.ImmediateRetry;
import org.testng.annotations.Test;
import pages.google.GooglePage;
import pages.google.ResultPage;

import static com.codeborne.selenide.Selenide.open;

public class FailedTest extends BaseTest {
    private final GooglePage googlePage = new GooglePage();
    private final ResultPage resultsPage = new ResultPage();

    @Test
    public void failedTest_01() {
        open("https://google.com/");
        googlePage.searchFor("selenide java 1");

        resultsPage.checkBotStuff1IsVisible();
    }

    @Test
    public void passTest_01() {
        open("https://google.com/");
        googlePage.searchFor("selenide java");
    }

    @Test(retryAnalyzer = ImmediateRetry.class)
    public void failedTest_02_set_retry() {
        open("https://google.com/");
        googlePage.searchFor("Failed Test 02 - Set ImmediateRetry");

        resultsPage.checkBotStuff1IsVisible();
    }

    @Test
    public void passTest_02() {
        open("https://google.com/");
        googlePage.searchFor("selenide java");
    }

    @Test
    public void failedTest_03() {
        open("https://google.com/");
        googlePage.searchFor("selenide java 1");

        resultsPage.checkBotStuff1IsVisible();
    }
}
