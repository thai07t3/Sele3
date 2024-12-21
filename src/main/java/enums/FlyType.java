package enums;

import base.BasePage;
import lombok.Getter;

@Getter
public enum FlyType {
    ONE_WAY(BasePage.localization.getContent("oneway")),
    RETURN(BasePage.localization.getContent("return"));

    private final String value;

    FlyType(String value) {
        this.value = value;
    }

}
