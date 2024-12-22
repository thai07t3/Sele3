package pages.vietjet;

import base.BasePage;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import enums.ClassType;
import io.qameta.allure.Step;
import models.FlyInfo;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.codeborne.selenide.Selenide.*;

public class SelectFlightPage extends BasePage {
    private final SelenideElement cancelButton = $("span.evenodd svg path[clip-rule='evenodd']");
//    private final SelenideElement allPrices = $x("//div[p[contains(.,'000 VND')]/preceding-sibling::p]");
    private final ElementsCollection flightRows = $$x("//div[p[contains(.,'000 VND')]/preceding-sibling::p]/ancestor::div[3]");
//    private final String ecoRows = "//div[p[contains(.,'000 VND')]/preceding-sibling::p]/ancestor::div[3]/div[2]/div[%d]";


    @Step("Close po-up if it is visible")
    public void closePopUpIfDisplayed() {
        if (cancelButton.isDisplayed()) {
            cancelButton.click();
        }
    }

    public List<FlyInfo> getAllFlyDatas() {
        return flightRows.stream()
                .map(row -> {
                    String content = row.getText(); // Lấy toàn bộ nội dung text của một row
                    String[] parts = content.split("\\n"); // Tách theo dấu xuống dòng (\n)

                    // Parse từng thành phần theo yêu cầu
                    String code = parts[0];
                    String[] times = parts[1].split(" To "); // Tách thời gian bắt đầu và kết thúc
                    LocalTime startTime = LocalTime.parse(times[0]);
                    LocalTime endTime = LocalTime.parse(times[1]);
                    String flyName = parts[2].split("-")[0].trim(); // Lấy tên chuyến bay (bỏ Direct flight)
                    String flyType = parts[2].split("-")[1].trim(); // Lấy loại chuyến bay
                    String businessPrice = parts[4]; // Lấy giá Business
                    String skyBoosPrice = parts[6]; // Lấy giá Sky Boss
                    String deluxePrice = parts[8]; // Lấy giá Deluxe
                    String ecoPrice = parts[10]; // Lấy giá Economy

                    // Tạo đối tượng FlyInfo
                    return FlyInfo.builder()
                            .code(code)
                            .startTime(startTime)
                            .endTime(endTime)
                            .flyName(flyName)
                            .flyType(flyType)
                            .businessPrice(businessPrice)
                            .skyBoosPrice(skyBoosPrice)
                            .deluxePrice(deluxePrice)
                            .ecoPrice(ecoPrice)
                            .build();
                })
                .collect(Collectors.toList()); // Trả về danh sách các FlyInfo
    }

//    @Step("Get all fly datas")
//    public List<FlyInfo> getAllFlyDatas() {
//        return flightRows.stream()
//                .map(row -> {
//                    String[] parts = row.getText().split("\\n");
//
//                    // Sử dụng hàm phụ để tách và xử lý từng phần
//                    String code = parts[0];
//                    LocalTime[] times = parseTimes(parts[1]);
//                    String[] flightDetails = parts[2].split("-");
//
//                    return FlyInfo.builder()
//                            .code(code)
//                            .startTime(times[0])
//                            .endTime(times[1])
//                            .flyName(flightDetails[0].trim())
//                            .flyType(flightDetails[1].trim())
//                            .businessPrice(parts[4])
//                            .skyBoosPrice(parts[6])
//                            .deluxePrice(parts[8])
//                            .ecoPrice(parts[10])
//                            .build();
//                })
//                .collect(Collectors.toList());
//    }
//
//    // Hàm phụ để tách và parse thời gian
//    private LocalTime[] parseTimes(String timeString) {
//        return Arrays.stream(timeString.split(" To "))
//                .map(LocalTime::parse)
//                .toArray(LocalTime[]::new);
//    }

    @Step("Select the cheapest fly")
    public void selectCheapestFly(List<FlyInfo> flyInfoList, ClassType classType) {
        int index = FlyInfo.getLowestPriceIndex(flyInfoList);
        flightRows.get(index).$x("div[2]/div[" + classType.getIndex() + "]").click();
    }

    @Step("Select the cheapest fly")
    public void selectCheapestFly(List<FlyInfo> flyInfoList) {
        selectCheapestFly(flyInfoList, ClassType.ECO);
    }
}
