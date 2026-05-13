package core;

import java.io.*;
import java.util.*;
import enums.*;
import models.*;

public class DBContext implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private static DBContext instance;
    private static final String DATA_PATH = "src/data/";

    private List<Course> courses = new ArrayList<>();
    private List<UserComponent> users = new ArrayList<>();
    private List<News> newsList = new ArrayList<>();
    private List<String> systemLogs = new ArrayList<>();
    private List<ResearchProject> projects = new ArrayList<>();

    private DBContext() {
    }

    public static DBContext getInstance() {
        if (instance == null) {
            instance = new DBContext();
            instance.load();
        }
        return instance;
    }


    public static List<UserComponent> getUsers()     { return getInstance().users; }
    public static List<Course> getCourses() { return getInstance().courses; }
    public static List<News> getNews()       { return getInstance().newsList; }
    public static List<String> getLogs()    { return getInstance().systemLogs; }


    public static void addLog(String message) {
        String log = "[" + new Date() + "] " + message;
        getInstance().systemLogs.add(log);
    }

    public static void addUser(User user) {
        getUsers().add(user);
        addLog("User added: " + user.getName() + " (" + user.getClass().getSimpleName() + ")");
    }
    
    public static void addCourse(Course c) { 
        getInstance().courses.add(c); 
        addLog("Course added: " + c.getName());
    }
    
    public static List<ResearchProject> getProjects() {
        return getInstance().projects;
    }

    public static void addProject(ResearchProject p) {
        getInstance().projects.add(p);
        addLog("Research project created: " + p.getTopic());
    }


    public static void save() {
        File dir = new File(DATA_PATH);
        if (!dir.exists()) dir.mkdirs();

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_PATH + "database.ser"))) {
            oos.writeObject(getInstance());
        } catch (IOException e) {
            System.err.println("Save error: " + e.getMessage());
        }
    }

    private void load() {
        File file = new File(DATA_PATH + "database.ser");
        if (!file.exists()) {
            seedInitialData();
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            DBContext loaded = (DBContext) ois.readObject();
            this.users = loaded.users;
            this.courses = loaded.courses;
            this.newsList = loaded.newsList;
            this.systemLogs = loaded.systemLogs;
        } catch (Exception e) {
            System.err.println("Load error: " + e.getMessage());
            seedInitialData();
        }
    }


    private void seedInitialData() {
        users.add(new Admin("A1", "System Admin", "admin", "admin", 1000000));
        
        courses.add(new Course("CS101", "Introduction to Programming", 5, Faculty.SITE));
        courses.add(new Course("MATH101", "Calculus I", 3, Faculty.BSE));

        Teacher prof = new Teacher("T1", "Dr. Smith", "smith", "smith123", 500000, TeacherType.PROFESSOR);
        prof.setHIndex(5);
        users.add(prof);

        addLog("Database initialized with seed data.");
        save();
    }
    

}





