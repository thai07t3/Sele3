package utils;

import enums.Languages;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ResourceBundle;

import static utils.EnumUtils.getByValueOrThrow;

@Getter
public class LocaleManager {
    private static Languages language;

    public static void setLocale(String code) {
        language = getByValueOrThrow(Languages.class, code, "getCode");
    }

    public static Languages getSelectedLocale() {
        return language != null ? language : Languages.ENGLISH;
    }

    public static ResourceBundle getLocaleBundle() {
        return ResourceBundle.getBundle("languages.message", getSelectedLocale().getLocale());
    }

    @NotNull
    public static String getBundleString(String key) {
        return getLocaleBundle().getString(key);
    }
}
