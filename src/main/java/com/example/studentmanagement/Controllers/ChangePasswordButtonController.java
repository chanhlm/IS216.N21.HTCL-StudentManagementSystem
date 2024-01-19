package com.example.studentmanagement.Controllers;

import com.example.studentmanagement.Enitities.UserEnitity;
import com.example.studentmanagement.Holder.UserHolder;
import com.example.studentmanagement.Utils.DatabaseConnection;
import com.example.studentmanagement.Utils.PasswordUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class ChangePasswordButtonController implements Initializable {

    @FXML
    private Pane changePasswordButton;

    @Override
    public void initialize(java.net.URL arg0, java.util.ResourceBundle arg1) {
        changePasswordButton.setOnMouseClicked(event -> {
            UserEnitity currentUser = UserHolder.getInstance().getUser();
            if (currentUser == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Vui lòng chọn giáo viên");
                alert.setHeaderText("Vui lòng chọn giáo viên để đổi mật khẩu");
                alert.setContentText("Vui lòng chọn giáo viên để đổi mật khẩu");
                alert.showAndWait();
                return;
            }
            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Đổi mật khẩu");
            dialog.setHeaderText("Đổi mật khẩu");
            GridPane dialogPane = new GridPane();
            dialogPane.setHgap(10);
            dialogPane.setVgap(10);
            dialogPane.setPadding(new Insets(20));

            Label first_name_label = new Label("Mật khẩu mới: ");
            PasswordField passwordField = new PasswordField();

            Label last_name_label = new Label("Nhập lại mật khẩu: ");
            PasswordField confirmPasswordField = new PasswordField();

            dialogPane.addRow(0, first_name_label, passwordField);
            dialogPane.addRow(1, last_name_label, confirmPasswordField);

            dialog.getDialogPane().setPrefWidth(300); // Set preferred width
            dialog.getDialogPane().setPrefHeight(200); // Set preferred height
            dialog.getDialogPane().setContent(dialogPane);

            ButtonType updateButtonType = new ButtonType("Chỉnh Sửa", ButtonBar.ButtonData.APPLY);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, updateButtonType);
            dialog.setResultConverter(buttonType -> {
                if (buttonType == updateButtonType) {
                    String password = passwordField.getText();
                    String confirmPassword = confirmPasswordField.getText();

                    if (password.equals(confirmPassword) != true) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Có lỗi xảy ra");
                        alert.setHeaderText("Lỗi khi đổi mật khẩu");
                        alert.setContentText("Mật khẩu không khớp");
                        alert.showAndWait();
                        return null;
                    }

                    Integer userID = currentUser.getUser_id();
                    String hashedPassword = PasswordUtils.hashPassword(passwordField.getText());
                    String sqlChangePassword = "UPDATE users SET " +
                            "password = '" + hashedPassword + "' " +
                            "WHERE user_id = " + userID;
                    try {
                        DatabaseConnection.mutation(sqlChangePassword);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Thành công");
                        alert.setHeaderText("Đổi mật khẩu thành công");
                        alert.showAndWait();
                        dialog.close();
                    } catch (Exception e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Có lỗi xảy ra");
                        alert.setHeaderText("Có lỗi xảy ra khi đổi mật khẩu");
                        alert.setContentText(e.getMessage());
                        alert.showAndWait();
                    }
                }
                return null;
            });
            dialog.showAndWait();
        });
    }

}
