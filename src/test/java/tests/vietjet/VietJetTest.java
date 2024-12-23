package tests.vietjet;

import base.BaseTest;
import enums.FlyType;
import models.FlyInfo;
import models.Ticket;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.vietjet.SelectFlightPage;
import utils.Constants;
import pages.vietjet.HomePage;

import java.time.LocalDate;
import java.util.List;

import static base.BasePage.localization;
import static com.codeborne.selenide.Selenide.open;

public class VietJetTest extends BaseTest {
    LocalDate currentDate = LocalDate.now();
    HomePage homePage = new HomePage();
    SelectFlightPage selectFlightPage = new SelectFlightPage();
    Ticket ticket = Ticket.builder()
            .flyType(FlyType.RETURN)
            .from(localization.getLocation("ho.chi.minh"))
            .to(localization.getLocation("ha.noi"))
            .departureDate(currentDate.plusDays(1))
            .returnDate(currentDate.plusDays(4))
            .numberOfAdult(2)
            .build();

    @BeforeMethod
    public void setUp() {
        open(Constants.URL + language);
        homePage.acceptCookiesIfDisplay();
        homePage.clickLaterButtonIfDisplay();
    }

    @Test
    public void testSearch() {
        homePage.fillTicketInformation(ticket);
        homePage.shouldTicketSelectionFormBeDisplayed(ticket);

        homePage.clickOn(localization.getContent("search.button"));

        selectFlightPage.closePopUp();
        List<FlyInfo> takeOnList = selectFlightPage.getAllFlyDatas();
        System.out.println(takeOnList);
        selectFlightPage.selectCheapestFly(takeOnList);
        selectFlightPage.clickOn(localization.getContent("continue.button"));
        List<FlyInfo> takeOffList = selectFlightPage.getAllFlyDatas();
        System.out.println(takeOffList);
        selectFlightPage.selectCheapestFly(takeOffList);


    }
}
