package views;

import java.io.IOException;
import core.DBContext;
import enums.*;
import models.*;

public class MainView extends BaseView {
    private static UserComponent currentUser = null;
    private static Language currentSessionLang = Language.ENG;

    public static void run() throws IOException {
        welcomeMsg();
        while (true) {
            if (currentUser == null) showAuthMenu();
            else routeToRoleView();
        }
    }

    public static String sm(String eng, String rus, String kaz) {
        if (currentSessionLang == Language.RUS) return rus;
        if (currentSessionLang == Language.KAZ) return kaz;
        return eng;
    }

    private static void showAuthMenu() throws IOException {
        System.out.println("\n1 - " + sm("Login","Войти","Кіру"));
        System.out.println("2 - " + sm("Exit","Выйти","Шығу"));
        String choice = reader.readLine();
        if ("1".equals(choice)) loginProcess();
        else { System.out.println(sm("Goodbye!","До свидания!","Сау болыңыз!")); DBContext.save(); System.exit(0); }
    }

    private static void loginProcess() throws IOException {
        System.out.print(sm("Username: ", "Логин: ", "Логин: "));
        String login = reader.readLine();
        System.out.print(sm("Password: ", "Пароль: ", "Пароль: "));
        String pass = reader.readLine();

        currentUser = null;

        for (UserComponent uc : DBContext.getUsers()) {
            User baseUser = null;

            if (uc instanceof User) {
                baseUser = (User) uc;
            } 
            else if (uc instanceof ResearchDecorator) {
                baseUser = ((ResearchDecorator) uc).getBaseUser();
            }
            if (baseUser != null && baseUser.getLogin().equals(login) && baseUser.getPassword().equals(pass)) {
                currentUser = uc;
                break;
            }
        }

        if (currentUser != null) {
            currentUser.switchLanguage(currentSessionLang);
            
            String welcome = currentUser.getLanguageMessage("Welcome", "Добро пожаловать", "Қош келдіңіз");
            successMsg(welcome + ", " + currentUser.getName() + "!");

        } else {
            System.out.println(sm("Error: Invalid credentials.", "Ошибка: Неверные данные.", "Қате мәліметтер."));
        }
    }


    private static void routeToRoleView() throws IOException {
        if (currentUser instanceof ResearchDecorator) {
            ResearchView.showMenu((ResearchDecorator) currentUser);
        }

        if (currentUser instanceof Student || (currentUser instanceof ResearchDecorator && ((ResearchDecorator)currentUser).getBaseUser() instanceof Student)) {
            StudentView.show((Student) (currentUser instanceof Student ? currentUser : ((ResearchDecorator)currentUser).getBaseUser()));
            currentUser = null;
        } else if (currentUser instanceof Admin || (currentUser instanceof ResearchDecorator && ((ResearchDecorator)currentUser).getBaseUser() instanceof Admin)) {
            Admin admin = (Admin) (currentUser instanceof Admin ? currentUser : ((ResearchDecorator)currentUser).getBaseUser());
            AdminView.show(admin);
            currentUser = null;
        } else if (currentUser instanceof Teacher || (currentUser instanceof ResearchDecorator && ((ResearchDecorator)currentUser).getBaseUser() instanceof Teacher)) {
            TeacherView.show((Teacher) (currentUser instanceof Teacher ? currentUser : ((ResearchDecorator)currentUser).getBaseUser()));
            currentUser = null;
        } else if (currentUser instanceof TechSupportSpecialist) {
            TechSupportView.show((TechSupportSpecialist) currentUser);
            currentUser = null;
        }else if (currentUser instanceof Manager) {
            ManagerView.show((Manager) currentUser);
            currentUser = null;
        }else {
            System.out.println("No menu for role: " + currentUser.getClass().getSimpleName());
            currentUser = null;
        }
    }


    private static void welcomeMsg() {
        System.out.println("\n" + sm("  UNIVERSITY MANAGEMENT SYSTEM","  СИСТЕМА УПРАВЛЕНИЯ УНИВЕРСИТЕТОМ","  УНИВЕРСИТЕТТІ БАСҚАРУ ЖҮЙЕСІ"));
        System.out.println("  " + "─".repeat(36));
    }
}







