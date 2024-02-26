package controller;

import model.ToDoItem;
import util.StorageManager;

import java.util.List;

public class ToDoController {
    private StorageManager storageManager;

    public ToDoController() {
        storageManager = new StorageManager();
    }

    public void addToDoItem(ToDoItem item) {
        storageManager.saveToDoItem(item);
        // Additional logic for setting reminders can be added here
    }

    public void deleteToDoItem(ToDoItem item) {
        storageManager.deleteToDoItem(item);
    }

    public List<ToDoItem> getAllToDoItems() {
        return storageManager.loadAllToDoItems();
    }

    // Additional methods for sorting, filtering, and reminders can be added here
}
