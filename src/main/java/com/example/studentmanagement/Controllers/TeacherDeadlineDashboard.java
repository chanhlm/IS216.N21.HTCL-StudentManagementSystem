package com.example.studentmanagement.Controllers;

import com.example.studentmanagement.Enitities.ClassEnitity;
import com.example.studentmanagement.Enitities.CoursesEnitity;
import com.example.studentmanagement.Enitities.DeadlineEnitity;
import com.example.studentmanagement.Enitities.UserEnitity;
import com.example.studentmanagement.Holder.UserHolder;
import com.example.studentmanagement.Utils.DatabaseConnection;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class TeacherDeadlineDashboard implements Initializable {
    private final UserEnitity userLoggedIn = UserHolder.getInstance().getUser();

    @FXML
    private TableView<DeadlineEnitity> deadline_table;

    @FXML
    private Button addButton;

    @FXML
    private TableColumn<DeadlineEnitity, LocalDate> date_end_col;

    @FXML
    private TableColumn<DeadlineEnitity, LocalDate> date_start_col;

    @FXML
    private Button detailButton;

    @FXML
    private TableColumn<DeadlineEnitity, String> course_col;

    @FXML
    private TableColumn<DeadlineEnitity, Integer> student_count_col;

    @FXML
    private TableColumn<DeadlineEnitity, Integer> id_col;

    @FXML
    private TableColumn<DeadlineEnitity, Integer> student_done_col;

    @FXML
    private TableColumn<DeadlineEnitity, Integer> student_grade_done_col;

    @FXML
    private TableColumn<DeadlineEnitity, String> title_col;

    @FXML
    private TableColumn<DeadlineEnitity, String> class_col;

    private void showAddClassDialog() {
        //       open a dialog form to add class
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Thêm bài tập");
        dialog.setHeaderText("Giao bài tập cho lớp");
        GridPane dialogPane = new GridPane();
        dialogPane.setHgap(10);
        dialogPane.setVgap(10);
        dialogPane.setPadding(new Insets(20));
        // Add labels and text fields for student details

        Label course_label = new Label("Môn học: ");
        ComboBox<String> course_combobox = new ComboBox<>();
        String sqlGetCourseTeaching = "SELECT c.course_id, c.course_name, c.class_id, cl.class_grade, cl.class_character, cl.class_sequence " +
                "FROM course c " +
                "JOIN class cl ON c.class_id = cl.class_id " +
                "WHERE c.course_teacher_id = " + userLoggedIn.getUser_id();
        ObservableList<String> courseList = FXCollections.observableArrayList();
        try {
            ResultSet courseTeaching = DatabaseConnection.query(sqlGetCourseTeaching);
            while (courseTeaching.next()) {
                CoursesEnitity coursesEnitity = new CoursesEnitity();
                coursesEnitity.setCourse_id(courseTeaching.getInt("course_id"));
                coursesEnitity.setCourse_name(courseTeaching.getString("course_name"));
                ClassEnitity classEnitity = new ClassEnitity();
                classEnitity.setClass_id(courseTeaching.getInt("class_id"));
                classEnitity.setClass_grade(courseTeaching.getInt("class_grade"));
                classEnitity.setClass_character(courseTeaching.getString("class_character"));
                classEnitity.setClass_sequence(courseTeaching.getInt("class_sequence"));
                coursesEnitity.setClassEnitity(classEnitity);
                courseList.add(coursesEnitity.getCourses_name_with_id() + " - " + coursesEnitity.getClassEnitity().getClass_name());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error when getting course list");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }
        course_combobox.getItems().addAll(courseList);

        Label title_label = new Label("Tiêu đề: ");
        TextField title_textfield = new TextField();

        Label description_label = new Label("Mô tả: ");
        TextArea description_textarea = new TextArea();

        Label date_start_label = new Label("Ngày mở: ");
        DatePicker date_start_picker = new DatePicker();
        date_start_picker.setValue(LocalDate.now());

        Label date_end_label = new Label("Ngày đóng: ");
        DatePicker date_end_picker = new DatePicker();
        date_end_picker.setValue(LocalDate.now().plusDays(7));

        dialogPane.addRow(0, course_label, course_combobox);
        dialogPane.addRow(1, title_label, title_textfield);
        dialogPane.addRow(2, description_label, description_textarea);
        dialogPane.addRow(3, date_start_label, date_start_picker);
        dialogPane.addRow(4, date_end_label, date_end_picker);
        dialog.getDialogPane().setContent(dialogPane);

//        Submit
        ButtonType createButtonType = new ButtonType("Create", ButtonBar.ButtonData.APPLY);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL, createButtonType);
        dialog.setResultConverter(buttonType -> {
            if (buttonType == createButtonType) {

                if (course_combobox.getSelectionModel().getSelectedItem() == null) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Thất bại");
                    alert.setHeaderText("Lỗi khi thêm lớp, vui lòng chọn môn học");
                    alert.setContentText("Please select a course");
                    alert.showAndWait();
                    return null;
                }

                String selectedCourse = course_combobox.getSelectionModel().getSelectedItem().split(" - ")[0];
                String title = title_textfield.getText();
                String description = description_textarea.getText();
                String date_start = date_start_picker.getValue().toString();
                String date_end = date_end_picker.getValue().toString();

                String sql = "INSERT INTO deadline (FROM_COURSE_ID, BY_TEACHER_ID, TITLE, DESCRIPTION, DATE_START, DATE_END) " +
                        "VALUES (" + selectedCourse + ", " + userLoggedIn.getUser_id() + ", '" + title + "', '" + description + "'," +
                        "TO_DATE ('" + date_start + "', 'YYYY-MM-DD'), " +
                        "TO_DATE ('" + date_end + "', 'YYYY-MM-DD'))";

                try {
                    DatabaseConnection.mutation(sql);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Thành công");
                    alert.setHeaderText("Giao bài tập thành công");
                    alert.setContentText("Đã giao bài tập cho môn " + selectedCourse);
                    alert.showAndWait();
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Thất bại");
                    alert.setHeaderText("Lỗi khi giao bài tập");
                    alert.setContentText(e.getMessage());
                    alert.showAndWait();
                }
            }
            return null;
        });
        dialog.getDialogPane().setPrefWidth(600); // Set preferred width
        dialog.getDialogPane().setPrefHeight(300); // Set preferred height
        dialog.showAndWait();
    }

    private void getRenderCourseList() {
        deadline_table.getItems().clear();
        String sqlGetDeadlineCustom = "SELECT" +
                " d.deadline_id," +
                " d.date_start," +
                " d.date_end," +
                " d.from_course_id," +
                " d.by_teacher_id," +
                " d.title," +
                " DBMS_LOB.SUBSTR(d.description, 4000, 1) AS description," +
                " c.course_name," +
                " cl.class_id, " +
                " cl.class_grade," +
                " cl.class_sequence," +
                " cl.class_character," +
                " COUNT(u.user_id) AS num_students_assigned, " +
                " COUNT(DISTINCT CASE WHEN ds.deadline_student_answer IS NOT NULL THEN u.user_id END) AS num_students_completed, " +
                " COUNT(DISTINCT CASE WHEN gds.grade_detail_id IS NOT NULL THEN u.user_id END) AS num_students_graded " +
                " FROM" +
                " deadline d" +
                " LEFT JOIN" +
                " deadline_student ds ON d.deadline_id = ds.deadline_id" +
                " LEFT JOIN" +
                " grade_detail gds ON ds.grade_detail_id = gds.grade_detail_id" +
                " LEFT JOIN" +
                " course c ON d.from_course_id = c.course_id" +
                " LEFT JOIN" +
                " class cl ON cl.class_id = c.class_id" +
                " LEFT JOIN" +
                " users u ON u.class_id = cl.class_id" +
                " WHERE" +
                " d.by_teacher_id = " + userLoggedIn.getUser_id() +
                " GROUP BY" +
                " d.deadline_id," +
                " d.date_start," +
                " d.date_end," +
                " d.from_course_id," +
                " d.by_teacher_id," +
                " d.title," +
                " DBMS_LOB.SUBSTR(d.description, 4000, 1)," +
                " c.course_name," +
                " cl.class_id, " +
                " cl.class_grade," +
                " cl.class_sequence," +
                " cl.class_character";

        ResultSet resultSet;
        try {
            resultSet = DatabaseConnection.query(sqlGetDeadlineCustom);
            ObservableList<DeadlineEnitity> deadlineList = FXCollections.observableArrayList();
            while (resultSet.next()) {
                DeadlineEnitity deadlineEnitity = new DeadlineEnitity();
                deadlineEnitity.setDeadline_id(resultSet.getInt("deadline_id"));
                deadlineEnitity.setDate_start(resultSet.getDate("date_start").toLocalDate());
                deadlineEnitity.setDate_end(resultSet.getDate("date_end").toLocalDate());

                CoursesEnitity coursesEnitity = new CoursesEnitity();
                ClassEnitity classEnitity = new ClassEnitity();
                classEnitity.setClass_id(resultSet.getInt("class_id"));
                classEnitity.setClass_character(resultSet.getString("class_character"));
                classEnitity.setClass_grade(resultSet.getInt("class_grade"));
                classEnitity.setClass_sequence(resultSet.getInt("class_sequence"));
                coursesEnitity.setCourse_id(resultSet.getInt("from_course_id"));
                coursesEnitity.setCourse_name(resultSet.getString("course_name"));
                coursesEnitity.setClassEnitity(classEnitity);

                UserEnitity userEnitity = new UserEnitity();
                userEnitity.setUser_id(resultSet.getInt("by_teacher_id"));

                deadlineEnitity.setCourse(coursesEnitity);
                deadlineEnitity.setTitle(resultSet.getString("title"));
                deadlineEnitity.setDescription(resultSet.getString("description"));
                deadlineEnitity.setCourse(coursesEnitity);
                deadlineEnitity.setTeacher(userEnitity);

                deadlineEnitity.setNum_students_assigned(resultSet.getInt("num_students_assigned"));
                deadlineEnitity.setNum_students_completed(resultSet.getInt("num_students_completed"));
                deadlineEnitity.setNum_students_graded(resultSet.getInt("num_students_graded"));

                deadlineList.add(deadlineEnitity);
            }
            deadline_table.setItems(deadlineList);
            id_col.setCellValueFactory(column -> new ReadOnlyObjectWrapper<>(column.getValue().getDeadline_id()));
            date_start_col.setCellValueFactory(column -> new ReadOnlyObjectWrapper<>(column.getValue().getDate_start()));
            date_end_col.setCellValueFactory(column -> new ReadOnlyObjectWrapper<>(column.getValue().getDate_end()));
            course_col.setCellValueFactory(column -> new ReadOnlyObjectWrapper<>(column.getValue().getCourse().getCourse_name()));
            class_col.setCellValueFactory(column -> new ReadOnlyObjectWrapper<>(column.getValue().getCourse().getClassEnitity().getClass_name()));
            title_col.setCellValueFactory(column -> new ReadOnlyObjectWrapper<>(column.getValue().getTitle()));
            student_count_col.setCellValueFactory(column -> new ReadOnlyObjectWrapper<>(column.getValue().getNum_students_assigned()));
            student_done_col.setCellValueFactory(column -> new ReadOnlyObjectWrapper<>(column.getValue().getNum_students_completed()));
            student_grade_done_col.setCellValueFactory(column -> new ReadOnlyObjectWrapper<>(column.getValue().getNum_students_graded()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Có lỗi xảy ra");
            alert.setHeaderText("Không thể lấy danh sách deadline");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        addButton.setOnAction(event -> {
            showAddClassDialog();
        });
        getRenderCourseList();
    }

}
