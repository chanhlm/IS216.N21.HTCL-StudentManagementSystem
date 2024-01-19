package com.example.studentmanagement.Controllers;

import com.example.studentmanagement.Enitities.UserEnitity;
import com.example.studentmanagement.Holder.UserHolder;
import com.example.studentmanagement.StudentManagementApplication;
import com.example.studentmanagement.Utils.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

public class AdminDashboardOverviewCard implements Initializable {
    private final UserEnitity userLoggedIn = UserHolder.getInstance().getUser();

    @FXML
    private Text allClassCountText;

    @FXML
    private AnchorPane allClassOverViewCard;

    @FXML
    private Text allStudentCountText;

    @FXML
    private AnchorPane allStudentOverViewCard;

    @FXML
    private Text allTeacherCountText;

    @FXML
    private AnchorPane allTeacherOverViewCard;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        allStudentOverViewCard.setOnMouseClicked(e -> {
            StudentManagementApplication.setRoot("AdminStudentDashboard.fxml");
        });
        allTeacherOverViewCard.setOnMouseClicked(e -> {
            StudentManagementApplication.setRoot("AdminTeacherDashboard.fxml");
        });
        allClassOverViewCard.setOnMouseClicked(e -> {
            StudentManagementApplication.setRoot("AdminClassDashboard.fxml");
        });
        String sqlCountStudents = "SELECT COUNT(user_id) FROM Users WHERE type_id = " + UserEnitity.getUserTypeInt("Student") + " AND school_id = " + userLoggedIn.getSchool().getSchool_id();
        String sqlCountTeachers = "SELECT COUNT(user_id) FROM Users WHERE type_id = " + UserEnitity.getUserTypeInt("Teacher") + " AND school_id = " + userLoggedIn.getSchool().getSchool_id();
        String sqlCountClasses = "SELECT COUNT(class_id) FROM Class" + " WHERE class_school_id = " + userLoggedIn.getSchool().getSchool_id();
        try {
            ResultSet countStudents = DatabaseConnection.query(sqlCountStudents);
            ResultSet countTeachers = DatabaseConnection.query(sqlCountTeachers);
            ResultSet countClasses = DatabaseConnection.query(sqlCountClasses);
            if (countStudents.next()) {
                allStudentCountText.setText(countStudents.getString(1));
            }
            if (countTeachers.next()) {
                allTeacherCountText.setText(countTeachers.getString(1));
            }
            if (countClasses.next()) {
                allClassCountText.setText(countClasses.getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
