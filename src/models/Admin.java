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

    // ─── Улучшенная Фабрика (Исправлен Manager и Teacher) ──────────────────────

    public User createUser(String type, String id, String name, String login, String password) {
        switch (type.toLowerCase()) {
            case "student":
                return new Student(id, name, login, password);

            case "graduate":
                try {
                    // По умолчанию создаем Магистра без куратора (куратор назначается позже)
                    return new GraduateStudent(id, name, login, password, GraduateLevel.MASTER, null);
                } catch (SupervisorIndexException e) {
                    System.out.println("Error: " + e.getMessage());
                    return null;
                }

            case "teacher":
                // Теперь создаем Лектора с дефолтной зарплатой
                return new Teacher(id, name, login, password, 500000, TeacherType.LECTOR);

            case "manager":
                // ИСПРАВЛЕНО: Добавлен ManagerType (по умолчанию OR)
                return new Manager(id, name, login, password, 450000, ManagerType.OR);

            case "techsupport":
                return new TechSupportSpecialist(id, name, login, password, 350000);

            default:
                System.out.println(getLanguageMessage("Unknown type: ", "Неизвестный тип: ", "Белгісіз тип: ") + type);
                return null;
        }
    }

    // ─── Методы управления (Requirement: Manage Users) ─────────────────────────

    public void removeUser(User user) {
        if (user.equals(this)) {
            System.out.println(getLanguageMessage("Cannot remove yourself!", "Нельзя удалить самого себя!", "Өзіңізді өшіре алмайсыз!"));
            return;
        }
        DBContext.getUsers().remove(user);
        DBContext.save(); // Сохраняем изменения в файл сразу
        System.out.println(getLanguageMessage("User removed: ", "Пользователь удален: ", "Пайдаланушы өшірілді: ") + user.getName());
    }

    public void updateUser(User user, String newName) {
        user.setName(newName);
        DBContext.save();
        System.out.println(getLanguageMessage("User info updated.", "Данные пользователя обновлены.", "Пайдаланушы мәліметтері жаңартылды."));
    }

    // ─── Системные логи (Requirement: See log files about user actions) ────────

    public void viewSystemLogs() {
        System.out.println("\n=== " + getLanguageMessage("SYSTEM LOGS", "СИСТЕМНЫЕ ЛОГИ", "ЖҮЙЕЛІК ЛОГТАР") + " ===");
        List<String> logs = DBContext.getLogs(); // Предполагаем, что ты добавила List<String> logs в DBContext
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



