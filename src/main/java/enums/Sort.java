package enums;

import lombok.Getter;

@Getter
public enum Sort {
    ASC("asc"),
    DESC("desc");

    private final String value;

    Sort(String value) {
        this.value = value;
    }
}
