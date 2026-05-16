package views;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import models.*;
import controllers.CourseController;
import core.DBContext;
import enums.Faculty;
import enums.LessonType;
import enums.Semester;

public class ManagerView extends BaseView {

    public static void show(Manager manager) throws IOException {
        boolean running = true;
        while (running) {
            printMenu(manager);
            String choice = reader.readLine().trim();
            switch (choice) {
                case "1" -> viewAllCourses(manager);
                case "2" -> approveRegistrationsMenu(manager);
                case "3" -> assignTeacherMenu(manager);
                case "4" -> publishNewsMenu(manager);
                case "5" -> viewReportsMenu(manager);
                case "6" -> manager.viewInfo();
                case "7" -> switchLang(manager);
                case "0" -> running = false;
                default  -> System.out.println("Unknown option.");
            }
        }
    }

    private static void printMenu(Manager manager) {
        String lang = manager.getCurrentLanguage().name();
        System.out.println("\n╔═══════════════════════════════════════╗");
        System.out.printf ("║  %-37s║%n", manager.getName() + " [" + manager.getType() + "]");
        System.out.println("╠═══════════════════════════════════════╣");
        option("1", manager.getLanguageMessage("View All Courses", "Просмотр всех курсов", "Barlyq kurstardy kóru"));
        option("2", manager.getLanguageMessage("Approve Registrations", "Одобрение регистраций", "Tіrkeludі maqýldaý"));
        option("3", manager.getLanguageMessage("Assign Teacher to Course", "Назначить преподавателя", "Oqytýşyny tağaıyndaý"));
        option("4", manager.getLanguageMessage("Publish News", "Опубликовать новость", "Jañaqyqtar jaryqqa şyğarý"));
        option("5", manager.getLanguageMessage("Academic Reports", "Статистические отчеты", "Ylgerіm esepterі"));
        option("6", manager.getLanguageMessage("View My Info", "Моя информация", "Menіñ málemetterіm"));
        option("7", manager.getLanguageMessage("Language: ", "Язык: ", "Tіl: ") + lang);
        option("0", manager.getLanguageMessage("Logout", "Выйти", "Şyğu"));
        System.out.println("╚═══════════════════════════════════════╝");
        System.out.print(manager.getLanguageMessage("Choice: ", "Выбор: ", "Tañdau: "));
    }

    private static void option(String key, String label) {
        System.out.printf("║  %s - %-33s║%n", key, label);
    }

    private static void viewAllCourses(Manager manager) {
        System.out.println("\n=== " + manager.getLanguageMessage("University Courses", "Курсы университета", "Ýnıversıtet kurstary") + " ===");
        if (DBContext.getCourses().isEmpty()) {
            System.out.println("No courses found.");
            return;
        }
        for (Course c : DBContext.getCourses()) {
            System.out.println(c + " | Enrolled: " + c.getEnrollments().size() + " | Pending: " + c.getPendingStudents().size());
        }
    }

