package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResearchProject implements Serializable {
    private static final long serialVersionUID = 1L;

    private String topic;
    private List<ResearchPaper> publishedPapers;
    private List<UserComponent> members;

    public ResearchProject(String topic) {
        this.topic = topic;
        this.publishedPapers = new ArrayList<>();
        this.members = new ArrayList<>();
    }


    public void addMember(UserComponent user) {
        if (!members.contains(user)) {
            members.add(user);
            System.out.println("User added to project: " + topic);
        }
    }

    public void addPaper(ResearchPaper paper) {
        publishedPapers.add(paper);
    }


    public String getTopic() {
        return topic;
    }

    public List<ResearchPaper> getPublishedPapers() {
        return publishedPapers;
    }

    public List<UserComponent> getMembers() {
        return members;
    }

    @Override
    public String toString() {
        return "Research Project: " + topic + " (Members: " + members.size() + ")";
    }
}
