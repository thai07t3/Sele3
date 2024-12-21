package vjetpage;

import base.BasePage;
import com.codeborne.selenide.SelenideElement;
import enums.Languages;
import io.qameta.allure.Step;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

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
    private final SelenideElement departureDateButton = $x("//p[text()='" + localization.getContent("departureDate") + "']");
    private final SelenideElement returnDateButton = $x("//p[text()='" + localization.getContent("returnDate") + "']");
    private final String dynamicDateLocator = "//div[text()='%s']/following-sibling::div[@class='rdrDays']//button[not(contains(@class, 'rdrDayDisabled'))]//span[text()='%d']";
//    private final SelenideElement currentDay = $x("//button[@class='rdrDay rdrDayToday']");
//    private final SelenideElement currentMonth = $x("//div[button[@class='rdrDay rdrDayToday']]/preceding-sibling::div[@class='rdrMonthName']");


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

    @Step("Select date from now")
    public void selectDateFromNow(int plusDays) {
        LocalDateTime lc = LocalDateTime.now().plusDays(plusDays);

        // Lấy định dạng tháng và năm theo locale
        String monthAndYear;
        if (localization.getLocale().equals(Languages.VIETNAMESE.getValue())) {
            // Tiếng Việt: "tháng 12 2024"
            monthAndYear = String.format("tháng %d %d", lc.getMonthValue(), lc.getYear());
        } else if (localization.getLocale().equals(Languages.KOREAN.getValue())) {
            // Tiếng Hàn: "12월 2024"
            monthAndYear = String.format("%d월 %d", lc.getMonthValue(), lc.getYear());
        } else {
            // Mặc định: Tên tháng đầy đủ và năm (ví dụ: "December 2024")
            monthAndYear = lc.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + lc.getYear();
        }
        System.out.println(monthAndYear);

        // Dynamic locator với định dạng chính xác
//        String dynamicDateLocator = "//div[text()='%s']/following-sibling::div[@class='rdrDays']//button[not(contains(@class, 'rdrDayDisabled'))]//span[text()='%d']";

//        // In thông tin để debug
//        System.out.println(monthAndYear);
//        System.out.println(lc.getDayOfMonth());
//        System.out.printf((dynamicDateLocator) + "%n", monthAndYear, lc.getDayOfMonth());

        // Sử dụng Selenide để click vào element
        $x(String.format(dynamicDateLocator, monthAndYear, lc.getDayOfMonth())).click();
    }
}
