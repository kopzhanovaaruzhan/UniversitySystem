package models;

import java.util.*;

public class ResearchDecorator extends UserDecorator {
    private static final long serialVersionUID = 1L;
    
    private List<ResearchPaper> papers = new ArrayList<>();

    public ResearchDecorator(UserComponent user) {
        super(user);
    }

    public void publishPaper(ResearchPaper paper) {
        papers.add(paper);
        System.out.println("Статья '" + paper + "' успешно опубликована!");
    }

    public int calculateHIndex() {
        return papers.size();
    }

    public void printPapers(Comparator<ResearchPaper> c) {
        papers.sort(c);
        papers.forEach(System.out::println);
    }
    
    public void joinProject(ResearchProject project) {
        String msg = ((User)component).getLanguageMessage(
            "Joined project: ", 
            "Присоединился к проекту: ", 
            "Жобаға қосылды: "
        );
        System.out.println(msg + project.getTopic());
    }
    
    public String getName() {
        if (component instanceof User) {
            return ((User) component).getName();
        }
        return "Unknown Researcher";
    }
}


