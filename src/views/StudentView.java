package views;

import core.DBContext;
import enums.Semester;
import exceptions.CourseRegistrationException;
import models.*;
import controllers.CourseController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class StudentView extends BaseView {

    public static void show(Student student) throws IOException {
        boolean running = true;
        while (running) {
            printMenu(student);
            String choice = reader.readLine().trim();
            switch (choice) {
                case "1" -> viewCourses(student);
                case "2" -> registerCourseMenu(student);
                case "3" -> viewTranscript(student);
                case "4" -> viewMarks(student);
                case "5" -> viewTeachers(student);
                case "6" -> rateTeacherMenu(student);
                case "7" -> switchLang(student); // Из BaseView
                case "8" -> {
                    if (student instanceof GraduateStudent)
                        graduateMenu((GraduateStudent) student);
                    else System.out.println("Not available.");
                }
                case "0" -> running = false;
                default  -> System.out.println("Unknown option.");
            }
        }
    }

    private static void printMenu(Student student) {
        String lang = student.getCurrentLanguage().name();
        System.out.println("\n╔═══════════════════════════════════════╗");
        System.out.printf ("║  %-37s║%n", student.getName() + " [" + student.getFaculty() + "]");
        System.out.println("╠═══════════════════════════════════════╣");
        option("1", student.getLanguageMessage("View Available Courses", "Доступные курсы", "Qol jetіmdі kurstar"));
        option("2", student.getLanguageMessage("Register for a Course",  "Записаться на курс",  "Kursqa tіrkelu"));
        option("3", student.getLanguageMessage("View Transcript",        "Транскрипт",          "Transcript"));
        option("4", student.getLanguageMessage("View Marks",             "Оценки",              "Bağalar"));
        option("5", student.getLanguageMessage("View Course Teachers",   "Учителя курса",       "Kurs oqytuşylary"));
        option("6", student.getLanguageMessage("Rate a Teacher",         "Оценить учителя",     "Oqytuşyny bağalau"));
        option("7", student.getLanguageMessage("Language: ", "Язык: ", "Tіl: ") + lang);
        if (student instanceof GraduateStudent) {
            option("8", student.getLanguageMessage("Diploma Projects", "Дипломные работы", "Diplom jumystary"));
        }
        option("0", student.getLanguageMessage("Logout", "Выйти", "Şyğu"));
        System.out.println("╚═══════════════════════════════════════╝");
        System.out.print(student.getLanguageMessage("Choice: ", "Выбор: ", "Tañdau: "));
    }

    private static void option(String key, String label) {
        System.out.printf("║  %s - %-33s║%n", key, label);
    }

    private static void viewCourses(Student student) {
        System.out.println("\n" + student.getLanguageMessage("=== Available Courses ===", "=== Доступные курсы ===", "=== Qol jetіmdі kurstar ==="));
        if (DBContext.getCourses().isEmpty()) {
            System.out.println("No courses available.");
            return;
        }
        System.out.printf("  %-8s %-30s %s%n", "Code", "Name", "Credits");
        System.out.println("  " + "─".repeat(48));
        for (Course c : DBContext.getCourses()) {
            System.out.printf("  %-8s %-30s %d cr.%n", c.getCourseCode(), c.getName(), c.getCredits());
        }
    }

    private static void registerCourseMenu(Student student) throws IOException {
        viewCourses(student);
        System.out.print("\n" + student.getLanguageMessage("Course code (0=cancel): ", "Код курса (0=отмена): ", "Kurs kody (0=bas tartu): "));
        String code = reader.readLine().trim();
        if ("0".equals(code) || code.isEmpty()) return;

        System.out.println("Semester: 1-FALL  2-SPRING  3-SUMMER");
        System.out.print("> ");
        Semester sem = switch (reader.readLine().trim()) {
            case "1" -> Semester.FALL;
            case "2" -> Semester.SPRING;
            case "3" -> Semester.SUMMER;
            default  -> null;
        };
        if (sem == null) { System.out.println("Invalid semester."); return; }

        try {
            // Передаем транзакцию в CourseController, ловим кастомный эксепшен!
            CourseController.registerStudent(student, code, sem);
            successMsg(student.getLanguageMessage("Successfully sent for approval!", "Успешно отправлено на одобрение менеджеру!", "Maqýldáýğa sáttі jіberіldі!"));
        } catch (CourseRegistrationException e) {
            System.out.println("[" + student.getLanguageMessage("Registration Error", "Ошибка регистрации", "Tіrkelu qatesі") + "] " + e.getMessage());
        }
    }

    private static void viewTranscript(Student student) {
        System.out.println("\n══════════════════════════════════════════");
        System.out.println(student.getLanguageMessage("TRANSCRIPT", "ТРАНСКРИПТ", "TRANSCRIPT"));
        System.out.println(student.getName() + "  |  ID: " + student.getStudentID() + "  |  " + student.getLanguageMessage("Year ", "Курс ", "Kurs ") + student.getYearOfStudy() + "  |  " + student.getFaculty());
        System.out.println("──────────────────────────────────────────");

        Map<Semester, List<Enrollment>> bySemester = student.getTranscriptData();
        if (bySemester.isEmpty()) {
            System.out.println(student.getLanguageMessage("No enrollments.", "Нет зачислений.", "Tіrkelu joq."));
            return;
        }

        double totalWeightedGpa = 0; int totalCredits = 0;
        for (Map.Entry<Semester, List<Enrollment>> entry : bySemester.entrySet()) {
            System.out.println("\n  ── " + entry.getKey() + " ──");
            for (Enrollment e : entry.getValue()) {
                System.out.printf("  %-30s %d cr.%n", e.getCourse().getName(), e.getCourse().getCredits());
                System.out.println("    " + (e.hasMark() ? e.getMark().toString() : student.getLanguageMessage("No mark yet.", "Оценка не выставлена.", "Bağa joq.")));
                System.out.printf("    " + student.getLanguageMessage("Attendance: ", "Посещаемость: ", "Qatysu: ") + "%.0f%%%n", e.getAttendancePercent());
                
                if (e.hasMark()) {
                    totalWeightedGpa += (e.getMark().convertToGpa() * e.getCourse().getCredits());
                    totalCredits += e.getCourse().getCredits();
                }
            }
        }
        System.out.println("──────────────────────────────────────────");
        if (totalCredits > 0) {
            System.out.printf(student.getLanguageMessage("Total GPA: ", "Общий ГПА: ", "Jalpy GPA: ") + "%.2f%n", (totalWeightedGpa / totalCredits));
        } else {
            System.out.println(student.getLanguageMessage("GPA: N/A", "ГПА: Н/Д", "GPA: Joq"));
        }
    }

    private static void viewMarks(Student student) {
        System.out.println("\n" + student.getLanguageMessage("=== Marks ===", "=== Оценки ===", "=== Bağalar ==="));
        if (student.getRegistrations().isEmpty()) {
            System.out.println(student.getLanguageMessage("No enrollments.", "Нет зачислений.", "Tіrkelu joq.")); 
            return;
        }
        for (Enrollment e : student.getRegistrations()) {
            System.out.printf("[%-6s] %-28s  %s%n", e.getSemester(), e.getCourse().getName(), e.hasMark() ? e.getMark().toString() : student.getLanguageMessage("No mark yet", "Нет оценки", "Bağa joq"));
        }
    }

    private static void viewTeachers(Student student) throws IOException {
        List<Enrollment> regs = student.getRegistrations();
        if (regs.isEmpty()) { System.out.println("No enrollments."); return; }
        
        System.out.println("\nYour courses:");
        for (int i = 0; i < regs.size(); i++) {
            System.out.printf("  %d. [%-6s] %s%n", i + 1, regs.get(i).getSemester(), regs.get(i).getCourse().getName());
        }
        System.out.print("Select number: ");
        try {
            int idx = Integer.parseInt(reader.readLine().trim()) - 1;
            if (idx >= 0 && idx < regs.size()) {
                Course course = regs.get(idx).getCourse();
                System.out.println("\n" + student.getLanguageMessage("Teachers of ", "Преподаватели курса ", "Oqytuşylar: ") + course.getName() + ":");
                if (course.getTeachers().isEmpty()) {
                    System.out.println("  None assigned.");
                } else {
                    course.getTeachers().forEach(t -> System.out.println("  - " + t.getName() + " (" + t.getType() + ")"));
                }
            }
        } catch (NumberFormatException e) { System.out.println("Invalid input."); }
    }

    private static void rateTeacherMenu(Student student) throws IOException {
        List<Enrollment> regs = student.getRegistrations();
        if (regs.isEmpty()) { System.out.println("No enrollments."); return; }
        
        for (int i = 0; i < regs.size(); i++) {
            System.out.printf("  %d. %s%n", i + 1, regs.get(i).getCourse().getName());
        }
        System.out.print("Select course number: ");
        try {
            int cIdx = Integer.parseInt(reader.readLine().trim()) - 1;
            Course course = regs.get(cIdx).getCourse();
            List<Teacher> teachers = course.getTeachers();
            
            if (teachers.isEmpty()) { System.out.println("No teachers assigned."); return; }

            for (int i = 0; i < teachers.size(); i++) {
                System.out.println((i + 1) + ". " + teachers.get(i).getName());
            }
            System.out.print("Select teacher number: ");
            int tIdx = Integer.parseInt(reader.readLine().trim()) - 1;

            System.out.print("Rating (1-5): ");
            int rating = Integer.parseInt(reader.readLine().trim());

            boolean success = CourseController.rateTeacher(student, teachers.get(tIdx), course, rating);
            if (success) {
                successMsg(student.getLanguageMessage("Thank you for your rating!", "Спасибо за оценку!", "Bağalağanýñyz úşіn raqmet!"));
            } else {
                System.out.println("Rating failed. Invalid range or context.");
            }
        } catch (Exception e) { System.out.println("Invalid input."); }
    }

    private static void graduateMenu(GraduateStudent gs) throws IOException {
        System.out.println("\n=== " + gs.getLanguageMessage("Graduate Workspace", "Кабинет послевузовского образования", "Postvuzdyq bіlіm beru") + " ===");
        System.out.println("Supervisor: " + (gs.getResearchSupervisor() != null ? gs.getResearchSupervisor().getName() : "None"));
        System.out.println("1 - " + gs.getLanguageMessage("View Diploma Projects", "Посмотреть дипломные работы", "Diplom jumystaryn kóru"));
        System.out.println("2 - " + gs.getLanguageMessage("Add Diploma Project", "Добавить тему дипломной", "Diplom taqyrybyn qosu"));
        System.out.print("> ");
        
        switch (reader.readLine().trim()) {
            case "1" -> {
                System.out.println("\n" + gs.getLanguageMessage("=== Diploma Projects ===", "=== Дипломные работы ===", "=== Diplom jumystary ==="));
                List<String> projects = gs.getDiplomaProjects();
                if (projects.isEmpty()) {
                    System.out.println(gs.getLanguageMessage("None yet.", "Пока нет.", "Ázіrge joq."));
                } else {
                    for (int i = 0; i < projects.size(); i++) {
                        System.out.println((i + 1) + ". " + projects.get(i));
                    }
                }
            }
            case "2" -> {
                System.out.print("Title: ");
                String title = reader.readLine().trim();
                if (!title.isEmpty()) {
                    gs.addDiplomaProjectLogic(title);
                    successMsg(gs.getLanguageMessage("Project topic saved!", "Тема сохранена!", "Taqyryp saqtaldy!"));
                    DBContext.save();
                }
            }
        }
    }
}



