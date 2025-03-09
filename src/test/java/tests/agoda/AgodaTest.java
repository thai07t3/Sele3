package tests.agoda;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import enums.Sort;
import enums.agoda.PropertyType;
import enums.agoda.SortType;
import models.agoda.Travel;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.agoda.AgodaHomePage;
import pages.agoda.AgodaResultPage;
import tests.BaseTest;
import utils.DateUtils;

import java.time.LocalDate;

import static com.codeborne.selenide.Selenide.open;

public class AgodaTest extends BaseTest {
    private final AgodaHomePage agodaHomePage = new AgodaHomePage();
    private final LocalDate expectedDate = DateUtils.getNext("friday");
    private final AgodaResultPage agodaResultPage = new AgodaResultPage();
    private final Travel travel = Travel.builder()
            .destination("Da Nang")
            .startDate(expectedDate)
            .endDate(expectedDate.plusDays(10))
            .numberOfRooms(2)
            .numberOfAdults(4)
            .build();

    @BeforeMethod
    public void setUp() {
        open(Configuration.baseUrl);
        agodaHomePage.setLanguageIfNot(language);
    }

    @Test(description = "Search for a flight", groups = {"smoke", "regression"})
    public void TC_01() throws InterruptedException {
        agodaHomePage.fillTravelInformation(travel);
        agodaHomePage.clickSearchButton();
        Selenide.switchTo().window(1); // Switch to the new tab
        agodaResultPage.shouldTicketSelectionFormBeDisplayed(travel);
        agodaResultPage.shouldDestinationBeCorrect(5, travel.getDestination());
        agodaResultPage.sortBy(PropertyType.HOTEL, SortType.LOWEST_PRICE);
        agodaResultPage.shouldFirstHotelsBeSortedWithTheRightOrder(5, Sort.ASC, "price");
        agodaResultPage.shouldDestinationBeCorrect(5, travel.getDestination());
    }
}
