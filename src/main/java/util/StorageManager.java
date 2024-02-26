package util;

import model.ToDoItem;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StorageManager {
    private static final String STORAGE_FILE = "todos.ser";

    public void saveToDoItem(ToDoItem item) {
        List<ToDoItem> items = loadAllToDoItems();
        items.add(item);
        saveAllToDoItems(items);
    }

    public void deleteToDoItem(ToDoItem item) {
        List<ToDoItem> items = loadAllToDoItems();
        items.remove(item);
        saveAllToDoItems(items);
    }

    public List<ToDoItem> loadAllToDoItems() {
        List<ToDoItem> items = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(STORAGE_FILE))) {
            items = (List<ToDoItem>) ois.readObject();
        } catch (FileNotFoundException e) {
            // First use, or file deleted. Ignore.
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return items;
    }

    private void saveAllToDoItems(List<ToDoItem> items) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(STORAGE_FILE))) {
            oos.writeObject(items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
