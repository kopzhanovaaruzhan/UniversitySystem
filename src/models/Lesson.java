package models;

import java.util.Map;

import enums.LessonType;

public class Lesson {
    private int hours;
    private LessonType type;
    private Map<String, Boolean> attendence;
    
	public int getHours() {
		return hours;
	}
	public void setHours(int hours) {
		this.hours = hours;
	}
	public Map<String, Boolean> getAttendence() {
		return attendence;
	}
	public void setAttendence(Map<String, Boolean> attendence) {
		this.attendence = attendence;
	}
	public LessonType getType() {
		return type;
	}
	public void setType(LessonType type) {
		this.type = type;
	}

}

