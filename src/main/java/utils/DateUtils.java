package utils;

import enums.Languages;
import org.jetbrains.annotations.NotNull;

import java.time.*;
import java.time.format.TextStyle;
import java.util.*;

import static utils.LocaleManager.getLocaleBundle;

public class DateUtils {


    public static @NotNull String getMonthAndYear(LocalDate date) {
        ResourceBundle bundle = getLocaleBundle();
        String monthAndYear;
        if (bundle.getLocale().toString().equals(Languages.VIETNAMESE.getCode())) {
            // Vietnamese: "tháng 12 2024"
            monthAndYear = String.format("tháng %02d %d", date.getMonthValue(), date.getYear());
        } else if (bundle.getLocale().toString().equals(Languages.KOREAN.getCode())) {
            // Korean: "12월 2024"
            monthAndYear = String.format("%d월 %d", date.getMonthValue(), date.getYear());
        } else {
            // Default English: "December 2024"
            monthAndYear = date.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + date.getYear();
        }
        return monthAndYear;
    }

    public static @NotNull LocalDate getNext(@NotNull DayOfWeek dayOfWeek) {
        LocalDate today = LocalDate.now();
        int daysUntil = dayOfWeek.getValue() - today.getDayOfWeek().getValue();
        if (daysUntil <= 0) {
            daysUntil += 7;
        }
        return today.plusDays(daysUntil);
    }

    public static @NotNull LocalDate getNext(@NotNull String dayOfWeek) {
        return getNext(DayOfWeek.valueOf(dayOfWeek.toUpperCase()));
    }
}
