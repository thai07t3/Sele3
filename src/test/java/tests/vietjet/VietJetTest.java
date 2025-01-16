package tests.vietjet;

import base.BaseTest;
import enums.FlyType;
import models.Ticket;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.vietjet.PassengerPage;
import pages.vietjet.SelectFlightPage;
import utils.Constants;
import pages.vietjet.HomePage;

import java.time.LocalDate;

import static base.BasePage.localization;
import static com.codeborne.selenide.Selenide.open;

public class VietJetTest extends BaseTest {
    LocalDate currentDate = LocalDate.now();
    HomePage homePage = new HomePage();
    SelectFlightPage selectFlightPage = new SelectFlightPage();
    PassengerPage passengerPage = new PassengerPage();
    Ticket ticket = Ticket.builder()
            .flyType(FlyType.RETURN)
            .from(localization.getLocation("ho.chi.minh"))
            .to(localization.getLocation("ha.noi"))
            .departureDate(currentDate.plusDays(10))
            .returnDate(currentDate.plusDays(15))
            .numberOfAdult(2)
            .build();

    @BeforeMethod
    public void setUp() {
        open(System.getProperty("selenide.baseUrl") + language);
        homePage.acceptCookiesIfDisplay();
        homePage.clickLaterButtonIfDisplay();
    }

    @Test
    public void TC_01() {
        homePage.fillTicketInformation(ticket);
        homePage.shouldTicketSelectionFormBeDisplayed(ticket);

        homePage.clickOn(localization.getContent("search.button"));

        selectFlightPage.closePopUp();
        selectFlightPage.selectCheapestFlies(ticket.getFlyType());

        passengerPage.shouldTicketInformationBeCorrect(ticket);
    }
}
