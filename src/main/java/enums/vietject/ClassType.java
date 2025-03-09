package enums.vietject;

import lombok.Getter;

@Getter
public enum ClassType {
    BUSINESS(1, "Business"),
    SKY_BOOS(2, "Sky Boos"),
    DELUXE(3, "Deluxe"),
    ECO(4, "Eco");

    private final int index;
    private final String name;

    ClassType(int value, String name) {
        this.index = value;
        this.name = name;
    }
}
