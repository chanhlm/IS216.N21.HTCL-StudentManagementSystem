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

public class StudentDashboardOverviewCard implements Initializable {
    private final UserEnitity userLoggedIn = UserHolder.getInstance().getUser();

    @FXML
    private AnchorPane allCourseStudyingCard;

    @FXML
    private Text allCourseStudyingText;

    @FXML
    private AnchorPane averagePointCard;

    @FXML
    private Text averagePointText;

    @FXML
    private AnchorPane homeworkRemainCard;

    @FXML
    private Text homeworkRemainText;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
}
