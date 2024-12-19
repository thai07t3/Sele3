package vjetpage;

import base.BasePage;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

public class HomePage extends BasePage {

    private final SelenideElement acceptButton = $x(localization.getLocator("acceptButton"));
    private final SelenideElement iframe = $(byId(localization.getLocator("iframe")));
    private final ElementsCollection returnRadios = $$x(localization.getLocator("returnRadios"));
    private final ElementsCollection onewayRadios = $$x(localization.getLocator("onewayRadios"));
    private final ElementsCollection fromInputs = $$x(localization.getLocator("fromInputs"));
    private final ElementsCollection toInputs = $$x(localization.getLocator("toInputs"));


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
        onewayRadios.first().click();
    }

    @Step("Select return")
    public void selectReturn() {
        System.out.println("returnRadios.size() = " + returnRadios.size());
        returnRadios.first().shouldBe(visible).click();
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
