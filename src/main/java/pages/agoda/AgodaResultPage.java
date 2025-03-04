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
    private final SelenideElement lowestPrice = $("[data-element-name='search-sort-price']");

    @Step("Click on the sort lowest price")
    public void clickSortLowestPrice() {
        lowestPrice.click();
    }

    @Step("Get searched text")
    public String getSearchedText() {
        return inputSearch.getText();
    }

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
}
