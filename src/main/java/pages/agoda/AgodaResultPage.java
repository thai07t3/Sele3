package pages.agoda;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import enums.Sort;
import enums.agoda.PropertyType;
import enums.agoda.RatingType;
import enums.agoda.SortType;
import io.qameta.allure.Step;
import models.agoda.RoomInfo;
import models.agoda.Travel;
import org.apache.commons.lang3.tuple.Pair;
import org.openqa.selenium.Keys;
import org.testng.Assert;
import utils.PageUtils;
import utils.SortUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThanOrEqual;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static utils.Constants.DEFAULT_TIMEOUT;
import static utils.Constants.RATING_PATTERN;

public class AgodaResultPage extends AgodaBasePage {
    private final SelenideElement checkInBox = $("[data-selenium='checkInBox']");
    private final SelenideElement checkOutBox = $("[data-selenium='checkOutBox']");
    private final SelenideElement rooms = $("[data-selenium='roomValue']");
    private final SelenideElement adults = $("[data-selenium='adultValue']");
    private final SelenideElement children = $("[data-selenium='childValue']");
    private final ElementsCollection filterOptions = $$("[data-selenium='filter-item-text']");
    private final String dynamicFilterOption = "//span[text()='%s']//following-sibling::*[local-name()='svg']";
    //    private final String dynamicFilterOption = "label[.//span[text()='%s']]//*[local-name()='svg'";
    private final SelenideElement minPriceBox = $("#price_box_0");
    private final SelenideElement maxPriceBox = $("#price_box_1");
    private final ElementsCollection infoHeaders = $$("[data-element-name='property-info-header']");
    private final ElementsCollection ratings = $$x("//div[@data-element-name='property-card-review']/div/span");
    private final ElementsCollection finalPrices = $$("[data-element-name='final-price']");

    @Step("Should ticket selection form be displayed")
    public void shouldTicketSelectionFormBeDisplayed(Travel travel) {
        if (Objects.nonNull(travel)) {
            if (Objects.nonNull(travel.getDestination())) {
                inputSearch.shouldHave(text(travel.getDestination()));
            }
            if (Objects.nonNull(travel.getStartDate())) {
                checkInBox.shouldHave((text(travel.getStartDateAsString())));
            }
            if (Objects.nonNull(travel.getEndDate())) {
                checkOutBox.shouldHave((text(travel.getEndDateAsString())));
            }
            if (Objects.nonNull(travel.getNumberOfRooms())) {
                rooms.shouldBe(text(travel.getNumberOfRooms().toString()));
            }
            if (Objects.nonNull(travel.getNumberOfAdults())) {
                adults.shouldBe(text(travel.getNumberOfAdults().toString()));
            }
            if (Objects.nonNull(travel.getNumberOfChildren())) {
                children.shouldBe(text(travel.getNumberOfChildren().toString()));
            }
        }
    }

    @Step("Click on the recommended")
    public void clickRecommended() {
        $("[data-selenium='search-sort-recommended']").click();
    }

    @Step("Click on the sort lowest price")
    public void clickSortLowestPrice() {
        $("[data-element-name='search-sort-price']").click();
    }

    @Step("Click on the sort distance")
    public void clickSortDistance() {
        $("[data-element-name='search-sort-distance-landmark']").click();
    }

    @Step("Click on the sort rating")
    public void clickSortRating() {
        $("[data-element-name='search-sort-guest-rating']").click();
    }

    @Step("Click on the sort hot deal")
    public void clickSortHotDeal() {
        $("[data-element-name='search-sort-secret-deals']").click();
    }

    @Step("Get searched text")
    public String getSearchedText() {
        return inputSearch.getText();
    }

//    @Step("Sort hotels by {propertyType} - {sortType}")
//    public void sortBy(PropertyType propertyType, SortType sortType) {
//        waitForInitialLoad();
//        applyPropertyFilter(propertyType);
//        applySorting(sortType);
//        waitForResultsStabilization();
//    }

    @Step("Sort hotels by {sortType}")
    public void sortBy(SortType sortType) {
        waitForInitialLoad();
        applySorting(sortType);
        waitForResultsStabilization();
//        verifyHotelListLoaded();
    }

