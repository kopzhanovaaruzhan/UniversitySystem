package models;

import enums.Semester;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Enrollment — связующее звено между Student и Course в конкретном семестре.
 *
 * Course = "OOP"  →  просто курс, без оценок
 * Enrollment = "Spring OOP, Алиса"  →  хранит Mark + посещаемость этого студента
 *
 * Именно Enrollment хранит баллы (att1, att2, final) через Mark.
 */
public class Enrollment implements Serializable {
    private static final long serialVersionUID = 1L;

    private Course course;
    private Student student;
    private Semester semester;

    private Mark mark;                        // null пока учитель не выставил
    private Map<Integer, Boolean> attendance; // номер урока → пришёл?

    public Enrollment(Course course, Student student, Semester semester) {
        this.course = course;
        this.student = student;
        this.semester = semester;
        this.attendance = new HashMap<>();
    }

    // ─── Оценка ───────────────────────────────────────────────────────────────

    public void setMark(Mark mark) {
        this.mark = mark;
    }

    public Mark getMark() { return mark; }

    public boolean hasMark() { return mark != null; }

    // ─── Посещаемость ─────────────────────────────────────────────────────────

    /**
     * Учитель вызывает этот метод для отметки урока.
     * @param lessonNumber номер урока (1, 2, 3, ...)
     * @param present      true = пришёл, false = не пришёл
     */
    public void markAttendance(int lessonNumber, boolean present) {
        attendance.put(lessonNumber, present);
    }

    /** Процент посещаемости: 0.0 – 100.0 */
    public double getAttendancePercent() {
        if (attendance.isEmpty()) return 0.0;
        long attended = attendance.values().stream().filter(v -> v).count();
        return (attended * 100.0) / attendance.size();
    }

    // ─── Геттеры ──────────────────────────────────────────────────────────────

    public Course getCourse()              { return course; }
    public Student getStudent()            { return student; }
    public Semester getSemester()          { return semester; }
    public Map<Integer, Boolean> getAttendance() { return attendance; }

    @Override
    public String toString() {
        String markStr = mark != null ? mark.toString() : "No mark yet";
        return String.format("[%s] %s | %s | Attendance: %.0f%%",
                semester, course.getName(), markStr, getAttendancePercent());
    }
}



