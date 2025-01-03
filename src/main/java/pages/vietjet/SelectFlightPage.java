package pages.vietjet;

import base.BasePage;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import enums.ClassType;
import enums.FlyType;
import enums.Languages;
import io.qameta.allure.Step;
import models.FlyInfo;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class SelectFlightPage extends BasePage {
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
        if (!localization.getLocale().equals(Languages.KOREAN.getCode())) { // Korean language doesn't have pop-up //TODO: improve this condition to handle all languages
            cancelButton.shouldBe(visible);
            cancelButton.click();
        }
    }

    @Step("Get all fly datas")
    public List<FlyInfo> getAllFlyDatas() {
        return flightRows.stream()
                .map(row -> {
                    String[] parts = row.getDomProperty("outerText").split("\\n");
                    String code = parts[0];
                    LocalTime[] times = parseTimes(parts[1]);
                    String[] flightDetails = parts[2].split("-");
                    String flyName = flightDetails[0].trim();
                    String flyType = flightDetails[1].trim();
                    String businessPrice = parsePrice(parts[4]);
                    int priceOffset = businessPrice.equals(localization.getContent("sold.out")) ? 2 : 4;
                    String skyBoosPrice = parsePrice(parts[4 + priceOffset]);
                    String deluxePrice = parsePrice(parts[8 + priceOffset]);
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

    private LocalTime[] parseTimes(String timeString) {
        return Arrays.stream(timeString.split(localization.getContent("time.to.time")))
                .map(LocalTime::parse)
                .toArray(LocalTime[]::new);
    }

    private String parsePrice(String value) {
        return localization.getContent("sold.out").equalsIgnoreCase(value)
                ? value
                : value.replaceAll("[^0-9,]", "").trim();
    }

    @Step("Select the cheapest fly")
    public void selectCheapestFly(List<FlyInfo> flyInfoList, ClassType classType) {
        int index = FlyInfo.getLowestPriceIndex(flyInfoList);
        flightRows.get(index).$x("div[2]/div[" + classType.getIndex() + "]").click();
    }

    @Step("Select the cheapest fly")
    public void selectCheapestFly(List<FlyInfo> flyInfoList) {
        selectCheapestFly(flyInfoList, ClassType.ECO);
    }

    @Step("Select the cheapest flies")
    public void selectCheapestFlies(FlyType flyType) {
        if (flyType.equals(FlyType.RETURN)) {
            selectCheapestFly(getAllFlyDatas());
            clickOn(localization.getContent("continue.button"));
            selectCheapestFly(getAllFlyDatas());
            clickOn(localization.getContent("continue.button"));
        } else {
            {
                selectCheapestFly(getAllFlyDatas());
                clickOn(localization.getContent("continue.button"));
            }
        }
    }
}
