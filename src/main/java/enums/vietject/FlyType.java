package enums.vietject;

import lombok.Getter;

import static utils.LocaleManager.getBundleString;

@Getter
public enum FlyType {
    ONE_WAY(getBundleString("oneway")),
    RETURN(getBundleString("return"));

    private final String value;

    FlyType(String value) {
        this.value = value;
    }
}