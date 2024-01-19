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

public class AdminTeacherDashboard implements Initializable {
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
    private TableColumn<UserEnitity, String> phone_number_col;

    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button removeButton;

    @FXML
    private Button detailButton;

    @FXML
    private Button changePasswordButton;


    private void getAndRenderStudentList() {
        student_table.getItems().clear();
        String sql = "SELECT u.user_id, u.first_name, u.last_name, u.user_name, u.gender, u.date_of_birth, u.email," +
                " u.phone_number, class.class_id, class.class_grade, class.class_character," +
                " class.class_sequence " +
                "FROM users u " +
                "LEFT JOIN class ON u.class_id = class.class_id " +
                "WHERE u.type_id = " + UserEnitity.getUserTypeInt("Teacher");
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
                phone_number_col.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getPhone_number() != null ? p.getValue().getPhone_number() : "N/A"));
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
        dialog.setTitle("Thêm giáo viên");
        dialog.setHeaderText("Vui lòng nhập thông tin giáo viên mới");
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
                        UserEnitity.getUserTypeInt("Teacher") + ", '" +
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
                    alert.setHeaderText("Thêm giáo viên thành công");
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
        addButton.setOnAction(e -> addButtonClicked());
        editButton.setOnMouseClicked(event -> {
            UserEnitity currentStudent = student_table.getSelectionModel().getSelectedItem();
            if (currentStudent == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Có lỗi xảy ra");
                alert.setHeaderText("Lỗi khi sửa thông tin giáo viên");
                alert.setContentText("Vui lòng chọn giáo viên cần sửa thông tin");
                alert.showAndWait();
                return;
            }
            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Sửa thông tin giáo viên");
            dialog.setHeaderText("Sửa thông tin giáo viên");
            GridPane dialogPane = new GridPane();
            dialogPane.setHgap(10);
            dialogPane.setVgap(10);
            dialogPane.setPadding(new Insets(20));

            Label first_name_label = new Label("Họ: ");
            first_name_label.setMinWidth(50);
            TextField first_name = new TextField();
            first_name.setPrefWidth(300);
            first_name.setText(currentStudent.getFirst_name());

            Label last_name_label = new Label("Tên: ");
            TextField last_name = new TextField();
            last_name_label.setMinWidth(50);
            last_name.setPrefWidth(150);
            last_name.setText(currentStudent.getLast_name());

            Label gender_label = new Label("Giới tính: ");
            ObservableList<String> genderList = FXCollections.observableArrayList();
            genderList.add("Nam");
            genderList.add("Nữ");
            ComboBox<String> gender = new ComboBox<>(genderList);
            gender.setValue(UserEnitity.getGender_String(currentStudent.getGender()));

            Label dob_label = new Label("Ngày sinh: ");
            DatePicker dob = new DatePicker();
            dob.setValue(currentStudent.getDob() != null ? currentStudent.getDob() : LocalDate.now());


            Label username_label = new Label("Tên đăng nhập: ");
            TextField username = new TextField();
            username.setText(currentStudent.getUser_name());

            Label email_label = new Label("Email: ");
            TextField email = new TextField();
            email.setText(currentStudent.getEmail());

            Label phone_number_label = new Label("Số điện thoại: ");
            TextField phone_number = new TextField();
            phone_number.setText(currentStudent.getPhone_number());

            Label address_label = new Label("Địa chỉ: ");
            TextField address = new TextField();
            address.setText(currentStudent.getAddress());

            Label description_label = new Label("Mô tả: ");
            TextArea description = new TextArea();
            description.setText(currentStudent.getDescription());

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
            if (currentStudent.getClassEnitity() != null) {
                ClassEnitity selectedClassEnitity = currentStudent.getClassEnitity();
                Integer classID = selectedClassEnitity.getClass_id();
                Integer classGrade = selectedClassEnitity.getClass_grade();
                String classCharacter = selectedClassEnitity.getClass_character();
                Integer classSequence = selectedClassEnitity.getClass_sequence();
                class_name.setValue(classID + "-" + ClassEnitity.getRepairedClassName(classGrade, classCharacter, classSequence));
            } else {
                if (classList.size() > 0) {
                    class_name.setValue(classList.get(0));
                }
            }


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
            dialog.getDialogPane().setPrefWidth(800); // Set preferred width
            dialog.getDialogPane().setPrefHeight(600); // Set preferred height
            dialog.getDialogPane().setContent(dialogPane);

            ButtonType updateButtonType = new ButtonType("Chỉnh Sửa", ButtonBar.ButtonData.APPLY);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, updateButtonType);
            dialog.setResultConverter(buttonType -> {
                if (buttonType == updateButtonType) {
                    Integer studentID = currentStudent.getUser_id();
                    Integer selectedClassID = class_name.getValue() != null ? Integer.parseInt(class_name.getValue().split("-")[0]) : null;
                    String sqlUpdateStudent = "UPDATE users SET " +
                            "first_name = '" + first_name.getText() + "', " +
                            "last_name = '" + last_name.getText() + "', " +
                            "user_name = '" + username.getText() + "', " +
                            "gender = " + UserEnitity.getGender_Int(gender.getValue()) + ", " +
                            "date_of_birth = TO_DATE('" + dob.getValue() + "', 'YYYY-MM-DD'), " +
                            "phone_number = '" + phone_number.getText() + "', " +
                            "email = '" + email.getText() + "', " +
                            "address = '" + address.getText() + "', " +
                            "description = '" + description.getText() + "', " +
                            "class_id = " + selectedClassID + " " +
                            "WHERE user_id = " + studentID;

                    try {
                        DatabaseConnection.mutation(sqlUpdateStudent);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Thành công");
                        alert.setHeaderText("Sửa thông tin giáo viên thành công");
                        alert.showAndWait();
                        getAndRenderStudentList();
                        dialog.close();
                    } catch (Exception e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Có lỗi xảy ra");
                        alert.setHeaderText("Có lỗi xảy ra khi sửa thông tin giáo viên");
                        alert.setContentText(e.getMessage());
                        alert.showAndWait();
                    }
                }
                return null;
            });
            dialog.showAndWait();
        });
        removeButton.setOnMouseClicked(event -> {
            UserEnitity currentStudent = student_table.getSelectionModel().getSelectedItem();
            if (currentStudent == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Có lỗi xảy ra");
                alert.setHeaderText("Lỗi khi xóa lớp");
                alert.setContentText("Vui lòng chọn lớp để xóa");
                alert.showAndWait();
                return;
            }
            Integer classID = currentStudent.getClassEnitity().getClass_id();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Are you sure you want to delete this class - classID: " + classID);
            alert.setContentText("This action cannot be undone");
            ButtonType confirmButton = new ButtonType("Confirm");
            ButtonType cancelButton = ButtonType.CANCEL;
            alert.getButtonTypes().setAll(confirmButton, cancelButton);
            Button confirmActionButton = (Button) alert.getDialogPane().lookupButton(confirmButton);
            confirmActionButton.setOnAction(caEvent -> {
                String sql = "DELETE FROM class WHERE class_id = " + classID;
                try {
                    DatabaseConnection.mutation(sql);
                    getAndRenderStudentList();
                    alert.close();
                } catch (Exception e) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Có lỗi xảy ra");
                    errorAlert.setHeaderText("Lỗi khi xóa lớp");
                    errorAlert.setContentText(e.getMessage());
                    errorAlert.showAndWait();
                    System.out.println(e.getMessage());
                }
            });
            alert.showAndWait();
        });
        detailButton.setOnMouseClicked((event -> {
            UserEnitity currentStudent = student_table.getSelectionModel().getSelectedItem();
            if (currentStudent == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Có lỗi xảy ra");
                alert.setHeaderText("Lỗi khi xem thông tin giáo viên");
                alert.setContentText("Vui lòng chọn giáo viên cần xem");
                alert.showAndWait();
                return;
            }
            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Sửa thông tin giáo viên");
            dialog.setHeaderText("Sửa thông tin giáo viên");
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
            dialog.getDialogPane().setPrefWidth(800); // Set preferred width
            dialog.getDialogPane().setPrefHeight(600); // Set preferred height
            dialog.getDialogPane().setContent(dialogPane);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
            dialog.showAndWait();
        }));
        changePasswordButton.setOnMouseClicked(event -> {
            UserEnitity currentStudent = student_table.getSelectionModel().getSelectedItem();
            if (currentStudent == null) {
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

                    Integer studentID = currentStudent.getUser_id();
                    String hashedPassword = PasswordUtils.hashPassword(passwordField.getText());
                    String sqlChangePassword = "UPDATE users SET " +
                            "password = '" + hashedPassword + "' " +
                            "WHERE user_id = " + studentID;
                    try {
                        DatabaseConnection.mutation(sqlChangePassword);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Thành công");
                        alert.setHeaderText("Đổi mật khẩu thành công");
                        alert.showAndWait();
                        getAndRenderStudentList();
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