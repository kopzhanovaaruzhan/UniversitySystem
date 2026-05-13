package models;
import java.io.Serializable;
import java.util.*;
public class Course implements Serializable {
    private String code;
    private String name;
    private int credits;
    private List<Student> students = new ArrayList<>();
    private Map<LessonType, List<Teacher>> instructors = new HashMap<>();
    private List<Student> pendingStudents = new ArrayList<>();

    public Course(String code, String name, int credits) {
        this.code = code;
        this.name = name;
        this.credits = credits;
    }
    public void addPendingStudent(Student student) {
        pendingStudents.add(student);
    }

    public void approveStudent(Student student) {
        if (pendingStudents.contains(student)) {
            students.add(student);
            student.addCourse(this);
            pendingStudents.remove(student);
        }
    }
    public void addInstructor(Teacher teacher, LessonType type) {
        instructors.putIfAbsent(type, new ArrayList<>());
        instructors.get(type).add(teacher);
    }

    public List<Student> getStudents() {
        return students;
    }

    public List<Student> getPendingStudents() {
        return pendingStudents;
    }

    public int getCredits() {
        return credits;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}