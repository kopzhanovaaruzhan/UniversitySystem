package models;

import enums.ManagerType;

//import core.DBContext;

public class Manager extends Employee {
    private static final long serialVersionUID = 1L;
    private ManagerType type;
    
    public Manager(String id, String name, String login, String password, double salary) {
        super(id, name, login, password, salary);
    }

    /*public void addCourse(Course course) {
        DBContext.getInstance().addCourse(course);
        System.out.println("Менеджер добавил курс: " + course.getName());
    } */
    public void assignTeacher(Teacher teacher, Course course) {
        course.getTeachers().add(teacher);
        System.out.println("Преподаватель " + teacher.getName()
                + "назначен на курс" + course.getName());
    }
    /*public void manageNews(News news) {
    		DBContext.getInstance().addNews(news);
        System.out.println("Менеджер добавил новость: " + news.getContent());
    } */

    public void approveRegistration(Student student, Course course) {
        // Registration via Enrollment: student.registerForCourse(course, semester)
        System.out.println("Регистрация одобрена для " + student.getName());
    }
    public void createReport() {
        System.out.println("Менеджер сделал отчет");
    }

    public void viewInfo() {
        System.out.println("Менеджер: " + getName());
        System.out.println("Зарплата: " + getSalary());
    }
}


