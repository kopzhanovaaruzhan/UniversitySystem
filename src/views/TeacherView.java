package views;

import java.io.IOException;
import models.*;
import controllers.CourseController;
import enums.Semester;
import enums.UrgencyLevel;
import core.DBContext;
import java.util.List;

public class TeacherView extends BaseView {

    public static void show(Teacher teacher) throws IOException {
        boolean running = true;
        while (running) {
            printMenu(teacher);
            String choice = reader.readLine().trim();
            switch (choice) {
                case "1" -> viewTeacherCourses(teacher);
                case "2" -> viewTeacherStudents(teacher);
                case "3" -> teacherPutMark(teacher);
                case "4" -> teacherAttendance(teacher);
                case "5" -> sendComplaintMenu(teacher);
                case "6" -> sendMessageMenu(teacher);
                case "7" -> handleResearcherOption(teacher);
                case "8" -> teacher.viewInfo();
                case "9" -> switchLang(teacher);
                case "0" -> running = false;
                default  -> System.out.println("Unknown option.");
            }
        }
    }

    private static void printMenu(Teacher teacher) {
        String lang = teacher.getCurrentLanguage().name();
        System.out.println("\n╔═══════════════════════════════════════╗");
        System.out.printf ("║  %-37s║%n", teacher.getName() + " [" + teacher.getType() + "]");
        System.out.println("╠═══════════════════════════════════════╣");
        option("1", teacher.getLanguageMessage("View My Courses", "Мои курсы", "Mentіñ kurstarym"));
        option("2", teacher.getLanguageMessage("View My Students", "Мои студенты", "Mentіñ studentterіm"));
        option("3", teacher.getLanguageMessage("Put Mark", "Поставить оценку", "Bağa qoıu"));
        option("4", teacher.getLanguageMessage("Mark Attendance", "Отметить посещаемость", "Sabaqqa qatysudy belgіleu"));
        option("5", teacher.getLanguageMessage("Send Complaint", "Отправить жалобу", "Şağym túsіru"));
        option("6", teacher.getLanguageMessage("Send Message", "Отправить сообщение", "Habarlama jіberu"));
        
        if (teacher.isResearcher()) {
            option("7", teacher.getLanguageMessage("Research Cabinet", "Кабинет исследователя", "Zertteuşі kabınetі"));
        } else {
            option("7", teacher.getLanguageMessage("Become a Researcher", "Стать исследователем", "Zertteuşі bolu"));
        }

        option("8", teacher.getLanguageMessage("View My Info", "Моя информация", "Mentіñ málemetterіm"));
        option("9", teacher.getLanguageMessage("Language: ", "Язык: ", "Tіl: ") + lang);
        option("0", teacher.getLanguageMessage("Logout", "Выйти", "Şyğu"));
        System.out.println("╚═══════════════════════════════════════╝");
        System.out.print(teacher.getLanguageMessage("Choice: ", "Выбор: ", "Tañdau: "));
    }

    private static void option(String key, String label) {
        System.out.printf("║  %s - %-33s║%n", key, label);
    }

    private static void viewTeacherCourses(Teacher teacher) {
        System.out.println("\n" + teacher.getLanguageMessage("=== My Courses ===", "=== Мои курсы ===", "=== Mentіñ kurstarym ==="));
        List<Course> courses = CourseController.getTeacherCourses(teacher);
        if (courses.isEmpty()) {
            System.out.println("No courses assigned.");
            return;
        }
        courses.forEach(c -> System.out.printf("  [%s] %s (%d cr.)%n", c.getCourseCode(), c.getName(), c.getCredits()));
    }

    private static void viewTeacherStudents(Teacher teacher) {
        System.out.println("\n" + teacher.getLanguageMessage("=== My Students ===", "=== Мои студенты ===", "=== Mentіñ studentterіm ==="));
        List<Enrollment> enrollments = CourseController.getTeacherStudents(teacher);
        if (enrollments.isEmpty()) {
            System.out.println("No students registered.");
            return;
        }
        for (Enrollment e : enrollments) {
            System.out.printf("  ID: %s | Name: %s | Course: %s | Attendance: %.0f%%%n",
                    e.getStudent().getId(), e.getStudent().getName(), e.getCourse().getName(), e.getAttendancePercent());
        }
    }

    private static void teacherPutMark(Teacher teacher) throws IOException {
        System.out.print(teacher.getLanguageMessage("Student ID: ", "ID студента: ", "Student ID-і: "));
        String sId = reader.readLine().trim();
        System.out.print(teacher.getLanguageMessage("Course code: ", "Код курса: ", "Kurs kody: "));
        String cCode = reader.readLine().trim();
        
        Semester sem = localChooseSemester(teacher); 
        if (sem == null) return;

        Student student = localFindStudent(sId, teacher);
        Course course = localFindCourse(cCode, teacher);
        
        if (student == null || course == null) return;

        Enrollment enrollment = course.findEnrollment(student, sem);
        if (enrollment == null) {
            System.out.println(teacher.getLanguageMessage("Student not enrolled.", "Студент не записан.", "Student tіrkelmegen."));
            return;
        }

        try {
            System.out.print("Att1 (0-30): ");  double a1 = Double.parseDouble(reader.readLine());
            System.out.print("Att2 (0-30): ");  double a2 = Double.parseDouble(reader.readLine());
            System.out.print("Final (0-40): "); double fe = Double.parseDouble(reader.readLine());
            
            CourseController.putMark(teacher, student, course, sem, new Mark(a1, a2, fe));
            successMsg(teacher.getLanguageMessage("Mark successfully added!", "Оценка успешно выставлена!", "Bağa sáttі qoıyldy!"));
        } catch (NumberFormatException e) {
            System.out.println(teacher.getLanguageMessage("Invalid input.", "Ошибка ввода.", "Qațe engіzu."));
        }
    }

