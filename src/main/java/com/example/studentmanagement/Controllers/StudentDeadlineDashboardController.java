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

public class StudentDeadlineDashboardController implements Initializable {
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

    @FXML
    private Button submitButton;

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
                    "LEFT JOIN deadline_student ds ON d.deadline_id = ds.deadline_id AND ds.student_id = " + userLoggedIn.getUser_id() + " " +
                    "LEFT JOIN grade_detail g ON ds.grade_detail_id = g.grade_detail_id " +
                    "JOIN users u ON d.by_teacher_id = u.user_id " +
                    "WHERE cl.class_id = (SELECT class_id FROM users WHERE users.user_id = " + userLoggedIn.getUser_id() + ")";

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
                if (rs.getInt("student_id") != 0 && rs.getInt("student_id") == userLoggedIn.getUser_id()) {
                    deadlineStudent.setStudent(userLoggedIn);
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

    private void submitButtonOnClick() {
        try {
            DeadlineStudentEnitity selectedDeadline = deadline_table.getSelectionModel().getSelectedItem();
            if (selectedDeadline == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setContentText("Vui lòng chọn bài tập cần nộp");
                alert.showAndWait();
                return;
            }

            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Nộp bài tập");
            dialog.setHeaderText("Nộp bài tập cho giáo viên");
            GridPane dialogPane = new GridPane();
            dialogPane.setHgap(10);
            dialogPane.setVgap(10);
            dialogPane.setPadding(new Insets(20));
            // Add labels and text fields for student details

            Label course_label = new Label("Môn học: ");
            String selectedCourse = selectedDeadline.getDeadline().getCourse().getCourse_name();
            ComboBox<String> course_combobox = new ComboBox<>();
            course_combobox.getItems().add(selectedCourse);
            course_combobox.setValue(selectedCourse);
            course_combobox.setDisable(true);


            Label title_label = new Label("Tiêu đề: ");
            String currentTitle = selectedDeadline.getDeadline().getTitle();
            TextField title_textfield = new TextField();
            title_textfield.setText(currentTitle);
            title_textfield.setDisable(true);


            Label description_label = new Label("Mô tả: ");
            String currentDescription = selectedDeadline.getDeadline().getDescription();
            TextArea description_textarea = new TextArea();
            description_textarea.setText(currentDescription);
            description_textarea.setDisable(true);

            Label date_start_label = new Label("Ngày mở: ");
            LocalDate currentStartDate = selectedDeadline.getDeadline().getDate_start();
            DatePicker date_start_picker = new DatePicker();
            date_start_picker.setValue(currentStartDate);
            date_start_picker.setDisable(true);


            Label date_end_label = new Label("Ngày đóng: ");
            LocalDate currentEndDate = selectedDeadline.getDeadline().getDate_end();
            DatePicker date_end_picker = new DatePicker();
            date_end_picker.setValue(currentEndDate);
            date_end_picker.setDisable(true);

            Label answer_label = new Label("Trả lời: ");
            TextArea answer_textarea = new TextArea();

            dialogPane.addRow(0, course_label, course_combobox);
            dialogPane.addRow(1, title_label, title_textfield);
            dialogPane.addRow(2, description_label, description_textarea);
            dialogPane.addRow(3, date_start_label, date_start_picker);
            dialogPane.addRow(4, date_end_label, date_end_picker);
            dialogPane.addRow(5, answer_label, answer_textarea);

//        Submit
            ButtonType submitButton = new ButtonType("Nộp Bài", ButtonBar.ButtonData.APPLY);
            dialog.getDialogPane().getButtonTypes().addAll(submitButton, ButtonType.CANCEL);
            ValidateDeadlineSubmitResult validateDeadlineSubmitResult = ValidateDeadlineSubmitResult.isValidDeadlineToSubmit(selectedDeadline);
            if (validateDeadlineSubmitResult != ValidateDeadlineSubmitResult.VALID_TO_MODIFY) {
                dialog.setContentText(validateDeadlineSubmitResult.getMessage());
                Text text = new Text(validateDeadlineSubmitResult.getMessage());
                dialogPane.addRow(6, text);
                text.setStyle("-fx-fill: red;");
                if (selectedDeadline.getGrade_detail() != null) {
                    answer_textarea.setText(selectedDeadline.getDeadline_student_answer());
                }

                if (validateDeadlineSubmitResult == ValidateDeadlineSubmitResult.HAS_POINT){
                    text.setStyle("-fx-fill: green;");
                    Text text1 = new Text("Điểm: " + selectedDeadline.getGrade_detail().getGrade_point());
                    dialogPane.addRow(7, text1);
                    Text text2 = new Text("Hệ số: " + selectedDeadline.getGrade_detail().getGrade_detail_coefficient());
                    dialogPane.addRow(8, text2);
                    Text text3 = new Text("Nhận xét: " + selectedDeadline.getGrade_detail().getGrade_detail_description());
                    dialogPane.addRow(9, text3);
                }
                answer_textarea.setDisable(true);
                dialog.getDialogPane().lookupButton(submitButton).setDisable(true);
            } else {
                dialog.setResultConverter(buttonType -> {
                    if (buttonType == submitButton) {
                        if (answer_textarea.getText().isEmpty()) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Lỗi");
                            alert.setContentText("Vui lòng nhập nội dung trả lời");
                            alert.showAndWait();
                            return null;
                        }
                        String sql = "INSERT INTO deadline_student (deadline_id, student_id, deadline_student_answer) VALUES (" +
                                selectedDeadline.getDeadline().getDeadline_id() + ", " +
                                userLoggedIn.getUser_id() + ", '" +
                                answer_textarea.getText() + "')";
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
            }
            dialog.getDialogPane().setContent(dialogPane);
            dialog.getDialogPane().setPrefWidth(600); // Set preferred width
            dialog.getDialogPane().setPrefHeight(300); // Set preferred height
            dialog.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText("Lỗi khi nộp bài tập");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadDeadlines();
        submitButton.setOnMouseClicked((event) -> {
            submitButtonOnClick();
        });
    }

}
