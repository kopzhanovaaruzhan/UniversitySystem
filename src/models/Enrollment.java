package models;

import enums.Semester;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class Enrollment implements Serializable {
    private static final long serialVersionUID = 1L;

    private Course course;
    private Student student;
    private Semester semester;

    private Mark mark;                       
    private Map<Integer, Boolean> attendance;

    public Enrollment(Course course, Student student, Semester semester) {
        this.course = course;
        this.student = student;
        this.semester = semester;
        this.attendance = new HashMap<>();
    }


    public void setMark(Mark mark) {
        this.mark = mark;
    }

    public Mark getMark() { return mark; }

    public boolean hasMark() { return mark != null; }


    public void markAttendance(int lessonNumber, boolean present) {
        attendance.put(lessonNumber, present);
    }

    public double getAttendancePercent() {
        if (attendance.isEmpty()) return 0.0;
        long attended = attendance.values().stream().filter(v -> v).count();
        return (attended * 100.0) / attendance.size();
    }


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



