package models;

import enums.*;

public class Manager extends Employee {
    private static final long serialVersionUID = 1L;
    private ManagerType type; 

    public Manager(String id, String name, String login, String password, double salary, ManagerType type) {
        super(id, name, login, password, salary);
        this.type = type;
    }

    public void approveRegistration(Student student, Course course, Semester semester) {
        course.approveStudent(student, semester);
    }

    public void assignTeacherToCourse(Teacher teacher, Course course, LessonType lessonType) {
        course.addInstructor(teacher, lessonType);
    }

    public News manageNews(String content, NewsTopic topic) {
        return new News(content, topic);
    }

    public ManagerType getType() { return type; }
    public void setType(ManagerType type) { this.type = type; }

    public void viewInfo() {
        System.out.println(getLanguageMessage(
            "Manager: " + getName() + " | Type: " + type,
            "Менеджер: " + getName() + " | Тип: " + type,
            "Менеджер: " + getName() + " | Түрі: " + type
        ));
    }
}




