package enums;

import lombok.Getter;

@Getter
public enum PropertyType {
    HOTEL("Hotel"),
    MOTEL("Motel"),;

    private final String value;

    PropertyType(String value) {
        this.value = value;
    }
}
