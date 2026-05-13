package controllers;

import java.util.List;
import core.DBContext;
import models.*;

public class ResearchController {

    public static ResearchDecorator makeUserResearcher(UserComponent user) {
        if (user instanceof ResearchDecorator) {
            return (ResearchDecorator) user;
        }

        ResearchDecorator researcher = new ResearchDecorator(user);

        int index = DBContext.getUsers().indexOf(user);
        if (index != -1) {
            DBContext.getUsers().set(index, researcher);
        }

        
        DBContext.addLog("User " + user.getName() + " became a researcher.");
        DBContext.save();

        return researcher;
    }


    public static void addPaper(ResearchDecorator researcher, String title, String authors, String journal, int pages) {
        ResearchPaper paper = new ResearchPaper(title, authors, journal, pages);
        researcher.publishPaper(paper);
        
        DBContext.addLog("Researcher " + researcher.getName() + " published a new paper: " + title);
        DBContext.save();
    }
    
    public static List<ResearchProject> getProjectsForUser(UserComponent user) {
        return DBContext.getProjects().stream()
                .filter(p -> p.getMembers().contains(user))
                .toList();
    }
    
    
}