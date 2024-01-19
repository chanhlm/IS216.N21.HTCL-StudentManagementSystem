package com.example.studentmanagement.Enitities;

//CREATE TABLE class(
//        class_id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
//        class_academic_year NUMBER NOT NULL,
//        class_grade NUMBER NOT NULL,
//        class_character NVARCHAR2(1) NOT NULL,
//        class_sequence NUMBER NOT NULL,
//        class_teacher NUMBER
//);

public class ClassEnitity {
    private int class_id;
    private int class_academic_year;
    private int class_grade;
    private String class_character;
    private int class_sequence;
    private UserEnitity class_teacher;
    private int class_number_of_students;

    public ClassEnitity() {
    }

    public ClassEnitity(int class_id) {
        this.class_id = class_id;
    }

    public ClassEnitity(int class_id, int class_academic_year, int class_grade, String class_character, int class_sequence, UserEnitity class_teacher) {
        this.class_id = class_id;
        this.class_academic_year = class_academic_year;
        this.class_grade = class_grade;
        this.class_character = class_character;
        this.class_sequence = class_sequence;
        this.class_teacher = class_teacher;
    }

    public int getClass_id() {
        return class_id;
    }

    public void setClass_id(int class_id) {
        this.class_id = class_id;
    }

    public int getClass_academic_year() {
        return class_academic_year;
    }

    public void setClass_academic_year(int class_academic_year) {
        this.class_academic_year = class_academic_year;
    }

    public int getClass_grade() {
        return class_grade;
    }

    public void setClass_grade(int class_grade) {
        this.class_grade = class_grade;
    }

    public String getClass_character() {
        return class_character;
    }

    public void setClass_character(String class_character) {
        this.class_character = class_character;
    }

    public int getClass_sequence() {
        return class_sequence;
    }

    public void setClass_sequence(int class_sequence) {
        this.class_sequence = class_sequence;
    }

    public UserEnitity getClass_teacher() {
        return class_teacher;
    }

    public void setClass_teacher(UserEnitity class_teacher) {
        this.class_teacher = class_teacher;
    }

    public String getClass_name() {
        return getRepairedClassName(class_grade, class_character, class_sequence);
    }

    public static String getRepairedClassName(int class_grade, String class_character, int class_sequence) {
        if (class_sequence == 0) {
            return class_grade + class_character;
        };
        if (class_character == null || class_character.isBlank()) {
            return class_grade + "/" + class_sequence;
        }
        return class_grade + class_character + class_sequence;
    }

    public int getClass_number_of_students() {
        return class_number_of_students;
    }

    public void setClass_number_of_students(int class_number_of_students) {
        this.class_number_of_students = class_number_of_students;
    }
}
