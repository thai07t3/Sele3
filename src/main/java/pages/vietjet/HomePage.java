package pages.vietjet;

import base.BasePage;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import enums.AdjustType;
import enums.AgeType;
import enums.FlyType;
import io.qameta.allure.Step;
import models.Ticket;
import utils.Constants;
import utils.DateUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

public class HomePage extends BasePage {

    private final SelenideElement acceptButton = $x(
            "//div[@id='popup-dialog-description']//following-sibling::div/button"
    );
    private final SelenideElement iframe = $(byId("preview-notification-frame"));
    private final SelenideElement fromInput = $x(
            "//label[contains(text(),'"
                    + localization.getContent("from")
                    + "')]/following-sibling::div//input");
    private final SelenideElement toInput = $x("//input[@id='arrivalPlaceDesktop']");
    private final SelenideElement departureDateButton = $x(
            "//p[text()='" + localization.getContent("departureDate") + "']");
    private final SelenideElement returnDateButton = $x(
            "//p[text()='" + localization.getContent("returnDate") + "']");
    private final SelenideElement baseCustomSelect = $("[id^='input-base-custom']");
    private final String dynamicDateLocator = "//div[text()='%s']/following-sibling::div[@class='rdrDays']//" +
            "button[not(contains(@class, 'rdrDayDisabled'))]//span[text()='%d']";
    private final String quantityAdjustment = "//div[div[p[text()='%s']]]/following-sibling::div//button[%d]";
    private final String currentNumber = "//div[div[p[text()='%s']]]/following-sibling::div//span[@weight='Bold']";


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

    @Step("Select round trip: {flyType}")
    public void selectFlyType(FlyType flyType) {
        $x(String.format("//span[text()='%s']", flyType.getValue())).click();
    }

    @Step
    public void closePassengerForm() {
        baseCustomSelect.click();
    }

    @Step("Click on: {text}")
    public void clickOn(String text) {
        $$x(String.format("//span[text()=\"%s\"]", text)).get(1).click();
    }

    @Step("Fill From with value: {from}")
    public void fillFrom(String from) {
        fromInput.val(from);
        fromInput.click();
        String locator = "//div[.='%s']";
        $x(String.format(locator, from)).click();
    }

    @Step("Fill From with value: {to}")
    public void fillTo(String to) {
        toInput.val(to);
        String locator = "//div[.='%s']";
        $x(String.format(locator, to)).click();
    }

    @Step("Click on Departure Date button")
    public void clickDepartureDateButton() {
        departureDateButton.click();
    }

    @Step("Click on Return Date button")
    public void clickReturnDateButton() {
        returnDateButton.click();
    }


    @Step("Select date: {plusDays} days from now")
    public void selectDate(int plusDays) {
        LocalDate targetDate = LocalDate.now().plusDays(plusDays);
        $x(String.format(
                dynamicDateLocator,
                DateUtils.getMonthAndYear(targetDate),
                targetDate.getDayOfMonth())
        ).click();
    }

    @Step("Select date: {date}")
    public void selectDate(LocalDate date) {
        $x(String.format(
                dynamicDateLocator,
                DateUtils.getMonthAndYear(date),
                date.getDayOfMonth())
        ).click();
    }

    @Step("Select date: {date} that is next friday")
    public void selectNextFriday(LocalDate date) {
        int daysUntilFriday = 5 - date.getDayOfWeek().getValue();
        if (daysUntilFriday < 0) {
            daysUntilFriday += 7;
        }
        LocalDate nextFriday = date.plusDays(daysUntilFriday);
        $x(String.format(
                dynamicDateLocator,
                DateUtils.getMonthAndYear(nextFriday),
                nextFriday.getDayOfMonth())
        ).click();
    }

    @Step("Adjust quantity of {type} to {quantity}")
    public void adjustQuantity(AgeType type, int expectedNumber) {
        int currentQuantity = Integer.parseInt($x(String.format(currentNumber, type.getValue())).getText());
        AdjustType adjustType = currentQuantity < expectedNumber ? AdjustType.INCREASE : AdjustType.DECREASE;
        while (currentQuantity < expectedNumber) {
            $x(String.format(quantityAdjustment, type.getValue(), adjustType.getValue())).click();
            currentQuantity = Integer.parseInt($x(String.format(currentNumber, type.getValue())).getText());
            if (currentQuantity == Constants.MAX_QUANTITY) break; // prevent infinite loop
        }
    }

    private int getQuantity(AgeType type) {
        return Integer.parseInt($x(String.format(currentNumber, type.getValue())).getText());
    }

    @Step("Should ticket selection form be displayed")
    public void shouldTicketSelectionFormBeDisplayed(Ticket ticket) {
        fromInput.shouldBe(Condition.have(Condition.text(ticket.getFrom())));
        toInput.shouldBe(Condition.have(Condition.text(ticket.getTo())));
        departureDateButton.sibling(0).shouldHave(Condition.text(
                ticket.getDepartureDate()
                        .format(DateTimeFormatter
                                .ofPattern(Constants.DATE_FORMAT))
        ));
        returnDateButton.sibling(0).shouldBe(Condition.text(
                ticket.getReturnDate()
                        .format(DateTimeFormatter
                                .ofPattern(Constants.DATE_FORMAT))
        ));
        $x(String.format(currentNumber, AgeType.ADULT.getValue())).shouldHave(Condition.text(
                String.valueOf(ticket.getNumberOfAdult())));
    }
}
