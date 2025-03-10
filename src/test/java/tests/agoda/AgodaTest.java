package tests.agoda;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import enums.Sort;
import enums.agoda.RatingType;
import enums.agoda.SortType;
import models.agoda.RoomInfo;
import models.agoda.Travel;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.agoda.AgodaHomePage;
import pages.agoda.AgodaResultPage;
import tests.BaseTest;
import utils.DateUtils;

import java.time.LocalDate;
import java.util.List;

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

    @Test(description = "Search and sort", groups = {"smoke", "regression"})
    public void TC_01() {
        agodaHomePage.fillTravelInformation(travel);
        agodaHomePage.clickSearchButton();
        Selenide.switchTo().window(1); // Switch to the new tab
        agodaResultPage.shouldDestinationBeCorrect(5, travel.getDestination());
        agodaResultPage.sortBy(SortType.LOWEST_PRICE);
        agodaResultPage.shouldFirstHotelsBeSortedWithTheRightOrder(5, Sort.ASC, "price");
        agodaResultPage.shouldDestinationBeCorrect(5, travel.getDestination());
    }

    @Test(description = "Filter", groups = {"smoke", "regression"})
    public void TC_02() {
        agodaHomePage.fillTravelInformation(travel);
        agodaHomePage.clickSearchButton();
        Selenide.switchTo().window(1); // Switch to the new tab
        agodaResultPage.shouldDestinationBeCorrect(5, travel.getDestination());
        agodaResultPage.enterMinAndMaxPrice(500000, 1000000);
        agodaResultPage.applyFilter(RatingType.THREE_STAR);
        List<RoomInfo> roomInfos = agodaResultPage.getFirstHotels(5);
        for (RoomInfo room : roomInfos) {
            System.out.println("Name: " + room.getName());
            System.out.println("Address: " + room.getAddress());
            System.out.println("Is available: " + room.getIsAvailable());
            System.out.println("Price: " + room.getPrice());
            System.out.println("Rating: " + room.getRating());
            System.out.println("Score: " + room.getScore());
            System.out.println("Score type: " + room.getScoreType());
            System.out.println("------------------------------------");
        }
    }
}
