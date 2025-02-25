package helpers;

import io.qameta.allure.Allure;
import io.qameta.allure.model.Status;
import io.qameta.allure.model.StepResult;

import java.util.UUID;

public class AllureHelper {
    public static void logStep(String message, boolean isPassed) {
        String uuid = UUID.randomUUID().toString();
        Allure.getLifecycle().startStep(uuid, new StepResult().setName(message));
        if (!isPassed) {
            // Set status to failed if the step is not passed
            Allure.getLifecycle().updateStep(uuid, step -> step.setStatus(Status.FAILED));
        } else {
            // Set status to passed if the step is passed
            Allure.getLifecycle().updateStep(uuid, step -> step.setStatus(Status.PASSED));
        }
        Allure.getLifecycle().stopStep(uuid);
    }
}
