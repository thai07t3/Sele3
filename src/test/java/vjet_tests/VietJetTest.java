package vjet_tests;

import base.BaseTest;
import enums.AgeType;
import enums.FlyType;
import models.Ticket;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.Constants;
import vjetpage.HomePage;

import java.time.LocalDate;

import static com.codeborne.selenide.Selenide.open;

public class VietJetTest extends BaseTest {
    LocalDate currentDate = LocalDate.now();
    HomePage homePage = new HomePage();
    Ticket ticket = Ticket.builder()
            .flyType(FlyType.RETURN)
            .from("Ho Chi Minh")
            .to("Ha Noi")
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
        homePage.selectFlyType(FlyType.RETURN);
        homePage.fillFrom(ticket.getFrom());
        homePage.clickDepartureDateButton();
        homePage.selectDate(ticket.getDepartureDate());
        homePage.fillTo(ticket.getTo());
        homePage.clickReturnDateButton();
        homePage.selectDate(ticket.getReturnDate());
        homePage.adjustQuantity(AgeType.ADULT, ticket.getNumberOfAdult());
        homePage.shouldTicketSelectionFormBeDisplayed(ticket);
    }
}
