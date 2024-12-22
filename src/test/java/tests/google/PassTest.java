package tests.google;

import base.BaseTest;
import org.testng.annotations.Test;
import pages.google.GooglePage;
import pages.google.ResultPage;

import static com.codeborne.selenide.Selenide.open;

public class PassTest extends BaseTest {

    private final GooglePage googlePage = new GooglePage();
    private final ResultPage resultsPage = new ResultPage();

    @Test
    public void passTest() {
        open("https://google.com/");
        googlePage.searchFor("selenide java");

        resultsPage.checkBotStuffIsVisible();
    }
}
