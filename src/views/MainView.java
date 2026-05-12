package views;

import java.io.IOException;
import core.DBContext;
import enums.*;
import models.*;

public class MainView extends BaseView {
    private static User currentUser = null;
    private static Language currentSessionLang = Language.ENG;

    public static void run() throws IOException {
        welcomeMsg();
        while (true) {
            if (currentUser == null) showAuthMenu();
            else routeToRoleView();
        }
    }

    private static String sm(String eng, String rus, String kaz) {
        if (currentSessionLang == Language.RUS) return rus;
        if (currentSessionLang == Language.KAZ) return kaz;
        return eng;
    }

    // ─── Auth ─────────────────────────────────────────────────────────────────

    private static void showAuthMenu() throws IOException {
        System.out.println("\n1 - " + sm("Login","Войти","Кіру"));
        System.out.println("2 - " + sm("Exit","Выйти","Шығу"));
        String choice = reader.readLine();
        if ("1".equals(choice)) loginProcess();
        else { System.out.println(sm("Goodbye!","До свидания!","Сау болыңыз!")); DBContext.save(); System.exit(0); }
    }

    private static void loginProcess() throws IOException {
        System.out.print(sm("Username: ","Логин: ","Логин: "));
        String login = reader.readLine();
        System.out.print(sm("Password: ","Пароль: ","Пароль: "));
        String pass = reader.readLine();

        currentUser = DBContext.getUsers().stream()
                .filter(u -> u.login(login, pass)).findFirst().orElse(null);

        if (currentUser != null) {
            currentUser.switchLanguage(currentSessionLang);
            successMsg(currentUser.getLanguageMessage("Welcome","Добро пожаловать","Қош келдіңіз")
                + ", " + currentUser.getName() + "!");
        } else {
            System.out.println(sm("Error: Invalid credentials.","Ошибка: Неверные данные.","Қате мәліметтер."));
        }
    }

    // ─── Role Routing ─────────────────────────────────────────────────────────

    private static void routeToRoleView() throws IOException {
        if (currentUser instanceof Student) {
            StudentView.show((Student) currentUser);
            currentUser = null;
        } else if (currentUser instanceof Admin) {
            adminMenu();
        } else if (currentUser instanceof Teacher) {
            teacherMenu();
        } else {
            System.out.println("No menu for role: " + currentUser.getClass().getSimpleName());
            currentUser = null;
        }
    }

    // ─── Admin Menu ───────────────────────────────────────────────────────────

    private static void adminMenu() throws IOException {
        String lu = currentUser.getLanguageMessage("\nLogged in as Admin: ","Вы вошли как Admin: ","Admin: ");
        System.out.println(lu + currentUser.getName());
        System.out.println("1 - " + currentUser.getLanguageMessage("Register New User","Зарег. пользователя","Пайд. тіркеу"));
        System.out.println("2 - " + currentUser.getLanguageMessage("View All Users","Все пользователи","Барлық пайд."));
        System.out.println("3 - " + currentUser.getLanguageMessage("View All Courses","Все курсы","Барлық курстар"));
        System.out.println("4 - " + currentUser.getLanguageMessage("Switch Language","Сменить язык","Тілді ауыстыру")
            + " (" + currentUser.getCurrentLanguage() + ")");
        System.out.println("0 - " + currentUser.getLanguageMessage("Logout","Выйти","Шығу"));

        Admin admin = (Admin) currentUser;
        switch (reader.readLine()) {
            case "1":
                System.out.println("1-Student 2-Graduate 3-Teacher 4-Manager 5-TechSupport");
                String typeChoice = reader.readLine();
                String type;
                switch (typeChoice) {
                    case "1": type="student"; break; case "2": type="graduate"; break;
                    case "3": type="teacher"; break; case "4": type="manager";  break;
                    case "5": type="techsupport"; break;
                    default: System.out.println("Invalid."); return;
                }
                System.out.print("Name: ");    String name = reader.readLine();
                System.out.print("Login: ");   String log  = reader.readLine();
                System.out.print("Password: ");String pw   = reader.readLine();
                User nu = admin.createUser(type, "ID-"+(DBContext.getUsers().size()+1), name, log, pw);
                if (nu != null) { DBContext.addUser(nu); DBContext.save(); successMsg(name + " registered."); }
                break;
            case "2": DBContext.getUsers().forEach(System.out::println); break;
            case "3": DBContext.getCourses().forEach(System.out::println); break;
            case "4":
                System.out.print("1-ENG 2-RUS 3-KAZ > ");
                switch (reader.readLine()) {
                    case "1": currentSessionLang=Language.ENG; break;
                    case "2": currentSessionLang=Language.RUS; break;
                    case "3": currentSessionLang=Language.KAZ; break;
                }
                currentUser.switchLanguage(currentSessionLang);
                break;
            case "0": currentUser = null; break;
        }
    }

