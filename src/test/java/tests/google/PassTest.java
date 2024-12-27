package tests.google;

import base.BaseTest;
import listener.retry.ImmediateRetry;
import org.testng.annotations.Test;
import pages.google.GooglePage;
import pages.google.ResultPage;

import static com.codeborne.selenide.Selenide.open;

public class PassTest extends BaseTest {

    private final GooglePage googlePage = new GooglePage();
    private final ResultPage resultsPage = new ResultPage();

//    @Test
//    public void passTest_03() {
//        open("https://google.com/");
//        googlePage.searchFor("selenide java");
//    }

    @Test
    public void failedTest_04_non_set_retry() {
        open("https://google.com/");
        googlePage.searchFor("Failed Test 04 - Non Set ImmediateRetry");

        resultsPage.checkBotStuff1IsVisible();
    }

//    @Test
//    public void passTest_04() {
//        open("https://google.com/");
//        googlePage.searchFor("selenide java");
//    }
//
    @Test(retryAnalyzer = ImmediateRetry.class)
    public void failedTest_05_set_retry() {
        open("https://google.com/");
        googlePage.searchFor("Failed Test 05 - Set ImmediateRetry");

        resultsPage.checkBotStuff1IsVisible();
    }
//
//    @Test
//    public void passTest_05() {
//        open("https://google.com/");
//        googlePage.searchFor("selenide java");
//    }
}
