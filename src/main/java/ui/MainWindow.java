package ui;

import model.ToDoItem;
import model.ToDoItem.Importance;
import controller.ToDoController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
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

        return bottomPanel;
    }

    private void showAddToDoDialog(ActionEvent event) {
        JTextField titleField = new JTextField(20);
        JTextField descriptionField = new JTextField(20);
        SpinnerDateModel dateModel = new SpinnerDateModel();
        JSpinner dateSpinner = new JSpinner(dateModel);
        JComboBox<Importance> importanceComboBox = new JComboBox<>(Importance.values());

        Object[] message = {
                "Title:", titleField,
                "Description:", descriptionField,
                "Due Date:", dateSpinner,
                "Importance:", importanceComboBox,
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add New To-Do", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            ToDoItem item = new ToDoItem(
                    titleField.getText(),
                    descriptionField.getText(),
                    dateModel.getDate(),
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
                        break;
                    case MEDIUM:
                        setBackground(Color.YELLOW);
                        break;
                    case LOW:
                        setBackground(Color.GREEN);
                        break;
                }
            }
            return c;
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
