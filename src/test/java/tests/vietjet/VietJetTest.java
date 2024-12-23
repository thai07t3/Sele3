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
        homePage.fillTo(ticket.getTo());
        homePage.selectDate(ticket.getDepartureDate());
        homePage.selectDate(ticket.getReturnDate());
        homePage.adjustQuantity(AgeType.ADULT, ticket.getNumberOfAdult());
//        homePage.shouldTicketSelectionFormBeDisplayed(ticket); // Need to correct this method

        homePage.clickOn("Let's go");

        selectFlightPage.closePopUp();
        List<FlyInfo> takeOnList = selectFlightPage.getAllFlyDatas();
        System.out.println(takeOnList);
        selectFlightPage.selectCheapestFly(takeOnList);
        selectFlightPage.clickOn("Continue");
        List<FlyInfo> takeOffList = selectFlightPage.getAllFlyDatas();
        System.out.println(takeOffList);
        selectFlightPage.selectCheapestFly(takeOffList);


    }
}
