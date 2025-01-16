package base;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import utils.Localization;

public class BaseTest {
    public static String language;

    static {
        setLanguage(); // Set up the language
    }

    @BeforeClass
    public void beforeClass() {
        SelenideLogger.addListener("AllureSelenide",
                new AllureSelenide()
                        .includeSelenideSteps(true)
                        .screenshots(true));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        Selenide.closeWebDriver();
    }

    @Parameters("language")
    protected static void setLanguage() {
        language = System.getProperty("language");
        BasePage.setLocalization(new Localization(language));
    }
}
