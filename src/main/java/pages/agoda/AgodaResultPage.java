package pages.agoda;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import enums.PropertyType;
import enums.SortType;
import io.qameta.allure.Step;
import models.agoda.RoomInfo;
import models.agoda.Travel;
import org.openqa.selenium.By;
import utils.PageUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.codeborne.selenide.Selenide.*;

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
    public void sortBy(PropertyType propertyType, SortType sortType) throws InterruptedException {
        for (SelenideElement filterOption : filterOptions) {
            if (filterOption.getText().equalsIgnoreCase(propertyType.getValue())) {
                filterOption.click();
                break;
            }
        }
        Thread.sleep(5000);
        PageUtils.waitForPageFullyLoaded();
        switch (sortType) {
            case RECOMMENDED:
                clickRecommended();
                break;
            case LOWEST_PRICE:
                clickSortLowestPrice();
                PageUtils.waitForPageFullyLoaded();
                break;
            case DISTANCE:
                clickSortDistance();
                break;
            case RATING:
                clickSortRating();
                break;
            case HOT_DEAL:
                clickSortHotDeal();
                break;
        }
    }

    @Step("Get {number} first hotels")
    public List<RoomInfo> getFirstHotels(int number) {
        List<RoomInfo> roomInfoList = new ArrayList<>();

        // Lấy tất cả các phần tử hotel (li có attribute data-hotelid)
        ElementsCollection hotelElements = $$("li[data-hotelid]");

        // Duyệt qua các phần tử, chỉ lấy số lượng yêu cầu (sử dụng Math.min để tránh vượt quá số phần tử)
        for (int i = 0; i < Math.min(number, hotelElements.size()); i++) {
            SelenideElement hotel = hotelElements.get(i).shouldBe(Condition.visible).scrollTo();

            // Lấy tên khách sạn từ thẻ <h3 data-selenium="hotel-name">
            String name = hotel.$("h3[data-selenium='hotel-name']").shouldBe(Condition.visible).getText().trim();

            // Lấy địa chỉ từ nút chứa data-selenium="area-city-text" (trong thẻ span)
            String address = hotel.$("button[data-selenium='area-city-text'] span").shouldBe(Condition.visible).getText().trim();

            // Kiểm tra tính khả dụng: nếu tồn tại phần tử có class chứa "sold-out-message" thì khách sạn không khả dụng
            boolean isAvailable = !hotel.$(".sold-out-message").exists();

            String priceText = null;
            if (isAvailable) {
                // Lấy giá từ phần tử có data-element-name="final-price" (trích xuất text từ span data-selenium="display-price")
                priceText = hotel.$("div[data-element-name='final-price'] span[data-selenium='display-price']")
                        .getText();
                // Loại bỏ ký tự không phải số (ví dụ dấu phẩy, ký hiệu tiền tệ)
                priceText = priceText.replaceAll("[^\\d]", "");

            }
            Integer price = null;
            if (priceText != null) {
                price = priceText.isEmpty() ? null : Integer.parseInt(priceText);
            }

            // Lấy rating từ phần tử chứa data-testid="rating-container"
            float rating = 0.0f;
            SelenideElement ratingElement = hotel.$("div[data-testid='rating-container']");
            if (ratingElement.exists()) {
                String ratingText = ratingElement.getText();
                Pattern pattern = Pattern.compile("([0-9]+\\.?[0-9]*)");
                Matcher matcher = pattern.matcher(ratingText);
                if (matcher.find()) {
                    rating = Float.parseFloat(matcher.group(1));
                }
            }

            // Sử dụng reviewSection để lấy score
            SelenideElement reviewSection = hotel.$("div[data-element-name='property-card-review']");
            Float score = null;
            if (reviewSection.exists()) {
                SelenideElement scoreElement = reviewSection.$("span");
                if (scoreElement.exists()) {
                    String scoreText = scoreElement.getText().trim(); // ví dụ: "Average rating Review score 5.8 out of 10 with 102 reviews"
                    Pattern scorePattern = Pattern.compile("Review score\\s*([0-9]+\\.?[0-9]*)");
                    Matcher scoreMatcher = scorePattern.matcher(scoreText);
                    if (scoreMatcher.find()) {
                        score = Float.parseFloat(scoreMatcher.group(1));
                    }
                }
            }


            // Xây dựng đối tượng RoomInfo theo mẫu (giả sử đã có Lombok @Builder)
            RoomInfo roomInfo = RoomInfo.builder()
                    .name(name)
                    .address(address)
                    .isAvailable(isAvailable)
                    .price(price)
                    .rating(rating)
                    .score(score)
                    .build();

            roomInfoList.add(roomInfo);
        }

        return roomInfoList;
    }
}
//    public List<RoomInfo> getFirstHotels(int number) {
//        List<RoomInfo> roomInfos = null;
//        for (int i = 1; i <= number; i++) {
//            infoHeaders.get(i).scrollTo();
//            String hotelName = infoHeaders.get(i).$x("//h3").getText();
//            String address = infoHeaders.get(i).$x("div[@data-selenium='area-city']").getText();
//            String rating = ratings.get(i).getText();
//            String finalPrice = finalPrices.get(i).getText();
//
//            roomInfos.add(RoomInfo.builder()
//                    .name(hotelName)
//                    .address(address)
//                    .rating(Float.parseFloat(rating))
//                    .price(Integer.parseInt(finalPrice))
//                    .build());
//        }
//        return roomInfos;
//    }
//}
