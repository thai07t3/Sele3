package utils;

import enums.Languages;

import java.time.*;
import java.time.format.TextStyle;
import java.util.*;

import static base.BasePage.localization;

public class DateUtils {

    public static String getMonthAndYear(LocalDate date) {
        String monthAndYear;
        if (localization.getLocale().equals(Languages.VIETNAMESE.getValue())) {
            // Vietnamese: "tháng 12 2024"
            monthAndYear = String.format("tháng %d %d", date.getMonthValue(), date.getYear());
        } else if (localization.getLocale().equals(Languages.KOREAN.getValue())) {
            // Korean: "12월 2024"
            monthAndYear = String.format("%d월 %d", date.getMonthValue(), date.getYear());
        } else {
            // Default English: "December 2024"
            monthAndYear = date.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + date.getYear();
        }
        return monthAndYear;
    }

    public static LocalDate getNext(DayOfWeek dayOfWeek) {
        LocalDate today = LocalDate.now();
        int daysUntil = dayOfWeek.getValue() - today.getDayOfWeek().getValue();
        if (daysUntil <= 0) {
            daysUntil += 7;
        }
        return today.plusDays(daysUntil);
    }

    public static LocalDate getNext(String dayOfWeek) {
        return getNext(DayOfWeek.valueOf(dayOfWeek.toUpperCase()));
    }
}
