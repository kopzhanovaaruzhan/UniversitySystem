package controllers;

import models.*;
import enums.Semester;
import enums.LessonType;
import enums.Faculty;
import core.DBContext;
import exceptions.CourseRegistrationException;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

public class CourseController {

    public static void registerStudent(Student student, String courseCode, Semester semester) throws CourseRegistrationException {
        Course found = DBContext.getCourses().stream()
                .filter(c -> courseCode.equalsIgnoreCase(c.getCourseCode()))
                .findFirst()
                .orElse(null);

        if (found == null) {
            throw new CourseRegistrationException("Course not found with code: " + courseCode);
        }

        student.registerForCourse(found, semester);
        DBContext.save(); 
    }


    public static void createCourse(String code, String name, int credits, Faculty faculty) {
        Course newCourse = new Course(code, name, credits, faculty);
        DBContext.getCourses().add(newCourse);
        DBContext.save();
    }


    public static boolean assignTeacherToCourse(String courseCode, String teacherLogin, LessonType lessonType) {
        Course course = DBContext.getCourses().stream()
                .filter(c -> c.getCourseCode().equalsIgnoreCase(courseCode))
                .findFirst().orElse(null);

        User baseUser = DBContext.getUsers().stream()
                .map(uc -> (uc instanceof User) ? (User) uc : ((ResearchDecorator) uc).getBaseUser())
                .filter(u -> u != null && u.getLogin().equals(teacherLogin))
                .findFirst().orElse(null);

        if (course == null || !(baseUser instanceof Teacher)) {
            return false;
        }

        Teacher teacher = (Teacher) baseUser;
        course.addInstructor(teacher, lessonType);
        DBContext.save();
        return true;
    }


    public static void approveStudentRegistration(Manager manager, Student student, Course course, Semester semester) {
        manager.approveRegistration(student, course, semester);
        DBContext.save();
    }


    public static Map<Faculty, List<Student>> getCourseReportByFaculty(Course course) {
        Map<Faculty, List<Student>> report = new HashMap<>();
        for (Enrollment e : course.getEnrollments()) {
            Student s = e.getStudent();
            report.computeIfAbsent(s.getFaculty(), _ -> new ArrayList<>()).add(s);
        }
        return report;
    }


    public static List<Student> getStudentsSortedByGpa() {
        return DBContext.getUsers().stream()
                .filter(u -> u instanceof Student)
                .map(u -> (Student) u)
                .sorted((s1, s2) -> Double.compare(s2.getGpa(), s1.getGpa()))
                .toList();
    }

    public static boolean rateTeacher(Student student, Teacher teacher, Course course, int rating) {
        boolean success = student.rateTeacherLogic(teacher, course, rating);
        if (success) {
            DBContext.save();
        }
        return success;
    }

    public static void putMark(Teacher teacher, Student student, Course course, Semester semester, Mark mark) {
        teacher.putMark(student, course, semester, mark);
        student.recalculateGpa();
        DBContext.save();
    }
    
    public static void markAttendance(Teacher teacher, Enrollment enrollment, int lesson, boolean present) {
        teacher.markAttendance(enrollment, lesson, present);
        DBContext.save();
    }
    
    public static void sendComplaint(Teacher teacher, Student student, String text, enums.UrgencyLevel level) {
        Complaint complaint = teacher.createComplaint(student, text, level);
        DBContext.addComplaint(complaint); 
        DBContext.save();
    }

    public static List<Course> getTeacherCourses(Teacher teacher) {
        return DBContext.getCourses().stream()
                .filter(c -> c.getTeachers().contains(teacher))
                .toList();
    }

    public static List<Enrollment> getTeacherStudents(Teacher teacher) {
        List<Course> teacherCourses = getTeacherCourses(teacher);
        return DBContext.getUsers().stream()
                .filter(u -> u instanceof Student)
                .map(u -> (Student) u)
                .flatMap(s -> s.getRegistrations().stream())
                .filter(e -> teacherCourses.contains(e.getCourse()))
                .toList();
    }

    public static void sendMessage(Employee sender, Employee receiver, String text) {
        DBContext.getLogs().add(String.format("[MSG] From %s to %s: %s", sender.getName(), receiver.getName(), text));
        DBContext.save();
    }
}






