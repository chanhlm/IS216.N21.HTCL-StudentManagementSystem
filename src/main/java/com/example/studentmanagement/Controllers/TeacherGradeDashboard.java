package com.example.studentmanagement.Controllers;

import com.example.studentmanagement.CommonObject.ValidateDeadlineSubmitResult;
import com.example.studentmanagement.Enitities.*;
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
import javafx.scene.text.Text;
import javafx.util.converter.NumberStringConverter;

import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class TeacherGradeDashboard implements Initializable {
    private final UserEnitity userLoggedIn = UserHolder.getInstance().getUser();

    @FXML
    private TableView<DeadlineStudentEnitity> deadline_table;

    @FXML
    private TableColumn<DeadlineStudentEnitity, LocalDate> date_end_col;

    @FXML
    private TableColumn<DeadlineStudentEnitity, LocalDate> date_start_col;

    @FXML
    private Button detailButton;

    @FXML
    private TableColumn<DeadlineStudentEnitity, String> course_col;

    @FXML
    private TableColumn<DeadlineStudentEnitity, Integer> id_col;


    @FXML
    private TableColumn<DeadlineStudentEnitity, String> title_col;

    @FXML
    private TableColumn<DeadlineStudentEnitity, String> class_col;

    @FXML
    private TableColumn<DeadlineStudentEnitity, String> student_name_col;

    @FXML
    private TableColumn<DeadlineStudentEnitity, Button> status_col;

    @FXML
    private Button gradeButton;

    private void gradeButtonOnClick() {
        try {
            DeadlineStudentEnitity selectedDeadline = deadline_table.getSelectionModel().getSelectedItem();
            if (selectedDeadline == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setContentText("Vui lòng chọn bài tập cần chấm điểm");
                alert.showAndWait();
                return;
            }

            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Chấm điểm");
            dialog.setHeaderText("Chấm điểm cho bài tập " + selectedDeadline.getDeadline().getTitle());
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
            answer_textarea.setText(selectedDeadline.getDeadline_student_answer());
            answer_textarea.setDisable(true);

            Label grade_label = new Label("Điểm: ");
            TextField grade_textfield = new TextField();
            grade_textfield.setTextFormatter(new TextFormatter<>(new NumberStringConverter()));

            Label grade_detail_coefficient_label = new Label("Hệ số: ");
            ComboBox<Double> grade_detail_coefficient_textfield = new ComboBox<>();
            grade_detail_coefficient_textfield.getItems().addAll(0.1, 0.2, 0.5, 1.0, 2.0, 3.0, 4.0);
            grade_detail_coefficient_textfield.setValue(1.0);

            Label grade_detail_comment_label = new Label("Nhận xét: ");
            TextArea grade_detail_comment_textarea = new TextArea();

            dialogPane.addRow(0, course_label, course_combobox);
            dialogPane.addRow(1, title_label, title_textfield);
            dialogPane.addRow(2, description_label, description_textarea);
            dialogPane.addRow(3, date_start_label, date_start_picker);
            dialogPane.addRow(4, date_end_label, date_end_picker);
            dialogPane.addRow(5, answer_label, answer_textarea);
            dialogPane.addRow(6, grade_label, grade_textfield);
            dialogPane.addRow(7, grade_detail_coefficient_label, grade_detail_coefficient_textfield);
            dialogPane.addRow(8, grade_detail_comment_label, grade_detail_comment_textarea);

//        Submit
            ButtonType submitButton = new ButtonType("Chấm Bài", ButtonBar.ButtonData.APPLY);
            dialog.getDialogPane().getButtonTypes().addAll(submitButton, ButtonType.CANCEL);
            ValidateDeadlineSubmitResult validateDeadlineSubmitResult = ValidateDeadlineSubmitResult.isValidDeadlineToSubmit(selectedDeadline);
            if (validateDeadlineSubmitResult != ValidateDeadlineSubmitResult.SUBMITTED || validateDeadlineSubmitResult == ValidateDeadlineSubmitResult.DEADLINE_NOT_OPEN || validateDeadlineSubmitResult == ValidateDeadlineSubmitResult.HAS_POINT) {
                dialog.setContentText(validateDeadlineSubmitResult.getMessage());
                Text text = new Text(validateDeadlineSubmitResult.getMessage());
                dialogPane.addRow(6, text);
                if (validateDeadlineSubmitResult == ValidateDeadlineSubmitResult.HAS_POINT){
                    text.setStyle("-fx-fill: green;");
                    String comment = selectedDeadline.getGrade_detail().getGrade_detail_description();
                    grade_detail_comment_textarea.setText(comment);
                }
                else text.setStyle("-fx-fill: red;");
                grade_textfield.setDisable(true);
                grade_detail_coefficient_textfield.setDisable(true);
                dialog.getDialogPane().lookupButton(submitButton).setDisable(true);
                grade_detail_comment_textarea.setDisable(true);
            } else {
                dialog.setResultConverter(buttonType -> {
                    if (buttonType == submitButton) {
                        String grade_title = selectedDeadline.getDeadline().getTitle() + " - Học sinh: " + selectedDeadline.getStudent().getFull_Name();
                        String grade_detail_description = grade_detail_comment_textarea.getText() == null ? "" : grade_detail_comment_textarea.getText();
                        Integer grade_course_id = selectedDeadline.getDeadline().getCourse().getCourse_id();
                        Double grade_point;
                        Double grade_detail_coefficient;
                        try {
                            grade_point = Double.parseDouble(grade_textfield.getText());
                            grade_detail_coefficient = grade_detail_coefficient_textfield.getValue();
                            if (grade_point < 0 || grade_point > 10) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Lỗi");
                                alert.setContentText("Vui lòng nhập điểm từ 0 đến 10");
                                alert.showAndWait();
                                return null;
                            }
                        } catch (Exception e) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Lỗi");
                            alert.setContentText("Vui lòng nhập điểm và hệ số là số thực");
                            alert.showAndWait();
                            return null;
                        }
                        String sqlCreateGradeDetail = "INSERT INTO grade_detail (GRADE_DETAIL_NAME, GRADE_DETAIL_COEFFICIENT, GRADE_POINT, GRADE_DETAIL_DESCRIPTION, GRADE_DETAIL_COURSE_ID) " +
                                "VALUES ('"
                                + grade_title + "', "
                                + grade_detail_coefficient + ", "
                                + grade_point + ", '"
                                + grade_detail_description + "', "
                                + grade_course_id + ")";
                        try {
                            Integer insertedGradeDetail_ID = DatabaseConnection.mutationReturnGeneratedKeys(sqlCreateGradeDetail, "GRADE_DETAIL_ID");
                            String sqlUpdateDeadlineStudent = "UPDATE deadline_student SET GRADE_DETAIL_ID = " + insertedGradeDetail_ID + " WHERE DEADLINE_ID = " + selectedDeadline.getDeadline().getDeadline_id()
                                    + " AND STUDENT_ID = " + selectedDeadline.getStudent().getUser_id();
                            DatabaseConnection.mutation(sqlUpdateDeadlineStudent);
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Thành công");
                            alert.setHeaderText("Giao bài tập thành công");
                            alert.setContentText("Đã giao bài tập cho môn " + selectedCourse);
                            alert.showAndWait();
                            loadDeadlineSubmitStudent();
                        } catch (Exception e) {
                            e.printStackTrace();
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

    private void loadDeadlineSubmitStudent() {
        deadline_table.getItems().clear();
        String query = "SELECT d.deadline_id, d.date_start, d.date_end, d.from_course_id, d.by_teacher_id, d.title, d.description, " +
                "ds.student_id, ds.deadline_student_answer, " +
                "g.grade_detail_id, g.grade_detail_name, g.grade_detail_coefficient, g.grade_point, g.grade_detail_description, g.grade_detail_id, " +
                "c.course_name, " +
                "u.first_name AS teacher_first_name, u.last_name AS teacher_last_name, " +
                "st.first_name AS student_first_name, st.last_name AS student_last_name " +
                "FROM deadline d " +
                "JOIN course c ON d.from_course_id = c.course_id " +
                "JOIN class cl ON c.class_id = cl.class_id " +
                "JOIN deadline_student ds ON d.deadline_id = ds.deadline_id " +
                "JOIN users st ON ds.student_id = st.user_id " +
                "JOIN users u ON d.by_teacher_id = u.user_id " +
                "LEFT JOIN grade_detail g ON ds.grade_detail_id = g.grade_detail_id " +
                "WHERE d.by_teacher_id = " + userLoggedIn.getUser_id();
        ResultSet resultSet;
        try {
            resultSet = DatabaseConnection.query(query);
            ObservableList<DeadlineStudentEnitity> deadlineList = FXCollections.observableArrayList();
            while (resultSet.next()) {
                DeadlineEnitity deadline = new DeadlineEnitity();
                deadline.setDeadline_id(resultSet.getInt("deadline_id"));
                deadline.setDate_start(resultSet.getDate("date_start").toLocalDate());
                deadline.setDate_end(resultSet.getDate("date_end").toLocalDate());

                CoursesEnitity course = new CoursesEnitity();
                course.setCourse_id(resultSet.getInt("from_course_id"));
                course.setCourse_name(resultSet.getString("course_name"));
                deadline.setCourse(course);

                UserEnitity teacher = new UserEnitity();
                teacher.setFirst_name(resultSet.getString("teacher_first_name"));
                teacher.setLast_name(resultSet.getString("teacher_last_name"));

                deadline.setTeacher(teacher);
                deadline.setTitle(resultSet.getString("title"));
                deadline.setDescription(resultSet.getString("description"));

                DeadlineStudentEnitity deadlineStudent = new DeadlineStudentEnitity();
                deadlineStudent.setDeadline(deadline);

                UserEnitity student = new UserEnitity();
                student.setUser_id(resultSet.getInt("student_id"));
                student.setFirst_name(resultSet.getString("student_first_name"));
                student.setLast_name(resultSet.getString("student_last_name"));
                deadlineStudent.setStudent(student);
                deadlineStudent.setDeadline_student_answer(resultSet.getString("deadline_student_answer"));

                if (resultSet.getInt("grade_detail_id") != 0) {
                    GradeEnitity gradeDetail = new GradeEnitity();
                    gradeDetail.setGrade_detail_id(resultSet.getInt("grade_detail_id"));
                    gradeDetail.setGrade_detail_name(resultSet.getString("grade_detail_name"));
                    gradeDetail.setGrade_detail_coefficient(resultSet.getDouble("grade_detail_coefficient"));
                    gradeDetail.setGrade_point(resultSet.getDouble("grade_point"));
                    gradeDetail.setGrade_detail_description(resultSet.getString("grade_detail_description"));
                    gradeDetail.setCourse(course);
                    deadlineStudent.setGrade_detail(gradeDetail);
                }

                deadline_table.getItems().add(deadlineStudent);
                course_col.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getDeadline().getCourse().getCourse_name()));
                date_end_col.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getDeadline().getDate_end()));
                date_start_col.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getDeadline().getDate_start()));
                id_col.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getDeadline().getDeadline_id()));
                title_col.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getDeadline().getTitle()));
                student_name_col.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getStudent() != null ? cellData.getValue().getStudent().getFull_Name() : "N/A"));
                status_col.setStyle("-fx-alignment: CENTER;");
                status_col.setCellValueFactory(cellData -> {
                    ValidateDeadlineSubmitResult result = ValidateDeadlineSubmitResult.isValidDeadlineToSubmit(cellData.getValue());
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
                    if (result == ValidateDeadlineSubmitResult.SUBMITTED) {
                        Button button = new Button("Cần chấm");
                        button.setStyle("-fx-background-color: #d2af12; -fx-text-fill: white; -fx-opacity: 1.0;");
                        button.setDisable(true);
                        return new ReadOnlyObjectWrapper<>(button);
                    }
                    if (result == ValidateDeadlineSubmitResult.VALID_TO_MODIFY) {
                        Button button = new Button("Chưa nộp");
                        button.setStyle("-fx-background-color: #266bcc; -fx-text-fill: white; -fx-opacity: 1.0;");
                        button.setDisable(true);
                        return new ReadOnlyObjectWrapper<>(button);
                    }
                    if (result == ValidateDeadlineSubmitResult.HAS_POINT) {
                        Double point = cellData.getValue().getGrade_detail().getGrade_point();
                        Button button = new Button(point.toString() + " điểm");
                        button.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-opacity: 1.0;");
                        button.setDisable(true);
                        return new ReadOnlyObjectWrapper<>(button);
                    }
                    return null;
                });
            }
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
        gradeButton.setOnAction(event -> {
            gradeButtonOnClick();
        });
        loadDeadlineSubmitStudent();
    }

}
