package controllers;

import models.*;
import enums.Semester;
import exceptions.CourseRegistrationException;

public class StudentController {
    
    public static void registerStudentForCourse(Student student, Course course, Semester semester) {
        try {
            student.registerForCourse(course, semester);
            core.DBContext.save();
        } catch (CourseRegistrationException e) {
            System.err.println("Registration failed: " + e.getMessage());
        }
    }
}