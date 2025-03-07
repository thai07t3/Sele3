package pages.vietjet;

import org.jetbrains.annotations.NotNull;
import pages.BasePage;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import enums.AdjustType;
import enums.AgeType;
import enums.FlyType;
import enums.Location;
import io.qameta.allure.Step;
import models.vietjet.Ticket;
import utils.Constants;
import utils.DateUtils;
import utils.EnumUtils;

import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;
import static utils.LocaleManager.getLocaleBundle;

public class HomePage extends BasePage {
    ResourceBundle bundle = getLocaleBundle();

    private final SelenideElement acceptButton = $x(
            "//div[@id='popup-dialog-description']//following-sibling::div/button"
    );
    private final SelenideElement iframe = $(byId("preview-notification-frame"));
    private final SelenideElement fromInput = $x(
            "//label[contains(text(),'"
                    + bundle.getString("from")
                    + "')]/following-sibling::div//input");
    private final SelenideElement toInput = $x("//input[@id='arrivalPlaceDesktop']");
    private final SelenideElement departureDateButton = $x(
            "//p[text()='" + bundle.getString("departureDate") + "']");
    private final SelenideElement returnDateButton = $x(
            "//p[text()='" + bundle.getString("returnDate") + "']");
    private final SelenideElement baseCustomSelect = $("[id^='input-base-custom']");
    private final String dynamicDateLocator = "//div[text()='%s']/following-sibling::div[@class='rdrDays']//" +
            "button[not(contains(@class, 'rdrDayDisabled'))]//span[text()='%d']";
    private final String quantityAdjustment = "//div[div[p[text()='%s']]]/following-sibling::div//button[%d]";
    private final String currentNumber = "//div[div[p[text()='%s']]]/following-sibling::div//span[@weight='Bold']";


    @Step("Fill ticket information")
    public void fillTicketInformation(Ticket ticket) {
        if (Objects.nonNull(ticket)) {
            if (Objects.nonNull(ticket.getFlyType())) {
                selectFlyType(ticket.getFlyType());
            }
            if (Objects.nonNull(ticket.getFrom())) {
                fillFrom(ticket.getFrom());
            }
            if (Objects.nonNull(ticket.getTo())) {
                fillTo(ticket.getTo());
            }
            if (Objects.nonNull(ticket.getDepartureDate())) {
                selectDate(ticket.getDepartureDate());
            }
            if (Objects.nonNull(ticket.getReturnDate())) {
                selectDate(ticket.getReturnDate());
            }
            if (Objects.nonNull(ticket.getNumberOfAdult())) {
                adjustQuantity(AgeType.ADULT, ticket.getNumberOfAdult());
            }
            if (Objects.nonNull(ticket.getNumberOfChild())) {
                adjustQuantity(AgeType.CHILD, ticket.getNumberOfChild());
            }
            if (Objects.nonNull(ticket.getNumberOfInfant())) {
                adjustQuantity(AgeType.INFANT, ticket.getNumberOfInfant());
            }
            if (Objects.nonNull(ticket.getPromotionCode())) {
                // TODO: implement this
            }
            if (Objects.nonNull(ticket.isLowestFare())) {
                // TODO: implement this
            }
        }
    }

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
    public void selectFlyType(@NotNull FlyType flyType) {
        $x(String.format("//span[text()= '%s']", flyType.getValue())).click();
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

    @Step("Fill To with value: {to}")
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
    public void selectNextFriday(@NotNull LocalDate date) {
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

    @Step("Adjust quantity of {ageType} to {expectedNumber}")
    public void adjustQuantity(AgeType ageType, int expectedNumber) {
        int currentQuantity = getQuantity(ageType);
        AdjustType adjustType = currentQuantity < expectedNumber ? AdjustType.INCREASE : AdjustType.DECREASE;
        while (currentQuantity != expectedNumber) {
            $x(String.format(quantityAdjustment, ageType.getValue(), adjustType.getValue())).click();
            currentQuantity = Integer.parseInt($x(String.format(currentNumber, ageType.getValue())).getText());
            if (currentQuantity == Constants.MAX_QUANTITY) break; // Maximum quantity is 9
        }
    }

    private int getQuantity(@NotNull AgeType ageType) {
        return Integer.parseInt($x(String.format(currentNumber, ageType.getValue())).getText());
    }

    @Step("Should ticket selection form be displayed")
    public void shouldTicketSelectionFormBeDisplayed(@NotNull Ticket ticket) {
        Location from = EnumUtils.getByValueOrThrow(Location.class, ticket.getFrom(), "getName");
        Location to = EnumUtils.getByValueOrThrow(Location.class, ticket.getTo(), "getName");

        fromInput.shouldBe(
                Condition.have(
                        Condition.attribute(
                                "value",
                                from.getName() + " (" + from.getCode() + ")")));
        toInput.shouldBe(
                Condition.have(
                        Condition.attribute(
                                "value",
                                to.getName() + " (" + to.getCode() + ")")));
        departureDateButton.sibling(0).shouldHave(Condition.text(
                ticket.getDepartureDateAsString()
        ));
        returnDateButton.sibling(0).shouldBe(Condition.text(
                ticket.getReturnDateAsString()
        ));
        $x(String.format(currentNumber, AgeType.ADULT.getValue())).shouldHave(Condition.text(
                String.valueOf(ticket.getNumberOfAdult())));

        // TODO: Childrens and Infants are not implemented yet
    }
}
