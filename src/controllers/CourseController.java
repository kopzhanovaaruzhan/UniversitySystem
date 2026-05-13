package controllers;

import core.DBContext;
import models.Course;
import enums.Faculty;

public class CourseController {

    public static Course createCourse(String code, String name, int credits, Faculty faculty) {
        Course newCourse = new Course(code, name, credits, faculty);
        DBContext.addCourse(newCourse);
        DBContext.addLog("New course created: " + name + " [" + code + "]");
        DBContext.save();
        
        return newCourse;
    }

   
    public static boolean courseExists(String code) {
        return DBContext.getCourses().stream()
                .anyMatch(c -> c.getCourseCode().equalsIgnoreCase(code));
    }
}