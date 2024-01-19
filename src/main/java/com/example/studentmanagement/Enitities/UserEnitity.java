package com.example.studentmanagement.Enitities;

//CREATE TABLE Users (
//        user_id VARCHAR(50) PRIMARY KEY,
//        type_id INT,
//        user_name VARCHAR(100) NOT NULL,
//        password VARCHAR(100) NOT NULL,
//        phone_number VARCHAR(20),
//        email VARCHAR(100),
//        address VARCHAR(200),
//        school INT,
//        occupation VARCHAR(100),
//        description TEXT,
//        FOREIGN KEY (type_id) REFERENCES account_type (type_id),
//        FOREIGN KEY (school) REFERENCES School (school)
//        );

import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.Date;

public class UserEnitity {
    private int user_id;
    private int type_id;
    private int gender;
    private LocalDate dob;
    private String first_name;
    private String last_name;
    private String user_name;
    private String phone_number;
    private String email;
    private String address;
    private String occupation;
    private String description;
    private ClassEnitity classEnitity;
    private UserEnitity student_parent;
    private ObservableList<UserEnitity> parent_children;
    private UserEnitity current_children_viewing;
    private SchoolEnitity school;


    public UserEnitity() {
    }

    public UserEnitity(int user_id){
        this.user_id = user_id;
    }

    public UserEnitity(int user_id, int type_id, String user_name, String first_name, String last_name,  String phone_number, String email, String address, String occupation, String description, ClassEnitity classEnitity, SchoolEnitity school) {
        this.user_id = user_id;
        this.type_id = type_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.user_name = user_name;
        this.phone_number = phone_number;
        this.email = email;
        this.address = address;
        this.occupation = occupation;
        this.description = description;
        this.classEnitity = classEnitity;
        this.school = school;
    };

    public int getUser_id() {
        return user_id;
    }

    public int getType_id() {
        return type_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getOccupation() {
        return occupation;
    }

    public String getDescription() {
        return description;
    }

    public SchoolEnitity getSchool() {
        return school;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSchool(SchoolEnitity school) {
        this.school = school;
    }

    public int getGender() {
        return gender;
    }
    public void setGender(int gender) {
        this.gender = gender;
    }
    public LocalDate getDob() {
        return dob;
    }
    public void setDob(LocalDate dob) {
        this.dob = dob;
    }
    public ClassEnitity getClassEnitity() {
        return classEnitity;
    }

    public void setClassEnitity(ClassEnitity classEnitity) {
        this.classEnitity = classEnitity;
    }
    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFull_Name() {
        if (this.first_name == null && this.last_name == null)
            return null;
        else if (this.first_name == null)
            return this.last_name;
        else if (this.last_name == null)
            return this.first_name;
        else
        return this.first_name + " " + this.last_name;
    }

    public static String getGender_String(Integer gender){
        switch (gender){
            case 0:
                return "Nam";
            case 1:
                return "Nữ";
            default:
                return "N/A";
        }
    }

    public static Integer getGender_Int(String gender){
        switch (gender){
            case "Nam":
                return 0;
            case "Nữ":
                return 1;
            default:
                return null;
        }
    }

    public UserEnitity getStudent_parent() {
        return student_parent;
    }

    public void setStudent_parent(UserEnitity student_parent) {
        this.student_parent = student_parent;
    }

    public static Integer getUserTypeInt(String type){
        switch (type){
            case "Admin":
                return 1;
            case "Teacher":
                return 2;
            case "Parent":
                return 3;
            case "Student":
                return 4;
            default:
                return null;
        }
    }

    public ObservableList<UserEnitity> getParent_student() {
        return parent_children;
    }

    public void setParent_children(ObservableList<UserEnitity> parent_children) {
        this.parent_children = parent_children;
    }

    public UserEnitity getCurrent_children_viewing() {
        return current_children_viewing;
    }

    public void setCurrent_children_viewing(UserEnitity current_children_viewing) {
        this.current_children_viewing = current_children_viewing;
    }


}
