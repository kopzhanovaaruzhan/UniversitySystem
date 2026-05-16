package models;

import enums.*;
import exceptions.CourseRegistrationException;
import java.util.*;

public class Student extends User {
    private static final long serialVersionUID = 1L;

    private String studentID;
    private double gpa;
    private int yearOfStudy;
    private Faculty faculty;
    private List<Enrollment> registrations = new ArrayList<>();
    private Map<String, Integer> teacherRatings;

    public Student(String id, String name, String login, String password,
                   Faculty faculty, int yearOfStudy) {
        super(id, name, login, password);
        this.studentID = id;
        this.faculty = faculty;
        this.yearOfStudy = yearOfStudy;
        this.registrations = new ArrayList<>();
        this.teacherRatings = new HashMap<>();
    }

    public Student(String id, String name, String login, String password) {
        this(id, name, login, password, Faculty.SITE, 1);
    }


    public Enrollment registerForCourse(Course course, Semester semester) throws CourseRegistrationException {
        if (course.isEnrolled(this, semester)) {
            throw new CourseRegistrationException("Already enrolled in: " + course.getName());
        }
        if (course.getFailCount(this) >= 3) {
            throw new CourseRegistrationException("Failed 3 times already: " + course.getName());
        }

        int currentCredits = getTotalCredits(semester);
        if (currentCredits + course.getCredits() > 21) {
            throw new CourseRegistrationException("Limit exceeded! Current: " + currentCredits);
        }

        if (course.isFull(semester)) {
            throw new CourseRegistrationException("Course is full: " + course.getName());
        }

        Enrollment enrollment = new Enrollment(course, this, semester);
        registrations.add(enrollment);
        course.addEnrollment(enrollment);

        return enrollment;
    }
    
    
    public Map<Semester, List<Enrollment>> getTranscriptData() {
        Map<Semester, List<Enrollment>> bySemester = new LinkedHashMap<>();
        for (Enrollment e : registrations) {
            bySemester.computeIfAbsent(e.getSemester(), _ -> new ArrayList<>()).add(e);
        }
        return bySemester;
    }

    public boolean rateTeacherLogic(Teacher teacher, Course course, int rating) {
        if (rating < 1 || rating > 5) return false;
        
        boolean enrolled = registrations.stream().anyMatch(e -> e.getCourse().equals(course));
        if (!enrolled || !course.getTeachers().contains(teacher)) return false;
        
        teacherRatings.put(teacher.getId(), rating);
        return true;
    }


    public int getTotalCredits(Semester semester) {
        return registrations.stream()
                .filter(e -> e.getSemester() == semester)
                .mapToInt(e -> e.getCourse().getCredits()).sum();
    }

    public Enrollment findEnrollment(Course course, Semester semester) {
        return registrations.stream()
                .filter(e -> e.getCourse().equals(course) && e.getSemester() == semester)
                .findFirst().orElse(null);
    }

    public void recalculateGpa() {
        OptionalDouble avg = registrations.stream()
                .filter(Enrollment::hasMark)
                .mapToDouble(e -> e.getMark().convertToGpa())
                .average();
        gpa = avg.orElse(0.0);
    }


    public String getStudentID()                    { return studentID; }
    public double getGpa()                          { return gpa; }
    public int getYearOfStudy()                     { return yearOfStudy; }
    public void setYearOfStudy(int y)               { this.yearOfStudy = y; }
    public Faculty getFaculty()                     { return faculty; }
    public void setFaculty(Faculty f)               { this.faculty = f; }
    public List<Enrollment> getRegistrations()      { return registrations; }
    public Map<String, Integer> getTeacherRatings() { return teacherRatings; }

    @Override
    public String toString() {
        return String.format("Student[ID='%s', Name='%s', GPA=%.2f, Year=%d, Faculty=%s]",
                studentID, getName(), gpa, yearOfStudy, faculty);
    }
}



