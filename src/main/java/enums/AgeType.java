package enums;

import lombok.Getter;

import static utils.LocaleManager.getBundleString;

@Getter
public enum AgeType {
    ADULT(getBundleString("adult")),
    CHILD(getBundleString("children")),
    INFANT(getBundleString("infant"));

    private final String value;

    AgeType(String value) {
        this.value = value;
    }
}
