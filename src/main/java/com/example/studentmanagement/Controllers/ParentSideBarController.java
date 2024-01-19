package com.example.studentmanagement.Controllers;

import com.example.studentmanagement.Enitities.UserEnitity;
import com.example.studentmanagement.Holder.RouteHolder;
import com.example.studentmanagement.Holder.UserHolder;
import com.example.studentmanagement.StudentManagementApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

public class ParentSideBarController implements Initializable {
    private final RouteHolder routeHolder = RouteHolder.getInstance();
    private final UserEnitity userLoggedIn = UserHolder.getInstance().getUser();

    @FXML
    private ImageView avatarImageView;

    @FXML
    private HBox class_sidebar_btn;

    @FXML
    private HBox course_sidebar_btn;

    @FXML
    private HBox deadline_sidebar_btn;

    @FXML
    private ComboBox<String> childChoosingComboBox;

    @FXML
    private HBox announcement_sidebar_btn;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try  {
            avatarImageView.setClip(new Circle(avatarImageView.getFitWidth() / 2, avatarImageView.getFitHeight() / 2, 50));
            ObservableList<String> children = FXCollections.observableArrayList();
            try {
                if (userLoggedIn.getParent_student() != null) {
                    for (UserEnitity student : userLoggedIn.getParent_student()) {
                        children.add(student.getUser_id() + " - " + student.getFull_Name());
                    }
                    childChoosingComboBox.setItems(children);
                    UserEnitity currentChild = userLoggedIn.getCurrent_children_viewing();
                    childChoosingComboBox.setValue(currentChild.getUser_id() + " - " + currentChild.getFull_Name());
                    childChoosingComboBox.setOnAction(e -> {
                        Integer selectedChildID = Integer.parseInt(childChoosingComboBox.getValue().split(" - ")[0]);
                        UserEnitity selectedChild = userLoggedIn.getParent_student().stream().filter(student -> student.getUser_id() == selectedChildID).findFirst().orElse(null);
                        UserEnitity newLoggedInUser = userLoggedIn;
                        newLoggedInUser.setCurrent_children_viewing(selectedChild);
                        UserHolder.getInstance().setUser(newLoggedInUser);
                        StudentManagementApplication.reloadRoot();
                    });
                    class_sidebar_btn.setOnMouseClicked(e -> {
                        StudentManagementApplication.setRoot("ParentClassDashboard.fxml");
                    });
                    course_sidebar_btn.setOnMouseClicked(e -> {
                        StudentManagementApplication.setRoot("ParentCourseDashboard.fxml");
                    });
                    deadline_sidebar_btn.setOnMouseClicked(e -> {
                        StudentManagementApplication.setRoot("ParentDeadlineDashboard.fxml");
                    });
                    announcement_sidebar_btn.setOnMouseClicked(e -> {
                        StudentManagementApplication.setRoot("ParentAnnouncementDashboard.fxml");
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            String currentRoute = routeHolder.getRoute();
            switch (currentRoute) {
                case "ParentClassDashboard.fxml":
                    class_sidebar_btn.getStyleClass().add("active");
                    break;
                case "ParentCourseDashboard.fxml":
                    course_sidebar_btn.getStyleClass().add("active");
                    break;
                case "ParentDeadlineDashboard.fxml":
                    deadline_sidebar_btn.getStyleClass().add("active");
                    break;
                case "ParentAnnouncementDashboard.fxml":
                    announcement_sidebar_btn.getStyleClass().add("active");
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Error loading sidebar");
            alert.showAndWait();
        }
    }

}
