package com.example.studentmanagement.Controllers;

import com.example.studentmanagement.StudentManagementApplication;
import com.example.studentmanagement.Utils.DatabaseConnection;
import com.example.studentmanagement.Utils.PasswordUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import java.util.UUID;

public class SignUpController implements Initializable {
    @FXML
    private Button login_button;

    @FXML
    private AnchorPane main_form;

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField confirmPassword;

    @FXML
    private TextField username;

    @FXML
    private ChoiceBox<String> roleChoiceBox;

    @FXML
    private ChoiceBox<String> schoolChoiceBox;

    @FXML
    private TextField email;


    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            ResultSet accountTypes = DatabaseConnection.query("SELECT * FROM account_type");
            while (accountTypes.next()) {
                String account_type = "";
                account_type = accountTypes.getString("account_type_name");
                Integer account_type_id = accountTypes.getInt("type_id");
                if (account_type_id == 1) continue;
                roleChoiceBox.getItems().add(account_type);
            }
            roleChoiceBox.getSelectionModel().selectLast();
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        try {
            ResultSet schools = DatabaseConnection.query("SELECT * FROM school");
            while (schools.next()) {
                String school_name = "";
                school_name = schools.getString("school_name");
                schoolChoiceBox.getItems().add(school_name);
            }
            schoolChoiceBox.getSelectionModel().selectFirst();
        } catch (Exception e) {
            System.out.println(e.toString());
        }



    }

    @FXML
    public void changeToLogin() throws Exception {
        StudentManagementApplication.setRoot("login.fxml");
    }

    @FXML
    public void register() {
        Boolean hasError = handleValidateRegister();
        if (hasError) return;
        String username = this.username.getText();
        String password = this.password.getText();
        String role = this.roleChoiceBox.getValue();
        String school = this.schoolChoiceBox.getValue();
        String email = this.email.getText();
        if (isUsernameExists(username)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Username already exists!");
            alert.setContentText("Please choose another username!");
            alert.showAndWait();
            return;
        }
        if (isEmailExists(email)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Email already exists!");
            alert.setContentText("Please choose another email!");
            alert.showAndWait();
            return;
        }
        String hashedPassword = PasswordUtils.hashPassword(password);
        String type_id = getTypeId(role);
        String school_id = getSchoolId(school);
        String query = String.format("INSERT INTO users (email, user_name, password, type_id, school_id) VALUES ('%s', '%s', '%s', '%s', '%s')", email, username, hashedPassword, type_id, school_id);
        try {
            DatabaseConnection.mutation(query);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Register successfully!");
            alert.setContentText("You can now login with your account!");
            alert.showAndWait();
            StudentManagementApplication.setRoot("login.fxml");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    private Boolean handleValidateRegister() {
        String username = this.username.getText();
        String password = this.password.getText();
        String confirmPassword = this.confirmPassword.getText();
        Boolean hasError = false;
        clearErrorOnTextField(this.username);
        clearErrorOnTextField(this.password);
        clearErrorOnTextField(this.confirmPassword);
        if (username.isEmpty()) {
            putErrorOnTextField(this.username, "Please enter your username!");
            hasError = true;
        }
        ;
        if (password.isEmpty()) {
            putErrorOnTextField(this.password, "Please enter your password!");
            hasError = true;
        }
        ;
        if (confirmPassword.isEmpty()) {
            putErrorOnTextField(this.confirmPassword, "Please confirm your password!");
            hasError = true;
        }
        ;
        if (!password.equals(confirmPassword)) {
            putErrorOnTextField(this.confirmPassword, "Password does not match!");
            putErrorOnTextField(this.password, "Password does not match!");
            hasError = true;
        }
        ;
        return hasError;
    }

    private void putErrorOnTextField(TextField textField, String error) {
        textField.setStyle("-fx-border-color: red");
        textField.setPromptText(error);
    }

    private void clearErrorOnTextField(TextField textField) {
        textField.setStyle("-fx-border-color: none");
        textField.setPromptText("");
    }

    private boolean isUsernameExists(String username) {
        String query = String.format("SELECT * FROM users WHERE user_name = '%s'", username);
        try {
            ResultSet rs = DatabaseConnection.query(query);
            if (rs.next()) {
                return true;
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return false;
    }

    private boolean isEmailExists(String email) {
        String query = String.format("SELECT * FROM users WHERE email = '%s'", email);
        try {
            ResultSet rs = DatabaseConnection.query(query);
            if (rs.next()) {
                return true;
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return false;
    }

    private String getTypeId(String role) {
        String query = String.format("SELECT * FROM account_type WHERE account_type_name = '%s'", role);
        try {
            ResultSet rs = DatabaseConnection.query(query);
            if (rs.next()) {
                return rs.getString("type_id");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return "";
    }

    private String getSchoolId(String school) {
        String query = String.format("SELECT * FROM school WHERE school_name = '%s'", school);
        try {
            ResultSet rs = DatabaseConnection.query(query);
            if (rs.next()) {
                return rs.getString("school_id");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return "";
    }

}
