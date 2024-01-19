package com.example.studentmanagement.Controllers;

import com.example.studentmanagement.Holder.RouteHolder;
import com.example.studentmanagement.StudentManagementApplication;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class TeacherSideBarController implements Initializable {
    private final RouteHolder routerHolder = RouteHolder.getInstance();

    @FXML
    private ImageView avatarImageView;

    @FXML
    private HBox class_sidebar_btn;

    @FXML
    private HBox deadline_grade_sidebar_btn;

    @FXML
    private HBox deadline_sidebar_btn;

    @FXML
    private HBox schedule_sidebar_btn;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String currentRoute = routerHolder.getRoute();
        class_sidebar_btn.setOnMouseClicked(e -> {
            StudentManagementApplication.setRoot("TeacherClassDashboard.fxml");
        });

        deadline_sidebar_btn.setOnMouseClicked(e -> {
            StudentManagementApplication.setRoot("TeacherDeadlineDashboard.fxml");
        });

        deadline_grade_sidebar_btn.setOnMouseClicked(e -> {
            StudentManagementApplication.setRoot("TeacherGradeDashboard.fxml");
        });

        schedule_sidebar_btn.setOnMouseClicked(e -> {
            StudentManagementApplication.setRoot("TeacherCourseDashboard.fxml");
        });



        switch (currentRoute){
            case "TeacherClassDashboard.fxml":
                class_sidebar_btn.getStyleClass().add("active");
                break;
            case "TeacherDeadlineDashboard.fxml":
                deadline_sidebar_btn.getStyleClass().add("active");
                break;
            case "TeacherGradeDashboard.fxml":
                deadline_grade_sidebar_btn.getStyleClass().add("active");
                break;
            case "TeacherCourseDashboard.fxml":
                schedule_sidebar_btn.getStyleClass().add("active");
                break;
            default:
                break;
        }
    }

}