    private static void teacherAttendance(Teacher teacher) throws IOException {
        System.out.print(teacher.getLanguageMessage("Student ID: ", "ID студента: ", "Student ID-і: "));
        String sId = reader.readLine().trim();
        System.out.print(teacher.getLanguageMessage("Course code: ", "Код курса: ", "Kurs kody: "));
        String cCode = reader.readLine().trim();
        
        Semester sem = localChooseSemester(teacher);
        if (sem == null) return;

        Student student = localFindStudent(sId, teacher);
        Course course = localFindCourse(cCode, teacher);
        if (student == null || course == null) return;

        Enrollment enrollment = course.findEnrollment(student, sem);
        if (enrollment == null) {
            System.out.println(teacher.getLanguageMessage("Enrollment not found.", "Запись не найдена.", "Tіrkelu tabylmady."));
            return;
        }

        System.out.print(teacher.getLanguageMessage("Lesson number (1-15): ", "Номер занятия (1-15): ", "Sabaq nómіrі (1-15): "));
        try {
            int lesson = Integer.parseInt(reader.readLine().trim());
            System.out.print(teacher.getLanguageMessage("Present? (y/n): ", "Присутствует? (y/n): ", "Qatysty ma? (ı/j): "));
            boolean present = reader.readLine().trim().equalsIgnoreCase("y");
            
            CourseController.markAttendance(teacher, enrollment, lesson, present);
            successMsg(teacher.getLanguageMessage("Attendance marked!", "Посещаемость отмечена!", "Qatysu belgіlendі!"));
        } catch (NumberFormatException e) {
            System.out.println(teacher.getLanguageMessage("Invalid number.", "Неверное число.", "Qațe san."));
        }
    }

    private static void sendComplaintMenu(Teacher teacher) throws IOException {
        System.out.print(teacher.getLanguageMessage("Enter Student ID: ", "ID студента: ", "Student ID-і: "));
        String sId = reader.readLine().trim();
        
        Student student = localFindStudent(sId, teacher);
        if (student == null) return;

        System.out.print(teacher.getLanguageMessage("Enter complaint text: ", "Текст жалобы: ", "Şağym mátіnі: "));
        String text = reader.readLine().trim();

        System.out.println(teacher.getLanguageMessage("Select Urgency Level: 1-LOW, 2-MEDIUM, 3-HIGH", "1-LOW, 2-MEDIUM, 3-HIGH", "1-LOW, 2-MEDIUM, 3-HIGH"));
        UrgencyLevel level = switch (reader.readLine().trim()) {
            case "2" -> UrgencyLevel.MEDIUM;
            case "3" -> UrgencyLevel.HIGH;
            default  -> UrgencyLevel.LOW;
        };

        CourseController.sendComplaint(teacher, student, text, level);
        successMsg(teacher.getLanguageMessage("Complaint sent!", "Жалоба отправлена!", "Şağym jіberіldі!"));
    }

    private static void sendMessageMenu(Teacher teacher) throws IOException {
        System.out.print(teacher.getLanguageMessage("Enter Employee Login: ", "Введите логин сотрудника: ", "Qyzmetker logınіn engіzіñіz: "));
        String login = reader.readLine().trim();
        
        Employee receiver = DBContext.getUsers().stream()
                .filter(u -> u instanceof Employee && ((Employee) u).getLogin().equals(login))
                .map(u -> (Employee) u)
                .findFirst().orElse(null);

        if (receiver == null) {
            System.out.println("Employee not found.");
            return;
        }

        System.out.print("Message text: ");
        String text = reader.readLine().trim();
        
        CourseController.sendMessage(teacher, receiver, text);
        successMsg("Message sent!");
    }

    private static void handleResearcherOption(Teacher teacher) throws IOException {
        if (teacher.isResearcher()) {
            System.out.println("Redirecting to Research Cabinet...");
        } else {
            teacher.setResearcher(true);
            DBContext.save();
            successMsg(teacher.getLanguageMessage("You are now a researcher!", "Теперь вы являетесь исследователем!", "Endі sіz zertteuşіsіz!"));
        }
    }


    private static Semester localChooseSemester(User user) throws IOException {
        System.out.print(user.getLanguageMessage("Semester (1-FALL 2-SPRING 3-SUMMER): ", "Семестр (1-FALL 2-SPRING 3-SUMMER): ", "Semestr (1-FALL 2-SPRING 3-SUMMER): "));
        switch (reader.readLine().trim()) {
            case "1" -> { return Semester.FALL; }
            case "2" -> { return Semester.SPRING; }
            case "3" -> { return Semester.SUMMER; }
            default -> { System.out.println("Invalid semester."); return null; }
        }
    }

    private static Student localFindStudent(String id, User user) {
        for (UserComponent uc : DBContext.getUsers()) {
            User baseUser = (uc instanceof User) ? (User) uc : ((ResearchDecorator) uc).getBaseUser();
            if (baseUser instanceof Student && baseUser.getId().equals(id)) {
                return (Student) baseUser;
            }
        }
        System.out.println(user.getLanguageMessage("Student not found: ", "Студент не найден: ", "Student tabylmady: ") + id);
        return null;
    }

    private static Course localFindCourse(String code, User user) {
        Course c = DBContext.getCourses().stream()
                .filter(x -> code.equalsIgnoreCase(x.getCourseCode()))
                .findFirst().orElse(null);
        if (c == null) {
            System.out.println(user.getLanguageMessage("Course not found: ", "Курс не найден: ", "Kurs tabylmady: ") + code);
        }
        return c;
    }
}













