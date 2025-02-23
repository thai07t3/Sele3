package tests.vietjet;

import com.codeborne.selenide.Configuration;
import org.testng.annotations.BeforeClass;
import tests.BaseTest;
import enums.FlyType;
import models.Ticket;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.vietjet.PassengerPage;
import pages.vietjet.SelectFlightPage;
import pages.vietjet.HomePage;

import java.time.LocalDate;
import java.util.ResourceBundle;

import static com.codeborne.selenide.Selenide.open;
import static utils.LocaleManager.getLocaleBundle;

public class VietJetTest extends BaseTest {
    private ResourceBundle bundle;
    private Ticket ticket;
    private HomePage homePage;
    private SelectFlightPage selectFlightPage;
    private PassengerPage passengerPage;
    private LocalDate currentDate;

    @BeforeClass
    public void initTest() {
        bundle = getLocaleBundle();
        currentDate = LocalDate.now();
        homePage = new HomePage();
        selectFlightPage = new SelectFlightPage();
        passengerPage = new PassengerPage();
        ticket = Ticket.builder()
                .flyType(FlyType.RETURN)
                .from(bundle.getString("ho.chi.minh"))
                .to(bundle.getString("ha.noi"))
                .departureDate(currentDate.plusDays(1))
                .returnDate(currentDate.plusDays(4))
                .numberOfAdult(2)
                .build();
    }

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

        homePage.clickOn(bundle.getString("search.button"));

        selectFlightPage.closePopUp();
        selectFlightPage.selectCheapestFlies(ticket.getFlyType());

        passengerPage.shouldTicketInformationBeCorrect(ticket);
    }
}
