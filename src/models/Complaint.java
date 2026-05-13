package models;
import java.io.Serializable;
import java.time.LocalDateTime;
public class Complaint implements Serializable {
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

    public String toString() {
        return "Complaint from " + teacher.getName() +
                " about " + student.getName() +
                " | Level: " + urgencyLevel +
                " | Text: " + text;
    }
}
