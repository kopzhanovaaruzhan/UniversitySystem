package models;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Employee sender;
    private Employee receiver;
    private String text;
    private boolean official;
    private LocalDateTime date;

    public Message(Employee sender, Employee receiver, String text, boolean official) {
        this.sender = sender;
        this.receiver = receiver;
        this.text = text;
        this.official = official;
        this.date = LocalDateTime.now();
    }
    public String toString() {
        String type = official ? "[OFFICIAL]" : "[MESSAGE]";
        return type + " From: " + sender.getName() +
                " To: " + receiver.getName() +
                " Text: " + text +
                " Date: " + date;
    }
}