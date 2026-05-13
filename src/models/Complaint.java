package models;

import java.io.Serializable;
import java.time.LocalDateTime;

import enums.UrgencyLevel;
public class Complaint implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Teacher teacher;
    private Student student;
    private String text;
    private UrgencyLevel urgencyLevel;
    private LocalDateTime date;

    public Complaint(Teacher teacher, Student student, String text, UrgencyLevel urgencyLevel) {
        this.teacher = teacher;
        this.student = student;
        this.text = text;
        this.urgencyLevel = urgencyLevel;
        this.date = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return String.format("[%s] Complaint from %s on student %s. Urgency: %s. Text: %s",
                date.toString().substring(0, 16),
                teacher.getName(), 
                student.getName(), 
                urgencyLevel, 
                text);
    }
}