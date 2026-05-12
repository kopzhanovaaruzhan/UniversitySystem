package models;

import enums.Faculty;
import enums.Semester;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Course implements Serializable {
    private static final long serialVersionUID = 1L;

    private String courseCode;
    private String name;
    private int credits;
    private int maxStudents;
    private Faculty faculty;

    private List<Teacher> teachers;
    private List<Enrollment> list; // все зачисления: один элемент = один студент в одном семестре

    public Course(String name) {
        this.name = name;
        this.teachers = new ArrayList<>();
        this.list = new ArrayList<>();
        this.maxStudents = 30;
    }

    public Course(String courseCode, String name, int credits) {
        this(name);
        this.courseCode = courseCode;
        this.credits = credits;
    }

    public Course(String courseCode, String name, int credits, Faculty faculty) {
        this(courseCode, name, credits);
        this.faculty = faculty;
    }

    // ─── Enrollment ───────────────────────────────────────────────────────────

    /** Добавить зачисление (вызывается из Student.registerForCourse) */
    public void addEnrollment(Enrollment e) {
        list.add(e);
    }

    /** Найти зачисление студента в конкретном семестре */
    public Enrollment findEnrollment(Student student, Semester semester) {
        return list.stream()
                .filter(e -> e.getStudent().equals(student) && e.getSemester() == semester)
                .findFirst().orElse(null);
    }

    /** Количество студентов в семестре (для проверки переполнения) */
    public int getEnrollmentCount(Semester semester) {
        return (int) list.stream().filter(e -> e.getSemester() == semester).count();
    }

    /** Сколько раз студент провалил курс (для проверки 3-провалов) */
    public int getFailCount(Student student) {
        return (int) list.stream()
                .filter(e -> e.getStudent().equals(student))
                .filter(e -> e.hasMark() && !e.getMark().isPassing())
                .count();
    }

    public boolean isFull(Semester semester) {
        return getEnrollmentCount(semester) >= maxStudents;
    }

    public boolean isEnrolled(Student student, Semester semester) {
        return findEnrollment(student, semester) != null;
    }

    public List<Enrollment> getEnrollments() { return list; }

    // ─── Геттеры/Сеттеры ──────────────────────────────────────────────────────

    public String getName()            { return name; }
    public String getCourseCode()      { return courseCode; }
    public int getCredits()            { return credits; }
    public Faculty getFaculty()        { return faculty; }
    public void setFaculty(Faculty f)  { this.faculty = f; }
    public int getMaxStudents()        { return maxStudents; }
    public void setMaxStudents(int n)  { this.maxStudents = n; }
    public List<Teacher> getTeachers() { return teachers; }
    public void setTeachers(List<Teacher> t) { this.teachers = t; }

    @Override
    public String toString() {
        return String.format("%s [%s] (%d cr.)",
                name, courseCode != null ? courseCode : "N/A", credits);
    }
}


