package models;
import java.util.*;
public class Manager extends Employee {
    public Manager(String id, String name, String login, String password, double salary) {
        super(id, name, login, password, salary);
    }

    public void approveRegistration(Student student, Course course) {
        course.approveStudent(student);
        System.out.println(getLanguageMessage(
                "Student registration was approved.",
                "Регистрация студента была подтверждена.",
                "Студенттің тіркелуі мақұлданды."
        ));
    }
    public void assignTeacherToCourse(Teacher teacher, Course course, LessonType type) {
        course.addInstructor(teacher, type);
        System.out.println(getLanguageMessage(
                "Преподаватель был назначен на курс.",
                "Teacher was assigned to course.",
                "Мұғалім курсқа тағайындалды."
        ));
    }
    public void createPerformanceReport(List<Student> students) {
        System.out.println(getLanguageMessage(
                "Academic Performance Report:",
                "Статистический отчет по успеваемости:",
                "Академиялық үлгерім есебі:"
        ));
        for (Student student : students) {
            double sum = 0;
            int count = 0;

            for (Mark mark : student.getMarks().values()) {
                sum += mark.getTotal();
                count++;
            }
            double average = count == 0 ? 0 : sum / count;
            System.out.println(student.getName() + " average grade: " + average);
        }
    }
}

