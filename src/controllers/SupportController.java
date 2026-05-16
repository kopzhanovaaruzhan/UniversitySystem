package controllers;

import models.TechSupportSpecialist;
import models.TechSupportRequest;
import core.DBContext;

public class SupportController {

    public static void acceptRequest(TechSupportSpecialist spec, TechSupportRequest request) {
        spec.acceptRequest(request);
        DBContext.save();
    }

    public static void rejectRequest(TechSupportSpecialist spec, TechSupportRequest request) {
        spec.rejectRequest(request);
        DBContext.save();
    }

    public static void markAsDone(TechSupportSpecialist spec, TechSupportRequest request) {
        spec.markAsDone(request);
        DBContext.save();
    }
}