package models;

import enums.Faculty;
import enums.Language;
import enums.Semester;
import exceptions.CourseRegistrationException;
import java.util.*;

public class Student extends User {
    private static final long serialVersionUID = 1L;

    private String studentID;
    private double gpa;
    private int yearOfStudy;
    private Faculty faculty;

    /** Все зачисления студента — каждое = один курс в одном семестре */
    private List<Enrollment> registrations;

    /** Оценки учителей: teacherID → рейтинг 1–5 */
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

    /** Конструктор без faculty/year — для совместимости с Admin.createUser */
    public Student(String id, String name, String login, String password) {
        this(id, name, login, password, Faculty.SITE, 1);
    }

    // ─── Регистрация на курс ──────────────────────────────────────────────────

    /**
     * Записаться на курс в заданном семестре.
     * Правила: не более 21 кредита в семестре, не более 3 провалов, курс не переполнен.
     *
     * @return созданный Enrollment — чтобы учитель мог потом работать с ним
     */
    public Enrollment registerForCourse(Course course, Semester semester)
            throws CourseRegistrationException {

        if (course.isEnrolled(this, semester))
            throw new CourseRegistrationException(getLanguageMessage(
                "Already enrolled in: ", "Вы уже записаны: ", "Тіркелгенсіз: ") + course.getName());

        int fails = course.getFailCount(this);
        if (fails >= 3)
            throw new CourseRegistrationException(getLanguageMessage(
                "Failed 3 times already: ", "Провалено 3 раза: ", "3 рет тапсырылмаған: ") + course.getName());

        int currentCredits = getTotalCredits(semester);
        if (currentCredits + course.getCredits() > 21)
            throw new CourseRegistrationException(getLanguageMessage(
                "Credit limit exceeded! Current: ", "Превышен лимит кредитов! Сейчас: ",
                "Кредит шегі асты! Қазір: ") + currentCredits + " + " + course.getCredits() + " > 21");

        if (course.isFull(semester))
            throw new CourseRegistrationException(getLanguageMessage(
                "Course is full: ", "Курс переполнен: ", "Курс толы: ") + course.getName());

        Enrollment enrollment = new Enrollment(course, this, semester);
        registrations.add(enrollment);
        course.addEnrollment(enrollment);

        System.out.println(getLanguageMessage(
            "✓ Registered for: ", "✓ Записан на: ", "✓ Тіркелді: ") +
            course.getName() + " [" + semester + "]");
        return enrollment;
    }

    // ─── Транскрипт ───────────────────────────────────────────────────────────

    public void viewTranscript() {
        System.out.println("\n══════════════════════════════════════════");
        System.out.println(getLanguageMessage("TRANSCRIPT", "ТРАНСКРИПТ", "ТРАНСКРИПТ"));
        System.out.println(getName() + "  |  ID: " + studentID +
                "  |  " + getLanguageMessage("Year ", "Курс ", "Курс ") + yearOfStudy +
                "  |  " + faculty);
        System.out.println("──────────────────────────────────────────");

        if (registrations.isEmpty()) {
            System.out.println(getLanguageMessage("No enrollments.", "Нет зачислений.", "Тіркелу жоқ."));
        } else {
            // Группируем по семестру
            Map<Semester, List<Enrollment>> bySemester = new LinkedHashMap<>();
            for (Enrollment e : registrations)
                bySemester.computeIfAbsent(e.getSemester(), k -> new ArrayList<>()).add(e);

            double totalGpa = 0; int graded = 0;
            for (Map.Entry<Semester, List<Enrollment>> entry : bySemester.entrySet()) {
                System.out.println("\n  ── " + entry.getKey() + " ──");
                for (Enrollment e : entry.getValue()) {
                    System.out.printf("  %-30s %d cr.%n", e.getCourse().getName(), e.getCourse().getCredits());
                    if (e.hasMark()) {
                        System.out.println("    " + e.getMark());
                        totalGpa += e.getMark().convertToGpa();
                        graded++;
                    } else {
                        System.out.println("    " + getLanguageMessage("No mark yet.", "Оценка не выставлена.", "Баға жоқ."));
                    }
                    System.out.printf("    " + getLanguageMessage("Attendance: ", "Посещаемость: ", "Қатысу: ") + "%.0f%%%n",
                            e.getAttendancePercent());
                }
            }
            System.out.println("──────────────────────────────────────────");
            if (graded > 0) {
                gpa = totalGpa / graded;
                System.out.printf(getLanguageMessage("GPA: ", "ГПА: ", "ГПА: ") + "%.2f%n", gpa);
            }
        }
        System.out.println("══════════════════════════════════════════\n");
    }

    // ─── Просмотр оценок ──────────────────────────────────────────────────────

    public void viewMarks() {
        System.out.println("\n" + getLanguageMessage("=== Marks ===", "=== Оценки ===", "=== Бағалар ==="));
        if (registrations.isEmpty()) {
            System.out.println(getLanguageMessage("No enrollments.", "Нет зачислений.", "Тіркелу жоқ.")); return;
        }
        for (Enrollment e : registrations) {
            System.out.printf("[%-6s] %-28s  %s%n",
                    e.getSemester(), e.getCourse().getName(),
                    e.hasMark() ? e.getMark().toString() :
                        getLanguageMessage("No mark yet", "Нет оценки", "Баға жоқ"));
        }
    }

    // ─── Просмотр учителей курса ─────────────────────────────────────────────

    public void viewCourseTeachers(Course course) {
        System.out.println("\n" + getLanguageMessage("Teachers of ", "Преподаватели курса ", "Оқытушылар: ") + course.getName() + ":");
        if (course.getTeachers().isEmpty()) {
            System.out.println("  " + getLanguageMessage("None assigned.", "Не назначены.", "Тағайындалмаған."));
        } else {
            course.getTeachers().forEach(t ->
                System.out.println("  - " + t.getName() + " (" + t.getType() + ")"));
        }
    }

    // ─── Рейтинг учителя ─────────────────────────────────────────────────────

    public void rateTeacher(Teacher teacher, Course course, int rating) {
        if (rating < 1 || rating > 5) {
            System.out.println(getLanguageMessage("Rating must be 1-5.", "Оценка от 1 до 5.", "Рейтинг 1-ден 5-ке дейін.")); return;
        }
        boolean enrolled = registrations.stream().anyMatch(e -> e.getCourse().equals(course));
        if (!enrolled) {
            System.out.println(getLanguageMessage("Not enrolled in this course.", "Вы не записаны на этот курс.", "Бұл курсқа тіркелмегенсіз.")); return;
        }
        if (!course.getTeachers().contains(teacher)) {
            System.out.println(getLanguageMessage("Teacher not in this course.", "Преподаватель не ведёт этот курс.", "Оқытушы бұл курста жоқ.")); return;
        }
        teacherRatings.put(teacher.getId(), rating);
        System.out.println(getLanguageMessage("Rated ", "Оценили ", "Бағаладыңыз: ") + teacher.getName() + " → " + rating + "/5");
    }

    // ─── Вспомогательные ─────────────────────────────────────────────────────

    /** Сумма кредитов студента в одном семестре */
    public int getTotalCredits(Semester semester) {
        return registrations.stream()
                .filter(e -> e.getSemester() == semester)
                .mapToInt(e -> e.getCourse().getCredits()).sum();
    }

    /** Найти конкретный enrollment */
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

    // ─── Геттеры/Сеттеры ─────────────────────────────────────────────────────

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



