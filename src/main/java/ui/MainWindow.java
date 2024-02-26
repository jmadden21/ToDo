package ui;

import model.ToDoItem;
import model.ToDoItem.Importance;
import controller.ToDoController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Calendar;
import java.util.Date;

public class MainWindow extends JFrame {
    private ToDoController controller;
    private JList<ToDoItem> toDoList;
    private DefaultListModel<ToDoItem> toDoListModel;

    public MainWindow(ToDoController controller) {
        this.controller = controller;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("To-Do List Application");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());
        toDoListModel = new DefaultListModel<>();
        toDoList = new JList<>(toDoListModel);
        toDoList.setCellRenderer(new ToDoItemRenderer());

        add(new JScrollPane(toDoList), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);

        refreshToDoList();
    }

    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton addButton = new JButton("Add To-Do");
        addButton.addActionListener(this::showAddToDoDialog);
        bottomPanel.add(addButton);

        // Add a Delete button
        JButton deleteButton = new JButton("Delete To-Do");
        deleteButton.addActionListener(this::deleteToDoItem);
        bottomPanel.add(deleteButton);

        return bottomPanel;
    }

    private void showAddToDoDialog(ActionEvent event) {
        JTextField titleField = new JTextField(20);
        JTextField descriptionField = new JTextField(20);

        // Date picker component
        SpinnerDateModel dateModel = new SpinnerDateModel();
        JSpinner dateSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy");
        dateSpinner.setEditor(dateEditor);

        // Time picker component
        SpinnerDateModel timeModel = new SpinnerDateModel();
        JSpinner timeSpinner = new JSpinner(timeModel);
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);

        JComboBox<Importance> importanceComboBox = new JComboBox<>(Importance.values());

        Object[] message = {
                "Title:", titleField,
                "Description:", descriptionField,
                "Select Date:", dateSpinner,
                "Select Time:", timeSpinner,
                "Importance:", importanceComboBox,
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add New To-Do", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            Calendar dateCalendar = Calendar.getInstance();
            dateCalendar.setTime((Date)dateSpinner.getValue());

            Calendar timeCalendar = Calendar.getInstance();
            timeCalendar.setTime((Date)timeSpinner.getValue());

            dateCalendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY));
            dateCalendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));

            ToDoItem item = new ToDoItem(
                    titleField.getText(),
                    descriptionField.getText(),
                    dateCalendar.getTime(),
                    (Importance) importanceComboBox.getSelectedItem()
            );
            controller.addToDoItem(item);
            refreshToDoList();
        }
    }

    private void refreshToDoList() {
        toDoListModel.clear();
        for (ToDoItem item : controller.getAllToDoItems()) {
            toDoListModel.addElement(item);
        }
    }

    private static class ToDoItemRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof ToDoItem) {
                ToDoItem item = (ToDoItem) value;
                setText(item.toString());
                switch (item.getImportance()) {
                    case HIGH:
                        setBackground(Color.RED);
                        setForeground(Color.WHITE);
                        break;
                    case MEDIUM:
                        setBackground(Color.YELLOW);
                        setForeground(Color.BLACK);
                        break;
                    case LOW:
                        setBackground(Color.GREEN);
                        setForeground(Color.BLACK);
                        break;
                }
            }
            return c;
        }
    }

    private void deleteToDoItem(ActionEvent event) {
        int selectedIndex = toDoList.getSelectedIndex();
        if (selectedIndex != -1) {
            // Remove from the UI list
            ToDoItem itemToRemove = toDoListModel.getElementAt(selectedIndex);
            toDoListModel.removeElementAt(selectedIndex);

            // Remove from the controller/storage
            controller.deleteToDoItem(itemToRemove);

            JOptionPane.showMessageDialog(this, "To-Do item deleted successfully.");
        } else {
            JOptionPane.showMessageDialog(this, "Please select a To-Do item to delete.");
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ToDoController controller = new ToDoController();
            MainWindow mainWindow = new MainWindow(controller);
            mainWindow.setVisible(true);
        });
    }
}
