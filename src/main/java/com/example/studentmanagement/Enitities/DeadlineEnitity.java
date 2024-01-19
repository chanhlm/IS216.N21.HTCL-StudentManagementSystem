package com.example.studentmanagement.Enitities;

//-- Create the Deadline table
//        CREATE TABLE deadline (
//        deadline_id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
//        date_start DATE NOT NULL,
//        date_end DATE NOT NULL,
//        from_course_id NUMBER,
//        by_teacher_id NUMBER,
//        title NVARCHAR2(100) NOT NULL,
//        description CLOB,
//        FOREIGN KEY (from_course_id) REFERENCES course (course_id),
//        FOREIGN KEY (by_teacher_id) REFERENCES users (user_id)
//        );

import java.time.LocalDate;

public class DeadlineEnitity {
    private int deadline_id;
    private LocalDate date_start;
    private LocalDate date_end;
    private CoursesEnitity course;
    private UserEnitity teacher;
    private String title;
    private String description;
    private Integer num_students_assigned;
    private Integer num_students_completed;
    private Integer num_students_graded;

    public DeadlineEnitity() {
    }

    public DeadlineEnitity(int deadline_id) {
        this.deadline_id = deadline_id;
    }

    public DeadlineEnitity(int deadline_id, LocalDate date_start, LocalDate date_end, CoursesEnitity course, UserEnitity teacher, String title, String description) {
        this.deadline_id = deadline_id;
        this.date_start = date_start;
        this.date_end = date_end;
        this.course = course;
        this.teacher = teacher;
        this.title = title;
        this.description = description;
    }

    public int getDeadline_id() {
        return deadline_id;
    }

    public void setDeadline_id(int deadline_id) {
        this.deadline_id = deadline_id;
    }

    public LocalDate getDate_start() {
        return date_start;
    }

    public void setDate_start(LocalDate date_start) {
        this.date_start = date_start;
    }

    public LocalDate getDate_end() {
        return date_end;
    }

    public void setDate_end(LocalDate date_end) {
        this.date_end = date_end;
    }

    public CoursesEnitity getCourse() {
        return course;
    }

    public void setCourse(CoursesEnitity course) {
        this.course = course;
    }

    public UserEnitity getTeacher() {
        return teacher;
    }

    public void setTeacher(UserEnitity teacher) {
        this.teacher = teacher;
    }

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

    public Integer getNum_students_assigned() {
        return num_students_assigned;
    }

    public void setNum_students_assigned(Integer num_students_assigned) {
        this.num_students_assigned = num_students_assigned;
    }

    public Integer getNum_students_completed() {
        return num_students_completed;
    }

    public void setNum_students_completed(Integer num_students_completed) {
        this.num_students_completed = num_students_completed;
    }

    public Integer getNum_students_graded() {
        return num_students_graded;
    }

    public void setNum_students_graded(Integer num_students_graded) {
        this.num_students_graded = num_students_graded;
    }
}
