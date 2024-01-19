package com.example.studentmanagement.Controllers;

import java.awt.Desktop;

//CREATE TABLE Users (
//        user_id VARCHAR(50) PRIMARY KEY,
//        type_id INT,
//        user_name VARCHAR(100) NOT NULL,
//        password VARCHAR(100) NOT NULL,
//        phone_number VARCHAR(20),
//        email VARCHAR(100),
//        address VARCHAR(200),
//        school_id INT,
//        occupation VARCHAR(100),
//        description TEXT,
//        FOREIGN KEY (type_id) REFERENCES account_type (type_id),
//        FOREIGN KEY (school_id) REFERENCES School (school_id)
//        );

import com.example.studentmanagement.Enitities.ClassEnitity;
import com.example.studentmanagement.Enitities.SchoolEnitity;
import com.example.studentmanagement.Holder.UserHolder;
import com.example.studentmanagement.Enitities.UserEnitity;
import com.example.studentmanagement.StudentManagementApplication;
import com.example.studentmanagement.Utils.DatabaseConnection;
import com.example.studentmanagement.Utils.PasswordUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import org.w3c.dom.Text;

import java.net.URI;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;


public class LoginController implements Initializable {

    @FXML
    private Button login_button;

    @FXML
    private AnchorPane main_form;

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;

    @FXML
    private HBox forgotPasswordButton;


    @FXML
    private void login() {
        try {
            Boolean hasError = handleValidateLogin();
            if (hasError) return;
            String username = this.username.getText();
            String password = this.password.getText();
            ResultSet account = DatabaseConnection.query("SELECT * FROM users WHERE user_name = '" + username + "'");
            if (!account.next()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Login failed");
                alert.setContentText("Username or password is incorrect!");
                alert.showAndWait();
                return;
            }
            String hashedPassword = account.getString("password");
            Boolean isMatched = PasswordUtils.verifyPassword(password, hashedPassword);
            if (!isMatched) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Login failed");
                alert.setContentText("Username or password is incorrect!");
                alert.showAndWait();
                return;
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Login successfully");
            alert.setContentText("Welcome back, " + username + "!");
            alert.showAndWait();

            Integer school_id = account.getInt("school_id");
            SchoolEnitity schoolEnitity = null;
            if (school_id != null) {
                schoolEnitity = new SchoolEnitity(school_id);
            }

            Integer class_id = account.getInt("class_id");
            ClassEnitity classEnitity = null;
            if (class_id != null) {
                classEnitity = new ClassEnitity(class_id);
            }



            UserEnitity userEnitity = new UserEnitity(account.getInt("user_id"), account.getInt("type_id"), account.getString("user_name"), null, null, account.getString("phone_number"), account.getString("email"), account.getString("address"), account.getString("occupation"), account.getString("description"), classEnitity, schoolEnitity);
            if (account.getInt("type_id") == 3) {
                String sqlParentChildren = "SELECT st.user_id, st.type_id, st.user_name, st.first_name, st.last_name, st.phone_number, st.email, st.class_id FROM users st JOIN users pr ON st.STUDENT_PARENT_ID = pr.user_id WHERE pr.user_id = " + userEnitity.getUser_id();
                ResultSet parentChildren = DatabaseConnection.query(sqlParentChildren);
                ObservableList<UserEnitity> children = FXCollections.observableArrayList();
                while (parentChildren.next()) {
                    UserEnitity child = new UserEnitity();
                    child.setUser_id(parentChildren.getInt("user_id"));
                    child.setType_id(parentChildren.getInt("type_id"));
                    child.setUser_name(parentChildren.getString("user_name"));
                    child.setFirst_name(parentChildren.getString("first_name"));
                    child.setLast_name(parentChildren.getString("last_name"));
                    child.setPhone_number(parentChildren.getString("phone_number"));
                    child.setEmail(parentChildren.getString("email"));
                    child.setClassEnitity(new ClassEnitity(parentChildren.getInt("class_id")));
                    children.add(child);
                }
                if (children.size() == 0) {
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    alert1.setTitle("Error");
                    alert1.setHeaderText("Vui lòng liên hệ với quản trị viên để thêm học sinh vào tài khoản!");
                    alert1.showAndWait();
                    return;
                }
                userEnitity.setParent_children(children);
                userEnitity.setCurrent_children_viewing(children.get(0));
            }
            UserHolder userHolder = UserHolder.getInstance();
            userHolder.setUser(userEnitity);
            if (userEnitity.getType_id() == 1) {
               StudentManagementApplication.setRoot("AdminClassDashboard.fxml");
            } else if (userEnitity.getType_id() == 2) {
                StudentManagementApplication.setRoot("TeacherClassDashboard.fxml");
            } else if (userEnitity.getType_id() == 3) {
                StudentManagementApplication.setRoot("ParentCourseDashboard.fxml");
            } else if (userEnitity.getType_id() == 4) {
                StudentManagementApplication.setRoot("StudentClassDashboard.fxml");
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    @FXML
    private void changeToRegister() throws Exception {
        StudentManagementApplication.setRoot("signup.fxml");
    }

    private void putErrorOnTextField(TextField textField, String error) {
        textField.setStyle("-fx-border-color: red");
        textField.setPromptText(error);
    }

    private void clearErrorOnTextField(TextField textField) {
        textField.setStyle("-fx-border-color: none");
        textField.setPromptText("");
    }

    private Boolean handleValidateLogin() {
        String username = this.username.getText();
        String password = this.password.getText();
        Boolean hasError = false;
        clearErrorOnTextField(this.username);
        clearErrorOnTextField(this.password);
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
        return hasError;
    }


    public void handleOpenForgotPassword(){
        try{
            Desktop.getDesktop().browse(new URI("http://127.0.0.1:5173"));
        } catch (Exception e){
            e.printStackTrace();
            System.out.println(e.toString());
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb){
//        forgotPasswordButton.set
    }

}