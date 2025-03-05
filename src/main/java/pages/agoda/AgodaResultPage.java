package pages.agoda;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import models.agoda.Travel;

import java.util.Objects;

import static com.codeborne.selenide.Selenide.$;

public class AgodaResultPage extends AgodaBasePage {
    private final SelenideElement checkInBox = $("[data-selenium='checkInBox']");
    private final SelenideElement checkOutBox = $("[data-selenium='checkOutBox']");
    private final SelenideElement rooms = $("[data-selenium='roomValue']");
    private final SelenideElement adults = $("[data-selenium='adultValue']");
    private final SelenideElement children = $("[data-selenium='childValue']");
    private final SelenideElement sortRecommended = $("[data-selenium='search-sort-recommended']");
    private final SelenideElement sortLowestPrice = $("[data-element-name='search-sort-price']");
    private final SelenideElement sortDistance = $("[data-element-name='search-sort-distance-landmark']");
    private final SelenideElement sortRating = $("[data-element-name='search-sort-guest-rating']");
    private final SelenideElement sortHotDeal = $("[data-element-name='search-sort-secret-deals']");

    @Step("Should ticket selection form be displayed")
    public void shouldTicketSelectionFormBeDisplayed(Travel travel) {
        if (Objects.nonNull(travel)) {
            if (Objects.nonNull(travel.getDestination())) {
                inputSearch.shouldHave(Condition.text(travel.getDestination()));
            }
            if (Objects.nonNull(travel.getStartDate())) {
                checkInBox.shouldHave((Condition.text(travel.getStartDateAsString())));
            }
            if (Objects.nonNull(travel.getEndDate())) {
                checkOutBox.shouldHave((Condition.text(travel.getEndDateAsString())));
            }
            if (Objects.nonNull(travel.getNumberOfRooms())) {
                rooms.shouldBe(Condition.text(travel.getNumberOfRooms().toString()));
            }
            if (Objects.nonNull(travel.getNumberOfAdults())) {
                adults.shouldBe(Condition.text(travel.getNumberOfAdults().toString()));
            }
            if (Objects.nonNull(travel.getNumberOfChildren())) {
                children.shouldBe(Condition.text(travel.getNumberOfChildren().toString()));
            }
        }
    }

    @Step("Click on the recommended")
    public void clickRecommended() {
        sortRecommended.click();
    }

    @Step("Click on the sort lowest price")
    public void clickSortLowestPrice() {
        sortLowestPrice.click();
    }

    @Step("Click on the sort distance")
    public void clickSortDistance() {
        sortDistance.click();
    }

    @Step("Click on the sort rating")
    public void clickSortRating() {
        sortRating.click();
    }

    @Step("Click on the sort hot deal")
    public void clickSortHotDeal() {
        sortHotDeal.click();
    }

    @Step("Get searched text")
    public String getSearchedText() {
        return inputSearch.getText();
    }

}
