package models;

import java.io.Serializable;
import java.util.*;

import enums.NewsTopic;

public class News implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String content;
    private NewsTopic topic;
    private boolean pinned;
    private List<User> observers = new ArrayList<>();
    
    public News(String content, NewsTopic topic) {
        this.content = content;
        this.topic = topic;
        this.pinned = topic == NewsTopic.RESEARCH;
    }
    
    public void subscribe(User user) {
        observers.add(user);
    }

    public void unsubscribe(User user) {
        observers.remove(user);
    }
    public void notifyObservers() {
        for (User user : observers) {
            System.out.println(user.getLanguageMessage(
                    "New news: " + content,
                    "Новая новость: " + content,
                    "Жаңа жаңалық: " + content
            ));
        }
    }
    public boolean isPinned() {
        return pinned;
    }
    public NewsTopic getTopic() {
        return topic;
    }
    public String getContent() {
        return content;
    }
    public String toString() {
        return (pinned ? "[PINNED] " : "") + topic + ": " + content;
    }
}


