package models;

import enums.*;
import java.util.*;

public class Teacher extends Employee {
    private static final long serialVersionUID = 1L;

    private TeacherType type;
    private int hIndex; 
    private List<Complaint> complaints = new ArrayList<>();
    private boolean researcher; // Профессора ВСЕГДА исследователи по ТЗ

    public Teacher(String id, String name, String login, String password,
                   double salary, TeacherType type) {
        super(id, name, login, password, salary);
        this.type = type;
        this.hIndex = 0;
        
        // Автоматическое назначение исследователем согласно PDF 
        if (type == TeacherType.PROFESSOR) {
            this.researcher = true;
        }
    }

    // ─── Оценка через Enrollment (Интеграция версий) ──────────────────────────

    public void putMark(Student student, Course course, Semester semester, Mark mark) {
        Enrollment enrollment = course.findEnrollment(student, semester);
        if (enrollment == null) {
            System.out.println(getLanguageMessage(
                "Student not enrolled in this course.",
                "Студент не записан на этот курс.",
                "Студент бұл курсқа тіркелмеген."
            ));
            return;
        }
        enrollment.setMark(mark);
        System.out.println(getLanguageMessage(
            "Mark added successfully.",
            "Оценка успешно поставлена.",
            "Баға сәтті қойылды."
        ));
    }

    // ─── Жалобы (Требование PDF: complaints with urgency level) ──────────────

    public Complaint createComplaint(Student student, String text, UrgencyLevel level) {
        //  Жалоба декану с уровнем срочности
        Complaint complaint = new Complaint(this, student, text, level);
        complaints.add(complaint);

        System.out.println(getLanguageMessage(
            "Complaint sent to dean. Urgency: " + level,
            "Жалоба отправлена декану. Срочность: " + level,
            "Шағым деканға жіберілді. Шұғылдығы: " + level
        ));
        return complaint;
    }

    // ─── Посещаемость ─────────────────────────────────────────────────────────

    public void markAttendance(Enrollment enrollment, int lessonNumber, boolean present) {
        enrollment.markAttendance(lessonNumber, present);
    }

    // ─── Исследовательская деятельность ───────────────────────────────────────

    public boolean isResearcher() {
        return researcher;
    }

    public void setResearcher(boolean researcher) {
        this.researcher = researcher;
    }

    public int getHIndex() { return hIndex; }
    public void setHIndex(int h) { this.hIndex = h; }
    public TeacherType getType() { return type; }

    public void viewInfo() {
        System.out.println(getLanguageMessage(
            "Teacher: " + getName() + " | Type: " + type + " | Researcher: " + researcher,
            "Преподаватель: " + getName() + " | Тип: " + type + " | Исследователь: " + researcher,
            "Оқытушы: " + getName() + " | Түрі: " + type + " | Зерттеуші: " + researcher
        ));
    }
}






