package models.agoda;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RoomInfo {
    private String name;
    private String address;
    private int price;
    private float rating;
}
