package com.example.studentmanagement.Controllers;

import com.example.studentmanagement.Enitities.UserEnitity;
import com.example.studentmanagement.Holder.UserHolder;
import com.example.studentmanagement.StudentManagementApplication;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class NavDashboardController implements Initializable {
    private final UserHolder userHolder = UserHolder.getInstance();

    @FXML
    private ImageView signoutButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        signoutButton.setOnMouseClicked(e -> {
            userHolder.setUser(null);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Đăng xuất");
            alert.setHeaderText("Đăng xuất thành công");
            alert.setContentText("Bạn đã đăng xuất khỏi hệ thống");
            alert.showAndWait();
            StudentManagementApplication.setRoot("Login.fxml");
        });
    }

}
