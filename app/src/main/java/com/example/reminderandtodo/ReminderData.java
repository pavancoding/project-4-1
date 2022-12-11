package com.example.reminderandtodo;

public class ReminderData {
    int image;
    String reminder_id;
    String TimeLeft;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ReminderData(int image, String reminder_id, String timeLeft, String type, String name, String[] days) {
        this.image = image;
        this.reminder_id = reminder_id;
        TimeLeft = timeLeft;
        this.type = type;
        Name = name;
        this.days = days;
    }

    String type;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    String Name;
    String days[];


    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getReminder_id() {
        return reminder_id;
    }

    public void setReminder_id(String reminder_id) {
        this.reminder_id = reminder_id;
    }

    public String getTimeLeft() {
        return TimeLeft;
    }

    public void setTimeLeft(String timeLeft) {
        TimeLeft = timeLeft;
    }

    public String[] getDays() {
        return days;
    }

    public void setDays(String[] days) {
        this.days = days;
    }



}
