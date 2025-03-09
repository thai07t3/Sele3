package enums.vietject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Locale;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum Languages {
    ENGLISH("en", Locale.ENGLISH, "English", "MM/dd/yyyy"),
    VIETNAMESE("vi", new Locale("vi", "VN"), "Vietnamese", "dd/MM/yyyy"),
    KOREAN("ko", Locale.KOREAN, "Korean", "yyyy-MM-dd");

    private String code;
    private Locale locale;
    private String name;
    private String localeDatePattern;
}
