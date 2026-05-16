package models;

import java.util.*;

import enums.Language;

public class ResearchDecorator extends UserDecorator {
    private static final long serialVersionUID = 1L;
    
    private List<ResearchPaper> papers = new ArrayList<>();

    public ResearchDecorator(UserComponent user) {
        super(user);
    }

    public void publishPaper(ResearchPaper paper) {
        papers.add(paper);
    }

    public void joinProject(ResearchProject project) {
        project.addMember(this);
    }
    
    public List<ResearchPaper> getPapers() {
        return this.papers; 
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
    
    @Override
    public void switchLanguage(Language lang) {
        component.switchLanguage(lang);
    }

    @Override
    public Language getCurrentLanguage() {
        return component.getCurrentLanguage();
    }

    @Override
    public String getLanguageMessage(String en, String ru, String kz) {
        return component.getLanguageMessage(en, ru, kz);
    }

}


