package tests.vietjet;

import com.codeborne.selenide.Configuration;
import tests.BaseTest;
import enums.FlyType;
import models.Ticket;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.vietjet.PassengerPage;
import pages.vietjet.SelectFlightPage;
import pages.vietjet.HomePage;

import java.time.LocalDate;

import static pages.BasePage.localization;
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
            .departureDate(currentDate.plusDays(1))
            .returnDate(currentDate.plusDays(4))
            .numberOfAdult(2)
            .build();

    @BeforeMethod
    public void setUp() {
        System.out.println(Configuration.baseUrl + language);
        open(Configuration.baseUrl + language);
        homePage.acceptCookiesIfDisplay();
        homePage.clickLaterButtonIfDisplay();
    }

    @Test(description = "Search for a flight", groups = {"smoke", "regression"})
    public void TC_01() {
        homePage.fillTicketInformation(ticket);
        homePage.shouldTicketSelectionFormBeDisplayed(ticket);

        homePage.clickOn(localization.getContent("search.button"));

        selectFlightPage.closePopUp();
        selectFlightPage.selectCheapestFlies(ticket.getFlyType());

        passengerPage.shouldTicketInformationBeCorrect(ticket);
    }
}
