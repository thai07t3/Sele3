package vjet;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

public class HomePage {

    private final SelenideElement acceptButton = $x("//div[@id='popup-dialog-description']//following-sibling::div/button");
    private final SelenideElement iframe = $(byId("preview-notification-frame"));
    private final ElementsCollection returnLabels = $$x("//div[@role='radiogroup']//input[@value='roundTrip']");
    private final ElementsCollection onewayLabels = $$x("//div[@role='radiogroup']//input[@value='oneway']");
    private final ElementsCollection fromInputs = $$x("//label[contains(text(),'From')]/following-sibling::div//input");
    private final ElementsCollection toInputs = $$x("//input[@id='arrivalPlaceDesktop']");


    @Step("Accept cookies if present")
    public void acceptCookiesIfDisplay() {
        if (acceptButton.exists()) {
            acceptButton.click();
        }
    }

    @Step("Click on later button if present in iframe")
    public void clickLaterButtonIfDisplay() {
        if (iframe.exists()) {
            Selenide.switchTo().frame(iframe);
            $(byId("NC_CTA_TWO")).click();
            Selenide.switchTo().defaultContent();
        }
    }

    @Step("Select one way")
    public void selectOneWay() {
        onewayLabels.first().click();
    }

    @Step("Select return")
    public void selectReturn() {
        returnLabels.first().click();
    }

    @Step("Fill From with value: {from}")
    public void fillFrom(String from) {
        fromInputs.first().val(from);
    }

    @Step("Fill From with value: {to}")
    public void fillTo(String to) {
        toInputs.first().val(to);
    }
}
