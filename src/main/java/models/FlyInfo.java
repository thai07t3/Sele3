package models;

import enums.ClassType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.time.LocalTime;
import java.util.List;

import static utils.LocaleManager.getBundleString;

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

    public static int getLowestPriceIndex(@NotNull List<FlyInfo> flyInfoList, ClassType classType) {
        int lowestPrice = Integer.MAX_VALUE;
        int lowestPriceIndex = -1;
        for (int i = 0; i < flyInfoList.size(); i++) {
            int price = getPrice(flyInfoList.get(i), classType);
            if (price == -1) continue;// Skip if the price is not available or sold out
            if (price != -1 && price < lowestPrice) {
                lowestPrice = price;
                lowestPriceIndex = i;
            }
        }
        return lowestPriceIndex;
    }

    private static int getPrice(FlyInfo flyInfo, @NotNull ClassType classType) {
        String price;
        switch (classType) {
            case BUSINESS -> price = flyInfo.getBusinessPrice();
            case SKY_BOOS -> price = flyInfo.getSkyBoosPrice();
            case DELUXE -> price = flyInfo.getDeluxePrice();
            case ECO -> price = flyInfo.getEcoPrice();
            default -> throw new IllegalArgumentException("Invalid class type: " + classType);
        }

        return price.equals(getBundleString("sold.out"))
                ? -1
                : Integer.parseInt(price.replaceAll("[^0-9]", ""));
    }
}
