package com.example.studentmanagement.Controllers;

import com.example.studentmanagement.Enitities.AnnouncementEnitity;
import com.example.studentmanagement.Enitities.ClassEnitity;
import com.example.studentmanagement.Enitities.ParentEnitity;
import com.example.studentmanagement.Enitities.UserEnitity;
import com.example.studentmanagement.Holder.UserHolder;
import com.example.studentmanagement.Service.SendEmailService;
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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AdminParentDashboard implements Initializable {
    private final UserEnitity userLoggedIn = UserHolder.getInstance().getUser();

    @FXML
    private TableView<ParentEnitity> parent_table;

    @FXML
    private TableColumn<ParentEnitity, String> dob_col;

    @FXML
    private TableColumn<ParentEnitity, String> phone_col;

    @FXML
    private TableColumn<ParentEnitity, String> fullName_col;

    @FXML
    private TableColumn<ParentEnitity, String> gender_col;

    @FXML
    private TableColumn<ParentEnitity, Integer> id_col;

    @FXML
    private TableColumn<ParentEnitity, String> student_parent_col;

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

    @FXML
    private Button sendAnnouncementButton;


    private void getAndRenderParentList() {
        parent_table.getItems().clear();
        String sql = "SELECT" +
                "  p.user_id," +
                "  p.first_name," +
                "  p.last_name," +
                "  p.user_name," +
                "  p.gender," +
                "  p.date_of_birth," +
                "  p.email," +
                "  p.phone_number," +
                "  p.class_id," +
                "  LISTAGG(u.first_name || ' ' || u.last_name, ', ') WITHIN GROUP (ORDER BY u.first_name, u.last_name) AS all_students_parent_name," +
                "  LISTAGG(u.email, ', ') WITHIN GROUP (ORDER BY u.first_name, u.last_name) AS all_students_parent_email" +
                "  FROM" +
                "  users p" +
                "  LEFT JOIN" +
                "  users u ON p.user_id = u.student_parent_id" +
                "  WHERE" +
                "  p.type_id = 3" +
                "  AND p.school_id = 1" +
                "  GROUP BY" +
                "  p.user_id," +
                "  p.first_name," +
                "  p.last_name," +
                "  p.user_name," +
                "  p.gender," +
                "  p.date_of_birth," +
                "  p.email," +
                "  p.phone_number," +
                "  p.class_id";

        try {
            ResultSet dataParents = DatabaseConnection.query(sql);
            ObservableList<ParentEnitity> parentList = FXCollections.observableArrayList();
            while (dataParents.next()) {
                ParentEnitity parentEnitity = new ParentEnitity();
                parentEnitity.setUser_id(dataParents.getInt("user_id"));
                parentEnitity.setFirst_name(dataParents.getString("first_name"));
                parentEnitity.setLast_name(dataParents.getString("last_name"));
                parentEnitity.setGender(dataParents.getInt("gender"));
                parentEnitity.setDob(dataParents.getDate("date_of_birth") != null ? dataParents.getDate("date_of_birth").toLocalDate() : null);
                parentEnitity.setEmail(dataParents.getString("email"));
                parentEnitity.setPhone_number(dataParents.getString("phone_number") != null ? dataParents.getString("phone_number") : "Chưa Có Dữ Liệu");
                parentEnitity.setUser_name(dataParents.getString("user_name"));
//                Phụ huynh không có lớp
                parentEnitity.setClassEnitity(null);
//                Phụ huynh không có phụ huynh
                parentEnitity.setStudent_parent(null);

                if (dataParents.getString("all_students_parent_name") != null && dataParents.getString("all_students_parent_name").isBlank() == false) {
                    parentEnitity.setAll_students_parent_name(dataParents.getString("all_students_parent_name"));
                    parentEnitity.setAll_students_parent_email(dataParents.getString("all_students_parent_email"));
                } else {
                    parentEnitity.setAll_students_parent_name(null);
                }

                parentList.add(parentEnitity);
                parent_table.setItems(parentList);
                id_col.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getUser_id()));
                fullName_col.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getFull_Name() != null ? p.getValue().getFull_Name() : "N/A"));
                dob_col.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getDob() != null ? p.getValue().getDob().toString() : "N/A"));
                gender_col.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(UserEnitity.getGender_String(p.getValue().getGender())));
                phone_col.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getPhone_number()));
                student_parent_col.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getAll_students_parent_name() != null ? p.getValue().getAll_students_parent_name() : "N/A"));
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
        dialog.setTitle("Thêm phụ huynh");
        dialog.setHeaderText("Vui lòng nhập thông tin phụ huynh mới");
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
        dialog.getDialogPane().setPrefWidth(800); // Set preferred width
        dialog.getDialogPane().setPrefHeight(600); // Set preferred height
        dialog.getDialogPane().setContent(dialogPane);

