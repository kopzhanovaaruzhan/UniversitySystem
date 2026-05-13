package models;

import java.util.*;
import core.DBContext;

public class ResearchDecorator extends UserDecorator {
    private static final long serialVersionUID = 1L;
    
    private List<ResearchPaper> papers = new ArrayList<>();

    public ResearchDecorator(UserComponent user) {
        super(user);
    }

    public void publishPaper(ResearchPaper paper) {
        papers.add(paper);
        System.out.println("Researcher " + getName() + " published: " + paper.getTitle());
    }

    public void joinProject(ResearchProject project) {
        project.addMember(this);
        
        String msg = getBaseUser().getLanguageMessage(
            "Successfully joined: ", 
            "Вы вступили в проект: ", 
            "Жобаға қосылдыңыз: "
        );
        System.out.println(msg + project.getTopic());
    }

    public List<ResearchProject> getMyProjects() {
        return DBContext.getProjects().stream()
                .filter(p -> p.getMembers().contains(this))
                .toList();
    }

    public void viewMyPapers() {
        if (papers.isEmpty()) {
            System.out.println(getBaseUser().getLanguageMessage(
                "No papers.", "У вас нет работ.", "Жұмыстар жоқ."));
            return;
        }
        printPapers(Comparator.comparing(ResearchPaper::getTitle));
    }

    public void printPapers(Comparator<ResearchPaper> c) {
        papers.sort(c);
        for (ResearchPaper paper : papers) {
            System.out.println(paper);
        }
    }


    public User getBaseUser() {
        UserComponent current = this.component;
        while (current instanceof UserDecorator) {
            current = ((UserDecorator) current).getComponent();
        }
        return (User) current;
    }


    public String getName() {
        return getBaseUser().getName();
    }

    public int calculateHIndex() {
        if (papers.isEmpty()) return 0;
        List<Integer> citations = papers.stream()
                .map(ResearchPaper::getCitationsCount)
                .sorted(Comparator.reverseOrder())
                .toList();

        int h = 0;
        for (int i = 0; i < citations.size(); i++) {
            if (citations.get(i) >= i + 1) h = i + 1;
            else break;
        }
        return h;
    }
}


