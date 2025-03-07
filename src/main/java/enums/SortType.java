package enums;

import lombok.Getter;

@Getter
public enum SortType {
    RECOMMENDED("Recommended"),
    LOWEST_PRICE("Lowest Price"),
    DISTANCE("Distance"),
    RATING("Rating"),
    HOT_DEAL("Hot Deals");

    private final String value;

    SortType(String value) {
        this.value = value;
    }
}
