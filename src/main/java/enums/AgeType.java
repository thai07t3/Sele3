package enums;

import base.BasePage;
import lombok.Getter;

@Getter
public enum AgeType {
    ADULT(BasePage.localization.getContent("adult")),
    CHILD(BasePage.localization.getContent("children")),
    INFANT(BasePage.localization.getContent("infant"));

    private final String value;

    AgeType(String value) {
        this.value = value;
    }

}
