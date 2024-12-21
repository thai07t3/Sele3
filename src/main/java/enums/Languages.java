package enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum Languages {
    ENGLISH("en"),
    VIETNAMESE("vi"),
    KOREAN("ko");

    private String value;
}
