package tests.agoda;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import enums.agoda.PropertyType;
import models.agoda.RoomInfo;
import models.agoda.Travel;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.agoda.AgodaDetailPage;
import pages.agoda.AgodaHomePage;
import pages.agoda.AgodaResultPage;
import tests.BaseTest;

import java.time.LocalDate;

import static com.codeborne.selenide.Selenide.open;

public class TC01 extends BaseTest {
    private final AgodaHomePage agodaHomePage = new AgodaHomePage();
    private final LocalDate expectedDate = LocalDate.now();
    private final AgodaResultPage agodaResultPage = new AgodaResultPage();
    private final AgodaDetailPage agodaDetailPage = new AgodaDetailPage();
    private final Travel travel = Travel.builder()
            .destination("Da Lat")
            .startDate(expectedDate)
            .endDate(expectedDate.plusDays(3))
            .numberOfRooms(1)
            .numberOfAdults(2)
            .build();

    @BeforeMethod
    public void setUp() {
        open(Configuration.baseUrl);
        agodaHomePage.setLanguageIfNot(language);
    }


    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        Selenide.closeWebDriver();
    }

    @Test(description = "Search and filter", groups = {"smoke", "regression"})
    public void TC_01() {
        agodaHomePage.fillTravelInformation(travel);
        agodaHomePage.clickSearchButton();
        Selenide.switchTo().window(1); // Switch to the new tab
        agodaResultPage.shouldDestinationBeCorrect(5, travel.getDestination());
        agodaResultPage.applyPropertyFilter(PropertyType.BREAKFAST_INCLUDED);
        RoomInfo firstHotelInfo = agodaResultPage.getFirstHotel();
        agodaResultPage.selectFirstHotel();
        Selenide.switchTo().window(2); // Switch to the new tab

        agodaDetailPage.shouldHotelNameBeCorrect(firstHotelInfo.getName());
        agodaDetailPage.shouldHotelAddressBeCorrect(firstHotelInfo.getAddress());
        agodaDetailPage.shouldFilterBeCorrect(PropertyType.BREAKFAST_INCLUDED);
    }
}
