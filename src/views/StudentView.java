package views;

import core.DBContext;
import enums.Language;
import enums.Semester;
import exceptions.CourseRegistrationException;
import models.*;

import java.io.IOException;
import java.util.List;

public class StudentView extends BaseView {

    public static void show(Student student) throws IOException {
        boolean running = true;
        while (running) {
            printMenu(student);
            String choice = reader.readLine();
            switch (choice) {
                case "1": viewCourses(student);    break;
                case "2": registerCourse(student); break;
                case "3": student.viewTranscript();break;
                case "4": student.viewMarks();     break;
                case "5": viewTeachers(student);   break;
                case "6": rateTeacher(student);    break;
                case "7": switchLang(student);     break;
                case "8":
                    if (student instanceof GraduateStudent)
                        graduateMenu((GraduateStudent) student);
                    else System.out.println("Not available.");
                    break;
                case "0": running = false; break;
                default:  System.out.println("Unknown option."); break;
            }
        }
    }

    // ─── Меню ─────────────────────────────────────────────────────────────────

    private static void printMenu(Student student) {
        String lang = student.getCurrentLanguage().name();
        System.out.println("\n╔══════════════════════════════════════╗");
        System.out.printf ("║  %-36s║%n", student.getName() + " [" + student.getFaculty() + "]");
        System.out.println("╠══════════════════════════════════════╣");
        option("1", student.getLanguageMessage("View available courses", "Доступные курсы", "Қол жетімді курстар"));
        option("2", student.getLanguageMessage("Register for a course",  "Записаться на курс",  "Курсқа тіркелу"));
        option("3", student.getLanguageMessage("View transcript",        "Транскрипт",          "Транскрипт"));
        option("4", student.getLanguageMessage("View marks",             "Оценки",              "Бағалар"));
        option("5", student.getLanguageMessage("View course teachers",   "Учителя курса",       "Курс оқытушылары"));
        option("6", student.getLanguageMessage("Rate a teacher",         "Оценить учителя",     "Оқытушыны бағалау"));
        option("7", student.getLanguageMessage("Language: ", "Язык: ", "Тіл: ") + lang);
        if (student instanceof GraduateStudent)
            option("8", student.getLanguageMessage("Diploma projects", "Дипломные работы", "Диплом жұмыстары"));
        option("0", student.getLanguageMessage("Logout", "Выйти", "Шығу"));
        System.out.println("╚══════════════════════════════════════╝");
        System.out.print(student.getLanguageMessage("Choice: ", "Выбор: ", "Таңдау: "));
    }

    private static void option(String key, String label) {
        System.out.printf("║  %s - %-33s║%n", key, label);
    }

    // ─── Опции ────────────────────────────────────────────────────────────────

    /** 1 — Список всех курсов */
    private static void viewCourses(Student student) {
        System.out.println("\n" + student.getLanguageMessage(
            "=== Available Courses ===", "=== Доступные курсы ===", "=== Қол жетімді курстар ==="));
        System.out.printf("  %-8s %-30s %s%n", "Code", "Name", "Credits");
        System.out.println("  " + "─".repeat(48));
        for (Course c : DBContext.getCourses())
            System.out.printf("  %-8s %-30s %d cr.%n", c.getCourseCode(), c.getName(), c.getCredits());
    }

    /** 2 — Запись на курс */
    private static void registerCourse(Student student) throws IOException {
        viewCourses(student);
        System.out.print("\n" + student.getLanguageMessage(
            "Course code (0=cancel): ", "Код курса (0=отмена): ", "Курс коды (0=бас тарту): "));
        String code = reader.readLine().trim();
        if ("0".equals(code)) return;

        Course found = DBContext.getCourses().stream()
                .filter(c -> code.equalsIgnoreCase(c.getCourseCode()))
                .findFirst().orElse(null);
        if (found == null) { System.out.println("Course not found."); return; }

        System.out.println("Semester:  1-FALL  2-SPRING  3-SUMMER");
        System.out.print("Choice: ");
        Semester sem;
        switch (reader.readLine().trim()) {
            case "1": sem = Semester.FALL;   break;
            case "2": sem = Semester.SPRING; break;
            case "3": sem = Semester.SUMMER; break;
            default: System.out.println("Invalid semester."); return;
        }

        try {
            student.registerForCourse(found, sem);
            DBContext.save();
        } catch (CourseRegistrationException e) {
            System.out.println("[" + student.getLanguageMessage("Error", "Ошибка", "Қате") +
                "] " + e.getMessage());
        }
    }

