package models.agoda;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import utils.Constants;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Builder
public class Travel {
    private String destination;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer numberOfRooms;
    private Integer numberOfAdults;
    private Integer numberOfChildren;

    public String getStartDateAsString(String pattern) {
        return getStartDate().format(DateTimeFormatter.ofPattern(pattern));
    }

    public String getEndDateAsString(String pattern) {
        return getEndDate().format(DateTimeFormatter.ofPattern(pattern));
    }

    public String getStartDateAsString() {
        return getStartDateAsString(Constants.AGODA_DATE_FORMAT_2);
    }

    public String getEndDateAsString() {
        return getEndDateAsString(Constants.AGODA_DATE_FORMAT_2);
    }
}
