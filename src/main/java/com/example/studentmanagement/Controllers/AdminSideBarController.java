package com.example.studentmanagement.Controllers;

import com.example.studentmanagement.Enitities.UserEnitity;
import com.example.studentmanagement.Holder.RouteHolder;
import com.example.studentmanagement.Holder.UserHolder;
import com.example.studentmanagement.StudentManagementApplication;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminSideBarController implements Initializable {
    private final UserEnitity userLoggedIn = UserHolder.getInstance().getUser();

    @FXML
    private ImageView avatarImageView;

    @FXML
    private HBox class_sidebar_btn;

    @FXML
    private HBox course_sidebar_btn;

    @FXML
    private HBox student_sidebar_btn;

    @FXML
    private HBox teacher_sidebar_btn;

    @FXML
    private HBox parent_sidebar_btn;

    private RouteHolder routeHolder = RouteHolder.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String currentRoute = routeHolder.getRoute();

        switch (currentRoute) {
            case "AdminClassDashboard.fxml":
                class_sidebar_btn.getStyleClass().add("active");
                break;
            case "AdminCourseDashboard.fxml":
                course_sidebar_btn.getStyleClass().add("active");
                break;
            case "AdminStudentDashboard.fxml":
                student_sidebar_btn.getStyleClass().add("active");
                break;
            case "AdminTeacherDashboard.fxml":
                teacher_sidebar_btn.getStyleClass().add("active");
                break;
            case "AdminParentDashboard.fxml":
                parent_sidebar_btn.getStyleClass().add("active");
                break;
            default:
                break;
        }

        class_sidebar_btn.setOnMouseClicked(e -> {
            StudentManagementApplication.setRoot("AdminClassDashboard.fxml");
        });
        course_sidebar_btn.setOnMouseClicked(e -> {
            StudentManagementApplication.setRoot("AdminCourseDashboard.fxml");
        });
        student_sidebar_btn.setOnMouseClicked(e -> {
            StudentManagementApplication.setRoot("AdminStudentDashboard.fxml");
        });
        teacher_sidebar_btn.setOnMouseClicked(e -> {
            StudentManagementApplication.setRoot("AdminTeacherDashboard.fxml");
        });
        parent_sidebar_btn.setOnMouseClicked(e -> {
            StudentManagementApplication.setRoot("AdminParentDashboard.fxml");
        });
    }

}
