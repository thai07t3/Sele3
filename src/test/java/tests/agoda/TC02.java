package tests.agoda;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import enums.agoda.PropertyType;
import models.agoda.RoomInfo;
import models.agoda.Travel;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.agoda.*;
import tests.BaseTest;
import utils.DateUtils;

import java.time.LocalDate;

import static com.codeborne.selenide.Selenide.open;

public class TC02 extends BaseTest {
    private final SignInPage signInPage = new SignInPage();
    private final AgodaHomePage agodaHomePage = new AgodaHomePage();
    private final LocalDate expectedDate = DateUtils.getNext("friday");
    private final AgodaResultPage agodaResultPage = new AgodaResultPage();
    private final AgodaDetailPage agodaDetailPage = new AgodaDetailPage();
    private final FavoritePage favoritePage = new FavoritePage();
    private final Travel travel = Travel.builder()
            .destination("Da Lat")
            .startDate(expectedDate)
            .endDate(expectedDate.plusDays(3))
            .numberOfRooms(2)
            .numberOfAdults(4)
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
    public void TC_02() {
        agodaHomePage.fillTravelInformation(travel);
        agodaHomePage.clickSearchButton();
        Selenide.switchTo().window(1); // Switch to the new tab
        agodaResultPage.shouldDestinationBeCorrect(5, travel.getDestination());

        agodaResultPage.applyPropertyFilter(PropertyType.SWIMMING_POOL);
        RoomInfo firstHotelInfo = agodaResultPage.getFirstHotel();
        agodaResultPage.selectFirstHotel();
        Selenide.switchTo().window(2); // Switch to the new tab

        agodaDetailPage.shouldHotelNameBeCorrect(firstHotelInfo.getName());
        agodaDetailPage.shouldHotelAddressBeCorrect(firstHotelInfo.getAddress());
        agodaDetailPage.shouldHaveActivity(PropertyType.SWIMMING_POOL.getValue());

        agodaDetailPage.addHotelToFavorite();
        signInPage.shouldLoginPopupBeVisible();
        signInPage.login("thai07t3@gmail.com");
        agodaDetailPage.addHotelToFavorite();
        agodaDetailPage.clickOnUserMenu();
        agodaDetailPage.clickOnSavedProperty();

        favoritePage.selectCard();
        favoritePage.shouldTravelInformationBeCorrect(travel); //Issue here
        favoritePage.shouldHotelInformationBeCorrect(firstHotelInfo);
    }
}
