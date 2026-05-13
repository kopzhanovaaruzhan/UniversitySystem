package models;
import java.util.*;
public class TechSupportSpecialist extends Employee {
    private Queue<TechSupportRequest> requests = new LinkedList<>();
    public TechSupportSpecialist(String id, String name, String login, String password, double salary) {
        super(id, name, login, password, salary);
    }
    public void addRequest(TechSupportRequest request) {
        requests.add(request);
    }
    public void viewRequests() {
        if (requests.isEmpty()) {
            System.out.println(getLanguageMessage(
                    "No support requests found.",
                    "Нет запросов в техподдержку.",
                    "Техникалық қолдау сұраныстары жоқ."
            ));
            return;
        }
        for (TechSupportRequest request : requests) {
            if (request.getStatus() == RequestStatus.NEW) {
                request.setStatus(RequestStatus.VIEWED);
            }
            System.out.println(request);
        }
    }
    public void acceptRequest(TechSupportRequest request) {
        if (request.getStatus() == RequestStatus.VIEWED) {
            request.setStatus(RequestStatus.ACCEPTED);
        }
    }
    public void rejectRequest(TechSupportRequest request) {
        if (request.getStatus() == RequestStatus.VIEWED) {
            request.setStatus(RequestStatus.REJECTED);
        }
    }
    public void markAsDone(TechSupportRequest request) {
        if (request.getStatus() == RequestStatus.ACCEPTED) {
            request.setStatus(RequestStatus.DONE);
        }
    }
}
