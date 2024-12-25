package pages.google;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;

public class ResultPage {
    private final SelenideElement botstuff = $("#botstuff");
    private final SelenideElement botstuff1 = $("#botstuff1");

    @Step("Check that bot stuff is visible")
    public void checkBotStuffIsVisible() {
        botstuff.shouldBe(Condition.visible);
    }

    @Step("Check that bot stuff 1 is visible")
    public void checkBotStuff1IsVisible() {
        botstuff1.shouldBe(Condition.visible);
    }
}
