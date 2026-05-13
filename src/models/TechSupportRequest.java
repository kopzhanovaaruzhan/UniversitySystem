package models;
import java.io.Serializable;
public class TechSupportRequest implements Serializable {
    private String description;
    private Employee sender;
    private RequestStatus status;

    public TechSupportRequest(Employee sender, String description) {
        this.sender = sender;
        this.description = description;
        this.status = RequestStatus.NEW;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public Employee getSender() {
        return sender;
    }
    public String toString() {
        return "Request from " + sender.getName() +
                " | Status: " + status +
                " | Description: " + description;
    }
}