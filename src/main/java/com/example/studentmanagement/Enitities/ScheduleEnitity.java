package com.example.studentmanagement.Enitities;

import java.time.LocalTime;

//-- Create the Schedule table
//        CREATE TABLE Schedule (
//        schedule_id INT AUTO_INCREMENT PRIMARY KEY,
//        day_of_week INT,
//        event_name VARCHAR(100) NOT NULL,
//        start_time TIME,
//        end_time TIME,
//        description TEXT,
//        belong_to_student_id VARCHAR(50),
//        FOREIGN KEY (belong_to_student_id) REFERENCES Users (user_id)
//        );
public class ScheduleEnitity {

    private int schedule_id;
    private int day_of_week;
    private String event_name;
    private LocalTime start_time;
    private LocalTime end_time;
    private String description;
    private Integer belong_to_student_id;

    public ScheduleEnitity() {
    }

    public ScheduleEnitity(int schedule_id, int day_of_week, String event_name, LocalTime start_time, LocalTime end_time, String description, Integer belong_to_student_id) {
        this.schedule_id = schedule_id;
        this.day_of_week = day_of_week;
        this.event_name = event_name;
        this.start_time = start_time;
        this.end_time = end_time;
        this.description = description;
        this.belong_to_student_id = belong_to_student_id;
    }

    public int getSchedule_id() {
        return schedule_id;
    }

    public void setSchedule_id(int schedule_id) {
        this.schedule_id = schedule_id;
    }

    public int getDay_of_week() {
        return day_of_week;
    }

    public void setDay_of_week(int day_of_week) {
        this.day_of_week = day_of_week;
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public LocalTime getStart_time() {
        return start_time;
    }

    public void setStart_time(LocalTime start_time) {
        this.start_time = start_time;
    }

    public LocalTime getEnd_time() {
        return end_time;
    }

    public void setEnd_time(LocalTime end_time) {
        this.end_time = end_time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getBelong_to_student_id() {
        return belong_to_student_id;
    }

    public void setBelong_to_student_id(Integer belong_to_student_id) {
        this.belong_to_student_id = belong_to_student_id;
    }
}
