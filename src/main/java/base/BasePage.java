package base;

import utils.Localization;

public class BasePage {
    public static Localization localization;

    public static void setLocalization(Localization loc) {
        localization = loc;
    }

    protected Localization getLocalization() {
        if (localization == null) {
            throw new IllegalStateException("Localization has not been initialized. Call setLocalization() first.");
        }
        return localization;
    }
}
