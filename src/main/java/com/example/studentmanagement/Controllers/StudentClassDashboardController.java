package com.example.studentmanagement.Controllers;

import com.example.studentmanagement.Enitities.ClassEnitity;
import com.example.studentmanagement.Enitities.UserEnitity;
import com.example.studentmanagement.Holder.UserHolder;
import com.example.studentmanagement.Utils.DatabaseConnection;
import com.example.studentmanagement.Utils.PasswordUtils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class StudentClassDashboardController implements Initializable {
    private final UserEnitity userLoggedIn = UserHolder.getInstance().getUser();

    @FXML
    private TableView<UserEnitity> student_table;

    @FXML
    private TableColumn<UserEnitity, String> class_name_col;

    @FXML
    private TableColumn<UserEnitity, String> dob_col;

    @FXML
    private TableColumn<UserEnitity, String> email_col;

    @FXML
    private TableColumn<UserEnitity, String> fullName_col;

    @FXML
    private TableColumn<UserEnitity, String> gender_col;

    @FXML
    private TableColumn<UserEnitity, Integer> id_col;

    private void getAndRenderStudentList() {
        student_table.getItems().clear();
        String sql = "SELECT u.user_id, u.first_name, u.last_name, u.user_name, u.gender, u.date_of_birth, u.email," +
                " u.phone_number, class.class_id, class.class_grade, class.class_character," +
                " class.class_sequence " +
                "FROM users u " +
                "LEFT JOIN class ON u.class_id = class.class_id " +
                "WHERE u.type_id = " + UserEnitity.getUserTypeInt("Student") + " " +
                " AND class.class_id = " + userLoggedIn.getClassEnitity().getClass_id() + " " +
                "ORDER BY u.user_id ASC";
        try {
            ResultSet dataStudents = DatabaseConnection.query(sql);
            ObservableList<UserEnitity> studentList = FXCollections.observableArrayList();
            while (dataStudents.next()) {
                UserEnitity userEnitity = new UserEnitity();
                userEnitity.setUser_id(dataStudents.getInt("user_id"));
                userEnitity.setFirst_name(dataStudents.getString("first_name"));
                userEnitity.setLast_name(dataStudents.getString("last_name"));
                userEnitity.setGender(dataStudents.getInt("gender"));
                userEnitity.setDob(dataStudents.getDate("date_of_birth") != null ? dataStudents.getDate("date_of_birth").toLocalDate() : null);
                userEnitity.setEmail(dataStudents.getString("email"));
                userEnitity.setPhone_number(dataStudents.getString("phone_number"));
                userEnitity.setUser_name(dataStudents.getString("user_name"));

                if (dataStudents.getString("class_id") != null) {
                    ClassEnitity classEnitity = new ClassEnitity();
                    classEnitity.setClass_id(dataStudents.getInt("class_id"));
                    classEnitity.setClass_grade(dataStudents.getInt("class_grade"));
                    classEnitity.setClass_character(dataStudents.getString("class_character"));
                    classEnitity.setClass_sequence(dataStudents.getInt("class_sequence"));
                    userEnitity.setClassEnitity(classEnitity);
                } else {
                    userEnitity.setClassEnitity(null);
                }

                studentList.add(userEnitity);
                student_table.setItems(studentList);
                id_col.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getUser_id()));
                fullName_col.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getFull_Name() != null ? p.getValue().getFull_Name() : "N/A"));
                dob_col.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getDob() != null ? p.getValue().getDob().toString() : "N/A"));
                gender_col.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(UserEnitity.getGender_String(p.getValue().getGender())));
                email_col.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getEmail()));
                class_name_col.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getClassEnitity() != null ? p.getValue().getClassEnitity().getClass_name() : "N/A"));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error when getting course list");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void addButtonClicked() {
        //       open a dialog form to add class
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Thêm học sinh");
        dialog.setHeaderText("Vui lòng nhập thông tin học sinh mới");
        GridPane dialogPane = new GridPane();
        dialogPane.setHgap(10);
        dialogPane.setVgap(10);
        dialogPane.setPadding(new Insets(20));

        Label first_name_label = new Label("Họ: ");
        first_name_label.setMinWidth(50);
        TextField first_name = new TextField();
        first_name.setPrefWidth(300);

        Label last_name_label = new Label("Tên: ");
        TextField last_name = new TextField();
        last_name_label.setMinWidth(50);
        last_name.setPrefWidth(150);

        Label gender_label = new Label("Giới tính: ");
        ObservableList<String> genderList = FXCollections.observableArrayList();
        genderList.add("Nam");
        genderList.add("Nữ");
        ComboBox<String> gender = new ComboBox<>(genderList);
        gender.getSelectionModel().selectFirst();


        Label dob_label = new Label("Ngày sinh: ");
        DatePicker dob = new DatePicker();
        dob.setValue(LocalDate.now());

        Label username_label = new Label("Tên đăng nhập: ");
        TextField username = new TextField();

        Label password_label = new Label("Mật khẩu: ");
        PasswordField password = new PasswordField();

        Label email_label = new Label("Email: ");
        TextField email = new TextField();

        Label phone_number_label = new Label("Số điện thoại: ");
        TextField phone_number = new TextField();

        Label address_label = new Label("Địa chỉ: ");
        TextField address = new TextField();

        Label description_label = new Label("Mô tả: ");
        TextArea description = new TextArea();

        // Add labels and text fields for student details
        Label class_name_label = new Label("Lớp: ");
        String sqlGetClass = "SELECT class_id, class_grade, class_character, class_sequence FROM class";
        ObservableList<String> classList = FXCollections.observableArrayList();
        try {
            ResultSet resultSet = DatabaseConnection.query(sqlGetClass);
            while (resultSet.next()) {
                Integer classID = resultSet.getInt("class_id");
                Integer classGrade = resultSet.getInt("class_grade");
                String classCharacter = resultSet.getString("class_character");
                Integer classSequence = resultSet.getInt("class_sequence");
                classList.add(classID + "-" + ClassEnitity.getRepairedClassName(classGrade, classCharacter, classSequence));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        ComboBox<String> class_name = new ComboBox<>(classList);
        if (classList.size() > 0) {
            class_name.getSelectionModel().selectFirst();
        }

        Label parents_label = new Label("Phụ huynh: ");
        ComboBox<String> parents = new ComboBox<>();
        String sqlGetParents = "SELECT user_id, first_name, last_name FROM users WHERE type_id = " + UserEnitity.getUserTypeInt("Parent");
        ObservableList<String> parentsList = FXCollections.observableArrayList();
        try {
            ResultSet resultSet = DatabaseConnection.query(sqlGetParents);
            while (resultSet.next()) {
                Integer userID = resultSet.getInt("user_id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                parentsList.add(userID + "-" + firstName + " " + lastName);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        parents.setItems(parentsList);
        parents.getSelectionModel().selectFirst();

        HBox fullNameRow = new HBox(first_name_label, first_name, last_name_label, last_name);
        fullNameRow.setSpacing(10);
        dialogPane.addRow(0, fullNameRow);
        dialogPane.addRow(1, gender_label, gender);
        dialogPane.addRow(2, dob_label, dob);
        dialogPane.addRow(3, username_label, username);
        dialogPane.addRow(4, password_label, password);
        dialogPane.addRow(5, email_label, email);
        dialogPane.addRow(6, phone_number_label, phone_number);
        dialogPane.addRow(7, address_label, address);
        dialogPane.addRow(8, description_label, description);
        dialogPane.addRow(9, class_name_label, class_name);
        dialogPane.addRow(10, parents_label, parents);
        dialog.getDialogPane().setPrefWidth(800); // Set preferred width
        dialog.getDialogPane().setPrefHeight(600); // Set preferred height
        dialog.getDialogPane().setContent(dialogPane);

//        Submit
        ButtonType createButtonType = new ButtonType("Create", ButtonBar.ButtonData.APPLY);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, createButtonType);
        dialog.setResultConverter(buttonType -> {
            if (buttonType == createButtonType) {
                Integer selectedClassID = class_name.getValue() != null ? Integer.parseInt(class_name.getValue().split("-")[0]) : null;
                String hashedPassword = PasswordUtils.hashPassword(password.getText());
                String sqlInsertNewStudent = "INSERT INTO users (type_id, first_name, last_name, user_name, gender, date_of_birth, password, phone_number, email, address, description, school_id, class_id) " +
                        "VALUES (" +
                        UserEnitity.getUserTypeInt("Student") + ", '" +
                        first_name.getText() + "', '" +
                        last_name.getText() + "', '" +
                        username.getText() + "', " +
                        UserEnitity.getGender_Int(gender.getValue()) + ", " +
                        "TO_DATE('" + dob.getValue() + "', 'YYYY-MM-DD')" + ", '" +
                        hashedPassword + "', '" +
                        phone_number.getText() + "', '" +
                        email.getText() + "', '" +
                        address.getText() + "', '" +
                        description.getText() + "', " +
                        userLoggedIn.getSchool().getSchool_id() + ", " +
                        selectedClassID + ")";

                try {
                    DatabaseConnection.mutation(sqlInsertNewStudent);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Thao tác thành công");
                    alert.setHeaderText("Thêm học sinh thành công");
                    alert.showAndWait();
                    getAndRenderStudentList();
                    dialog.close();
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error when adding class");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            }
            return null;
        });
        dialog.showAndWait();
    };


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getAndRenderStudentList();

    }
}