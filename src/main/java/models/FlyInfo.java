package models;

import enums.ClassType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

import static base.BasePage.localization;

@Getter
@Setter
@Builder
public class FlyInfo {
    private String code;
    private String flyName;
    private LocalTime startTime;
    private LocalTime endTime;
    private String flyType;
    private String businessPrice;
    private String skyBoosPrice;
    private String deluxePrice;
    private String ecoPrice;

    public static int getLowestPriceIndex(List<FlyInfo> flyInfos) {
        return getLowestPriceIndex(flyInfos, ClassType.ECO);
    }

    public static int getLowestPriceIndex(List<FlyInfo> flyInfoList, ClassType classType) {
        int lowestPrice = Integer.MAX_VALUE;
        int lowestPriceIndex = -1;
        for (int i = 0; i < flyInfoList.size(); i++) {
            int price = getPrice(flyInfoList.get(i), classType);
            System.out.println(flyInfoList.get(i).getEcoPrice());
            System.out.println(price);
            if (price == -1) continue;// Skip if the price is not available or sold out
            if (price != -1 && price < lowestPrice) {
                lowestPrice = price;
                lowestPriceIndex = i;
            }
        }
        return lowestPriceIndex;
    }

    private static int getPrice(FlyInfo flyInfo, ClassType classType) {
        String price;
        switch (classType) {
            case BUSINESS -> price = flyInfo.getBusinessPrice();
            case SKY_BOOS -> price = flyInfo.getSkyBoosPrice();
            case DELUXE -> price = flyInfo.getDeluxePrice();
            case ECO -> price = flyInfo.getEcoPrice();
            default -> throw new IllegalArgumentException("Invalid class type: " + classType);
        }

        return price.equals(localization.getContent("sold.out"))
                ? -1
                : Integer.parseInt(price.replaceAll("[^0-9]", ""));
    }
}