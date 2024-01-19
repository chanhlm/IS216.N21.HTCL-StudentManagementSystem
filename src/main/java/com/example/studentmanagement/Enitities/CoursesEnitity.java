package com.example.studentmanagement.Enitities;


//CREATE TABLE course (
//        course_id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
//        course_name NVARCHAR2(100) NOT NULL,
//        time_start_in_day TIMESTAMP NOT NULL,
//        time_end_in_day TIMESTAMP NOT NULL,
//        day_of_week NUMBER NOT NULL,
//        teacher NUMBER NOT NULL,
//        FOREIGN KEY (teacher) REFERENCES users (user_id),
//        );

import java.time.LocalTime;

public class CoursesEnitity {
    private int course_id;
    private String course_name;
    private String time_start_in_day;
    private String time_end_in_day;
    private int day_of_week;
    private UserEnitity teacher;
    private ClassEnitity classEnitity;
    private String class_name;
    private String class_teacher_name;

    public CoursesEnitity() {
    }
    public CoursesEnitity(int course_id, String course_name, String time_start_in_day, String time_end_in_day, int day_of_week, UserEnitity teacher, ClassEnitity classEnitity) {
        this.course_id = course_id;
        this.course_name = course_name;
        this.time_start_in_day = time_start_in_day;
        this.time_end_in_day = time_end_in_day;
        this.day_of_week = day_of_week;
        this.teacher = teacher;
        this.classEnitity = classEnitity;
    }

    public int getCourse_id() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public String getCourses_name_with_id(){
        return this.course_id + " - " + this.course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getTime_start_in_day() {
        return time_start_in_day;
    }

    public LocalTime getTime_start_in_day_as_localtime() {
        return LocalTime.parse(time_start_in_day);
    }

    public LocalTime getTime_end_in_day_as_localtime() {
        return LocalTime.parse(time_end_in_day);
    }

    public void setTime_start_in_day(String time_start_in_day) {
        this.time_start_in_day = time_start_in_day;
    }

    public String getTime_end_in_day() {
        return time_end_in_day;
    }

    public void setTime_end_in_day(String time_end_in_day) {
        this.time_end_in_day = time_end_in_day;
    }

    public int getDay_of_week() {
        return day_of_week;
    }

    public void setDay_of_week(int day_of_week) {
        this.day_of_week = day_of_week;
    }

    public ClassEnitity getClassEnitity() {
        return classEnitity;
    }

    public void setClassEnitity(ClassEnitity classEnitity) {
        this.classEnitity = classEnitity;
    }

    public String getDay_of_week_as_string(){
        return getRepairedDay_of_week_as_string(this.day_of_week);
    }

    public UserEnitity getTeacher() {
        return teacher;
    }

    public void setTeacher(UserEnitity teacher) {
        this.teacher = teacher;
    }

    public static String getRepairedDay_of_week_as_string(Integer day_of_week) {
        return switch (day_of_week) {
            case 2 -> "2";
            case 3 -> "3";
            case 4 -> "4";
            case 5 -> "5";
            case 6 -> "6";
            case 7 -> "7";
            case 8 -> "Chủ Nhật";
            default -> "Unknown";
        };
    }
}
