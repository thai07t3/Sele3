package pages.agoda;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import enums.agoda.PropertyType;
import io.qameta.allure.Step;
import utils.PageUtils;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

public class AgodaDetailPage {
    private final SelenideElement hotelName = $("[data-selenium='hotel-header-name']");
    private final SelenideElement hotelAddress = $("[data-selenium='hotel-address-map']");
    private final ElementsCollection gridFilters = $$("[data-selenium='RoomGridFilter-filter']");
    private final ElementsCollection hotelRating = $$("[data-testid='ReviewScoreCompact'] h2 span");
    private final SelenideElement favoriteButton = $("[data-selenium='favorite-heart']");
    private final ElementsCollection propertyInfoList = $$("[data-element-name='property-info-section'] span");
    private final ElementsCollection viewPoints = $$("[data-element-name='highlight-facility-mentions-sentiment']");


    @Step("Should hotel name be correct")
    public void shouldHotelNameBeCorrect(String expectedHotelName) {
        PageUtils.waitForPageFullyLoaded();
        hotelName.shouldHave(text(expectedHotelName));
    }

    @Step("Should hotel address be correct")
    public void shouldHotelAddressBeCorrect(String destination) {
        hotelAddress.shouldHave(Condition.ownText(destination));
    }

    @Step("Should filter be correct")
    public void shouldFilterBeCorrect(PropertyType filter) {
        gridFilters.filter(text(filter.getValue()))
                .first()
                .shouldHave(Condition.attribute("aria-checked", "true"));
    }

    @Step("Should have activity")
    public void shouldHaveActivity(String activity) {
        $x(String.format("//span[text()='%s']", activity))
                .shouldBe(Condition.visible);
    }

    @Step("Add hotel to favorite")
    public void addHotelToFavorite() {
        PageUtils.waitForPageFullyLoaded();
        favoriteButton.click();
    }
}
