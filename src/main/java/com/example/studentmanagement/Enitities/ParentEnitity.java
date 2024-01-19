package com.example.studentmanagement.Enitities;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ParentEnitity extends  UserEnitity{
    private String all_students_parent_name;
    private ObservableList<String> all_students_parent_email;

    public ParentEnitity() {
    }

    public String getAll_students_parent_name() {
        return all_students_parent_name;
    }

    public void setAll_students_parent_name(String all_students_parent_name) {
        this.all_students_parent_name = all_students_parent_name;
    }

    public ObservableList<String> getAll_students_parent_email() {
        return all_students_parent_email;
    }

//    quick fix, will change sql query later
    public void setAll_students_parent_email(String all_students_parent_email_string) {
        String[] stringArray = all_students_parent_email_string.split(", ");
        ObservableList<String> all_students_parent_email_list = FXCollections.observableArrayList(stringArray);
        this.all_students_parent_email = all_students_parent_email_list;
    }

    public void setAll_students_parent_email(ObservableList<String> all_students_parent_email) {
        this.all_students_parent_email = all_students_parent_email;
    }

}