    // ─── Teacher Menu ─────────────────────────────────────────────────────────

    private static void teacherMenu() throws IOException {
        Teacher teacher = (Teacher) currentUser;
        System.out.println("\n[TEACHER] " + teacher.getName() + " (" + teacher.getType() + ")");
        System.out.println("1 - Put mark for student");
        System.out.println("2 - Mark attendance");
        System.out.println("3 - View my info");
        System.out.println("4 - Switch language (" + teacher.getCurrentLanguage() + ")");
        System.out.println("0 - Logout");

        switch (reader.readLine()) {
            case "1": teacherPutMark(teacher);      break;
            case "2": teacherAttendance(teacher);   break;
            case "3": teacher.viewInfo();            break;
            case "4":
                System.out.print("1-ENG 2-RUS 3-KAZ > ");
                switch (reader.readLine()) {
                    case "1": teacher.switchLanguage(Language.ENG); currentSessionLang=Language.ENG; break;
                    case "2": teacher.switchLanguage(Language.RUS); currentSessionLang=Language.RUS; break;
                    case "3": teacher.switchLanguage(Language.KAZ); currentSessionLang=Language.KAZ; break;
                }
                break;
            case "0": currentUser = null; break;
        }
    }

    private static void teacherPutMark(Teacher teacher) throws IOException {
        System.out.print("Student ID: ");  String sId   = reader.readLine().trim();
        System.out.print("Course code: "); String cCode = reader.readLine().trim();
        Semester sem = chooseSemester();
        if (sem == null) return;

        Student student = findStudent(sId);
        Course  course  = findCourse(cCode);
        if (student == null || course == null) return;

        System.out.print("Att1  (0-30): "); double a1 = parseDouble();
        System.out.print("Att2  (0-30): "); double a2 = parseDouble();
        System.out.print("Final (0-40): "); double fe = parseDouble();
        teacher.putMark(student, course, sem, new Mark(a1, a2, fe));
        DBContext.save();
    }

    private static void teacherAttendance(Teacher teacher) throws IOException {
        System.out.print("Student ID: ");  String sId   = reader.readLine().trim();
        System.out.print("Course code: "); String cCode = reader.readLine().trim();
        Semester sem = chooseSemester();
        if (sem == null) return;

        Student  student = findStudent(sId);
        Course   course  = findCourse(cCode);
        if (student == null || course == null) return;

        Enrollment enrollment = course.findEnrollment(student, sem);
        if (enrollment == null) { System.out.println("Enrollment not found."); return; }

        System.out.print("Lesson number: ");
        int lesson;
        try { lesson = Integer.parseInt(reader.readLine().trim()); }
        catch (NumberFormatException e) { System.out.println("Invalid."); return; }

        System.out.print("Present? (y/n): ");
        boolean present = reader.readLine().trim().equalsIgnoreCase("y");
        teacher.markAttendance(enrollment, lesson, present);
        DBContext.save();
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private static Semester chooseSemester() throws IOException {
        System.out.print("Semester (1-FALL 2-SPRING 3-SUMMER): ");
        switch (reader.readLine().trim()) {
            case "1": return Semester.FALL;
            case "2": return Semester.SPRING;
            case "3": return Semester.SUMMER;
            default: System.out.println("Invalid semester."); return null;
        }
    }

    private static Student findStudent(String id) {
        User u = DBContext.getUsers().stream()
                .filter(x -> x instanceof Student && x.getId().equals(id))
                .findFirst().orElse(null);
        if (u == null) System.out.println("Student not found: " + id);
        return (Student) u;
    }

    private static Course findCourse(String code) {
        Course c = DBContext.getCourses().stream()
                .filter(x -> code.equalsIgnoreCase(x.getCourseCode()))
                .findFirst().orElse(null);
        if (c == null) System.out.println("Course not found: " + code);
        return c;
    }

    private static double parseDouble() throws IOException {
        try { return Double.parseDouble(reader.readLine().trim()); }
        catch (NumberFormatException e) { return 0; }
    }

    private static void welcomeMsg() {
        System.out.println("\n" + sm("  UNIVERSITY MANAGEMENT SYSTEM","  СИСТЕМА УПРАВЛЕНИЯ УНИВЕРСИТЕТОМ","  УНИВЕРСИТЕТТІ БАСҚАРУ ЖҮЙЕСІ"));
        System.out.println("  " + "─".repeat(36));
    }
}







