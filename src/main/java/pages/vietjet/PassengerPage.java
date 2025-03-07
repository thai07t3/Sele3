package pages.vietjet;

import org.jetbrains.annotations.NotNull;
import pages.BasePage;
import com.codeborne.selenide.Condition;
import enums.FlyType;
import io.qameta.allure.Step;
import models.vietjet.Ticket;

import java.util.ResourceBundle;

import static com.codeborne.selenide.Selenide.$$x;
import static utils.LocaleManager.getLocaleBundle;

public class PassengerPage extends BasePage {
    ResourceBundle bundle = getLocaleBundle();

    private final String dynamicInfoList = "//p[.='%s']/parent::div//following-sibling::div/div";

    private void shouldDepartureFlightInformationBeCorrect(@NotNull Ticket ticket) {
        $$x(String.format(dynamicInfoList, bundle.getString("departure.flight"))).get(0).
                shouldHave(Condition.allOf(
                        Condition.text(ticket.getFrom()),
                        Condition.text(ticket.getTo()),
                        Condition.text(ticket.getDepartureDateAsString()))
                );
    }

    private void shouldReturnFlightInformationBeCorrect(@NotNull Ticket ticket) {
        $$x(String.format(dynamicInfoList, bundle.getString("return.flight"))).get(0).
                shouldHave(Condition.allOf(
                        Condition.text(ticket.getTo()),
                        Condition.text(ticket.getFrom()),
                        Condition.text(ticket.getReturnDateAsString()))
                );
    }

    @Step("Should ticket information be correct")
    public void shouldTicketInformationBeCorrect(@NotNull Ticket ticket) {
        if (ticket.getFlyType().equals(FlyType.ONE_WAY)) {
            shouldDepartureFlightInformationBeCorrect(ticket);
        } else {
            shouldDepartureFlightInformationBeCorrect(ticket);
            shouldReturnFlightInformationBeCorrect(ticket);
        }
    }
}
