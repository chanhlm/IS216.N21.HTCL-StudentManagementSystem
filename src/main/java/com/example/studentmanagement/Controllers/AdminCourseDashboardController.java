package com.example.studentmanagement.Controllers;

import com.example.studentmanagement.Enitities.ClassEnitity;
import com.example.studentmanagement.Enitities.CoursesEnitity;
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
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import jfxtras.scene.control.LocalDatePicker;
import jfxtras.scene.control.LocalTimePicker;

import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class AdminCourseDashboardController implements Initializable {

    @FXML
    private TableColumn<CoursesEnitity, String> class_name;

    @FXML
    private TableColumn<CoursesEnitity, Integer> course_id;

    @FXML
    private TableColumn<CoursesEnitity, String> course_name;

    @FXML
    private TableColumn<CoursesEnitity, String> day_of_week;

    @FXML
    private TableColumn<CoursesEnitity, String> teacher_name;

    @FXML
    private TableColumn<CoursesEnitity, LocalTime> time_end_in_day;

    @FXML
    private TableColumn<CoursesEnitity, LocalTime> time_start_in_day;

    @FXML
    private TableView<CoursesEnitity> course_table;

    void showAddCourseDialog() {
        //       open a dialog form to add class
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Thêm môn học");
        dialog.setHeaderText("Vui lòng nhập thông tin môn học");
        GridPane dialogPane = new GridPane();
        dialogPane.setHgap(10);
        dialogPane.setVgap(10);
        dialogPane.setPadding(new Insets(20));
        // Add labels and text fields for student details
        Label class_name_label = new Label("Lớp: ");
        String sqlGetClass = "SELECT class_id, class_grade, class_character, class_sequence FROM class";
        ObservableList<String> classList = FXCollections.observableArrayList();
        try {
            ResultSet resultSet = DatabaseConnection.query(sqlGetClass);
            while (resultSet.next()) {
                ClassEnitity classEnitity = new ClassEnitity();
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
            class_name.setValue(classList.get(0));
        };

        Label course_name_label = new Label("Tên môn học: ");
        TextField course_name = new TextField();

        Label time_start_in_day_label = new Label("Vào tiết: ");
        LocalTimePicker time_start_in_day = new LocalTimePicker();
        time_start_in_day.setLocalTime(LocalTime.of(7, 30, 0));

        Label time_end_in_day_label = new Label("Ra tiết: ");
        LocalTimePicker time_end_in_day = new LocalTimePicker();
        time_end_in_day.setLocalTime(LocalTime.of(16, 30, 0));

        Label day_of_week_label = new Label("Thứ: ");
        ComboBox<String> day_of_week = new ComboBox<>();
        day_of_week.getItems().addAll("Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6");
        day_of_week.setValue(day_of_week.getItems().get(0));

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
        Label teacher_label = new Label("Giáo viên: ");
        ComboBox<String> teacher = new ComboBox<>();
        teacher.getItems().addAll(teacherList);
        if (teacherList.size() > 0) {
            teacher.setValue(teacherList.get(0));
        };

        dialogPane.addRow(0, class_name_label, class_name);
        dialogPane.addRow(1, course_name_label, course_name);
        dialogPane.addRow(2, time_start_in_day_label, time_start_in_day);
        dialogPane.addRow(3, time_end_in_day_label, time_end_in_day);
        dialogPane.addRow(4, day_of_week_label, day_of_week);
        dialogPane.addRow(5, teacher_label, teacher);
        dialog.getDialogPane().setContent(dialogPane);

//        Submit
        ButtonType createButtonType = new ButtonType("Create", ButtonBar.ButtonData.APPLY);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, createButtonType);
        dialog.setResultConverter(buttonType -> {
            if (buttonType == createButtonType) {
                String selectedTeacherID = teacher.getValue() != null ? teacher.getValue().split(" - ")[0] : null;
                Integer selectedClassID = class_name.getValue() != null ? Integer.parseInt(class_name.getValue().split("-")[0]) : null;
                Integer selectedDayOfWeek = day_of_week.getSelectionModel().getSelectedIndex() + 2;
                String sqlInsert = "INSERT INTO course (course_name, time_start_in_day, time_end_in_day, day_of_week, course_teacher_id, class_id) VALUES ('" + course_name.getText() + "', '" + time_start_in_day.getLocalTime() + "', '" + time_end_in_day.getLocalTime() + "', '" + selectedDayOfWeek + "', '" + selectedTeacherID + "', '" + selectedClassID + "')";
                try {
                    DatabaseConnection.mutation(sqlInsert);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText("Class added successfully");
                    alert.showAndWait();
                    getRenderCourseList();
                    dialog.close();
                } catch (Exception e) {
                    System.out.println("SQL Error: " + sqlInsert);
                    e.printStackTrace();
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

    @FXML
    private Button addButton;

    @FXML
    private Button modifyButton;

    @FXML
    private Button removeButton;

    private void getRenderCourseList(){
        course_table.getItems().clear();
        String sqlCourseTeacherClass = "SELECT " +
                "course.course_id, course.course_name, course.time_start_in_day," +
                " course.time_end_in_day, course.day_of_week, course.course_teacher_id, course.class_id," +
                "u.first_name, u.last_name, " +
                " class.class_grade, class.class_character, class.class_sequence FROM course" +
                " INNER JOIN users u ON course.course_teacher_id = u.user_id " +
                " INNER JOIN class ON course.class_id = class.class_id";
        ResultSet resultSet;
        ObservableList<CoursesEnitity> courseList = FXCollections.observableArrayList();
        try {
            resultSet = DatabaseConnection.query(sqlCourseTeacherClass);
            while (resultSet.next()) {
                ClassEnitity classEnitity = new ClassEnitity();
                classEnitity.setClass_id(resultSet.getInt("class_id"));
                classEnitity.setClass_grade(resultSet.getInt("class_grade"));
                classEnitity.setClass_character(resultSet.getString("class_character"));
                classEnitity.setClass_sequence(resultSet.getInt("class_sequence"));

                UserEnitity teacher = new UserEnitity();
                teacher.setUser_id(resultSet.getInt("course_teacher_id"));
                teacher.setFirst_name(resultSet.getString("first_name"));
                teacher.setLast_name(resultSet.getString("last_name"));

                CoursesEnitity coursesEnitity = new CoursesEnitity();
                coursesEnitity.setTeacher(teacher);
                coursesEnitity.setClassEnitity(classEnitity);
                coursesEnitity.setCourse_id(resultSet.getInt("course_id"));
                coursesEnitity.setCourse_name(resultSet.getString("course_name"));
                coursesEnitity.setTime_start_in_day(resultSet.getString("time_start_in_day"));
                coursesEnitity.setTime_end_in_day(resultSet.getString("time_end_in_day"));
                coursesEnitity.setDay_of_week(resultSet.getInt("day_of_week"));
                courseList.add(coursesEnitity);
                course_table.setItems(courseList);
                course_id.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getCourse_id()));
                course_name.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getCourse_name()));
                time_start_in_day.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getTime_start_in_day_as_localtime()));
                time_end_in_day.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getTime_end_in_day_as_localtime()));
                day_of_week.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getDay_of_week_as_string()));
                teacher_name.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getTeacher().getFull_Name()));
                class_name.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getClassEnitity().getClass_name()));
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
    public void initialize(URL url, ResourceBundle rb) {
        getRenderCourseList();
        addButton.setOnAction(event -> {
            showAddCourseDialog();
        });
        modifyButton.setOnAction(event -> {
            CoursesEnitity currentCourse = course_table.getSelectionModel().getSelectedItem();
            if (currentCourse == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Có lỗi xảy ra");
                alert.setHeaderText("Lỗi khi sửa môn học");
                alert.setContentText("Vui lòng chọn môn học để sửa");
                alert.showAndWait();
                return;
            }
            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Sửa môn học");
            dialog.setHeaderText("Sửa môn học");
            GridPane dialogPane = new GridPane();
            dialogPane.setHgap(10);
            dialogPane.setVgap(10);
            dialogPane.setPadding(new Insets(20));
            // Add labels and text fields for student details
            Label class_name_label = new Label("Lớp: ");
            String sqlGetClass = "SELECT class_id, class_grade, class_character, class_sequence FROM class";
            ObservableList<String> classList = FXCollections.observableArrayList();
//                            try {
//                                ResultSet resultSet = DatabaseConnection.query(sqlGetClass);
//                                while (resultSet.next()) {
//                                    ClassEnitity classEnitity = new ClassEnitity();
//                                    Integer classID = resultSet.getInt("class_id");
//                                    Integer classGrade = resultSet.getInt("class_grade");
//                                    String classCharacter = resultSet.getString("class_character");
//                                    Integer classSequence = resultSet.getInt("class_sequence");
//                                    classList.add(classID + "-" + ClassEnitity.getRepairedClassName(classGrade, classCharacter, classSequence));
//                                }
//                            } catch (Exception e) {
//                                System.out.println(e.getMessage());
//                                e.printStackTrace();
//                            }
            ComboBox<String> class_name = new ComboBox<>(classList);
            class_name.setValue(currentCourse.getClassEnitity().getClass_name());
            class_name.setDisable(true);

            Label course_name_label = new Label("Tên môn học: ");
            TextField course_name = new TextField();
            course_name.setText(currentCourse.getCourse_name());

            Label time_start_in_day_label = new Label("Vào tiết: ");
            LocalTimePicker time_start_in_day = new LocalTimePicker();
            time_start_in_day.setLocalTime(currentCourse.getTime_start_in_day_as_localtime());

            Label time_end_in_day_label = new Label("Ra tiết: ");
            LocalTimePicker time_end_in_day = new LocalTimePicker();
            time_end_in_day.setLocalTime(currentCourse.getTime_end_in_day_as_localtime());

            Label day_of_week_label = new Label("Thứ: ");
            ComboBox<String> day_of_week = new ComboBox<>();
            day_of_week.getItems().addAll("Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6");
            day_of_week.setValue(currentCourse.getDay_of_week_as_string());

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
                alert.setTitle("Có lỗi xảy ra");
                alert.setHeaderText("Lỗi khi lấy danh sách giáo viên");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                return;
            }
            Label teacher_label = new Label("Giáo viên: ");
            ComboBox<String> teacher = new ComboBox<>();
            teacher.getItems().addAll(teacherList);
            teacher.setValue(teacher.getItems().get(0));

            dialogPane.addRow(0, class_name_label, class_name);
            dialogPane.addRow(1, course_name_label, course_name);
            dialogPane.addRow(2, time_start_in_day_label, time_start_in_day);
            dialogPane.addRow(3, time_end_in_day_label, time_end_in_day);
            dialogPane.addRow(4, day_of_week_label, day_of_week);
            dialogPane.addRow(5, teacher_label, teacher);

            dialog.getDialogPane().setContent(dialogPane);


            ButtonType updateButtonType = new ButtonType("Chỉnh Sửa", ButtonBar.ButtonData.APPLY);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, updateButtonType);
            dialog.setResultConverter(buttonType -> {
                if (buttonType == updateButtonType) {
                    Integer courseID = currentCourse.getCourse_id();
                    String selectedTeacherID = teacher.getValue().split(" - ")[0];
                    Integer selectedDayOfWeek = day_of_week.getSelectionModel().getSelectedIndex() + 2;
                    String sqlUpdateCourse = "UPDATE course SET course_name = '" + course_name.getText() + "', time_start_in_day = '" + time_start_in_day.getLocalTime().toString() + "', time_end_in_day = '" + time_end_in_day.getLocalTime().toString() + "', day_of_week = '" + selectedDayOfWeek + "', COURSE_TEACHER_ID = " + selectedTeacherID + " WHERE course_id = " + courseID;
                    try {
                        DatabaseConnection.mutation(sqlUpdateCourse);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Thành Công!");
                        alert.setHeaderText("Sửa lớp thành công");
                        alert.showAndWait();
                        getRenderCourseList();
                        dialog.close();
                    } catch (Exception e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Thất Bại");
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
        removeButton.setOnAction(event -> {
            CoursesEnitity currentCourse = course_table.getSelectionModel().getSelectedItem();
            if (currentCourse == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Có lỗi xảy ra");
                alert.setHeaderText("Lỗi khi xóa môn học");
                alert.setContentText("Vui lòng chọn môn học để xóa");
                alert.showAndWait();
                return;
            }
            Integer classID = currentCourse.getClassEnitity().getClass_id();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Are you sure you want to delete this class - classID: " + classID);
            alert.setContentText("This action cannot be undone");
            ButtonType confirmButton = new ButtonType("Confirm");
            ButtonType cancelButton = ButtonType.CANCEL;
            alert.getButtonTypes().setAll(confirmButton, cancelButton);
            Button confirmActionButton = (Button) alert.getDialogPane().lookupButton(confirmButton);
            confirmActionButton.setOnAction(caEvent -> {
                String sql = "DELETE FROM course WHERE course_id = " + currentCourse.getCourse_id();
                try {
                    DatabaseConnection.mutation(sql);
                    getRenderCourseList();
                    alert.close();
                } catch (Exception e) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Có lỗi xảy ra");
                    errorAlert.setHeaderText("Lỗi khi xóa môn học");
                    errorAlert.setContentText(e.getMessage());
                    errorAlert.showAndWait();
                    System.out.println(e.getMessage());
                }
            });
            alert.showAndWait();
        });
    }

}
