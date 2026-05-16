package views;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class BaseView {
	protected static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

	
	public static void successMsg(String details) {
		System.out.println("SUCCESS: " + details);
	}
	
	protected static void switchLang(models.UserComponent user) throws java.io.IOException {
	    System.out.print("1-ENG 2-RUS 3-KAZ > ");
	    String choice = reader.readLine().trim(); 
	    
	    enums.Language newLang = switch (choice) {
	        case "1" -> enums.Language.ENG;
	        case "2" -> enums.Language.RUS;
	        case "3" -> enums.Language.KAZ;
	        default  -> null;
	    };

	    if (newLang != null) {
	        user.switchLanguage(newLang);
	    } else {
	        System.out.println("Invalid choice.");
	    }
	}
	
	/**
    protected static void handleViewNews(models.User user) {
        System.out.println();
        models.NewsService.getInstance().printNews(user);
    }

 static void handleResearchOption(models.User currentUser) throws java.io.IOException {
        boolean isAlreadyResearcher = (currentUser instanceof models.ResearchDecorator);
        
        if (!isAlreadyResearcher && currentUser instanceof models.Teacher) {
            isAlreadyResearcher = ((models.Teacher) currentUser).isResearcher();
        }

        if (isAlreadyResearcher) {
            System.out.println("\nRedirecting to Research Cabinet...");
            models.ResearchDecorator decorator = (currentUser instanceof models.ResearchDecorator) ? 
                    (models.ResearchDecorator) currentUser : new models.ResearchDecorator(currentUser);
            
            ResearchView.showMenu(decorator);
        } else {
            if (currentUser instanceof models.Teacher) {
                ((models.Teacher) currentUser).setResearcher(true);
            } else {
                System.out.println(currentUser.getLanguageMessage(
                    "Requesting researcher status...", 
                    "Подача заявки на статус исследователя...", 
                    "Zertteuşіstatusyna suranys beru..."));
            }
            
            successMsg(currentUser.getLanguageMessage(
                "You have successfully become a researcher! Re-login to access the cabinet.", 
                "Вы успешно стали исследователем! Перезайдите в систему для доступа в кабинет.", 
                "Sіz sáttі zertteuşі boldyñyz! Jưıege qaıta kіrіñіz."));
                
            core.DBContext.save();
        }
    }
    */
}