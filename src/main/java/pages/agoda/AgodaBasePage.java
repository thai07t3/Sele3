package pages.agoda;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.attributeMatching;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Selenide.*;
import static utils.Constants.DEFAULT_TIMEOUT;
import static utils.PageUtils.waitForAjaxComplete;

public class AgodaBasePage {
    protected final SelenideElement inputSearch = $("[data-selenium='textInput']");

    protected void waitForInitialLoad() {
        $("body").shouldNotHave(cssClass("loading"), DEFAULT_TIMEOUT);
        waitForAjaxComplete();
    }

    protected void verifyHotelListLoaded() {
        $$("li[data-hotelid]").shouldHave(sizeGreaterThan(0), DEFAULT_TIMEOUT);
    }

    protected void clickAndVerify(Runnable action) {
        action.run();
        waitForResultsStabilization();
    }

    protected void waitForResultsStabilization() {
        $("body").shouldNotHave(attributeMatching("class", ".*loading.*"), DEFAULT_TIMEOUT);
        waitForAjaxComplete();
    }
}
