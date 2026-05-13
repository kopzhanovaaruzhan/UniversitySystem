package models;

public abstract class Employee extends User {
    private static final long serialVersionUID = 1L;
    
    private double salary;

    public Employee(String id, String name, String login, String password, double salary) {
        super(id, name, login, password); 
        this.salary = salary;
    }

    public Message sendMessage(Employee receiver, String text, boolean official) {
        Message message = new Message(this, receiver, text, official);

        System.out.println(getLanguageMessage(
                "Message was sent successfully.",
                "Сообщение успешно отправлено.",
                "Хабарлама сәтті жіберілді."
        ));
        return message;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}