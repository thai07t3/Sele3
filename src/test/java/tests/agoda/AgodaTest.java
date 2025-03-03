package tests.agoda;

import com.codeborne.selenide.Configuration;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.agoda.AgodaHomePage;
import tests.BaseTest;
import utils.DateUtils;

import java.time.LocalDate;

import static com.codeborne.selenide.Selenide.open;

public class AgodaTest extends BaseTest {
    private AgodaHomePage agodaHomePage = new AgodaHomePage();
    private LocalDate startDate = DateUtils.getNext("friday");
    private LocalDate endDate = startDate.plusDays(3);
    private int numberOfRooms = 2;
    private int numberOfAdults = 4;
    private int numberOfChildren = 5;

    @BeforeMethod
    public void setUp() {
        System.out.println(Configuration.baseUrl);
        open(Configuration.baseUrl);
    }

    @Test(description = "Search for a flight", groups = {"smoke", "regression"})
    public void TC_01() {
        agodaHomePage.selectSearchInformation("Da Nang");
        agodaHomePage.selectDateRange(startDate, endDate);
        agodaHomePage.adjustRoomQuantity(numberOfRooms);
        agodaHomePage.adjustAdultQuantity(numberOfAdults);
        agodaHomePage.adjustChildQuantity(numberOfChildren);


    }
}
