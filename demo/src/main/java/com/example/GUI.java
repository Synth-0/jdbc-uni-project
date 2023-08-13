// the assignment by zheer Raoof Muhammad Ali ID:1900335
//and Hani Amer Salih ID:2000431
// documentation for vector https://docs.oracle.com/javase/8/docs/api/java/util/Vector.html
// documentation for focusListener https://docs.oracle.com/javase/8/docs/api/java/awt/event/FocusListener.html
// documentation for PreparedStatement https://docs.oracle.com/javase/tutorial/jdbc/basics/prepared.html
// documentation for menuBar https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/MenuBar.html
// documentation for ResultSet https://docs.oracle.com/javase/8/docs/api/java/sql/ResultSet.html

package com.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;

public class GUI extends JFrame {

    static JTable table;
    static DefaultTableModel tableModel = new DefaultTableModel();
     database db = new database();

    public GUI() {
        setTitle("Task Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(400, 300));

        tableModel.addColumn("<html><font color='blue'>ID</font></html>");
        tableModel.addColumn("<html><font color='blue'>Title</font></html>");
        tableModel.addColumn("<html><font color='blue'>Due Date</font></html>");
        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        JMenuBar menuBar = new JMenuBar();
        JMenu taskMenu = new JMenu("<html><b>Tasks</b></html>");
        JMenuItem addMenuItem = new JMenuItem("<html><font color='blue'>Add Task</font></html>");
        JMenuItem updateMenuItem = new JMenuItem("<html><font color='blue'>Update Task</font></html>");
        JMenuItem deleteMenuItem = new JMenuItem("<html><font color='blue'>Delete Task</font></html>");

        addMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showAddTaskFrame();
            }
        });

        updateMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	updateCode();
            }
        });

        deleteMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	deleteCode();
            }
        });

        taskMenu.add(addMenuItem);
        taskMenu.add(updateMenuItem);
        taskMenu.add(deleteMenuItem);
        menuBar.add(taskMenu);

        setJMenuBar(menuBar);
        add(scrollPane, BorderLayout.CENTER);
        
        JButton addButton1 = new JButton("Add Task");
        JButton updateButton1 = new JButton("Update Task");
        JButton deleteButton1 = new JButton("Delete Task");

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addComponent(scrollPane)
                .addGroup(layout.createSequentialGroup()
                        .addComponent(addButton1)
                        .addComponent(updateButton1)
                        .addComponent(deleteButton1))
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addComponent(scrollPane)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(addButton1)
                        .addComponent(updateButton1)
                        .addComponent(deleteButton1))
        );
        
        addButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showAddTaskFrame();
            }
        });
        
        updateButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	updateCode();
            }
        });
        
        deleteButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteCode();
            }
        });

        // Load tasks from the database when the application is launched
        db.loadTasksFromDatabase();

        pack();
        setLocationRelativeTo(null);
    }

    private void showAddTaskFrame() {
        final JFrame addTaskFrame = new JFrame("Add Task");
        addTaskFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addTaskFrame.setPreferredSize(new Dimension(300, 150));

        JPanel contentPane = new JPanel();
        GroupLayout layout = new GroupLayout(contentPane);
        contentPane.setLayout(layout);

        JLabel titleLabel = new JLabel("Title:");
        final JTextField titleField = new JTextField();

        JLabel dueDateLabel = new JLabel("Due Date:");
        final JFormattedTextField dueDateField = new JFormattedTextField(java.time.format.DateTimeFormatter.ISO_DATE.toFormat());
        dueDateField.setText("YYYY-MM-DD");
        dueDateField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (dueDateField.getText().equals("YYYY-MM-DD")) {
                    dueDateField.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (dueDateField.getText().isEmpty()) {
                    dueDateField.setText("YYYY-MM-DD");
                }
            }
        });

        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText();
                String dueDateString = dueDateField.getText();

                if (title.isEmpty() || dueDateString.isEmpty()) {
                    JOptionPane.showMessageDialog(addTaskFrame,
                            "Please enter a title and due date.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    LocalDate dueDate = LocalDate.parse(dueDateString);
                    db.addTaskInDatabase(title, dueDate);
                    addTaskFrame.dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(addTaskFrame,
                            "Invalid due date format. Please enter a date in the format yyyy-mm-dd.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(titleLabel)
                .addComponent(dueDateLabel));
        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(titleField)
                .addComponent(dueDateField)
                .addComponent(addButton));
        layout.setHorizontalGroup(hGroup);

        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(titleLabel)
                .addComponent(titleField));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(dueDateLabel)
                .addComponent(dueDateField));
        vGroup.addComponent(addButton);
        layout.setVerticalGroup(vGroup);

        addTaskFrame.setContentPane(contentPane);
        addTaskFrame.pack();
        addTaskFrame.setLocationRelativeTo(null);
        addTaskFrame.setVisible(true);
    }

    private void showUpdateTaskFrame(final int taskId, String title, LocalDate dueDate) {
        final JFrame updateTaskFrame = new JFrame("Update Task");
        updateTaskFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        updateTaskFrame.setPreferredSize(new Dimension(300, 150));

        JPanel contentPane = new JPanel();
        GroupLayout layout = new GroupLayout(contentPane);
        contentPane.setLayout(layout);

        JLabel titleLabel = new JLabel("Title:");
        final JTextField titleField = new JTextField(title);

        JLabel dueDateLabel = new JLabel("Due Date:");
        final JTextField dueDateField = new JTextField(dueDate.toString());

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String newTitle = titleField.getText();
                String newDueDateString = dueDateField.getText();

                if (newTitle.isEmpty() || newDueDateString.isEmpty()) {
                    JOptionPane.showMessageDialog(updateTaskFrame,
                            "Please enter a title and due date.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    LocalDate newDueDate = LocalDate.parse(newDueDateString);
                    db.updateTaskInDatabase(taskId, newTitle, newDueDate);
                    updateTaskFrame.dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(updateTaskFrame,
                            "Invalid due date format. Please enter a date in the format yyyy-mm-dd.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(titleLabel)
                .addComponent(dueDateLabel));
        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(titleField)
                .addComponent(dueDateField)
                .addComponent(updateButton));
        layout.setHorizontalGroup(hGroup);

        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(titleLabel)
                .addComponent(titleField));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                .addComponent(dueDateLabel)
                .addComponent(dueDateField));
        vGroup.addComponent(updateButton);
        layout.setVerticalGroup(vGroup);

        updateTaskFrame.setContentPane(contentPane);
        updateTaskFrame.pack();
        updateTaskFrame.setLocationRelativeTo(null);
        updateTaskFrame.setVisible(true);
    }

    private void showDeleteConfirmationDialog(int taskId) {
        int option = JOptionPane.showConfirmDialog(GUI.this,
                "Are you sure you want to delete this task?", "Delete Task",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (option == JOptionPane.YES_OPTION) {
            db.deleteTaskFromDatabase(taskId);
        }
    }
    
    public void updateCode() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int taskId = (int) table.getValueAt(selectedRow, 0);
            String title = (String) table.getValueAt(selectedRow, 1);
            LocalDate dueDate = (LocalDate) table.getValueAt(selectedRow, 2);
            showUpdateTaskFrame(taskId, title, dueDate);
        } else {
            JOptionPane.showMessageDialog(GUI.this,
                    "Please select a task to update.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void deleteCode() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            int taskId = (int) table.getValueAt(selectedRow, 0);
            showDeleteConfirmationDialog(taskId);
        } else {
            JOptionPane.showMessageDialog(GUI.this,
                    "Please select a task to delete.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}