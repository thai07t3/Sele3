package base;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import utils.ConfigReader;
import utils.Constants;
import utils.Localization;

public class BaseTest {
    protected Localization localization;

    @BeforeClass
    public void beforeClass() {
        SelenideLogger.addListener("AllureSelenide",
                new AllureSelenide()
                        .includeSelenideSteps(false)
                        .screenshots(true));
    }

    @Parameters("browser")
    @BeforeMethod
    public void setUp() {
        //Set up the browser
        String browser = System.getProperty("browser");
        ConfigReader configReader = new ConfigReader(Constants.CONFIG_FILE_PATH);
        String prefix = browser + ".";

        Configuration.browser = configReader.getString(prefix + "browser");
//        Configuration.startMaximized = configReader.getBoolean(prefix + "isMaximized");
        Configuration.headless = configReader.getBoolean(prefix + "headless");

        String gridUrl = configReader.getString(prefix + "gridUrl");
        if (gridUrl != null && !gridUrl.isEmpty()) {
            Configuration.remote = gridUrl;
        }

        // Set up the language
        String language = System.getProperty("language");
        localization = new Localization(language);
        BasePage.setLocalization(localization);
    }

    @AfterMethod
    public void tearDown() {
        Selenide.closeWebDriver();
    }
}
