package enums.agoda;

import lombok.Getter;

@Getter
public enum PropertyType {
    HOTEL("Hotel"),
    MOTEL("Motel"),
    BREAKFAST_INCLUDED("Breakfast included"),
    SWIMMING_POOL("Swimming pool"),;

    private final String value;

    PropertyType(String value) {
        this.value = value;
    }
}
