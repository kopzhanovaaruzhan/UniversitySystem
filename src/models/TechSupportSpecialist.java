package models;

import java.util.*;

import enums.RequestStatus;

public class TechSupportSpecialist extends Employee {
	private static final long serialVersionUID = 1L;
	
	private Queue<TechSupportRequest> requests = new LinkedList<>();
    public TechSupportSpecialist(String id, String name, String login, String password, double salary) {
        super(id, name, login, password, salary);
    }
    public void addRequest(TechSupportRequest request) {
        requests.add(request);
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
    
    public Queue<TechSupportRequest> getRequestsQueue() {
        return requests;
    }

    public List<TechSupportRequest> getAndResetRequests() {
        List<TechSupportRequest> list = new ArrayList<>(requests);
        for (TechSupportRequest request : requests) {
            if (request.getStatus() == RequestStatus.NEW) {
                request.setStatus(RequestStatus.VIEWED);
            }
        }
        return list;
    }
}



