package models;

public class Manager extends Employee {
	private static final long serialVersionUID = 1L;

    public Manager(String id, String name, String login, String password, double salary) {
        super(id, name, login, password, salary);
        }
}