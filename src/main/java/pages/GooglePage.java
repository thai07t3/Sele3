package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selenide.$;

public class GooglePage {
    private final SelenideElement searchField = $(byName("q"));

    @Step("Search for text: {text}")
    public void searchFor(String text) {
        searchField.val(text).pressEnter();
    }
}