    private static void approveRegistrationsMenu(Manager manager) throws IOException {
        System.out.println("\n--- " + manager.getLanguageMessage("Pending Registrations", "Ожидающие регистрации", "Kútіp turğan tіrkeluler") + " ---");
        
        List<Course> pendingCourses = DBContext.getCourses().stream()
                .filter(c -> !c.getPendingStudents().isEmpty())
                .toList();

        if (pendingCourses.isEmpty()) {
            System.out.println("No pending registrations.");
            return;
        }

        for (int i = 0; i < pendingCourses.size(); i++) {
            Course c = pendingCourses.get(i);
            System.out.println((i + 1) + ". " + c.getName() + " (" + c.getCourseCode() + ") -> " + c.getPendingStudents().size() + " students");
        }

        System.out.print("> Select course number: ");
        try {
            int cIdx = Integer.parseInt(reader.readLine().trim()) - 1;
            if (cIdx >= 0 && cIdx < pendingCourses.size()) {
                Course selectedCourse = pendingCourses.get(cIdx);
                List<Student> students = selectedCourse.getPendingStudents();

                for (int j = 0; j < students.size(); j++) {
                    System.out.println((j + 1) + ". " + students.get(j).getName() + " [" + students.get(j).getFaculty() + "]");
                }

                System.out.print("> Select student number to APPROVE: ");
                int sIdx = Integer.parseInt(reader.readLine().trim()) - 1;

                if (sIdx >= 0 && sIdx < students.size()) {
                    Student targetStudent = students.get(sIdx);
                    
                    // Менеджер выбирает семестр для зачисления
                    System.out.print("Semester (1-FALL, 2-SPRING, 3-SUMMER): ");
                    Semester sem = switch (reader.readLine().trim()) {
                        case "2" -> Semester.SPRING;
                        case "3" -> Semester.SUMMER;
                        default  -> Semester.FALL;
                    };

                    CourseController.approveStudentRegistration(manager, targetStudent, selectedCourse, sem);
                    successMsg(manager.getLanguageMessage("Registration successfully approved!", "Регистрация успешно одобрена!", "Tіrkelu sáttі maqýldandy!"));
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid selection.");
        }
    }

    private static void assignTeacherMenu(Manager manager) throws IOException {
        System.out.print(manager.getLanguageMessage("Enter Course Code: ", "Введите код курса: ", "Kurs коdyn engіzіñіz: "));
        String courseCode = reader.readLine().trim();
        System.out.print(manager.getLanguageMessage("Enter Teacher Login: ", "Введите логин преподавателя: ", "Oqytýşy logınіn engіzіñіz: "));
        String teacherLogin = reader.readLine().trim();

        System.out.println("Select Lesson Type: 1-LECTURE, 2-PRACTICE");
        System.out.print("> ");
        LessonType type = reader.readLine().trim().equals("2") ? LessonType.PRACTICE : LessonType.LECTURE;

        boolean success = CourseController.assignTeacherToCourse(courseCode, teacherLogin, type);
        if (success) {
            successMsg(manager.getLanguageMessage("Teacher successfully assigned!", "Преподаватель успешно назначен!", "Oqytýşy sáttі tağaıyndaldy!"));
        } else {
            System.out.println("Assignment failed. Check code or login.");
        }
    }

    private static void publishNewsMenu(Manager manager) throws IOException {
        System.out.println("\n--- " + manager.getLanguageMessage("Publish University News", "Публикация новости", "Jañaqyq taratý") + " ---");
        System.out.print("Enter Content/Text: ");
        String content = reader.readLine().trim();

        System.out.println("Select Topic Type: 1-GENERAL, 2-ACADEMIC, 3-RESEARCH");
        System.out.print("> ");
        enums.NewsTopic topic = switch (reader.readLine().trim()) {
            case "2" -> enums.NewsTopic.ACADEMIC;
            case "3" -> enums.NewsTopic.RESEARCH;
            default  -> enums.NewsTopic.GENERAL;
        };

        if (!content.isEmpty()) {
            // Вызываем правильный откомпилированный NewsController
            controllers.NewsController.publishNews(content, topic);
            successMsg(manager.getLanguageMessage("News published successfully!", "Новость успешно опубликована!", "Jañaqyq sáttі taratyldy!"));
        } else {
            System.out.println("Content cannot be empty.");
        }
    }

    private static void viewReportsMenu(Manager manager) throws IOException {
        System.out.println("\n1 - " + manager.getLanguageMessage("Performance Report (by GPA)", "Отчет по успеваемости (по GPA)", "Ylgerіm esebі (GPA boıynşa)"));
        System.out.println("2 - " + manager.getLanguageMessage("Course Diversity Report (by Faculty)", "Междисциплинарный отчет (по Факультетам)", "Kýrs quramynyñ esebі"));
        System.out.print("> ");
        
        String choice = reader.readLine().trim();
        if ("1".equals(choice)) {
            System.out.println("\n--- " + manager.getLanguageMessage("Students Sorted by GPA", "Рейтинг студентов по GPA", "Studentter reıtıngі") + " ---");
            List<Student> sortedStudents = CourseController.getStudentsSortedByGpa();
            sortedStudents.forEach(s -> System.out.printf("  %-25s | GPA: %.2f | Faculty: %s%n", s.getName(), s.getGpa(), s.getFaculty()));
        } 
        else if ("2".equals(choice)) {
            System.out.print(manager.getLanguageMessage("Enter Course Code: ", "Введите код курса: ", "Kurs коdyn engіzіñіz: "));
            String code = reader.readLine().trim();
            Course course = DBContext.getCourses().stream().filter(c -> c.getCourseCode().equalsIgnoreCase(code)).findFirst().orElse(null);
            
            if (course == null) {
                System.out.println("Course not found.");
                return;
            }

            System.out.println("\n--- " + manager.getLanguageMessage("Course Distribution by Faculty for: ", "Распределение студентов по факультетам для: ", "Fakýltetaralyq tіrkelý kórsetkіşі: ") + course.getName() + " ---");
            Map<Faculty, List<Student>> facultyReport = CourseController.getCourseReportByFaculty(course);
            
            if (facultyReport.isEmpty()) {
                System.out.println("No active enrollments on this course.");
                return;
            }

            facultyReport.forEach((faculty, studentList) -> {
                System.out.println("\n  🔹 Faculty: " + faculty + " (" + studentList.size() + " students)");
                studentList.forEach(s -> System.out.println("    - " + s.getName() + " (GPA: " + s.getGpa() + ")"));
            });
        }
    }
}






