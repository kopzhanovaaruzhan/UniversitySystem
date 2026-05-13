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
            Map<Semester, List<Enrollment>> bySemester = new LinkedHashMap<>();
            for (Enrollment e : registrations) {
                bySemester.computeIfAbsent(e.getSemester(), _ -> new ArrayList<>()).add(e);
            }

            double totalWeightedGpa = 0; 
            int totalCredits = 0;

            for (Map.Entry<Semester, List<Enrollment>> entry : bySemester.entrySet()) {
                System.out.println("\n  ── " + entry.getKey() + " ──");
                for (Enrollment e : entry.getValue()) {
                    System.out.printf("  %-30s %d cr.%n", e.getCourse().getName(), e.getCourse().getCredits());
                    
                    if (e.hasMark()) {
                        System.out.println("    " + e.getMark().toString());
                        totalWeightedGpa += (e.getMark().convertToGpa() * e.getCourse().getCredits());
                        totalCredits += e.getCourse().getCredits();
                    } else {
                        System.out.println("    " + getLanguageMessage("No mark yet.", "Оценка не выставлена.", "Баға жоқ."));
                    }
                    
                    System.out.printf("    " + getLanguageMessage("Attendance: ", "Посещаемость: ", "Қатысу: ") + "%.0f%%%n",
                            e.getAttendancePercent());
                }
            }

            System.out.println("──────────────────────────────────────────");
            
            if (totalCredits > 0) {
                this.gpa = totalWeightedGpa / totalCredits;
                System.out.printf(getLanguageMessage("Total GPA: ", "Общий ГПА: ", "Жалпы ГПА: ") + "%.2f%n", this.gpa);
            } else {
                System.out.println(getLanguageMessage("GPA: N/A", "ГПА: Н/Д", "ГПА: Жоқ"));
            }
        }
        System.out.println("══════════════════════════════════════════\n");
    }


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

    public void viewCourseTeachers(Course course) {
        System.out.println("\n" + getLanguageMessage("Teachers of ", "Преподаватели курса ", "Оқытушылар: ") + course.getName() + ":");
        if (course.getTeachers().isEmpty()) {
            System.out.println("  " + getLanguageMessage("None assigned.", "Не назначены.", "Тағайындалмаған."));
        } else {
            course.getTeachers().forEach(t ->
                System.out.println("  - " + t.getName() + " (" + t.getType() + ")"));
        }
    }

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



