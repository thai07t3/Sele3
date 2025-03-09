package models.vietjet;

import enums.vietject.FlyType;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import utils.Constants;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Builder
public class Ticket {
    @NonNull
    private FlyType flyType;
    @NonNull
    private String from;
    @NonNull
    private String to;
    @NonNull
    private LocalDate departureDate;
    @NonNull
    private LocalDate returnDate;
    @NonNull
    private Integer numberOfAdult;
    private Integer numberOfChild;
    private Integer numberOfInfant;
    private String promotionCode;
    private boolean isLowestFare;

    public DayOfWeek getDepartureDayOfWeek() {
        return departureDate.getDayOfWeek();
    }

    public DayOfWeek getReturnDayOfWeek() {
        return returnDate.getDayOfWeek();
    }

    public String getDepartureDateAsString(String pattern) {
        return getDepartureDate().format(DateTimeFormatter.ofPattern(pattern));
    }

    public String getReturnDateAsString(String pattern) {
        return getReturnDate().format(DateTimeFormatter.ofPattern(pattern));
    }

    public String getDepartureDateAsString() {
        return getDepartureDateAsString(Constants.DATE_FORMAT);
    }

    public String getReturnDateAsString() {
        return getReturnDateAsString(Constants.DATE_FORMAT);
    }
}
