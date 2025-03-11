package pages.agoda;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import models.agoda.RoomInfo;
import models.agoda.Travel;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

public class FavoritePage extends AgodaBasePage{
    private final SelenideElement favoriteGroupCard = $("[data-selenium='favorite-group-card']");
    private final SelenideElement favoritePropertyCard = $("[data-selenium='favorite-property-card']");
    private final SelenideElement checkInDate = $("[data-selenium='checkInBox']");
    private final SelenideElement checkOutDate = $("[data-selenium='checkOutText']");
    private final SelenideElement adultValue = $("[data-selenium='adultValue']");
    private final SelenideElement roomValue = $("[data-selenium='roomValue']");
    private final SelenideElement searchButton = $("[data-selenium='searchButton]");
    private final String dynamicDivLocator = "//div[text()='%s']";
    private final String dynamicSpanLocator = "//p[text()='%s']";
    private final String dynamicDateLocator = "[data-selenium-date='%s']";

    @Step("Select card")
    public void selectCard() {
        favoriteGroupCard.click();
    }

    @Step("Select property card")
    public void selectPropertyCard() {
        favoritePropertyCard.click();
    }

    @Step("Should travel information be correct")
    public void shouldTravelInformationBeCorrect(Travel travel) {
        checkInDate.shouldHave(text(travel.getStartDateAsString()));
        checkOutDate.shouldHave(text(travel.getEndDateAsString()));
        adultValue.shouldHave(text(travel.getNumberOfAdults().toString()));
        roomValue.shouldHave(text(travel.getNumberOfRooms().toString()));
    }

    @Step("Should Hotel information be correct")
    public void shouldHotelInformationBeCorrect(RoomInfo room) {
        $x(String.format(dynamicDivLocator, room.getName())).shouldBe(Condition.visible);
        $x(String.format(dynamicSpanLocator, room.getAddress())).shouldBe(Condition.visible);
        $x(String.format(dynamicSpanLocator, room.getPriceAsString())).shouldBe(Condition.visible);
    }
}
