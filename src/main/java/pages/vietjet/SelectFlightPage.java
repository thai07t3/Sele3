package pages.vietjet;

import com.codeborne.selenide.Selenide;
import org.jetbrains.annotations.NotNull;
import pages.BasePage;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import enums.vietject.ClassType;
import enums.vietject.FlyType;
import enums.vietject.Languages;
import io.qameta.allure.Step;
import models.vietjet.FlyInfo;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Condition.clickable;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static utils.LocaleManager.getLocaleBundle;

public class SelectFlightPage extends BasePage {
    ResourceBundle bundle = getLocaleBundle();

    private final SelenideElement addPopup = $x("//div[@role='none presentation']");
    private final SelenideElement cancelButton = $x("//div[@role='none presentation']//*[local-name()='svg']");
    private final ElementsCollection flightRows = $$x("//div[p[contains(.,'000 VND')]/preceding-sibling::p]/ancestor::div[3]");

    @Step
    public void clickOn(String text) {
        $x(String.format("//span[contains(.,'%s')]", text)).click();
    }

    @Step("Wait for pop-up and then close it")
    public void closePopUp() {
        try {
            Thread.sleep(6000); // TODO: replace with a better way to wait for pop-up
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (!bundle.getLocale().toString().equals(Languages.KOREAN.getCode())) { // Korean language doesn't have pop-up //TODO: improve this condition to handle all languages
            cancelButton.shouldBe(visible);
            cancelButton.click();
        }
    }

    @Step("Get all fly data")
    public List<FlyInfo> getAllFlyData() {
        return flightRows.stream()
                .map(row -> {
                    String[] parts = Objects.requireNonNull(row.getDomProperty("outerText")).split("\\n");
                    String code = parts[0];
                    LocalTime[] times = parseTimes(parts[1]);
                    String[] flightDetails = parts[2].split("-");
                    String flyName = flightDetails[0].trim();
                    String flyType = flightDetails[1].trim();
                    String businessPrice = parsePrice(parts[4]);
                    int priceOffset = businessPrice.equals(bundle.getString("sold.out")) ? 2 : 4;
                    String skyBoosPrice = parsePrice(parts[4 + priceOffset]);
                    priceOffset = skyBoosPrice.equals(bundle.getString("sold.out")) ? priceOffset - 2 : priceOffset;
                    String deluxePrice = parsePrice(parts[8 + priceOffset]);
                    priceOffset = deluxePrice.equals(bundle.getString("sold.out")) ? priceOffset - 2 : priceOffset;
                    String ecoPrice = parsePrice(parts[12 + priceOffset]);

                    return FlyInfo.builder()
                            .code(code)
                            .startTime(times[0])
                            .endTime(times[1])
                            .flyName(flyName)
                            .flyType(flyType)
                            .businessPrice(businessPrice)
                            .skyBoosPrice(skyBoosPrice)
                            .deluxePrice(deluxePrice)
                            .ecoPrice(ecoPrice)
                            .build();
                })
                .collect(Collectors.toList());
    }

    private LocalTime @NotNull [] parseTimes(@NotNull String timeString) {
        return Arrays.stream(timeString.split(" " + bundle.getString("time.to.time") + " "))
                .map(LocalTime::parse)
                .toArray(LocalTime[]::new);
    }

    private String parsePrice(String value) {
        return bundle.getString("sold.out").equalsIgnoreCase(value)
                ? value
                : value.replaceAll("[^0-9,]", "").trim();
    }

    @Step("Select the cheapest fly")
    public void selectCheapestFly(List<FlyInfo> flyInfoList, @NotNull ClassType classType) {
        int index = FlyInfo.getLowestPriceIndex(flyInfoList);
        int additionalScroll = 250;
        SelenideElement cheapestFly = flightRows.get(index).$x("div[2]/div[" + classType.getIndex() + "]");
        cheapestFly.scrollIntoView("{behavior: 'instant', block: 'end' }"); // scroll to the element
        Selenide.executeJavaScript("window.scrollBy(0, arguments[0]);", additionalScroll); // scroll to make sure the element is clickable
        cheapestFly.shouldBe(clickable).click();
    }

    @Step("Select the cheapest fly")
    public void selectCheapestFly(List<FlyInfo> flyInfoList) {
        selectCheapestFly(flyInfoList, ClassType.ECO);
    }

    @Step("Select the cheapest flies")
    public void selectCheapestFlies(@NotNull FlyType flyType) {
        if (flyType.equals(FlyType.RETURN)) {
            selectCheapestFly(getAllFlyData());
            clickOn(bundle.getString("continue.button"));
            selectCheapestFly(getAllFlyData());
            clickOn(bundle.getString("continue.button"));
        } else {
            {
                selectCheapestFly(getAllFlyData());
                clickOn(bundle.getString("continue.button"));
            }
        }
    }
}
