package pages.agoda;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class SignUpPage {
    private final SelenideElement emailInput = $("input[data-cy='unified-email-input']");
    private final SelenideElement continueButton = $x("//div[span[text()='Continue']]");
    private final SelenideElement firstNameInput = $("input[data-cy='profile-firstname']");
    private final SelenideElement lastNameInput = $("input[data-cy='profile-lastname']");
    private final SelenideElement agreeCheckBox = $("input[data-cy='profile-email-subscription-checkbox']");
}
