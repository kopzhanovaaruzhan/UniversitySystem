package models;

import enums.*;
import java.io.Serializable;
import java.util.*;

public class Course implements Serializable {
    private static final long serialVersionUID = 1L;

    private String courseCode;
    private String name;
    private int credits;
    private int maxStudents;
    private Faculty faculty;

    private Map<LessonType, List<Teacher>> instructors = new HashMap<>();
    private List<Student> pendingStudents = new ArrayList<>();
    private List<Enrollment> enrollments = new ArrayList<>(); 

    public Course(String courseCode, String name, int credits, Faculty faculty) {
        this.courseCode = courseCode;
        this.name = name;
        this.credits = credits;
        this.faculty = faculty;
        this.maxStudents = 30;
    }
    
    public boolean isEnrolled(Student student, Semester semester) {
        return findEnrollment(student, semester) != null;
    }

    public void addPendingStudent(Student student) {
        if (!pendingStudents.contains(student)) {
            pendingStudents.add(student);
        }
    }
    
    public void approveStudent(Student student, Semester semester) {
        if (pendingStudents.contains(student)) {
            enrollments.add(new Enrollment(this, student, semester));
            pendingStudents.remove(student);
        }
    }

    public void addInstructor(Teacher teacher, LessonType type) {
        instructors.putIfAbsent(type, new ArrayList<>());
        instructors.get(type).add(teacher);
    }

    public List<Teacher> getInstructors(LessonType type) {
        return instructors.getOrDefault(type, new ArrayList<>());
    }


    public Enrollment findEnrollment(Student student, Semester semester) {
        return enrollments.stream()
                .filter(e -> e.getStudent().equals(student) && e.getSemester() == semester)
                .findFirst().orElse(null);
    }
    
    public List<Teacher> getTeachers() {
        List<Teacher> all = new ArrayList<>();
        instructors.values().forEach(all::addAll);
        return all;
    }

    public void addEnrollment(Enrollment e) {
        this.enrollments.add(e); 
    }

    public int getFailCount(Student student) {
        return (int) enrollments.stream()
                .filter(e -> e.getStudent().equals(student))
                .filter(e -> e.hasMark() && !e.getMark().isPassing())
                .count();
    }

    public boolean isFull(Semester semester) {
        long count = enrollments.stream().filter(e -> e.getSemester() == semester).count();
        return count >= maxStudents;
    }


    public String getName()        { return name; }
    public String getCourseCode()  { return courseCode; }
    public int getCredits()        { return credits; }
    public List<Enrollment> getEnrollments() { return enrollments; }
    public List<Student> getPendingStudents() { return pendingStudents; }

    @Override
    public String toString() {
        return String.format("%s [%s] | Credits: %d | Faculty: %s", 
                name, courseCode, credits, faculty);
    }
}



