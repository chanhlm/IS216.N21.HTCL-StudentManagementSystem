package com.example.studentmanagement.Controllers;

import com.example.studentmanagement.Holder.UserHolder;
import com.example.studentmanagement.Enitities.ScheduleEnitity;
import com.example.studentmanagement.Enitities.UserEnitity;
import com.example.studentmanagement.Utils.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.time.LocalTime;

public class ScheduleController {
    private final UserEnitity userEnitity = UserHolder.getInstance().getUser();
    private void Test(){
        String sql = "SELECT * FROM course WHERE course_id IN (SELECT course_id FROM course_student WHERE student_id = " + "\"" + userEnitity.getUser_id() + "\"" + ")";
        ResultSet resultSet = null;
        try {
            resultSet = DatabaseConnection.query(sql);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Error");
        }
        ObservableList<ScheduleEnitity> data = FXCollections.observableArrayList();
        Integer randomNumber = 0;
        if (resultSet != null) {
            try {
                while (resultSet.next()) {
                    String event_name = resultSet.getString("course_name");
                    Integer day_of_week = resultSet.getInt("day_of_week");
                    LocalTime start_time = resultSet.getTime("time_start_in_day").toLocalTime();
                    LocalTime end_time = resultSet.getTime("time_end_in_day").toLocalTime();
                    String description = resultSet.getString("course_name");
                    Integer belong_to_student_id = userEnitity.getUser_id();
                    ScheduleEnitity scheduleEnitity = new ScheduleEnitity(randomNumber, day_of_week, event_name, start_time, end_time,description, belong_to_student_id);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