    /** 5 — Учителя курса */
    private static void viewTeachers(Student student) throws IOException {
        List<Enrollment> regs = student.getRegistrations();
        if (regs.isEmpty()) { System.out.println("No enrollments."); return; }
        printEnrollmentList(regs);
        System.out.print("Number: ");
        try {
            int idx = Integer.parseInt(reader.readLine().trim()) - 1;
            if (idx >= 0 && idx < regs.size())
                student.viewCourseTeachers(regs.get(idx).getCourse());
        } catch (NumberFormatException e) { System.out.println("Invalid input."); }
    }

    /** 6 — Оценить учителя */
    private static void rateTeacher(Student student) throws IOException {
        List<Enrollment> regs = student.getRegistrations();
        if (regs.isEmpty()) { System.out.println("No enrollments."); return; }
        printEnrollmentList(regs);
        System.out.print("Course number: ");
        int cIdx;
        try { cIdx = Integer.parseInt(reader.readLine().trim()) - 1; }
        catch (NumberFormatException e) { System.out.println("Invalid."); return; }
        if (cIdx < 0 || cIdx >= regs.size()) { System.out.println("Invalid."); return; }

        Course course = regs.get(cIdx).getCourse();
        List<Teacher> teachers = course.getTeachers();
        if (teachers.isEmpty()) { System.out.println("No teachers assigned."); return; }

        for (int i = 0; i < teachers.size(); i++)
            System.out.println((i + 1) + ". " + teachers.get(i).getName() + " (" + teachers.get(i).getType() + ")");
        System.out.print("Teacher number: ");
        int tIdx;
        try { tIdx = Integer.parseInt(reader.readLine().trim()) - 1; }
        catch (NumberFormatException e) { System.out.println("Invalid."); return; }
        if (tIdx < 0 || tIdx >= teachers.size()) { System.out.println("Invalid."); return; }

        System.out.print("Rating 1-5: ");
        try {
            int rating = Integer.parseInt(reader.readLine().trim());
            student.rateTeacher(teachers.get(tIdx), course, rating);
        } catch (NumberFormatException e) { System.out.println("Invalid."); }
    }

    /** 7 — Сменить язык */
    private static void switchLang(Student student) throws IOException {
        System.out.print("1-ENG  2-RUS  3-KAZ > ");
        switch (reader.readLine().trim()) {
            case "1": student.switchLanguage(Language.ENG); break;
            case "2": student.switchLanguage(Language.RUS); break;
            case "3": student.switchLanguage(Language.KAZ); break;
        }
    }

    /** 8 — Меню аспиранта */
    private static void graduateMenu(GraduateStudent gs) throws IOException {
        System.out.println("\n=== Graduate: " + gs.getName() + " | " + gs.getType() + " ===");
        System.out.println("Supervisor: " +
                (gs.getResearchSupervisor() != null ? gs.getResearchSupervisor().getName() : "None"));
        System.out.println("1 - View diploma projects");
        System.out.println("2 - Add diploma project");
        System.out.println("0 - Back");
        System.out.print("Choice: ");
        switch (reader.readLine().trim()) {
            case "1": gs.viewDiplomaProjects(); break;
            case "2":
                System.out.print("Title: ");
                String t = reader.readLine().trim();
                if (!t.isEmpty()) gs.addDiplomaProject(t);
                break;
        }
    }

    // ─── Util ─────────────────────────────────────────────────────────────────

    private static void printEnrollmentList(List<Enrollment> regs) {
        System.out.println("\nYour courses:");
        for (int i = 0; i < regs.size(); i++)
            System.out.printf("  %d. [%-6s] %s%n",
                    i + 1, regs.get(i).getSemester(), regs.get(i).getCourse().getName());
    }
}



