package com.example.studentmanagement.Enitities;

public class GradeEnitity {
    private int grade_detail_id;
    private String grade_detail_name;
    private Double grade_detail_coefficient;
    private Double grade_point;
    private String grade_detail_description;
    private CoursesEnitity course;

    public GradeEnitity() {
    }

    public GradeEnitity(int grade_detail_id) {
        this.grade_detail_id = grade_detail_id;
    }

    public GradeEnitity(int grade_detail_id, String grade_detail_name, Double grade_detail_coefficient, Double grade_point, String grade_detail_description, CoursesEnitity course) {
        this.grade_detail_id = grade_detail_id;
        this.grade_detail_name = grade_detail_name;
        this.grade_detail_coefficient = grade_detail_coefficient;
        this.grade_point = grade_point;
        this.grade_detail_description = grade_detail_description;
        this.course = course;
    }

    public int getGrade_detail_id() {
        return grade_detail_id;
    }

    public void setGrade_detail_id(int grade_detail_id) {
        this.grade_detail_id = grade_detail_id;
    }

    public String getGrade_detail_name() {
        return grade_detail_name;
    }

    public void setGrade_detail_name(String grade_detail_name) {
        this.grade_detail_name = grade_detail_name;
    }

    public Double getGrade_detail_coefficient() {
        return grade_detail_coefficient;
    }

    public void setGrade_detail_coefficient(Double grade_detail_coefficient) {
        this.grade_detail_coefficient = grade_detail_coefficient;
    }

    public Double getGrade_point() {
        return grade_point;
    }

    public void setGrade_point(Double grade_point) {
        this.grade_point = grade_point;
    }

    public String getGrade_detail_description() {
        return grade_detail_description;
    }

    public void setGrade_detail_description(String grade_detail_description) {
        this.grade_detail_description = grade_detail_description;
    }

    public CoursesEnitity getCourse() {
        return course;
    }

    public void setCourse(CoursesEnitity course) {
        this.course = course;
    }
}
