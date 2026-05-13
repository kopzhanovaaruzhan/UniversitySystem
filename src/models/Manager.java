package models;

import enums.*;
import java.util.*;

public class Manager extends Employee {
    private static final long serialVersionUID = 1L;
    private ManagerType type; // Важно для требования "Manager types OR, Departments" 

    public Manager(String id, String name, String login, String password, double salary, ManagerType type) {
        super(id, name, login, password, salary);
        this.type = type;
    }

    // ─── Одобрение регистрации (Связываем с новой логикой Course) ──────────────

    public void approveRegistration(Student student, Course course, Semester semester) {
        // Вызываем метод из нашего интегрированного Course 
        course.approveStudent(student, semester);
        
        System.out.println(getLanguageMessage(
                "Registration approved for " + student.getName() + " on " + course.getName(),
                "Регистрация одобрена для " + student.getName() + " на курс " + course.getName(),
                student.getName() + " үшін " + course.getName() + " курсына тіркелу мақұлданды."
        ));
    }

    // ─── Назначение учителей (С учетом типов уроков LECTURE/PRACTICE) ──────────

    public void assignTeacherToCourse(Teacher teacher, Course course, LessonType lessonType) {
        // Используем требование PDF: разделение по типам занятий [cite: 64, 75, 121]
        course.addInstructor(teacher, lessonType);
        
        System.out.println(getLanguageMessage(
                "Teacher " + teacher.getName() + " assigned as " + lessonType,
                "Преподаватель " + teacher.getName() + " назначен на " + lessonType,
                "Мұғалім " + teacher.getName() + " " + lessonType + " түріне тағайындалды."
        ));
    }

    // ─── Отчеты (Requirement: "statistical reports on academic performance") ──

    public void createPerformanceReport(List<Student> students) {
        System.out.println("\n=== " + getLanguageMessage(
                "Academic Performance Report", 
                "Статистический отчет по успеваемости", 
                "Академиялық үлгерім есебі") + " ===");
        
        for (Student student : students) {
            // ВАЖНО: отчет должен идти через Enrollment и его оценки [cite: 124]
            double sumGpa = student.getGpa(); // Метод, который должна была сделать 1-я напарница
            System.out.println("Student: " + student.getName() + " | GPA: " + sumGpa);
        }
    }

    // ─── Управление новостями (Requirement: "Manage news") ─────────────────────

    public void manageNews(News news) {
        // Менеджер может добавлять и модерировать новости [cite: 125]
        System.out.println(getLanguageMessage(
            "News updated: " + news.getTopic(),
            "Новости обновлены: " + news.getTopic(),
            "Жаңалықтар жаңартылды: " + news.getTopic()
        ));
    }

    // ─── Геттеры ──────────────────────────────────────────────────────────────

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




