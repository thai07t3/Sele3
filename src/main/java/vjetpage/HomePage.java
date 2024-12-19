package vjetpage;

import base.BasePage;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

public class HomePage extends BasePage {

    private final SelenideElement acceptButton = $x("//div[@id='popup-dialog-description']//following-sibling::div/button");
    private final SelenideElement iframe = $(byId("preview-notification-frame"));
    private final SelenideElement returnRadio = $x("//div[@role='radiogroup']//input[@value='roundTrip']");
    private final SelenideElement onewayRadio = $x("//div[@role='radiogroup']//input[@value='oneway']");
    private final SelenideElement fromInput = $x(
            "//label[contains(text(),'"
                    + localization.getContent("from")
                    + "')]/following-sibling::div//input");
    private final SelenideElement toInput = $x("//input[@id='arrivalPlaceDesktop']");
    private final SelenideElement departureDateButton = $x("//p[text()='"+localization.getContent("departureDate")+"']");
    private final SelenideElement returnDateButton = $x("//p[text()='"+localization.getContent("returnDate")+"']");
    private final SelenideElement currentDay = $x("//button[@class='rdrDay rdrDayToday']");
    private final SelenideElement currentMonth = $x("//div[button[@class='rdrDay rdrDayToday']]/preceding-sibling::div[@class='rdrMonthName']");
    private final String dynamicSelectDate = "//div[text()='January 2025']/following-sibling::div[@class='rdrDays']//span[text()='20']";



    @Step("Accept cookies if present")
    public void acceptCookiesIfDisplay() {
        if (acceptButton.exists()) {
            acceptButton.click();
        }
    }

    @Step("Click on later button if present in iframe")
    public void clickLaterButtonIfDisplay() {
        if (iframe.exists()) {
            switchTo().frame(iframe);
            $(byId("NC_CTA_TWO")).click();
            switchTo().defaultContent();
        }
    }

    @Step("Select one way")
    public void selectOneWay() {
        onewayRadio.click();
    }

    @Step("Select return")
    public void selectReturn() {
        returnRadio.click();
    }

    @Step("Fill From with value: {from}")
    public void fillFrom(String from) {
        fromInput.val(from);
    }

    @Step("Fill From with value: {to}")
    public void fillTo(String to) {
        toInput.val(to);
    }

    @Step("Click on Departure Date button")
    public void clickDepartureDateButton() {
        departureDateButton.click();
    }

    @Step("Click on Return Date button")
    public void clickReturnDateButton() {
        returnDateButton.click();
    }
}
