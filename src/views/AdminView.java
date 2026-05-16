package views;

import java.io.IOException;
import models.*;
import core.DBContext;
import enums.*;

public class AdminView extends BaseView {

	public static void show(Admin admin) throws IOException {
        boolean running = true;
        while (running) {
            printMenu(admin);
            String choice = reader.readLine();
            switch (choice) {
                case "1": addUserMenu(admin); break;
                case "2": removeUserMenu(admin); break;
                case "3": admin.viewSystemLogs(); break;
                case "4": switchLang(admin); break;
                case "0": running = false; break;
                default: System.out.println("Unknown option."); break;
            }
        }
    }
	
	
    private static void printMenu(Admin admin) {
        String lang = admin.getCurrentLanguage().name();
        System.out.println("\n╔═══════════════════════════════════════╗");
        System.out.printf ("║  %-37s║%n", admin.getName() + " [ADMIN]");
        System.out.println("╠═══════════════════════════════════════╣");
        option("1", admin.getLanguageMessage("Create User", "Создать пользователя", "Пайдаланушыны құру"));
        option("2", admin.getLanguageMessage("Remove User", "Удалить пользователя", "Пайдаланушыны өшіру"));
        option("3", admin.getLanguageMessage("View Logs", "Просмотр логов", "Логтарды көру"));
        option("4", admin.getLanguageMessage("Language: ", "Язык: ", "Тіл: ") + lang);
        option("0", admin.getLanguageMessage("Logout", "Выйти", "Шығу"));
        System.out.println("╚═══════════════════════════════════════╝");
        System.out.print(admin.getLanguageMessage("Choice: ", "Выбор: ", "Таңдау: "));
    }

    private static void option(String key, String label) {
        System.out.printf("║  %s - %-33s║%n", key, label);
    }

    private static void addUserMenu(Admin admin) throws IOException {
        System.out.println("\n--- " + admin.getLanguageMessage("User Type", "Тип пользователя", "Пайдаланушы типі") + " ---");
        System.out.println("1-Student, 2-Graduate, 3-Teacher, 4-Manager, 5-TechSupport");
        System.out.print("> ");
        String choice = reader.readLine().trim();

        System.out.print("ID: ");        String id = reader.readLine().trim();
        System.out.print("Name: ");      String name = reader.readLine().trim();
        System.out.print("Login: ");     String login = reader.readLine().trim();
        System.out.print("Password: ");  String password = reader.readLine().trim();

        String type = "";
        double salary = 0.0;
        Object specialParam = null;

        switch (choice) {
            case "1" -> {
                type = "student";
                System.out.println("Select Faculty: 1-SITE, 2-BSE, 3ISE");
                specialParam = switch(reader.readLine().trim()) {
                    case "2" -> Faculty.BSE;
                    case "3" -> Faculty.ISE;
                    default  -> Faculty.SITE;
                };
            }
            case "2" -> {
                type = "graduate";
                salary = 150000;
                System.out.println("Select Level: 1-MASTER, 2-PHD");
                GraduateLevel level = reader.readLine().trim().equals("2") ? GraduateLevel.PHD : GraduateLevel.MASTER;
                
                var teachers = core.DBContext.getUsers().stream().filter(u -> u instanceof Teacher).map(u -> (Teacher)u).toList();
                for (int i = 0; i < teachers.size(); i++) System.out.println(i + " - " + teachers.get(i).getName());
                System.out.print("Supervisor index: ");
                Teacher supervisor = teachers.get(Integer.parseInt(reader.readLine().trim()));
                
                specialParam = new Object[]{ level, supervisor };
            }
            case "3" -> {
                type = "teacher";
                salary = 500000;
                System.out.println("Type: 1-TUTOR, 2-LECTOR, 3-PROFESSOR");
                specialParam = switch(reader.readLine().trim()) {
                    case "2" -> TeacherType.LECTOR;
                    case "3" -> TeacherType.PROFESSOR;
                    default  -> TeacherType.TUTOR;
                };
            }
            case "4" -> {
                type = "manager";
                salary = 450000;
                System.out.println("Type: 1-OR, 2-DEPARTMENT");
                specialParam = reader.readLine().trim().equals("2") ? ManagerType.DEPARTMENT : ManagerType.OR;
            }
            case "5" -> {
                type = "techsupport";
                salary = 350000;
            }
            default -> {
                System.out.println("Invalid selection.");
                return;
            }
        }

        User newUser = admin.createUser(type, id, name, login, password, salary, specialParam);

        if (newUser != null) {
            core.DBContext.getUsers().add(newUser);
            core.DBContext.save();
            successMsg(admin.getLanguageMessage("User created!", "Пользователь создан!", "Пайдаланушы құрылды!"));
        }
    }
    
    private static void removeUserMenu(Admin admin) throws IOException {
        System.out.print(admin.getLanguageMessage("Enter User ID to remove: ", "Введите ID для удаления: ", "Өшіру үшін ID енгізіңіз: "));
        String id = reader.readLine();

        UserComponent target = null;
        for (UserComponent u : DBContext.getUsers()) {
            if (u instanceof User && ((User)u).getId().equals(id)) {
                target = u;
                break;
            }
        }

        if (target != null) {
            admin.removeUser(target);
        } else {
            System.out.println("User not found.");
        }
    }




}



