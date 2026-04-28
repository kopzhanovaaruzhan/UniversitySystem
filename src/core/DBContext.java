package core;

import java.io.*;
import java.util.*;

import models.Admin;
import models.Course;
import models.User;

public class DBContext {
    private final static DBContext INSTANCE = new DBContext();
    private static String rootPath;
    
    private static List<Course> courses;
    private static List<User> users;

    static {
        rootPath = new File("src/data").getAbsolutePath();
    }

    private DBContext() {
        courses = new ArrayList<>();
        users = new ArrayList<>();
        load();
    }

    public static DBContext getInstance() {
        return INSTANCE;
    }


    public static void addUser(User user) {
        users.add(user);
    }

    public static List<User> getUsers() {
        return users;
    }

    public static boolean save() {
        boolean coursesSaved = serializer(courses, "courses");
        boolean usersSaved = serializer(users, "users");
        return coursesSaved && usersSaved;
    }

    @SuppressWarnings("unchecked")
    public static void load() {
        List<Course> loadedCourses = (List<Course>) deserialize("courses");
        List<User> loadedUsers = (List<User>) deserialize("users");
        if (users.isEmpty()) {
            users.add(new Admin("A1", "System Admin", "admin", "admin", 999999));
        }

        if (loadedCourses != null) courses = loadedCourses;
        if (loadedUsers != null) users = loadedUsers;
    }

    private static boolean serializer(Object data, String fileName) {
        String filePath = rootPath + "/" + fileName + ".txt";
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(data);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static Object deserialize(String fileName) {
        String filePath = rootPath + "/" + fileName + ".txt";
        File file = new File(filePath);
        if (!file.exists()) return null;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return ois.readObject();
        } catch (Exception e) {
            return null;
        }
    }
}


