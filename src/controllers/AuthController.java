package controllers;

import core.DBContext;
import models.UserComponent;
import models.User;
import models.ResearchDecorator;

public class AuthController {
    private static UserComponent currentUser;

    public static UserComponent login(String username, String password) {
        for (UserComponent uc : DBContext.getUsers()) {
            User baseUser = (uc instanceof ResearchDecorator) 
                            ? ((ResearchDecorator)uc).getBaseUser() 
                            : (User)uc;

            if (baseUser.getLogin().equals(username) && baseUser.getPassword().equals(password)) {
                currentUser = uc;
                DBContext.addLog("User logged in: " + baseUser.getName());
                return uc;
            }
        }
        return null;
    }

    public static void logout() {
        if (currentUser != null) {
            DBContext.addLog("User logged out.");
            DBContext.save();
            currentUser = null;
        }
    }
}

