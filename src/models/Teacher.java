package models;

import enums.*;
import java.util.*;

public class Teacher extends Employee {
    private static final long serialVersionUID = 1L;

    private TeacherType type;
    private int hIndex; 
    private List<Complaint> complaints = new ArrayList<>();
    private boolean researcher;

    public Teacher(String id, String name, String login, String password,
                   double salary, TeacherType type) {
        super(id, name, login, password, salary);
        this.type = type;
        this.hIndex = 0;
        
        if (type == TeacherType.PROFESSOR) {
            this.researcher = true;
        }
    }

    public void putMark(Student student, Course course, Semester semester, Mark mark) {
        Enrollment enrollment = course.findEnrollment(student, semester);
        if (enrollment != null) {
            enrollment.setMark(mark);
        }
    }

    public Complaint createComplaint(Student student, String text, UrgencyLevel level) {
        Complaint complaint = new Complaint(this, student, text, level);
        complaints.add(complaint);
        return complaint;
    }

    public void markAttendance(Enrollment enrollment, int lessonNumber, boolean present) {
        enrollment.markAttendance(lessonNumber, present);
    }

    public void setResearcher(boolean researcher) {
        this.researcher = researcher;
    }

    public int getHIndex() { return hIndex; }
    public void setHIndex(int h) { this.hIndex = h; }
    public TeacherType getType() { return type; }
    public boolean isResearcher() { return researcher; }

    public void viewInfo() {
        System.out.println(getLanguageMessage(
            "Teacher: " + getName() + " | Type: " + type + " | Researcher: " + researcher,
            "Преподаватель: " + getName() + " | Тип: " + type + " | Исследователь: " + researcher,
            "Оқытушы: " + getName() + " | Түрі: " + type + " | Зерттеуші: " + researcher
        ));
    }
}






