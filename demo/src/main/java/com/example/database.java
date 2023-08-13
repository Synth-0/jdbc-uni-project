package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Vector;

public class database {
    
     void loadTasksFromDatabase() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "user", "1234")) {
            String selectQuery = "SELECT * FROM tasks";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int taskId = resultSet.getInt("id");
                String title = resultSet.getString("title");
                LocalDate dueDate = resultSet.getDate("due_date").toLocalDate();

                Vector<Object> taskData = new Vector<>();
                taskData.add(taskId);
                taskData.add(title);
                taskData.add(dueDate);

                GUI.tableModel.addRow(taskData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

     void addTaskInDatabase(String title, LocalDate dueDate) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "user", "1234")) {
            String insertQuery = "INSERT INTO tasks (title, due_date) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, title);
            preparedStatement.setDate(2, java.sql.Date.valueOf(dueDate));

            preparedStatement.executeUpdate();

            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedId = generatedKeys.getInt(1);
                Vector<Object> taskData = new Vector<>();
                taskData.add(generatedId);
                taskData.add(title);
                taskData.add(dueDate);
                GUI.tableModel.addRow(taskData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

     void updateTaskInDatabase(int taskId, String title, LocalDate dueDate) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "user", "1234")) {
            String updateQuery = "UPDATE tasks SET title = ?, due_date = ? WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);

            preparedStatement.setString(1, title);
            preparedStatement.setDate(2, java.sql.Date.valueOf(dueDate));
            preparedStatement.setInt(3, taskId);

            preparedStatement.executeUpdate();

            int selectedRow = GUI.table.getSelectedRow();
            GUI.tableModel.setValueAt(title, selectedRow, 1);
            GUI.tableModel.setValueAt(dueDate, selectedRow, 2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

     void deleteTaskFromDatabase(int taskId) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "user", "1234")) {
            String deleteQuery = "DELETE FROM tasks WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);

            preparedStatement.setInt(1, taskId);

            preparedStatement.executeUpdate();

            int selectedRow = GUI.table.getSelectedRow();
            GUI.tableModel.removeRow(selectedRow);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
