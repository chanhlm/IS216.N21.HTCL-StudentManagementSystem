package com.example.studentmanagement.Enitities;

//CREATE TABLE grade_detail (
//        grade_detail_id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
//        grade_detail_name NVARCHAR2(100) NOT NULL,
//        grade_detail_coefficient FLOAT NOT NULL,
//        grade_point FLOAT NOT NULL,
//        grade_detail_description CLOB,
//        grade_detail_id NUMBER,
//        FOREIGN KEY (grade_detail_id) REFERENCES course (course_id)
//        );

import java.time.LocalDate;

public class AnnouncementEnitity {
    private int annoucement_id;
    private String annoucement_title;
    private String annoucement_content;
    private LocalDate annoucement_date;
    private UserEnitity userEnitity;
    private CoursesEnitity coursesEnitity;
    private SchoolEnitity schoolEnitity;

    private ClassEnitity classEnitity;

    public AnnouncementEnitity() {

    }

    public int getAnnouncement_id() {
        return annoucement_id;
    }

    public void setAnnouncement_id(int annoucement_id) {
        this.annoucement_id = annoucement_id;
    }

    public String getAnnouncement_title() {
        return annoucement_title;
    }

    public void setAnnouncement_title(String annoucement_title) {
        this.annoucement_title = annoucement_title;
    }

    public String getAnnouncement_content() {
        return annoucement_content;
    }

    public void setAnnouncement_content(String annoucement_content) {
        this.annoucement_content = annoucement_content;
    }

    public LocalDate getAnnouncement_date() {
        return annoucement_date;
    }

    public void setAnnouncement_date(LocalDate annoucement_date) {
        this.annoucement_date = annoucement_date;
    }

    public UserEnitity getUserEnitity() {
        return userEnitity;
    }

    public void setUserEnitity(UserEnitity userEnitity) {
        this.userEnitity = userEnitity;
    }

    public CoursesEnitity getCoursesEnitity() {
        return coursesEnitity;
    }

    public void setCoursesEnitity(CoursesEnitity coursesEnitity) {
        this.coursesEnitity = coursesEnitity;
    }

    public SchoolEnitity getSchoolEnitity() {
        return schoolEnitity;
    }

    public void setSchoolEnitity(SchoolEnitity schoolEnitity) {
        this.schoolEnitity = schoolEnitity;
    }

    public ClassEnitity getClassEnitity() {
        return classEnitity;
    }

    public void setClassEnitity(ClassEnitity classEnitity) {
        this.classEnitity = classEnitity;
    }
}
