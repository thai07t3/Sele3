package base;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import io.qameta.allure.selenide.LogType;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import utils.ConfigReader;
import utils.Constants;
import utils.Localization;

import static com.codeborne.selenide.Selenide.open;

public class BaseTest {
    public static String language;

    static {
        setBrowserConfig(); // Set up the browser
        setLanguage(); // Set up the language
    }

    @BeforeClass
    public void beforeClass() {
        SelenideLogger.addListener("AllureSelenide",
                new AllureSelenide()
                        .includeSelenideSteps(true)
                        .screenshots(true)
                        .enableLogs(LogType.BROWSER, java.util.logging.Level.INFO));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        Selenide.closeWebDriver();
    }

    @Parameters("browser")
    protected static void setBrowserConfig() {
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
    }

    @Parameters("language")
    protected static void setLanguage() {
        language = System.getProperty("language");
        BasePage.setLocalization(new Localization(language));
    }
}
