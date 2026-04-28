package models;

public abstract class Employee extends User {
    private static final long serialVersionUID = 1L;
    
    private double salary;

    public Employee(String id, String name, String login, String password, double salary) {
        super(id, name, login, password); 
        this.salary = salary;
    }

    public void sendMessage(User recipient, String text) {
        System.out.println("Отправитель: " + this.getName() + " -> Получатель: " + recipient.getName());
        System.out.println("Текст: " + text);
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}