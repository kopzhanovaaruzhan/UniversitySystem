package models;
import java.util.*;
public class Student extends User {
    private List<Course> courses = new ArrayList<>();
    private Map<Course, Mark> marks = new HashMap<>();
    private int credits = 0;

    public Student(String id, String name, String login, String password) {
        super(id, name, login, password);
    }

    public void addCourse(Course course) {
        courses.add(course);
        credits += course.getCredits();
    }

    public void addMark(Course course, Mark mark) {
        marks.put(course, mark);
    }

    public Map<Course, Mark> getMarks() {
        return marks;
    }

    public int getCredits() {
        return credits;
    }
}
