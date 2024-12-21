package enums;

import lombok.Getter;

@Getter
public enum AdjustType {
    DECREASE(1),
    INCREASE(2);

    private final int value;

    AdjustType(int value) {
        this.value = value;
    }
}
