package models;

import enums.TeacherType;

public class Teacher extends Employee {
    private static final long serialVersionUID = 1L;
	private TeacherType type;

    public Teacher(String id, String name, String login, String password,
                   double salary, TeacherType type) {
        super(id, name, login, password, salary);
        this.type = type;
    }

    public void createComplaint() {
        System.out.println(getName() + " создал жалобу");
    }

    public void putMark(String studentID, String courseCode, Mark markValue) {
        System.out.println("Преподаватель: " + getName() + " put mark:");
        System.out.println("ID студента: " + studentID);
        System.out.println("Код дисциплины: " + courseCode);
        System.out.println("Итог: " + markValue.getTotal());
    }
    public void viewInfo() {
        System.out.println("Преподаватель: " + getName());
        System.out.println("Позиция: " + type);
        System.out.println("Зарплата: " + getSalary());
    }
    public void markAttendance() {
        System.out.println("Преподаватель проверил посещение");
    }

    public TeacherType getType() {
        return type;
    }

    public void setType(TeacherType type) {
        this.type = type;
    }
}