    @Step("Enter min price")
    public void enterMinPrice(int min) {
        minPriceBox.shouldBe(visible, DEFAULT_TIMEOUT).setValue(String.valueOf(min));
    }

    @Step("Enter max price")
    public void enterMaxPrice(int max) {
        maxPriceBox.shouldBe(visible, DEFAULT_TIMEOUT).setValue(String.valueOf(max));
    }

    @Step("Enter min and max price")
    public void enterMinAndMaxPrice(int minPrice, int maxPrice) {
        enterMinPrice(minPrice);
        enterMaxPrice(maxPrice);
        Selenide.actions().sendKeys(Keys.ENTER).perform();
        verifyHotelListLoaded();
    }

    private void applyPropertyFilter(PropertyType propertyType) {
        filterOptions.findBy(text(propertyType.getValue()))
                .scrollIntoView("{behavior: 'instant', block: 'center'}")
                .shouldBe(visible, DEFAULT_TIMEOUT)
                .click();

        verifyHotelListLoaded();
    }

    @Step("Apply filters")
    public void applyFilters(PropertyType[] propertyTypes) {
        for (PropertyType propertyType : propertyTypes) {
            applyPropertyFilter(propertyType);
        }
    }

    @Step("Apply filter for rating")
    public void applyFilter(RatingType ratingType) {
        $$x(dynamicFilterOption.formatted(ratingType.getValue())).first()
                .scrollIntoView("{behavior: 'instant', block: 'center'}")
                .shouldBe(visible, DEFAULT_TIMEOUT)
                .click();

        verifyHotelListLoaded();
    }

    @Step("Apply filters for rating")
    public void applyFilters(RatingType[] ratingTypes) {
        for (RatingType ratingType : ratingTypes) {
            applyFilter(ratingType);
        }
    }

    private void applySorting(SortType sortType) {
        switch (sortType) {
            case RECOMMENDED -> clickAndVerify(this::clickRecommended);
            case LOWEST_PRICE -> clickAndVerify(this::clickSortLowestPrice);
            case DISTANCE -> clickAndVerify(this::clickSortDistance);
            case RATING -> clickAndVerify(this::clickSortRating);
            case HOT_DEAL -> clickAndVerify(this::clickSortHotDeal);
        }
        verifyHotelListLoaded();
    }

    @Step("Get first {number} hotels")
    public List<RoomInfo> getFirstHotels(int number) {
        return $$("li[data-hotelid]")
                .shouldHave(sizeGreaterThanOrEqual(number), DEFAULT_TIMEOUT)
                .first(number)
                .stream()
                .map(this::extractRoomInfo)
                .collect(Collectors.toList());
    }

    @Step("Should destination be correct")
    public void shouldDestinationBeCorrect(int number, String destination) {
        verifyHotelListLoaded();
        ElementsCollection address = $$("button[data-selenium='area-city-text']");
        // Scroll to the last address element to ensure all elements are loaded
        while (address.size() < number) {
            address.last().scrollIntoView(true);
            PageUtils.waitForPageFullyLoaded();
            // Re-fetch the address elements
            address = $$("button[data-selenium='area-city-text']");
        }
        for (int i = 0; i < number; i++) {
            address.get(i).scrollIntoView(true).shouldHave(text(destination));
        }
    }

    private RoomInfo extractRoomInfo(SelenideElement hotel) {
        hotel.scrollIntoView("{behavior: 'auto', block: 'center', inline: 'center'}");
        return RoomInfo.builder()
                .name(extractHotelName(hotel))
                .address(extractAddress(hotel))
                .isAvailable(checkAvailability(hotel))
                .price(extractPrice(hotel))
                .rating(extractRating(hotel))
                .score(extractScore(hotel))
                .scoreType(extractScoreType(hotel))
                .build();
    }

    private String extractHotelName(SelenideElement hotel) {
        return hotel.$("[data-selenium='hotel-name']")
                .getText()
                .trim();
    }

    private String extractAddress(SelenideElement hotel) {
        return hotel.$("[data-selenium='area-city-text'] span")
                .getText()
                .trim();
    }

