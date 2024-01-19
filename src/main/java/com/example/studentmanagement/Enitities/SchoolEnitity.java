package com.example.studentmanagement.Enitities;

//CREATE TABLE school (
//        school_id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
//        school_name NVARCHAR2(100) NOT NULL,
//        address NVARCHAR2(200) NOT NULL,
//        phone_number NVARCHAR2(20) NOT NULL,
//        email NVARCHAR2(100),
//        description CLOB,
//        school_logo NVARCHAR2(100)
//        );

public class SchoolEnitity {
    private int school_id;
    private String school_name;
    private String address;
    private String phone_number;
    private String email;
    private String description;
    private String school_logo;

    public SchoolEnitity() {
    }

    public SchoolEnitity(int school_id, String school_name, String address, String phone_number, String email, String description, String school_logo) {
        this.school_id = school_id;
        this.school_name = school_name;
        this.address = address;
        this.phone_number = phone_number;
        this.email = email;
        this.description = description;
        this.school_logo = school_logo;
    }

    public SchoolEnitity(String school_name, String address, String phone_number, String email, String description, String school_logo) {
        this.school_name = school_name;
        this.address = address;
        this.phone_number = phone_number;
        this.email = email;
        this.description = description;
        this.school_logo = school_logo;
    }

    public SchoolEnitity(String school_name, String address, String phone_number, String email, String description) {
        this.school_name = school_name;
        this.address = address;
        this.phone_number = phone_number;
        this.email = email;
        this.description = description;
    }

    public SchoolEnitity(String school_name, String address, String phone_number, String email) {
        this.school_name = school_name;
        this.address = address;
        this.phone_number = phone_number;
        this.email = email;
    }

    public SchoolEnitity(String school_name, String address, String phone_number) {
        this.school_name = school_name;
        this.address = address;
        this.phone_number = phone_number;
    }

    public SchoolEnitity(String school_name, String address) {
        this.school_name = school_name;
        this.address = address;
    }

    public SchoolEnitity(String school_name) {
        this.school_name = school_name;
    }

    public SchoolEnitity(int school_id) {
        this.school_id = school_id;
    }

    public int getSchool_id() {
        return school_id;
    }

    public String getSchool_name() {
        return school_name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getEmail() {
        return email;
    }

    public String getDescription() {
        return description;
    }

    public String getSchool_logo() {
        return school_logo;
    }

    public void setSchool_id(int school_id) {
        this.school_id = school_id;
    }

    public void setSchool_name(String school_name) {
        this.school_name = school_name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSchool_logo(String school_logo) {
        this.school_logo = school_logo;
    }
}
