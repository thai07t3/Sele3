package pages.agoda;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class AgodaBasePage {
    protected final SelenideElement inputSearch = $("[data-selenium='textInput']");
}
