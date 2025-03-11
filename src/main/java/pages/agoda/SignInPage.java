package pages.agoda;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import helpers.EmailHelper;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.*;

public class SignInPage extends AgodaBasePage {
    private final SelenideElement iframe = $("[data-cy='ul-app-frame']");
    private final SelenideElement loginPopup = $("[data-cy='mutation-sensor']");
    private final SelenideElement signinTitle = $x("//h2[text()='Sign in or create an account']");
    private final SelenideElement emailInput = $("[data-cy='unified-email-input']");
    private final SelenideElement continueButton = $x("//button[div[span[text()='Continue']]]");
    private final SelenideElement signInOption = $x("//p[text()='Other ways to sign in']");
    private final SelenideElement usePasswordButton = $("[data-cy='unified-auth-otp-use-password-button']");
    private final SelenideElement signInButton = $("[data-cy='signin-button']");
    private final SelenideElement firstOTP = $("[data-cy='otp-box-0']");


    @Step("Should login popup be visible")
    public void shouldLoginPopupBeVisible() {
        switchTo().frame(iframe);
        Selenide.sleep(5000);
        signinTitle.shouldHave(Condition.exist);
        switchTo().defaultContent();
    }

    @Step("Enter email")
    public void enterEmail(String email) {
        switchTo().frame(iframe);
        emailInput.click();
        emailInput.val(email);
        switchTo().defaultContent();
    }

    @Step("Enter OTP")
    public void enterOTP(String otp) {
        switchTo().frame(iframe);
        firstOTP.val(otp);
        switchTo().defaultContent();
    }

    @Step("Click continue button")
    public void clickContinueButton() {
        switchTo().frame(iframe);
        continueButton.click();
        switchTo().defaultContent();
    }

    @Step("Use password")
    public void usePasswordOption() {
        usePasswordButton.click();
    }

    @Step("Enter email and password")
    public void enterAccount(String email, String password) {
        $("[data-cy='email']").val(email);
        $("[data-cy='password']").val(password);
    }

    @Step("Log in")
    public void login(String email) {
        String otp = null;
        EmailHelper emailHelper = new EmailHelper(
                "imap.gmail.com",
                "thai07t3@gmail.com",
                "vkid txmb mlkx mcgc");
        enterEmail(email);
        clickContinueButton();
        Selenide.sleep(10000); //Wait for OTP email
        try {
            otp = emailHelper.fetchOtpFromEmail("Email OTP");
            if (otp != null) {
                System.out.println("OTP code lấy được: " + otp);
            } else {
                System.out.println("Không tìm thấy OTP trong email.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        String otp = emailHelper.fetchOtpFromEmail("Email OTP");
         //Wait for OTP email
        enterOTP(otp);
        clickContinueButton();
    }

}
