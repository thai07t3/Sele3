package pages.agoda;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import enums.Sort;
import enums.agoda.PropertyType;
import enums.agoda.SortType;
import io.qameta.allure.Step;
import models.agoda.RoomInfo;
import models.agoda.Travel;
import org.apache.commons.lang3.tuple.Pair;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.testng.Assert;
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

    @Step("Sort hotels by {propertyType} - {sortType}")
    public void sortBy(PropertyType propertyType, SortType sortType) {
        waitForInitialLoad();

        applyPropertyFilter(propertyType);
        applySorting(sortType);

        waitForResultsStabilization();
    }

    private void applyPropertyFilter(PropertyType propertyType) {
        filterOptions.findBy(text(propertyType.getValue()))
                .scrollIntoView("{behavior: 'instant', block: 'center'}")
                .shouldBe(visible, DEFAULT_TIMEOUT)
                .click();

        verifyHotelListLoaded();
    }

    private void applySorting(SortType sortType) {
        switch (sortType) {
            case RECOMMENDED -> clickAndVerify(this::clickRecommended);
            case LOWEST_PRICE -> clickAndVerify(this::clickSortLowestPrice);
            case DISTANCE -> clickAndVerify(this::clickSortDistance);
            case RATING -> clickAndVerify(this::clickSortRating);
            case HOT_DEAL -> clickAndVerify(this::clickSortHotDeal);
        }
    }

    @Step("Get first {number} hotels")
    public List<RoomInfo> getFirstHotels(int number) {
        return $$("li[data-hotelid]")
                .shouldHave(sizeGreaterThanOrEqual(number), DEFAULT_TIMEOUT)
                .asDynamicIterable()
                .stream()
                .limit(number)
                .map(this::safeExtractHotelInfo)
                .collect(Collectors.toList());
    }

    @Step("Should destination be correct")
    public void shouldDestinationBeCorrect(int number, String destination) {
        List<RoomInfo> rooms = getFirstHotels(number);
        for (RoomInfo room : rooms) {
            Assert.assertTrue(room.getAddress().contains(destination), "The hotel destination is not correct");
        }
    }

    // This method is used to handle stale element exceptions
    private RoomInfo safeExtractHotelInfo(SelenideElement hotel) {
        final int MAX_ATTEMPTS = 3;
        int attempts = 0;

        // Get identifying information before element gets stale
        String hotelName = hotel.$("[data-selenium='hotel-name']").text();
        String hotelAddress = hotel.$("[data-selenium='area-city-text']").text();

        while (attempts < MAX_ATTEMPTS) {
            try {
                // Find and scroll to the hotel element
                SelenideElement freshHotel = findHotelByNameAndAddress(hotelName, hotelAddress)
                        .scrollIntoView(true)
                        .shouldBe(visible, Duration.ofSeconds(10));

                return extractRoomInfo(freshHotel);
            } catch (StaleElementReferenceException | NoSuchElementException e) {
                attempts++;
                if (attempts >= MAX_ATTEMPTS) {
                    throw new RuntimeException("Failed to refresh hotel element after " + MAX_ATTEMPTS + " attempts", e);
                }
                // Wait before retry
                sleep(500);
            }
        }
        return null;
    }

    private SelenideElement findHotelByNameAndAddress(String name, String address) {
        return $$("li[data-hotelid]")
                .filterBy(Condition.and(
                        "name_and_address",
                        text(name),
                        text(address)
                ))
                .first()
                .should(exist, Duration.ofSeconds(5));
    }

    private RoomInfo extractRoomInfo(SelenideElement hotel) {
        return RoomInfo.builder()
                .name(extractName(hotel))
                .address(extractAddress(hotel))
                .isAvailable(checkAvailability(hotel))
                .price(extractPrice(hotel))
                .rating(extractRating(hotel))
                .score(extractScore(hotel))
                .scoreType(extractScoreType(hotel))
                .build();
    }

    private String extractName(SelenideElement hotel) {
        return hotel.$("[data-selenium='hotel-name']")
                .shouldBe(visible, DEFAULT_TIMEOUT)
                .text()
                .trim();
    }

    private String extractAddress(SelenideElement hotel) {
        return hotel.$("button[data-selenium='area-city-text'] span")
                .shouldBe(visible)
                .text()
                .trim();
    }

    private boolean checkAvailability(SelenideElement hotel) {
        return !hotel.$(".sold-out-message").exists();
    }

    private Integer extractPrice(SelenideElement hotel) {
        if (!checkAvailability(hotel)) return null;

        try {
            return Integer.parseInt(
                    hotel.$("div[data-element-name='final-price'] span[data-selenium='display-price']")
                            .text()
                            .replaceAll("[^\\d]", "")
            );
        } catch (NumberFormatException e) {
            System.err.println("Price parsing error: " + e.getMessage());
            return null;
        }
    }

    private float extractRating(SelenideElement hotel) {
        SelenideElement ratingElement = hotel.$("div[data-testid='rating-container']");
        return ratingElement.exists()
                ? parseRating(ratingElement.text())
                : 0f;
    }

    private Float extractScore(SelenideElement hotel) {
        Pair<Float, String> reviewInfo = extractReviewInfo(hotel);
        return reviewInfo.getLeft();
    }

    private String extractScoreType(SelenideElement hotel) {
        Pair<Float, String> reviewInfo = extractReviewInfo(hotel);
        return reviewInfo.getRight();
    }

    private Pair<Float, String> extractReviewInfo(SelenideElement hotel) {
        SelenideElement reviewSection = hotel.$("div[data-element-name='property-card-review']");
        if (!reviewSection.exists()) return Pair.of(null, null);

        SelenideElement ratingContainer = reviewSection.$("p");
        if (!ratingContainer.exists()) return Pair.of(null, null);

        ElementsCollection ratingSpans = ratingContainer.$$("span");
        if (ratingSpans.size() < 2) return Pair.of(null, null);

        String scoreText = ratingSpans.get(0).getText().replace(",", ".");
        Float score = parseFloatSafely(scoreText);

        String scoreType = ratingSpans.get(1).getText().trim();

        return Pair.of(score, scoreType);
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
