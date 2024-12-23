package models;

import enums.FlyType;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDate;

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

    public DayOfWeek getTakeOnDayOfWeek() {
        return departureDate.getDayOfWeek();
    }

    public DayOfWeek getTakeOffDayOfWeek() {
        return returnDate.getDayOfWeek();
    }
}
