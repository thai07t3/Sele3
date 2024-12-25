package pages.vietjet;

import base.BasePage;
import com.codeborne.selenide.Condition;
import enums.FlyType;
import io.qameta.allure.Step;
import models.Ticket;

import static com.codeborne.selenide.Selenide.$$x;

public class PassengerPage extends BasePage {
    private final String dynamicInfoList = "//p[.='%s']/parent::div//following-sibling::div/div";

    private void shouldDepartureFlightInformationBeCorrect(Ticket ticket) {
        $$x(String.format(dynamicInfoList, localization.getContent("departure.flight"))).get(0).
                shouldHave(Condition.allOf(
                        Condition.text(ticket.getFrom()),
                        Condition.text(ticket.getTo()),
                        Condition.text(ticket.getDepartureDateAsString()))
                );
    }

    private void shouldReturnFlightInformationBeCorrect(Ticket ticket) {
        $$x(String.format(dynamicInfoList, localization.getContent("return.flight"))).get(0).
                shouldHave(Condition.allOf(
                        Condition.text(ticket.getTo()),
                        Condition.text(ticket.getFrom()),
                        Condition.text(ticket.getReturnDateAsString()))
                );
    }

    @Step("Should ticket information be correct")
    public void shouldTicketInformationBeCorrect(Ticket ticket) {
        if (ticket.getFlyType().equals(FlyType.ONE_WAY)) {
            shouldDepartureFlightInformationBeCorrect(ticket);
        } else {
            shouldDepartureFlightInformationBeCorrect(ticket);
            shouldReturnFlightInformationBeCorrect(ticket);
        }
    }
}
