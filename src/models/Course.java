package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
//import java.util.Map;

public class Course implements Serializable {
    private static final long serialVersionUID = 1L;

    private String courseCode;
    private String name;
    private int credits;
    
    private int maxStudent;
    //private Map<Student, List<Mark>> gradebook;
    
    private List<Student> students;
    private List<Teacher> teachers;

    public Course(String name) {
        this.name = name;
        this.students = new ArrayList<>();
        this.setTeachers(new ArrayList<>());
    }

    public Course(String courseCode, String name, int credits) {
        this(name);
        this.courseCode = courseCode;
        this.credits = credits;
    }

    public void addStudent(Student s) {
    		if (!students.contains(s) && students.size() < maxStudent) {
            students.add(s);
        } else {
            System.out.println("Course is full");
        }
    }

    public String getName() { return name; }
    public String getCourseCode() { return courseCode; }
    public int getCredits() { return credits; }
    public List<Student> getStudents() { return students; }

    @Override
    public String toString() {
        return String.format("%s [%s] (%d credits)", name, courseCode != null ? courseCode : "N/A", credits);
    }

	public List<Teacher> getTeachers() {
		return teachers;
	}

	public void setTeachers(List<Teacher> teachers) {
		this.teachers = teachers;
	}
}