    private boolean checkAvailability(SelenideElement hotel) {
        return !hotel.$(".sold-out-message").exists();
    }

    private Integer extractPrice(SelenideElement hotel) {
        if (!checkAvailability(hotel)) return null;

        try {
            return Integer.parseInt(
                    hotel.$("[data-element-name='final-price'] [data-selenium='display-price']")
                            .getText()
                            .replaceAll("[^\\d]", "")
            );
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private float extractRating(SelenideElement hotel) {
        SelenideElement ratingElement = hotel.$("[data-testid='rating-container']");
        return ratingElement.exists()
                ? parseRating(ratingElement.getText())
                : 0f;
    }

    private Pair<Float, String> extractReviewInfo(SelenideElement hotel) {
        SelenideElement reviewSection = hotel.$("[data-element-name='property-card-review']");
        ElementsCollection ratingSpans = reviewSection.$("p").$$("span");

        return Pair.of(
                parseFloatSafely(ratingSpans.get(0).getText()),
                ratingSpans.get(1).getText().trim()
        );
    }

    private Float extractScore(SelenideElement hotel) {
        return extractReviewInfo(hotel).getLeft();
    }

    private String extractScoreType(SelenideElement hotel) {
        return extractReviewInfo(hotel).getRight();
    }

    private float parseRating(String text) {
        Matcher matcher = RATING_PATTERN.matcher(text);
        return matcher.find() ? Float.parseFloat(matcher.group()) : 0f;
    }

    private Float parseFloatSafely(String text) {
        try {
            return Float.parseFloat(text.replace(",", "."));
        } catch (NumberFormatException e) {
            System.err.println("Float parsing error: " + e.getMessage());
            return null;
        }
    }

    @Step("Should {number} first hotels be sorted with the right order via {attribute}")
    public void shouldFirstHotelsBeSortedWithTheRightOrder(int number, Sort sortType, String attribute) {
        List<RoomInfo> rooms = getFirstHotels(number);
        for (RoomInfo room : rooms) {
            System.out.println("Name: " + room.getName());
            System.out.println("Address: " + room.getAddress());
            System.out.println("Is available: " + room.getIsAvailable());
            System.out.println("Price: " + room.getPrice());
            System.out.println("Rating: " + room.getRating());
            System.out.println("Score: " + room.getScore());
            System.out.println("Score type: " + room.getScoreType());
            System.out.println("------------------------------------");
        }
        List<RoomInfo> availableRooms;
        if ("price".equals(attribute)) {
            availableRooms = rooms.stream()
                    .filter(room -> room.getIsAvailable() != null && room.getIsAvailable())
                    .collect(Collectors.toList());
        } else {
            availableRooms = rooms;
        }

        List<Comparable> attributeValues = new ArrayList<>();
        for (RoomInfo room : availableRooms) {
            Object value = getAttributeValue(room, attribute);
            if (value instanceof Comparable) {
                attributeValues.add((Comparable) value);
            } else {
                throw new IllegalArgumentException("Attribute " + attribute + " is not comparable");
            }
        }

        List<Comparable> filteredValues = attributeValues.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (filteredValues.isEmpty()) {
            throw new AssertionError("No rooms with non-null " + attribute + " after filtering");
        }

        boolean isSorted;
        if (sortType == Sort.ASC) {
            isSorted = SortUtils.isSortedAsc(filteredValues);
        } else {
            isSorted = SortUtils.isSortedDesc(filteredValues);
        }

        Assert.assertTrue(isSorted, "The first " + number + " hotels are not sorted " + sortType + " by " + attribute);
    }

    private Object getAttributeValue(RoomInfo room, String attribute) {
        switch (attribute) {
            case "name":
                return room.getName();
            case "address":
                return room.getAddress();
            case "isAvailable":
                return room.getIsAvailable();
            case "price":
                return room.getPrice();
            case "rating":
                return room.getRating();
            case "score":
                return room.getScore();
            case "scoreType":
                return room.getScoreType();
            default:
                throw new IllegalArgumentException("Invalid attribute: " + attribute);
        }
    }

}
