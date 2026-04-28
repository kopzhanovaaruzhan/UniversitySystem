package models;

//import core.DBContext;

public class Admin extends Employee implements UserFactory {
    private static final long serialVersionUID = 1L;

    public Admin(String id, String name, String login, String password, double salary) {
        super(id, name, login, password, salary);
    }

  
    @Override
    public User createUser(String type, String id, String name, String login, String password) {
        switch (type.toLowerCase()) {
            case "student":
                return new Student(id, name, login, password);

            case "graduate":
                return new GraduateStudent(id, name, login, password, GraduateLevel.MASTER, null);

            case "teacher":
                return new Teacher(id, name, login, password, 500000, TeacherType.LECTOR);

            case "manager":
                return new Manager(id, name, login, password, 450000);

            case "techsupport":
                return new TechSupportSpecialist(id, name, login, password, 350000);

            default:
                System.out.println("Unknown user type: " + type);
                return null;
        }
    }

/*
    public void removeUser(User user) {
        DBContext.getInstance().removeUser(user);
        System.out.println("Админ удалил пользователя: " + user.getName());
    } */

    public void updateUser(User user) {
        System.out.println("Админ обновил пользователя: " + user.getName());
    }

    public void viewSystemLogs() {
       System.out.println("Админ просматривает системные логи");
    }
}