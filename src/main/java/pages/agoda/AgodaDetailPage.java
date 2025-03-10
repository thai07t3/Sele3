package pages.agoda;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class AgodaDetailPage {
    private final SelenideElement hotelName = $("[data-selenium='hotel-header-name']");
    private final SelenideElement hotelAddress = $("[data-selenium='hotel-address-map']");
    private final ElementsCollection hotelRating = $$("[data-testid='ReviewScoreCompact'] h2 span");
    private final ElementsCollection propertyInfoList = $$("[data-element-name='property-info-section'] span");
    private final ElementsCollection viewPoints = $$("[data-element-name='highlight-facility-mentions-sentiment']");

    private boolean isNonSmokingHotel() {
        return $("[data-element-value='non-smoking']").exists();
    }
}
