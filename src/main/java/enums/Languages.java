package enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum Languages {
    ENGLISH("en", "English"),
    VIETNAMESE("vi", "Vietnamese"),
    KOREAN("ko", "Korean");

    private String code;
    private String name;
}
