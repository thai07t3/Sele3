package tests.vietjet;

import base.BaseTest;
import enums.AgeType;
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

import static com.codeborne.selenide.Selenide.open;

public class VietJetTest extends BaseTest {
    LocalDate currentDate = LocalDate.now();
    HomePage homePage = new HomePage();
    SelectFlightPage selectFlightPage = new SelectFlightPage();
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
//        homePage.clickDepartureDateButton();
//        homePage.selectDate(ticket.getDepartureDate());
        homePage.fillTo(ticket.getTo());
//        homePage.clickReturnDateButton();
        homePage.selectDate(ticket.getDepartureDate());
        homePage.selectDate(ticket.getReturnDate());
        homePage.adjustQuantity(AgeType.ADULT, ticket.getNumberOfAdult());
//        homePage.shouldTicketSelectionFormBeDisplayed(ticket); // Need to correct this method

//        homePage.closePassengerForm();
        homePage.clickOn("Let's go"); // Not working

        selectFlightPage.closePopUpIfDisplayed();
        List<FlyInfo> takeOnList = selectFlightPage.getAllFlyDatas(); // Need to correct this method
        System.out.println(takeOnList);
        selectFlightPage.selectCheapestFly(takeOnList);
        List<FlyInfo> takeOffList = selectFlightPage.getAllFlyDatas();
        System.out.println(takeOffList);
        selectFlightPage.selectCheapestFly(takeOffList);



    }
}
