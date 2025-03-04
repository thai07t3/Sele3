package models.vietjet;

import enums.ClassType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class Passenger {
    private String TotalPrice;
    private String basePrice;
    private String tax;
    private String departure;
    private String arrival;
    private LocalDate date;
    private String startTime;
    private String endTime;
    private String code;
    private ClassType classType;
}
