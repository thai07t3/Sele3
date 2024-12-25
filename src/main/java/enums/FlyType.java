package enums;

import lombok.Getter;

import static base.BasePage.localization;

@Getter
public enum FlyType {
    ONE_WAY(localization.getContent("oneway")),
    RETURN(localization.getContent("return"));

    private final String value;

    FlyType(String value) {
        this.value = value;
    }

}
