package vjet;

import base.BaseTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.open;

public class VietJetTest extends BaseTest {
    HomePage homePage = new HomePage();

    @BeforeMethod
    public void setUp() {
        open("https://www.vietjetair.com/en/");
        homePage.acceptCookiesIfDisplay();
        homePage.clickLaterButtonIfDisplay();
    }

    @Test
    public void testSearch() {
        homePage.selectReturn();
        homePage.fillFrom("Ho Chi Minh");
        homePage.fillTo("Ha Noi");
    }
}
