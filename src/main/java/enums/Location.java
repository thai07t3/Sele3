package enums;

import lombok.Getter;

import static base.BasePage.localization;

@Getter
public enum Location {
    HANOI(localization.getLocation("ha.noi"), "HAN"),
    HOCHIMINH(localization.getLocation("ho.chi.minh"), "SGN");

    private final String name;
    private final String code;

    Location(String name, String code) {
        this.name = name;
        this.code = code;
    }
}
