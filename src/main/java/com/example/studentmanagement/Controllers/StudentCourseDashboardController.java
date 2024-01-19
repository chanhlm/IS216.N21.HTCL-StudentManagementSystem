package com.example.studentmanagement.Controllers;

import com.example.studentmanagement.Enitities.ClassEnitity;
import com.example.studentmanagement.Enitities.CoursesEnitity;
import com.example.studentmanagement.Enitities.UserEnitity;
import com.example.studentmanagement.Holder.UserHolder;
import com.example.studentmanagement.Utils.DatabaseConnection;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class StudentCourseDashboardController implements Initializable {
    private final UserEnitity userLoggedIn = UserHolder.getInstance().getUser();

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

    private void getRenderCourseList(){
        course_table.getItems().clear();
        String sqlCourseTeacherClass = "SELECT " +
                "course.course_id, course.course_name, course.time_start_in_day," +
                " course.time_end_in_day, course.day_of_week, course.course_teacher_id, course.class_id," +
                "u.first_name, u.last_name, " +
                " class.class_grade, class.class_character, class.class_sequence FROM course" +
                " INNER JOIN users u ON course.course_teacher_id = u.user_id " +
                " INNER JOIN class ON course.class_id = class.class_id " +
                " WHERE course.class_id = " + userLoggedIn.getClassEnitity().getClass_id() +
                " ORDER BY course.day_of_week, course.time_start_in_day ASC";
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
    }

}
