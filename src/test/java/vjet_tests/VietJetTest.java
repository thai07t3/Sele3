package vjet_tests;

import base.BaseTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.Constants;
import vjetpage.HomePage;

import static com.codeborne.selenide.Selenide.open;

public class VietJetTest extends BaseTest {
    HomePage homePage = new HomePage();

    @BeforeMethod
    public void setUp() {
        open(Constants.URL + language);
        homePage.acceptCookiesIfDisplay();
        homePage.clickLaterButtonIfDisplay();
    }

    @Test
    public void testSearch() {
        homePage.selectReturn();
        homePage.fillFrom("Ho Chi Minh");
//        homePage.clickDepartureDateButton();
        homePage.fillTo("Ha Noi");
//        homePage.clickReturnDateButton();
    }
}
