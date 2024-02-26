package model;

import java.io.Serializable;
import java.util.Date;

public class ToDoItem implements Serializable {
    private String title;
    private String description;
    private Date dueDate;
    private Importance importance;

    public enum Importance {
        LOW, MEDIUM, HIGH
    }

    public ToDoItem(String title, String description, Date dueDate, Importance importance) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.importance = importance;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Importance getImportance() {
        return importance;
    }

    public void setImportance(Importance importance) {
        this.importance = importance;
    }

    // Add toString method for displaying in UI
    @Override
    public String toString() {
        return String.format("[%s] %s - %s (Due: %s)", importance, title, description, dueDate.toString());
    }
}
