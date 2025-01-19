package enums;

import lombok.Getter;

import static pages.BasePage.localization;

@Getter
public enum AgeType {
    ADULT(localization.getContent("adult")),
    CHILD(localization.getContent("children")),
    INFANT(localization.getContent("infant"));

    private final String value;

    AgeType(String value) {
        this.value = value;
    }

}
