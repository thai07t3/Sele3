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

public class BaseTest {

    @BeforeClass
    public void beforeClass() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
    }

    @Parameters("browser")
    @BeforeMethod
    public void setUp() {
        String browser = System.getProperty("browser", "chrome"); // Default to chrome if no parameter is passed
        ConfigReader configReader = new ConfigReader(Constants.CONFIG_FILE_PATH);
        String prefix = browser + ".";

        Configuration.browser = configReader.getString(prefix + "browser");
        Configuration.startMaximized = configReader.getBoolean(prefix + "isMaximized");
        Configuration.headless = configReader.getBoolean(prefix + "headless");

        String gridUrl = configReader.getString(prefix + "gridUrl");
        if (gridUrl != null && !gridUrl.isEmpty()) {
            Configuration.remote = gridUrl;
        }
    }

    @AfterMethod
    public void tearDown() {
        Selenide.closeWebDriver();
    }
}
