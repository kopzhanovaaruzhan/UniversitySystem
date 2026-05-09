package views;

import java.io.IOException;
import core.DBContext;
import enums.Language;
import models.*;

public class MainView extends BaseView {
    private static User currentUser = null;
    private static Language currentSessionLang = Language.ENG;

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
    
    // Вспомогательный метод для сообщений, когда пользователь еще не залогинен
    private static String getStaticMsg(String eng, String rus, String kaz) {
        if (currentSessionLang == Language.RUS) return rus;
        if (currentSessionLang == Language.KAZ) return kaz;
        return eng;
    }

    private static void showAuthMenu() throws IOException {
        System.out.println("\n1 - " + getStaticMsg("Login", "Войти", "Кіру"));
        System.out.println("2 - " + getStaticMsg("Exit", "Выйти", "Шығу"));
        
        String choice = reader.readLine();
        if (choice.equals("1")) {
            loginProcess();
        } else {
            System.out.println(getStaticMsg("Goodbye!", "До свидания!", "Сау болыңыз!"));
            DBContext.save();
            System.exit(0);
        }
    }

    private static void loginProcess() throws IOException {
        // ТЕПЕРЬ И ЭТИ ПОЛЯ НА ТРЕХ ЯЗЫКАХ
        System.out.print(getStaticMsg("Username: ", "Имя пользователя: ", "Пайдаланушы аты: "));
        String login = reader.readLine();
        System.out.print(getStaticMsg("Password: ", "Пароль: ", "Құпия сөз: "));
        String pass = reader.readLine();

        currentUser = DBContext.getUsers().stream()
                .filter(u -> u.login(login, pass))
                .findFirst()
                .orElse(null);

        if (currentUser != null) {
            // Тихая синхронизация языка сессии с объектом юзера
            currentUser.switchLanguage(currentSessionLang); 
            
            String welcome = currentUser.getLanguageMessage("Welcome", "Добро пожаловать", "Қош келдіңіз");
            successMsg(welcome + ", " + currentUser.getName() + "!");
        } else {
            System.out.println(getStaticMsg(
                "Error: Invalid credentials.", 
                "Ошибка: Неверные данные.", 
                "Қате: Мәліметтер дұрыс емес."
            ));
        }
    }

    private static void showRoleMenu() throws IOException {
        String loggedMsg = currentUser.getLanguageMessage("\nLogged in as: ", "\nВы вошли как: ", "\nЖүйеге кірдіңіз: ");
        System.out.println(loggedMsg + currentUser.getClass().getSimpleName());

        System.out.println("1 - " + currentUser.getLanguageMessage("Switch Language", "Сменить язык", "Тілді өзгерту") + " (" + currentUser.getCurrentLanguage() + ")");
        System.out.println("2 - " + currentUser.getLanguageMessage("View News", "Посмотреть новости", "Жаңалықтарды көру"));
        
        if (currentUser instanceof Admin) {
            System.out.println("3 - " + currentUser.getLanguageMessage("Register New User", "Зарегистрировать пользователя", "Пайдаланушыны тіркеу"));
            System.out.println("4 - " + currentUser.getLanguageMessage("View All Users", "Список всех пользователей", "Барлық пайдаланушылар тізімі"));
        } else if (currentUser instanceof Teacher) {
            System.out.println("3 - " + currentUser.getLanguageMessage("Put Mark", "Поставить оценку", "Баға қою"));
        } else if (currentUser instanceof Student) {
            System.out.println("3 - " + currentUser.getLanguageMessage("View Transcript", "Посмотреть транскрипт", "Транскриптті көру"));
        }
        
        System.out.println("0 - " + currentUser.getLanguageMessage("Logout", "Выйти", "Шығу"));

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
            if(l.equals("1")) currentSessionLang = Language.ENG;
            else if(l.equals("2")) currentSessionLang = Language.RUS;
            else if(l.equals("3")) currentSessionLang = Language.KAZ;
            
            if (currentUser != null) {
                currentUser.switchLanguage(currentSessionLang); 
            }
            return;
        }

        if (currentUser instanceof Admin && choice.equals("3")) {
            Admin admin = (Admin) currentUser;
            
            System.out.println(currentUser.getLanguageMessage(
                "Select user type number:", 
                "Выберите номер типа пользователя:", 
                "Пайдаланушы түрінің нөмірін таңдаңыз:"
            ));
            
            System.out.println("1 - Student");
            System.out.println("2 - Graduate Student");
            System.out.println("3 - Teacher");
            System.out.println("4 - Manager");
            System.out.println("5 - Tech Support");

            String typeChoice = reader.readLine();
            String type = "";

            switch (typeChoice) {
                case "1": type = "student"; break;
                case "2": type = "graduate"; break;
                case "3": type = "teacher"; break;
                case "4": type = "manager"; break;
                case "5": type = "techsupport"; break;
                default: 
                    System.out.println(currentUser.getLanguageMessage("Invalid number!", "Неверный номер!", "Қате нөмір!")); 
                    return;
            }

            System.out.print(currentUser.getLanguageMessage("Name: ", "Имя: ", "Аты: "));
            String name = reader.readLine();
            
            System.out.print(currentUser.getLanguageMessage("Login: ", "Логин: ", "Логин: "));
            String log = reader.readLine();
            
            System.out.print(currentUser.getLanguageMessage("Password: ", "Пароль: ", "Құпия сөз: "));
            String pass = reader.readLine();

            User newUser = admin.createUser(type, "ID-" + (DBContext.getUsers().size() + 1), name, log, pass);
            
            if (newUser != null) {
                DBContext.addUser(newUser);
                DBContext.save();
                String success = currentUser.getLanguageMessage("registered successfully!", "зарегистрирован успешно!", "сәтті тіркелді!");
                successMsg(name + " " + success);
            }
        }
        
        if (currentUser instanceof Admin && choice.equals("4")) {
            DBContext.getUsers().forEach(System.out::println);
        }
    }

    private static void welcomeMsg() {
        System.out.println("\n" + getStaticMsg(
            "   UNIVERSITY MANAGEMENT SYSTEM: ", 
            "   СИСТЕМА УПРАВЛЕНИЯ УНИВЕРСИТЕТОМ: ", 
            "   УНИВЕРСИТЕТТІ БАСҚАРУ ЖҮЙЕСІ: "
        ));
        System.out.println("---------------------------------");
    }
}




