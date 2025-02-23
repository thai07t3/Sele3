package enums;

import lombok.Getter;

import static utils.LocaleManager.getBundleString;

@Getter
public enum Location {
    HANOI(getBundleString("ha.noi"), "HAN"),
    HOCHIMINH(getBundleString("ho.chi.minh"), "SGN");

    private final String name;
    private final String code;

    Location(String name, String code) {
        this.name = name;
        this.code = code;
    }
}
