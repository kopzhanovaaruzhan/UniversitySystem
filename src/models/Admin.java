package models;

public class Admin extends Employee implements UserFactory {
	private static final long serialVersionUID = 1L;

    public Admin(String id, String name, String login, String password, double salary) {
        super(id, name, login, password, salary);
    }

    public User createUser(String type, String id, String name, String login, String password) {
        switch (type.toLowerCase()) {
            case "student": return new Student(id, name, login, password);
            case "teacher": return new Teacher(id, name, login, password, 500000);
            case "manager": return new Manager(id, name, login, password, 400000);
            default: return null;
        }
    }
}
