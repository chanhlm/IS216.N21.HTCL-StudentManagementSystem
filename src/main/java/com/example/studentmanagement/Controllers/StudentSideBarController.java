package com.example.studentmanagement.Controllers;

import com.example.studentmanagement.Holder.RouteHolder;
import com.example.studentmanagement.StudentManagementApplication;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

public class StudentSideBarController implements Initializable {
    private final RouteHolder routeHolder = RouteHolder.getInstance();

    @FXML
    private ImageView avatarImageView;

    @FXML
    private HBox class_sidebar_btn;

    @FXML
    private HBox course_sidebar_btn;

    @FXML
    private HBox deadline_sidebar_btn;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        avatarImageView.setClip(new Circle(avatarImageView.getFitWidth() / 2, avatarImageView.getFitHeight() / 2, 50));
        class_sidebar_btn.setOnMouseClicked(e -> {
            StudentManagementApplication.setRoot("StudentClassDashboard.fxml");
        });
        course_sidebar_btn.setOnMouseClicked(e -> {
            StudentManagementApplication.setRoot("StudentCourseDashboard.fxml");
        });
        deadline_sidebar_btn.setOnMouseClicked(e -> {
            StudentManagementApplication.setRoot("StudentDeadlineDashboard.fxml");
        });
        String currentRoute = routeHolder.getRoute();
        switch (currentRoute) {
            case "StudentClassDashboard.fxml":
                class_sidebar_btn.getStyleClass().add("active");
                break;
            case "StudentCourseDashboard.fxml":
                course_sidebar_btn.getStyleClass().add("active");
                break;
            case "StudentDeadlineDashboard.fxml":
                deadline_sidebar_btn.getStyleClass().add("active");
                break;
            default:
                break;
        }
    }

}
