package models;

public class TechSupportSpecialist extends Employee {
    private static final long serialVersionUID = 1L;

    public TechSupportSpecialist(String id, String name, String login, String password, double salary) {
        super(id, name, login, password, salary);
    }
    public void manageRequests() {
        System.out.println(getName() + " управляет запросами на поддержку");
    }
}

