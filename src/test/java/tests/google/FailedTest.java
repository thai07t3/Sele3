package tests.google;

import base.BaseTest;
import org.testng.annotations.Test;
import pages.google.GooglePage;
import pages.google.ResultPage;

import static com.codeborne.selenide.Selenide.open;

public class FailedTest extends BaseTest {
    private final GooglePage googlePage = new GooglePage();
    private final ResultPage resultsPage = new ResultPage();

    @Test
    public void failedTest() {
        open("https://google.com/");
        googlePage.searchFor("selenide java 1");

        resultsPage.checkBotStuff1IsVisible();
    }
}
