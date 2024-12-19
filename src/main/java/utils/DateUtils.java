package utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DateUtils {



    public static String formatLocalDateTime(LocalDateTime localDate, String format, boolean lowercasePeriod) {
        String result = localDate.format(DateTimeFormatter.ofPattern(format));
        if (lowercasePeriod) {
            return result.replace("AM", "am").replace("PM", "pm");
        }
        return result;
    }

    public static String formatLocalDateTime(LocalDateTime localDate, String format) {
        if (localDate == null) {
            return "";
        }
        // Timezone format
        if (format.contains("XXX")) {
            return formatZonedDateTime(localDate, format);
        }
        return formatLocalDateTime(localDate, format, true);
    }

    public static String formatLocalDateTime(long milliseconds, String format) {
        return formatLocalDateTime(milliseconds, format, true);
    }

    public static String formatZonedDateTime(LocalDateTime localDate, String format) {
        ZonedDateTime zonedDateTime = localDate.atZone(ZoneId.systemDefault());
        return zonedDateTime.format(DateTimeFormatter.ofPattern(format));
    }

    public static String formatLocalDateTime(long milliseconds, String format, boolean lowercasePeriod) {
        LocalDateTime localDate = Instant.ofEpochMilli(milliseconds).
                atZone(ZoneId.systemDefault()).toLocalDateTime();
        String result = localDate.format(DateTimeFormatter.ofPattern(format));
        if (lowercasePeriod) {
            return result.replace("AM", "am").replace("PM", "pm");
        }
        return result;
    }

    private static int[] parseTimeComponents(String time) {
        String[] strings = time.split("[dhms]");
        int day = Integer.parseInt(strings[0]);
        int hour = Integer.parseInt(strings[1]);
        int minute = Integer.parseInt(strings[2]);
        int second = Integer.parseInt(strings[3]);
        return new int[]{day, hour, minute, second};
    }

    public static String plusTimeForCurrentDate(int type, int amount, String dateTimeFormat, boolean isGMT) {
        DateFormat sdf = new SimpleDateFormat(dateTimeFormat);
        if (isGMT) sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(type, amount);
        return sdf.format(calendar.getTime()).replace("Z", "+00:00");
    }

    public static String getCurrentDate() {
        DateFormat sdf = new SimpleDateFormat(Constants.DATE_TIME_FORMAT_TIME_ZONE);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        return sdf.format(calendar.getTime()).replace("Z", "+00:00");
    }

    public static long calculatorHoursBetweenTwoDays(String firstDate, String secondDate) {
        DateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_TIME_FORMAT_TIME_ZONE);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        long diffInHour = 0;
        try {
            Date date = simpleDateFormat.parse(firstDate);
            Date date2 = simpleDateFormat.parse(secondDate);

            long diffInMilliseconds = Math.abs(date.getTime() - date2.getTime());
            diffInHour = diffInMilliseconds / 1000 / 60 / 60;
        } catch (ParseException pe) {
            throw new RuntimeException(pe);
        }
        return diffInHour;
    }

    public static void plusDays(int numberOfDays) {
        SystemDateTime.instance().plusDays(numberOfDays);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
