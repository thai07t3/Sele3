package tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import utils.LocaleManager;

public class BaseTest {
    public String language;

    @BeforeClass
    public void beforeClass() {
        SelenideLogger.addListener("AllureSelenide",
                new AllureSelenide()
                        .includeSelenideSteps(true)
                        .screenshots(true));
    }

    @Parameters("language")
    @BeforeClass
    public void setLanguage(String language) {
        this.language = language;
        LocaleManager.setLocale(language);
    }
}
