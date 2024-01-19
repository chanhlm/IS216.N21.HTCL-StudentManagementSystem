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

public class TeacherClassDashboard implements Initializable {
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

    @FXML
    private TableColumn<UserEnitity, String> student_parent_col;


    @FXML
    private Button detailButton;


    private void getAndRenderStudentList() {
        student_table.getItems().clear();
        String sql = "SELECT u.user_id, u.first_name, u.last_name, u.user_name, u.gender, u.date_of_birth, u.email," +
                " u.phone_number, class.class_id, class.class_grade, class.class_character," +
                " class.class_sequence, u.student_parent_id, " +
                "p.first_name as parent_first_name, p.last_name as parent_last_name " +
                "FROM users u " +
                "LEFT JOIN class ON u.class_id = class.class_id " +
                "LEFT JOIN users p ON u.student_parent_id = p.user_id " +
                "WHERE u.type_id = " + UserEnitity.getUserTypeInt("Student") + " AND u.class_id = " + userLoggedIn.getClassEnitity().getClass_id();
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

                if (dataStudents.getString("student_parent_id") != null) {
                    UserEnitity parent = new UserEnitity();
                    parent.setUser_id(dataStudents.getInt("student_parent_id"));
                    parent.setFirst_name(dataStudents.getString("parent_first_name"));
                    parent.setLast_name(dataStudents.getString("parent_last_name"));
                    userEnitity.setStudent_parent(parent);
                } else {
                    userEnitity.setStudent_parent(null);
                }

                studentList.add(userEnitity);
                student_table.setItems(studentList);
                id_col.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getUser_id()));
                fullName_col.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getFull_Name() != null ? p.getValue().getFull_Name() : "N/A"));
                dob_col.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getDob() != null ? p.getValue().getDob().toString() : "N/A"));
                gender_col.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(UserEnitity.getGender_String(p.getValue().getGender())));
                email_col.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getEmail()));
                student_parent_col.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getStudent_parent() != null ? p.getValue().getStudent_parent().getFull_Name() : "N/A"));
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getAndRenderStudentList();
        detailButton.setOnMouseClicked((event -> {
            UserEnitity currentStudent = student_table.getSelectionModel().getSelectedItem();
            if (currentStudent == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Có lỗi xảy ra");
                alert.setHeaderText("Lỗi khi xem thông tin học sinh");
                alert.setContentText("Vui lòng chọn học sinh cần xem");
                alert.showAndWait();
                return;
            }
            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Sửa thông tin học sinh");
            dialog.setHeaderText("Sửa thông tin học sinh");
            GridPane dialogPane = new GridPane();
            dialogPane.setHgap(10);
            dialogPane.setVgap(10);
            dialogPane.setPadding(new Insets(20));

            Label first_name_label = new Label("Họ: ");
            first_name_label.setMinWidth(50);
            TextField first_name = new TextField();
            first_name.setPrefWidth(300);
            first_name.setText(currentStudent.getFirst_name());
            first_name.setDisable(true);

            Label last_name_label = new Label("Tên: ");
            TextField last_name = new TextField();
            last_name_label.setMinWidth(50);
            last_name.setPrefWidth(150);
            last_name.setText(currentStudent.getLast_name());
            last_name.setDisable(true);

            Label gender_label = new Label("Giới tính: ");
            ObservableList<String> genderList = FXCollections.observableArrayList();
            genderList.add("Nam");
            genderList.add("Nữ");
            ComboBox<String> gender = new ComboBox<>(genderList);
            gender.setValue(UserEnitity.getGender_String(currentStudent.getGender()));
            gender.setDisable(true);

            Label dob_label = new Label("Ngày sinh: ");
            DatePicker dob = new DatePicker();
            dob.setValue(currentStudent.getDob() != null ? currentStudent.getDob() : LocalDate.now());
            dob.setDisable(true);


            Label username_label = new Label("Tên đăng nhập: ");
            TextField username = new TextField();
            username.setText(currentStudent.getUser_name());
            username.setDisable(true);

            Label email_label = new Label("Email: ");
            TextField email = new TextField();
            email.setText(currentStudent.getEmail());
            email.setDisable(true);

            Label phone_number_label = new Label("Số điện thoại: ");
            TextField phone_number = new TextField();
            phone_number.setText(currentStudent.getPhone_number());
            phone_number.setDisable(true);

            Label address_label = new Label("Địa chỉ: ");
            TextField address = new TextField();
            address.setText(currentStudent.getAddress());
            address.setDisable(true);

            Label description_label = new Label("Mô tả: ");
            TextArea description = new TextArea();
            description.setText(currentStudent.getDescription());
            description.setDisable(true);

            // Add labels and text fields for student details
            Label class_name_label = new Label("Lớp: ");
            ComboBox<String> class_name = new ComboBox<>();
            if (currentStudent.getClassEnitity() != null) {
                ClassEnitity selectedClassEnitity = currentStudent.getClassEnitity();
                Integer classID = selectedClassEnitity.getClass_id();
                Integer classGrade = selectedClassEnitity.getClass_grade();
                String classCharacter = selectedClassEnitity.getClass_character();
                Integer classSequence = selectedClassEnitity.getClass_sequence();
                class_name.setValue(classID + "-" + ClassEnitity.getRepairedClassName(classGrade, classCharacter, classSequence));
            } else {
                class_name.setValue("N/A");
            }
            class_name.setDisable(true);

            Label parents_label = new Label("Phụ huynh: ");
            ComboBox<String> parents = new ComboBox<>();
            if (currentStudent.getStudent_parent() != null) {
                UserEnitity selectedParent = currentStudent.getStudent_parent();
                Integer parentID = selectedParent.getUser_id();
                String parentFirstName = selectedParent.getFirst_name();
                String parentLastName = selectedParent.getLast_name();
                parents.setValue(parentID + "-" + parentFirstName + " " + parentLastName);
            } else {
                parents.setValue("N/A");
            }
            parents.setDisable(true);


            HBox fullNameRow = new HBox(first_name_label, first_name, last_name_label, last_name);
            fullNameRow.setSpacing(10);
            dialogPane.addRow(0, fullNameRow);
            dialogPane.addRow(1, gender_label, gender);
            dialogPane.addRow(2, dob_label, dob);
            dialogPane.addRow(3, username_label, username);
            dialogPane.addRow(4, email_label, email);
            dialogPane.addRow(5, phone_number_label, phone_number);
            dialogPane.addRow(6, address_label, address);
            dialogPane.addRow(7, description_label, description);
            dialogPane.addRow(8, class_name_label, class_name);
            dialogPane.addRow(9, parents_label, parents);
            dialog.getDialogPane().setPrefWidth(800); // Set preferred width
            dialog.getDialogPane().setPrefHeight(600); // Set preferred height
            dialog.getDialogPane().setContent(dialogPane);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
            dialog.showAndWait();
        }));
    }

}
