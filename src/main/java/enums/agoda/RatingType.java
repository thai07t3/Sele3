package enums.agoda;

import lombok.Getter;

@Getter
public enum RatingType {
    FIVE_STAR("5-Star rating"),
    FOUR_STAR("4-Star rating"),
    THREE_STAR("3-Star rating"),
    TWO_STAR("2-Star rating"),
    ONE_STAR("1-Star rating");

    private final String value;

    RatingType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
