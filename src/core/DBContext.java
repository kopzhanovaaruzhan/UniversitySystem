package core;

import java.io.*;
import java.util.*;
import enums.*;
import models.*;
import exceptions.SupervisorIndexException;

public class DBContext {
    private static final DBContext INSTANCE = new DBContext();
    private static String rootPath;

    private static List<Course> courses;
    private static List<User>   users;

    static {
        rootPath = new File("src/data").getAbsolutePath();
    }

    private DBContext() {
        courses = new ArrayList<>();
        users   = new ArrayList<>();
        load();
    }

    public static DBContext getInstance() { return INSTANCE; }

    // ─── Users ────────────────────────────────────────────────────────────────

    public static void addUser(User user)    { users.add(user); }
    public static List<User> getUsers()      { return users; }

    // ─── Courses ──────────────────────────────────────────────────────────────

    public static void addCourse(Course c)   { courses.add(c); }
    public static List<Course> getCourses()  { return courses; }

    // ─── Persistence ──────────────────────────────────────────────────────────

    public static boolean save() {
        return serializer(courses, "courses") & serializer(users, "users");
    }

    @SuppressWarnings("unchecked")
    public static void load() {
        List<Course> lc = (List<Course>) deserialize("courses");
        List<User>   lu = (List<User>)   deserialize("users");

        if (lc != null) courses = lc;
        if (lu != null) users   = lu;

        // Гарантируем наличие Admin
        boolean hasAdmin = users.stream().anyMatch(u -> u instanceof Admin);
        if (!hasAdmin)
            users.add(new Admin("A1", "System Admin", "admin", "admin", 999999));

        // Заполняем демо-данные если база пустая
        if (courses.isEmpty()) seedCourses();
        if (users.stream().noneMatch(u -> u instanceof Student)) seedStudents();
    }

    // ─── Демо-данные ──────────────────────────────────────────────────────────

    private static void seedCourses() {
        courses.add(new Course("CS101", "Introduction to Programming", 5, Faculty.SITE));
        courses.add(new Course("CS201", "Data Structures",             5, Faculty.SITE));
        courses.add(new Course("MATH101","Calculus I",                 3, Faculty.BSE));
        courses.add(new Course("ENG101", "Academic English",           3, Faculty.ISE));
        courses.add(new Course("CS301", "Algorithms",                  5, Faculty.SITE));
    }

    private static void seedStudents() {
        // Учитель с h-index=5 (может быть научруком)
        Teacher t = new Teacher("T1", "Dr. Smith", "smith", "smith123",
                                300000, TeacherType.PROFESSOR, 5);
        users.add(t);

        // Назначаем учителя на CS101
        courses.stream()
               .filter(c -> "CS101".equals(c.getCourseCode()))
               .findFirst()
               .ifPresent(c -> c.getTeachers().add(t));

        // Обычный студент
        users.add(new Student("S1", "Alice Johnson", "alice", "alice123", Faculty.SITE, 2));

        // Аспирант
        try {
            GraduateStudent gs = new GraduateStudent(
                    "G1", "Bob Master", "bob", "bob123", GraduateLevel.MASTER, t);
            gs.addDiplomaProject("Machine Learning in Education");
            users.add(gs);
        } catch (SupervisorIndexException e) {
            System.out.println("[Seed] " + e.getMessage());
        }
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private static boolean serializer(Object data, String fileName) {
        File dir = new File(rootPath);
        if (!dir.exists()) dir.mkdirs();
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(rootPath + "/" + fileName + ".txt"))) {
            oos.writeObject(data);
            return true;
        } catch (Exception e) { return false; }
    }

    private static Object deserialize(String fileName) {
        File file = new File(rootPath + "/" + fileName + ".txt");
        if (!file.exists()) return null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return ois.readObject();
        } catch (Exception e) { return null; }
    }
}



