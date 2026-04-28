package views;

import java.io.IOException;
import core.DBContext;
import models.*;

public class MainView extends BaseView {
    private static User currentUser = null;

    public static void run() throws IOException {
        welcomeMsg();
        
        while (true) {
            if (currentUser == null) {
                showAuthMenu();
            } else {
                showRoleMenu();
            }
        }
    }

    private static void showAuthMenu() throws IOException {
        System.out.println("\n1 - Login");
        System.out.println("2 - Exit");
        
        String choice = reader.readLine();
        if (choice.equals("1")) {
            loginProcess();
        } else {
            System.out.println("Goodbye!");
            DBContext.save();
            System.exit(0);
        }
    }

    private static void loginProcess() throws IOException {
        System.out.print("Username: ");
        String login = reader.readLine();
        System.out.print("Password: ");
        String pass = reader.readLine();

        currentUser = DBContext.getUsers().stream()
                .filter(u -> u.login(login, pass))
                .findFirst()
                .orElse(null);

        if (currentUser != null) {
            successMsg("Welcome, " + currentUser.getName() + "!");
        } else {
            System.out.println("Error: Invalid credentials.");
        }
    }

    private static void showRoleMenu() throws IOException {
        String msg = currentUser.getLanguageMessage(
            "\nLogged in as: ", 
            "\nВы вошли как: ", 
            "\nЖүйеге кірдіңіз: "
        );
        System.out.println(msg + currentUser.getClass().getSimpleName());

        System.out.println("1 - Switch Language (" + currentUser.getCurrentLanguage() + ")");
        System.out.println("2 - View News");
        
        if (currentUser instanceof Admin) {
            System.out.println("3 - Register New User (Factory)");
            System.out.println("4 - View All Users");
        } else if (currentUser instanceof Teacher) {
            System.out.println("3 - Put Mark");
        } else if (currentUser instanceof Student) {
            System.out.println("3 - View Transcript");
        }
        
        System.out.println("0 - Logout");

        String choice = reader.readLine();
        handleChoice(choice);
    }

    private static void handleChoice(String choice) throws IOException {
        if (choice.equals("0")) {
            currentUser = null;
            return;
        }
        if (choice.equals("1")) {
            System.out.println("Choose: 1-ENG, 2-RUS, 3-KAZ");
            String l = reader.readLine();
            if(l.equals("1")) currentUser.switchLanguage(Language.ENG);
            if(l.equals("2")) currentUser.switchLanguage(Language.RUS);
            if(l.equals("3")) currentUser.switchLanguage(Language.KAZ);
            return;
        }
        /*if (choice.equals("2")) {
            currentUser.viewNews();
            return;
        }*/

        if (currentUser instanceof Admin && choice.equals("3")) {
            Admin admin = (Admin) currentUser;
            System.out.println("Type (student/teacher/manager):");
            String type = reader.readLine();
            System.out.println("Name:");
            String name = reader.readLine();
            System.out.println("Login:");
            String log = reader.readLine();
            System.out.println("Password:");
            String pass = reader.readLine();

            User newUser = admin.createUser(type, "ID-" + (DBContext.getUsers().size() + 1), name, log, pass);
            
            if (newUser != null) {
                DBContext.addUser(newUser);
                DBContext.save();
                successMsg("User " + name + " registered successfully via Factory!");
            }
        }
        
        if (currentUser instanceof Admin && choice.equals("4")) {
            DBContext.getUsers().forEach(System.out::println);
        }
    }

    private static void welcomeMsg() {
        System.out.println("================================");
        System.out.println("   UNIVERSITY MANAGEMENT SYSTEM ");
        System.out.println("================================");
    }
}