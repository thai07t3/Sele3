package pages.agoda;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import models.agoda.Travel;
import utils.Constants;
import utils.PageUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.function.IntSupplier;

import static com.codeborne.selenide.Condition.clickable;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static utils.Constants.DEFAULT_TIMEOUT;

public class AgodaHomePage extends AgodaBasePage {
    private final SelenideElement loginButton = $("[data-element-name='sign-in-button']");
    private final SelenideElement registerButton = $("data-element-name='sign-up-button'");
    private final SelenideElement selectedLanguage = $("[data-selenium='language-container-selected-language']");
    private final String dynamicDateLocator = "[data-selenium-date='%s']";
    private final SelenideElement occupancyBox = $("[data-element-name='occupancy-box']");
    private final SelenideElement searchButton = $("[data-selenium='searchButton']");

    @Step("Click login button")
    public void clickLoginButton() {
        loginButton.click();
        PageUtils.waitForPageFullyLoaded();
        $("[class='whiteLabelLogo']").shouldHave(Condition.disappear, DEFAULT_TIMEOUT);
    }

    @Step("Get selected language")
    public String getSelectedLanguage() {
        return selectedLanguage.getAttribute("data-value");
    }

    @Step("Set selected language")
    public void setSelectedLanguage(String language) {
        selectedLanguage.click();
        $("[lang='" + language + "']").click();
    }

    @Step("Set language if it is not {language}")
    public void setLanguageIfNot(String language) {
        if (!getSelectedLanguage().contains(language)) {
            setSelectedLanguage(language);
        }
    }

    @Step("Fill search information")
    public void fillSearchInformation(String searchInfo) {
        inputSearch.val(searchInfo);
    }

    @Step("Select search information")
    public void selectSearchInformation(String searchInfo) {
        fillSearchInformation(searchInfo);
        $$("[data-selenium='autosuggest-item']").first().click();
    }

    @Step("Select date")
    public void selectDate(LocalDate date) {
        $(String.format(dynamicDateLocator,
                date.format(DateTimeFormatter.ofPattern(Constants.AGODA_DATE_FORMAT_1))
        )).click();
    }

    @Step("Select start date")
    public void selectStartDate(LocalDate date) {
        selectDate(date);
    }

    @Step("Select end date")
    public void selectEndDate(LocalDate date) {
        selectDate(date);
    }

    @Step("Select date range")
    public void selectDateRange(LocalDate startDate, LocalDate endDate) {
        selectStartDate(startDate);
        selectEndDate(endDate);
    }

    private int getRoomQuantity() {
        return Integer.parseInt($("[data-selenium='desktop-occ-room-value']").getText());
    }

    private int getAdultQuantity() {
        return Integer.parseInt($("[data-selenium='desktop-occ-adult-value']").getText());
    }

    private int getChildQuantity() {
        return Integer.parseInt($("[data-selenium='desktop-occ-children-value']").getText());
    }

    /**
     * Adjust the quantity for 1 ingredient as required.
     *
     * @param currentQuantitySupplier method to get the current quantity
     * @param containerSelector       selector of the container that contains the quantity
     * @param expectedNumber          expected number of the quantity
     * @param maxQuantity             maximum quantity allowed
     */
    private void adjustQuantity(IntSupplier currentQuantitySupplier, String containerSelector, int expectedNumber, int maxQuantity) {
        int currentQuantity = currentQuantitySupplier.getAsInt();
        String buttonSelector = currentQuantity < expectedNumber ? "button[data-selenium='plus']" : "button[data-selenium='minus']";
        SelenideElement container = $(containerSelector);

        while (currentQuantity != expectedNumber) {
            container.$(buttonSelector).click();
            currentQuantity = currentQuantitySupplier.getAsInt();
            if (currentQuantity == maxQuantity) break;
        }
    }

    @Step("Adjust room quantity")
    public void adjustRoomQuantity(int expectedNumber) {
        adjustQuantity(this::getRoomQuantity, "[data-selenium='occupancyRooms']", expectedNumber, Constants.AGODA_MAX_QUANTITY);
    }

    @Step("Adjust adult quantity")
    public void adjustAdultQuantity(int expectedNumber) {
        adjustQuantity(this::getAdultQuantity, "[data-selenium='occupancyAdults']", expectedNumber, 60);
    }

    @Step("Adjust child quantity")
    public void adjustChildQuantity(int expectedNumber) {
        adjustQuantity(this::getChildQuantity, "[data-selenium='occupancyChildren']", expectedNumber, Constants.MAX_QUANTITY);
    }

    @Step("Fill travel information")
    public void fillTravelInformation(Travel travel) {
        if (Objects.nonNull(travel)) {
            if (Objects.nonNull(travel.getDestination())) {
                selectSearchInformation(travel.getDestination());
            }
            if (Objects.nonNull(travel.getStartDate()) && Objects.nonNull(travel.getEndDate())) {
                System.out.println("Start date: " + travel.getStartDate());
                System.out.println("End date: " + travel.getEndDate());
                selectDateRange(travel.getStartDate(), travel.getEndDate());
            }
            if (Objects.nonNull(travel.getNumberOfRooms())) {
                adjustRoomQuantity(travel.getNumberOfRooms());
            }
            if (Objects.nonNull(travel.getNumberOfAdults())) {
                adjustAdultQuantity(travel.getNumberOfAdults());
            }
            if (Objects.nonNull(travel.getNumberOfChildren())) {
                adjustChildQuantity(travel.getNumberOfChildren());
            }
        }
    }

    @Step("Close occupancy box")
    public void closeOccupancyBox() {
        occupancyBox.click();
    }

    @Step("Click search button")
    public void clickSearchButton() {
        if (!searchButton.is(clickable)) {
            closeOccupancyBox();
        }
        searchButton.click();
    }
}
