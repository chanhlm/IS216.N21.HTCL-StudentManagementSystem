package com.example.studentmanagement.Controllers;

import com.example.studentmanagement.CommonObject.ValidateDeadlineSubmitResult;
import com.example.studentmanagement.Enitities.*;
import com.example.studentmanagement.Holder.UserHolder;
import com.example.studentmanagement.Utils.DatabaseConnection;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ParentDeadlineDashboardController implements Initializable {
    private final UserEnitity userLoggedIn = UserHolder.getInstance().getUser();

    @FXML
    private TableView<DeadlineStudentEnitity> deadline_table;

    @FXML
    private TableColumn<DeadlineStudentEnitity, String> course_col;

    @FXML
    private TableColumn<DeadlineStudentEnitity, LocalDate> date_end_col;

    @FXML
    private TableColumn<DeadlineStudentEnitity, LocalDate> date_start_col;

    @FXML
    private TableColumn<DeadlineStudentEnitity, Integer> id_col;

    @FXML
    private TableColumn<DeadlineStudentEnitity, String> title_col;

    @FXML
    private TableColumn<DeadlineStudentEnitity, String> content_col;

    @FXML
    private TableColumn<DeadlineStudentEnitity, Button> status_col;

    private void loadDeadlines() {
        try {
            String query = "SELECT d.deadline_id, d.date_start, d.date_end, d.from_course_id, d.by_teacher_id, d.title, d.description, " +
                    "ds.student_id, ds.deadline_student_answer, " +
                    "c.course_name, " +
                    "g.grade_detail_id, g.grade_detail_name, g.grade_detail_coefficient, g.grade_point, g.grade_detail_description, g.grade_detail_id, " +
                    "u.first_name AS teacher_first_name, u.last_name AS teacher_last_name " +
                    "FROM deadline d " +
                    "JOIN course c ON d.from_course_id = c.course_id " +
                    "JOIN class cl ON c.class_id = cl.class_id " +
                    "LEFT JOIN deadline_student ds ON d.deadline_id = ds.deadline_id AND ds.student_id = " + userLoggedIn.getCurrent_children_viewing().getUser_id() + " " +
                    "LEFT JOIN grade_detail g ON ds.grade_detail_id = g.grade_detail_id " +
                    "JOIN users u ON d.by_teacher_id = u.user_id " +
                    "WHERE cl.class_id = (SELECT class_id FROM users WHERE users.user_id = " + userLoggedIn.getCurrent_children_viewing().getUser_id() + ")";

            ResultSet rs = DatabaseConnection.query(query);
            while (rs.next()) {
                DeadlineEnitity deadline = new DeadlineEnitity();
                deadline.setDeadline_id(rs.getInt("deadline_id"));
                deadline.setDate_start(rs.getDate("date_start").toLocalDate());
                deadline.setDate_end(rs.getDate("date_end").toLocalDate());

                CoursesEnitity course = new CoursesEnitity();
                course.setCourse_name(rs.getString("course_name"));
                course.setCourse_name(rs.getString("course_name"));
                deadline.setCourse (course);

                GradeEnitity gradeDetail;
                if (rs.getInt("grade_detail_id") != 0) {
                    gradeDetail = new GradeEnitity();
                    gradeDetail.setGrade_detail_id(rs.getInt("grade_detail_id"));
                    gradeDetail.setGrade_detail_name(rs.getString("grade_detail_name"));
                    gradeDetail.setGrade_detail_coefficient(rs.getDouble("grade_detail_coefficient"));
                    gradeDetail.setGrade_point(rs.getDouble("grade_point"));
                    gradeDetail.setGrade_detail_description(rs.getString("grade_detail_description"));
                    gradeDetail.setCourse(course);
                } else {
                    gradeDetail = null;
                }


                UserEnitity teacher = new UserEnitity();
                teacher.setFirst_name(rs.getString("teacher_first_name"));
                teacher.setLast_name(rs.getString("teacher_last_name"));

                deadline.setTeacher(teacher);
                deadline.setTitle(rs.getString("title"));
                deadline.setDescription(rs.getString("description"));

                DeadlineStudentEnitity deadlineStudent = new DeadlineStudentEnitity();
                deadlineStudent.setDeadline(deadline);
                if (rs.getInt("student_id") != 0) {
                    deadlineStudent.setStudent(userLoggedIn.getCurrent_children_viewing());
                    deadlineStudent.setDeadline_student_answer(rs.getString("deadline_student_answer"));
                    deadlineStudent.setGrade_detail(gradeDetail);
                };
                deadline_table.getItems().add(deadlineStudent);
                course_col.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getDeadline().getCourse().getCourse_name()));
                date_end_col.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getDeadline().getDate_end()));
                date_start_col.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getDeadline().getDate_start()));
                id_col.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getDeadline().getDeadline_id()));
                title_col.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getDeadline().getTitle()));
                content_col.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getDeadline().getDescription()));
                status_col.setStyle("-fx-alignment: CENTER;");
                status_col.setCellValueFactory(cellData -> {
                    ValidateDeadlineSubmitResult result = ValidateDeadlineSubmitResult.isValidDeadlineToSubmit(cellData.getValue());
                    if (result == ValidateDeadlineSubmitResult.HAS_POINT) {
                        Double point = cellData.getValue().getGrade_detail().getGrade_point();
                        Button button = new Button(point.toString() + " điểm");
                        button.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-opacity: 1.0;");
                        button.setDisable(true);
                        return new ReadOnlyObjectWrapper<>(button);
                    }
                    if (result == ValidateDeadlineSubmitResult.SUBMITTED) {
                        Button button = new Button("Đợi chấm");
                        button.setStyle("-fx-background-color: #d2af12; -fx-text-fill: white; -fx-opacity: 1.0;");
                        button.setDisable(true);
                        return new ReadOnlyObjectWrapper<>(button);
                    }
                    if (result == ValidateDeadlineSubmitResult.DEADLINE_NOT_OPEN) {
                        Button button = new Button("Chưa mở");
                        button.setStyle("-fx-background-color: #73777c; -fx-text-fill: white; -fx-opacity: 1.0;");
                        button.setDisable(true);
                        return new ReadOnlyObjectWrapper<>(button);
                    }
                    if (result == ValidateDeadlineSubmitResult.DEADLINE_EXPIRED) {
                        Button button = new Button("Hết hạn");
                        button.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-opacity: 1.0;");
                        button.setDisable(true);
                        return new ReadOnlyObjectWrapper<>(button);
                    }
                    if (result == ValidateDeadlineSubmitResult.VALID_TO_MODIFY) {
                        Button button = new Button("Chưa nộp");
                        button.setStyle("-fx-background-color: #266bcc; -fx-text-fill: white; -fx-opacity: 1.0;");
                        button.setDisable(true);
                        return new ReadOnlyObjectWrapper<>(button);
                    }
                    return null;
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setContentText("Đã có lỗi xảy ra, vui lòng thử lại sau");
            alert.showAndWait();

        }

    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadDeadlines();
    }

}
