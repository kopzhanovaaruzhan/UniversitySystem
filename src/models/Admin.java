package models;

import enums.*;
import core.DBContext;
import exceptions.SupervisorIndexException;
import java.util.List;

public class Admin extends Employee {
    private static final long serialVersionUID = 1L;

    public Admin(String id, String name, String login, String password, double salary) {
        super(id, name, login, password, salary);
    }

    public User createUser(String type, String id, String name, String login, String password, double salary, Object specialParam) {
        switch (type.toLowerCase().trim()) {
            case "student":
                Faculty faculty = (specialParam instanceof Faculty) ? (Faculty) specialParam : Faculty.SITE;
                return new Student(id, name, login, password, faculty, 1);

            case "graduate":
                Object[] gradParams = (Object[]) specialParam;
                GraduateLevel level = (GraduateLevel) gradParams[0];
                Teacher supervisor = (Teacher) gradParams[1];
                try {
                    return new GraduateStudent(id, name, login, password, level, supervisor);
                } catch (SupervisorIndexException e) {
                    System.out.println("Error assigning supervisor: " + e.getMessage());
                    return null;
                }

            case "teacher":
                TeacherType tt = (specialParam instanceof TeacherType) ? (TeacherType) specialParam : TeacherType.TUTOR;
                return new Teacher(id, name, login, password, salary, tt);

            case "manager":
                ManagerType mt = (specialParam instanceof ManagerType) ? (ManagerType) specialParam : ManagerType.OR;
                return new Manager(id, name, login, password, salary, mt);

            case "techsupport":
                return new TechSupportSpecialist(id, name, login, password, salary);

            default:
                System.out.println(getLanguageMessage("Unknown type: ", "Неизвестный тип: ", "Белгісіз тип: ") + type);
                return null;
        }
    }


    public void removeUser(UserComponent user) {
        if (user.equals(this)) {
            System.out.println(getLanguageMessage("Cannot remove yourself!", "Нельзя удалить самого себя!", "Өзіңізді өшіре алмайсыз!"));
            return;
        }
        DBContext.getUsers().remove(user);
        DBContext.save(); 
        System.out.println(getLanguageMessage("User removed: ", "Пользователь удален: ", "Пайдаланушы өшірілді: ") + user.getName());
    }

    public void updateUser(UserComponent user, String newName) {
        if (user instanceof User) {
            ((User) user).setName(newName);
        } else if (user instanceof ResearchDecorator) {
            ((ResearchDecorator) user).getBaseUser().setName(newName);
        }
        
        DBContext.save();
        System.out.println(getLanguageMessage("User info updated.", "Данные пользователя обновлены.", "Пайдаланушы мәліметтері жаңартылды."));
    }


    public void viewSystemLogs() {
        System.out.println("\n=== " + getLanguageMessage("SYSTEM LOGS", "СИСТЕМНЫЕ ЛОГИ", "ЖҮЙЕЛІК ЛОГТАР") + " ===");
        List<String> logs = DBContext.getLogs();
        if (logs.isEmpty()) {
            System.out.println(getLanguageMessage("No logs recorded.", "Логи пусты.", "Логтар бос."));
        } else {
            logs.forEach(System.out::println);
        }
    }

    public void viewInfo() {
        System.out.println(getLanguageMessage(
            "Admin: " + getName() + " | System Access: FULL",
            "Админ: " + getName() + " | Доступ: ПОЛНЫЙ",
            "Админ: " + getName() + " | Қолжетімділік: ТОЛЫҚ"
        ));
    }
}



