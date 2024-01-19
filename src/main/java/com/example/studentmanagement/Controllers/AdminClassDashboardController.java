package com.example.studentmanagement.Controllers;

import com.example.studentmanagement.Holder.UserHolder;
import com.example.studentmanagement.Enitities.ClassEnitity;
import com.example.studentmanagement.Enitities.UserEnitity;
import com.example.studentmanagement.Utils.DatabaseConnection;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AdminClassDashboardController implements Initializable {
    private final UserEnitity userLoggedIn = UserHolder.getInstance().getUser();

    @FXML
    private TableColumn<ClassEnitity, Integer> class_grade;

    @FXML
    private TableColumn<ClassEnitity, Integer> class_id;

    @FXML
    private TableColumn<ClassEnitity, String> class_name;

    @FXML
    private TableColumn<ClassEnitity, Integer> class_student_count;

    @FXML
    private TableColumn<ClassEnitity, String> class_teacher;

    @FXML
    private TableView<ClassEnitity> class_table;

    @FXML
    private TableColumn<ClassEnitity, String> actionCol;

    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button removeButton;


    private void showAddClassDialog() {
        //       open a dialog form to add class
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Add class");
        dialog.setHeaderText("Add new class");
        GridPane dialogPane = new GridPane();
        dialogPane.setHgap(10);
        dialogPane.setVgap(10);
        dialogPane.setPadding(new Insets(20));
        // Add labels and text fields for student details
        Label academic_year_label = new Label("Academic year: ");
        ComboBox<Integer> academic_year = new ComboBox<>();
        Integer currentYear = LocalDate.now().getYear();
        academic_year.getItems().addAll(currentYear, currentYear + 1, currentYear + 2, currentYear + 3, currentYear + 4);
        academic_year.setValue(currentYear);

        Label grade_label = new Label("Grade: ");
        ComboBox<Integer> grade = new ComboBox<>();
        grade.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
        grade.setValue(grade.getItems().get(0));

        Label character_label = new Label("Character: ");
        TextField character = new TextField();
        character.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > 4) {
                character.setText(oldValue);
            }
        });

        Label sequence_label = new Label("Sequence: ");
        ComboBox<Integer> sequence = new ComboBox<>();
        sequence.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
                17, 18, 19, 20);
        sequence.setValue(grade.getItems().get(0));

        String sqlGetTeacher = "SELECT user_id, NVL2(first_name, first_name || ' ', '') || NVL(last_name, '') AS teacher_name FROM users WHERE type_id = (SELECT type_id FROM account_type WHERE account_type_name = 'Giáo Viên')";
        ResultSet resultSet;
        ObservableList<String> teacherList = FXCollections.observableArrayList();
        try {
            resultSet = DatabaseConnection.query(sqlGetTeacher);
            while (resultSet.next()) {
                teacherList.add(resultSet.getString("user_id") + " - " + resultSet.getString("teacher_name"));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error when getting teacher list");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }
        Label teacher_label = new Label("Teacher: ");
        ComboBox<String> teacher = new ComboBox<>();
        teacher.getItems().addAll(teacherList);
        if (teacherList.size() > 0) {
            teacher.setValue(teacherList.get(0));
        };

        dialogPane.addRow(0, academic_year_label, academic_year);
        dialogPane.addRow(1, grade_label, grade);
        dialogPane.addRow(2, character_label, character);
        dialogPane.addRow(3, sequence_label, sequence);
        dialogPane.addRow(4, teacher_label, teacher);
        dialog.getDialogPane().setContent(dialogPane);

//        Submit
        ButtonType createButtonType = new ButtonType("Create", ButtonBar.ButtonData.APPLY);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, createButtonType);
        dialog.setResultConverter(buttonType -> {
            if (buttonType == createButtonType) {
                String selectedTeacherID = teacher.getValue() != null ? teacher.getValue().split(" - ")[0] : null;
                String sql = "INSERT INTO class (class_academic_year, class_grade, class_character, class_sequence, class_teacher_id, CLASS_SCHOOL_ID) VALUES (" +
                        academic_year.getValue() + ", " +
                        grade.getValue() + ", '" +
                        character.getText() + "', " +
                        sequence.getValue() + ", " +
                        selectedTeacherID + ", " +
                        userLoggedIn.getSchool().getSchool_id() + ")";
                try {
                    DatabaseConnection.mutation(sql);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText("Class added successfully");
                    alert.showAndWait();
                    getAndRenderClassList();
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
        dialog.getDialogPane().setPrefWidth(400); // Set preferred width
        dialog.getDialogPane().setPrefHeight(300); // Set preferred height
        dialog.showAndWait();
    }

    public void getAndRenderClassList() {
        class_table.getItems().clear();
        String sql = "SELECT c.class_id, c.class_academic_year, c.class_grade, c.class_character, c.class_sequence, c.class_teacher_id, u.first_name, u.last_name, COUNT(u2.user_id) AS class_student_count FROM class c LEFT JOIN users u ON c.class_teacher_id = u.user_id " +
                "LEFT JOIN users u2 ON c.class_id = u2.class_id AND u2.type_id = 4" +
                "WHERE " +
                "c.class_school_id = " + userLoggedIn.getSchool().getSchool_id() +
                " GROUP BY c.class_id, c.class_academic_year, c.class_grade, c.class_character, c.class_sequence, c.class_teacher_id, u.first_name, u.last_name";
        ResultSet resultSet;
        try {
            resultSet = DatabaseConnection.query(sql);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            resultSet = null;
        }
        ObservableList<ClassEnitity> data = FXCollections.observableArrayList();
        if (resultSet != null) {
            try {
                while (resultSet.next()) {
                    Integer teacher_id = resultSet.getInt("class_teacher_id");
                    UserEnitity teacher = null;
                    if (teacher_id != null) {
                        teacher = new UserEnitity();
                        teacher.setUser_id(teacher_id);
                        teacher.setFirst_name(resultSet.getString("first_name"));
                        teacher.setLast_name(resultSet.getString("last_name"));
                    }
                    ClassEnitity classEnitity = new ClassEnitity(
                            resultSet.getInt("class_id"),
                            resultSet.getInt("class_academic_year"),
                            resultSet.getInt("class_grade"),
                            resultSet.getString("class_character"),
                            resultSet.getInt("class_sequence"),
                            teacher
                    );
                    classEnitity.setClass_number_of_students(resultSet.getInt("class_student_count"));
                    data.add(classEnitity);
                }
                class_table.setItems(data);
                class_id.setCellValueFactory((p -> new ReadOnlyObjectWrapper<>(p.getValue().getClass_id())));
                class_name.setCellValueFactory((p -> new ReadOnlyObjectWrapper<>(p.getValue().getClass_name())));
                class_grade.setCellValueFactory((p -> new ReadOnlyObjectWrapper<>(p.getValue().getClass_grade())));
                class_student_count.setCellValueFactory((p -> new ReadOnlyObjectWrapper<>(p.getValue().getClass_number_of_students())));
                class_teacher.setCellValueFactory((p -> new ReadOnlyObjectWrapper<>(p.getValue().getClass_teacher() != null ? p.getValue().getClass_teacher().getFull_Name() : "")));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lấy danh sách lớp thất bại");
                alert.setHeaderText("Lỗi khi lấy danh sách lớp");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        } else {
            System.out.println("resultSet == null");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getAndRenderClassList();
        addButton.setOnMouseClicked(event -> showAddClassDialog());
        editButton.setOnMouseClicked(event -> {
            ClassEnitity currentClass = class_table.getSelectionModel().getSelectedItem();
            if (currentClass == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Có lỗi xảy ra");
                alert.setHeaderText("Lỗi khi sửa lớp");
                alert.setContentText("Vui lòng chọn lớp để sửa");
                alert.showAndWait();
                return;
            }
            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Modify class");
            dialog.setHeaderText("Modify class");
            GridPane dialogPane = new GridPane();
            dialogPane.setHgap(10);
            dialogPane.setVgap(10);
            dialogPane.setPadding(new Insets(20));
            // Add labels and text fields for student details
            Label academic_year_label = new Label("Academic year: ");
            ComboBox<Integer> academic_year = new ComboBox<>();
            Integer currentYear = LocalDate.now().getYear();
            academic_year.getItems().addAll(currentYear, currentYear + 1, currentYear + 2, currentYear + 3, currentYear + 4);
            academic_year.setValue(currentClass.getClass_academic_year());

            Label grade_label = new Label("Grade: ");
            ComboBox<Integer> grade = new ComboBox<>();
            grade.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
            grade.setValue(currentClass.getClass_grade());

            Label character_label = new Label("Character: ");
            TextField character = new TextField();
            character.textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null && newValue.length() > 4) {
                    character.setText(oldValue);
                }
            });
            character.setText(currentClass.getClass_character());

            Label sequence_label = new Label("Sequence: ");
            ComboBox<Integer> sequence = new ComboBox<>();
            sequence.getItems().addAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16,
                    17, 18, 19, 20);
            sequence.setValue(currentClass.getClass_sequence());

            String sqlGetTeacher = "SELECT user_id, NVL2(first_name, first_name || ' ', '') || NVL(last_name, '') AS teacher_name FROM users WHERE type_id = (SELECT type_id FROM account_type WHERE account_type_name = 'Giáo Viên')";
            ResultSet resultSet;
            ObservableList<String> teacherList = FXCollections.observableArrayList();
            try {
                resultSet = DatabaseConnection.query(sqlGetTeacher);
                while (resultSet.next()) {
                    teacherList.add(resultSet.getString("user_id") + " - " + resultSet.getString("teacher_name"));
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error when getting teacher list");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                return;
            }
            Label teacher_label = new Label("Teacher: ");
            ComboBox<String> teacher = new ComboBox<>();
            teacher.getItems().addAll(teacherList);

            dialogPane.addRow(0, academic_year_label, academic_year);
            dialogPane.addRow(1, grade_label, grade);
            dialogPane.addRow(2, character_label, character);
            dialogPane.addRow(3, sequence_label, sequence);
            dialogPane.addRow(4, teacher_label, teacher);
            dialog.getDialogPane().setContent(dialogPane);

            ButtonType updateButtonType = new ButtonType("Chỉnh Sửa", ButtonBar.ButtonData.APPLY);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, updateButtonType);
            dialog.setResultConverter(buttonType -> {
                if (buttonType == updateButtonType) {
                    Integer classID = currentClass.getClass_id();
                    String selectedTeacherID = teacher.getValue() != null ? teacher.getValue().split(" - ")[0] : null;
                    String sql = "UPDATE class SET class_academic_year = "
                            + academic_year.getValue() + ", " +
                            "class_grade = " + grade.getValue() + ", " +
                            "class_character = '" + character.getText() + "', " +
                            "class_sequence = " + sequence.getValue() +
                            ", class_teacher_id = " + selectedTeacherID +
                            " WHERE class_id = " + classID;
                    try {
                        DatabaseConnection.mutation(sql);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Success");
                        alert.setHeaderText("Sửa lớp thành công");
                        alert.showAndWait();
                        getAndRenderClassList();
                        dialog.close();
                    } catch (Exception e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("Có lỗi xảy ra khi sửa lớp");
                        alert.setContentText(e.getMessage());
                        alert.showAndWait();
                    }
                }
                return null;
            });
            dialog.getDialogPane().setPrefWidth(400); // Set preferred width
            dialog.getDialogPane().setPrefHeight(300); // Set preferred height
            dialog.showAndWait();
        });
        removeButton.setOnMouseClicked(event -> {
            ClassEnitity currentClass = class_table.getSelectionModel().getSelectedItem();
            if (currentClass == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Có lỗi xảy ra");
                alert.setHeaderText("Lỗi khi xóa lớp");
                alert.setContentText("Vui lòng chọn lớp để xóa");
                alert.showAndWait();
                return;
            }
            Integer classID = currentClass.getClass_id();
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
                    getAndRenderClassList();
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
    }
}
