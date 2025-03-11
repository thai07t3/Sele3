package pages.agoda;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.attributeMatching;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Selenide.*;
import static utils.Constants.DEFAULT_TIMEOUT;
import static utils.PageUtils.waitForAjaxComplete;

public class AgodaBasePage {
    protected final SelenideElement inputSearch = $("[data-selenium='textInput']");
    protected final SelenideElement userMenu = $("[data-element-name='user-menu']");
    protected final SelenideElement savedProperty = $("[data-element-name='favorite-menu']");

    protected void waitForInitialLoad() {
        $("body").shouldNotHave(cssClass("loading"), DEFAULT_TIMEOUT);
        waitForAjaxComplete();
    }

    protected void verifyHotelListLoaded() {
        $$("li[data-hotelid]").shouldHave(sizeGreaterThan(0), DEFAULT_TIMEOUT);
    }

    protected void verifyRatingListLoaded() {
        $$("[data-element-name='search-filter-starratingwithluxury']").shouldHave(sizeGreaterThan(0), DEFAULT_TIMEOUT);
    }

    protected void clickAndVerify(Runnable action) {
        action.run();
        waitForResultsStabilization();
    }

    protected void waitForResultsStabilization() {
        $("body").shouldNotHave(attributeMatching("class", ".*loading.*"), DEFAULT_TIMEOUT);
        waitForAjaxComplete();
    }

    @Step("Click on user menu")
    public void clickOnUserMenu() {
        userMenu.click();
    }

    @Step("Click on saved property")
    public void clickOnSavedProperty() {
        savedProperty.click();
    }
}