//        Submit
        ButtonType createButtonType = new ButtonType("Create", ButtonBar.ButtonData.APPLY);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, createButtonType);
        dialog.setResultConverter(buttonType -> {
            if (buttonType == createButtonType) {
                String hashedPassword = PasswordUtils.hashPassword(password.getText());
                String sqlInsertNewStudent = "INSERT INTO users (type_id, first_name, last_name, user_name, gender, date_of_birth, password, phone_number, email, address, description, school_id, class_id) " +
                        "VALUES (" +
                        UserEnitity.getUserTypeInt("Parent") + ", '" +
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
                        null + ")";

                try {
                    DatabaseConnection.mutation(sqlInsertNewStudent);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Thao tác thành công");
                    alert.setHeaderText("Thêm phụ huynh thành công");
                    alert.showAndWait();
                    getAndRenderParentList();
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

    private void sendAnnouncementButtonClicked(){
        ParentEnitity currentParent = parent_table.getSelectionModel().getSelectedItem();
        if (currentParent == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Có lỗi xảy ra");
            alert.setHeaderText("Lỗi khi gửi thông báo");
            alert.setContentText("Vui lòng chọn phụ huynh cần gửi thông báo");
            alert.showAndWait();
            return;
        }
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Gửi thông báo");
        dialog.setHeaderText("Gửi thông báo đến phụ huynh");
        GridPane dialogPane = new GridPane();
        dialogPane.setHgap(10);
        dialogPane.setVgap(10);
        dialogPane.setPadding(new Insets(20));

        Label title_label = new Label("Tiêu đề: ");
        TextField title = new TextField();
        title.setPrefWidth(300);

        Label content_label = new Label("Nội dung: ");
        TextArea content = new TextArea();
        content.setPrefWidth(300);
        content.setPrefHeight(200);

        dialogPane.addRow(0, title_label, title);
        dialogPane.addRow(1, content_label, content);
        dialog.getDialogPane().setPrefWidth(600); // Set preferred width
        dialog.getDialogPane().setPrefHeight(300); // Set preferred height
        dialog.getDialogPane().setContent(dialogPane);

        ButtonType createButtonType = new ButtonType("Gửi", ButtonBar.ButtonData.APPLY);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, createButtonType);
        dialog.setResultConverter(buttonType -> {
            String titleInput = title.getText();
            String contentInput = content.getText();
            if (buttonType == createButtonType) {
                if (titleInput.isEmpty() || contentInput.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Có lỗi xảy ra");
                    alert.setHeaderText("Lỗi khi gửi thông báo");
                    alert.setContentText("Vui lòng điền đầy đủ thông tin");
                    alert.showAndWait();
                    return null;
                };

                String sqlInsertNewStudent = "INSERT INTO announcement (announcement_TITLE, announcement_CONTENT, announcement_SCHOOL_ID, announcement_CLASS_ID, announcement_USER_ID, ANNOUNCEMENT_DATETIME) " +
                        "VALUES ('" +
                        titleInput + "', '" +
                        contentInput + "', " +
                        null + ", " +
                        null + ", " +
                        currentParent.getUser_id() + ", " +
                        "TO_DATE('" + LocalDate.now() + "', 'YYYY-MM-DD HH24:MI:SS')" + ")";

                try {
                    DatabaseConnection.mutation(sqlInsertNewStudent);
                    ObservableList<String> cc = currentParent.getAll_students_parent_email();
                    SendEmailService.sendEmailNotification(currentParent.getEmail(), titleInput, contentInput, cc);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Thao tác thành công");
                    alert.setHeaderText("Gửi thông báo thành công");
                    alert.showAndWait();
                    getAndRenderParentList();
                    dialog.close();
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Gửi thông báo thất bại");
                    alert.setHeaderText("Gửi thông báo thất bại");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            }
            return null;
        });
        dialog.showAndWait();
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getAndRenderParentList();
        addButton.setOnAction(e -> addButtonClicked());
        editButton.setOnMouseClicked(event -> {
            ParentEnitity currentParent = parent_table.getSelectionModel().getSelectedItem();
            if (currentParent == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Có lỗi xảy ra");
                alert.setHeaderText("Lỗi khi sửa thông tin phụ huynh");
                alert.setContentText("Vui lòng chọn phụ huynh cần sửa thông tin");
                alert.showAndWait();
                return;
            }
            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Sửa thông tin phụ huynh");
            dialog.setHeaderText("Sửa thông tin phụ huynh");
            GridPane dialogPane = new GridPane();
            dialogPane.setHgap(10);
            dialogPane.setVgap(10);
            dialogPane.setPadding(new Insets(20));

            Label first_name_label = new Label("Họ: ");
            first_name_label.setMinWidth(50);
            TextField first_name = new TextField();
            first_name.setPrefWidth(300);
            first_name.setText(currentParent.getFirst_name());

            Label last_name_label = new Label("Tên: ");
            TextField last_name = new TextField();
            last_name_label.setMinWidth(50);
            last_name.setPrefWidth(150);
            last_name.setText(currentParent.getLast_name());

            Label gender_label = new Label("Giới tính: ");
            ObservableList<String> genderList = FXCollections.observableArrayList();
            genderList.add("Nam");
            genderList.add("Nữ");
            ComboBox<String> gender = new ComboBox<>(genderList);
            gender.setValue(UserEnitity.getGender_String(currentParent.getGender()));

            Label dob_label = new Label("Ngày sinh: ");
            DatePicker dob = new DatePicker();
            dob.setValue(currentParent.getDob() != null ? currentParent.getDob() : LocalDate.now());


            Label username_label = new Label("Tên đăng nhập: ");
            TextField username = new TextField();
            username.setText(currentParent.getUser_name());

            Label email_label = new Label("Email: ");
            TextField email = new TextField();
            email.setText(currentParent.getEmail());

            Label phone_number_label = new Label("Số điện thoại: ");
            TextField phone_number = new TextField();
            phone_number.setText(currentParent.getPhone_number());

            Label address_label = new Label("Địa chỉ: ");
            TextField address = new TextField();
            address.setText(currentParent.getAddress());

            Label description_label = new Label("Mô tả: ");
            TextArea description = new TextArea();
            description.setText(currentParent.getDescription());


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
            dialog.getDialogPane().setPrefWidth(800); // Set preferred width
            dialog.getDialogPane().setPrefHeight(600); // Set preferred height
            dialog.getDialogPane().setContent(dialogPane);

            ButtonType updateButtonType = new ButtonType("Chỉnh Sửa", ButtonBar.ButtonData.APPLY);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, updateButtonType);
            dialog.setResultConverter(buttonType -> {
                if (buttonType == updateButtonType) {
                    Integer studentID = currentParent.getUser_id();

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
                            "class_id = " + null + ", " +
                            "student_parent_id = " + null + " " +
                            "WHERE user_id = " + studentID;

                    try {
                        DatabaseConnection.mutation(sqlUpdateStudent);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Thành công");
                        alert.setHeaderText("Sửa thông tin phụ huynh thành công");
                        alert.showAndWait();
                        getAndRenderParentList();
                        dialog.close();
                    } catch (Exception e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Có lỗi xảy ra");
                        alert.setHeaderText("Có lỗi xảy ra khi sửa thông tin phụ huynh");
                        alert.setContentText(e.getMessage());
                        alert.showAndWait();
                    }
                }
                return null;
            });
            dialog.showAndWait();
        });
        removeButton.setOnMouseClicked(event -> {
            ParentEnitity currentParent = parent_table.getSelectionModel().getSelectedItem();
            if (currentParent == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Có lỗi xảy ra");
                alert.setHeaderText("Lỗi khi xóa phụ huynh");
                alert.setContentText("Vui lòng chọn phụ huynh để xóa");
                alert.showAndWait();
                return;
            }
            Integer classID = currentParent.getClassEnitity().getClass_id();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Xóa phụ huynh");
            alert.setContentText("Bạn có chắc chắn muốn xóa phụ huynh này?");
            ButtonType confirmButton = new ButtonType("Confirm");
            ButtonType cancelButton = ButtonType.CANCEL;
            alert.getButtonTypes().setAll(confirmButton, cancelButton);
            Button confirmActionButton = (Button) alert.getDialogPane().lookupButton(confirmButton);
            confirmActionButton.setOnAction(caEvent -> {
                String sql = "DELETE FROM class WHERE class_id = " + classID;
                try {
                    DatabaseConnection.mutation(sql);
                    getAndRenderParentList();
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
            ParentEnitity currentParent = parent_table.getSelectionModel().getSelectedItem();
            if (currentParent == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Có lỗi xảy ra");
                alert.setHeaderText("Lỗi khi xem thông tin phụ huynh");
                alert.setContentText("Vui lòng chọn phụ huynh cần xem");
                alert.showAndWait();
                return;
            }
            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Sửa thông tin phụ huynh");
            dialog.setHeaderText("Sửa thông tin phụ huynh");
            GridPane dialogPane = new GridPane();
            dialogPane.setHgap(10);
            dialogPane.setVgap(10);
            dialogPane.setPadding(new Insets(20));

            Label first_name_label = new Label("Họ: ");
            first_name_label.setMinWidth(50);
            TextField first_name = new TextField();
            first_name.setPrefWidth(300);
            first_name.setText(currentParent.getFirst_name());
            first_name.setDisable(true);

            Label last_name_label = new Label("Tên: ");
            TextField last_name = new TextField();
            last_name_label.setMinWidth(50);
            last_name.setPrefWidth(150);
            last_name.setText(currentParent.getLast_name());
            last_name.setDisable(true);

            Label gender_label = new Label("Giới tính: ");
            ObservableList<String> genderList = FXCollections.observableArrayList();
            genderList.add("Nam");
            genderList.add("Nữ");
            ComboBox<String> gender = new ComboBox<>(genderList);
            gender.setValue(UserEnitity.getGender_String(currentParent.getGender()));
            gender.setDisable(true);

            Label dob_label = new Label("Ngày sinh: ");
            DatePicker dob = new DatePicker();
            dob.setValue(currentParent.getDob() != null ? currentParent.getDob() : LocalDate.now());
            dob.setDisable(true);


            Label username_label = new Label("Tên đăng nhập: ");
            TextField username = new TextField();
            username.setText(currentParent.getUser_name());
            username.setDisable(true);

            Label email_label = new Label("Email: ");
            TextField email = new TextField();
            email.setText(currentParent.getEmail());
            email.setDisable(true);

            Label phone_number_label = new Label("Số điện thoại: ");
            TextField phone_number = new TextField();
            phone_number.setText(currentParent.getPhone_number());
            phone_number.setDisable(true);

            Label address_label = new Label("Địa chỉ: ");
            TextField address = new TextField();
            address.setText(currentParent.getAddress());
            address.setDisable(true);

            Label description_label = new Label("Mô tả: ");
            TextArea description = new TextArea();
            description.setText(currentParent.getDescription());
            description.setDisable(true);


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
            dialog.getDialogPane().setPrefWidth(800); // Set preferred width
            dialog.getDialogPane().setPrefHeight(600); // Set preferred height
            dialog.getDialogPane().setContent(dialogPane);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
            dialog.showAndWait();
        }));
        changePasswordButton.setOnMouseClicked(event -> {
            ParentEnitity currentParent = parent_table.getSelectionModel().getSelectedItem();
            if (currentParent == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Vui lòng chọn phụ huynh");
                alert.setHeaderText("Vui lòng chọn phụ huynh để đổi mật khẩu");
                alert.setContentText("Vui lòng chọn phụ huynh để đổi mật khẩu");
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

                    Integer studentID = currentParent.getUser_id();
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
                        getAndRenderParentList();
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
        sendAnnouncementButton.setOnMouseClicked(event -> {
            sendAnnouncementButtonClicked();
        });
    }
}