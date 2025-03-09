package utils;

import java.time.Duration;
import java.util.regex.Pattern;

public class Constants {
    public static final String LANGUAGE_PATH = "src/test/resources/languages/";
    public static final int MAX_QUANTITY = 9;
    public static final int AGODA_MAX_QUANTITY = 30;
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String AGODA_DATE_FORMAT_1 = "yyyy-MM-dd";
    public static final String AGODA_DATE_FORMAT_2 = "d MMM yyyy";
    public static final Pattern RATING_PATTERN = Pattern.compile("\\d+\\.?\\d*");
    public static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(30);
    public static final String LEAPFROG_EXEL_PATH = "src/test/resources/data/Content_Testing_LeapFrog-games.xlsx";
}
