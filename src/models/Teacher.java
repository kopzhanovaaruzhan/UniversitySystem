package models;

import enums.Semester;
import enums.TeacherType;

public class Teacher extends Employee {
    private static final long serialVersionUID = 1L;

    private TeacherType type;
    private int hIndex; // для проверки при назначении научрука

    public Teacher(String id, String name, String login, String password,
                   double salary, TeacherType type) {
        super(id, name, login, password, salary);
        this.type = type;
        this.hIndex = 0;
    }

    public Teacher(String id, String name, String login, String password,
                   double salary, TeacherType type, int hIndex) {
        this(id, name, login, password, salary, type);
        this.hIndex = hIndex;
    }

    // ─── Оценка через Enrollment ──────────────────────────────────────────────

    /**
     * Поставить оценку студенту.
     * Ищет нужный Enrollment и задаёт ему Mark.
     */
    public void putMark(Student student, Course course, Semester semester, Mark mark) {
        Enrollment enrollment = course.findEnrollment(student, semester);
        if (enrollment == null) {
            System.out.println("Student " + student.getName() +
                " is not enrolled in " + course.getName() + " [" + semester + "]");
            return;
        }
        enrollment.setMark(mark);
        System.out.println("Mark set: " + student.getName() +
            " | " + course.getName() + " [" + semester + "] | " + mark);
    }

    // ─── Посещаемость через Enrollment ───────────────────────────────────────

    /**
     * Отметить посещаемость.
     * Учитель получает Enrollment и вызывает этот метод.
     */
    public void markAttendance(Enrollment enrollment, int lessonNumber, boolean present) {
        enrollment.markAttendance(lessonNumber, present);
        System.out.println("Attendance: lesson " + lessonNumber +
            " → " + enrollment.getStudent().getName() +
            " → " + (present ? "present" : "absent"));
    }

    // ─── Прочее ───────────────────────────────────────────────────────────────

    public void createComplaint() {
        System.out.println(getName() + " submitted a complaint.");
    }

    public void viewInfo() {
        System.out.println("Teacher: " + getName());
        System.out.println("Type:    " + type);
        System.out.println("Salary:  " + getSalary());
        System.out.println("H-Index: " + hIndex);
    }

    public TeacherType getType()      { return type; }
    public void setType(TeacherType t){ this.type = t; }
    public int getHIndex()            { return hIndex; }
    public void setHIndex(int h)      { this.hIndex = h; }

    @Override
    public String toString() {
        return String.format("Teacher[Name='%s', Type=%s, H-Index=%d]", getName(), type, hIndex);
    }
}



