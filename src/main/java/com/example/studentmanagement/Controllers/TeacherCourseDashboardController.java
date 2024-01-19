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

public class TeacherCourseDashboardController implements Initializable {
    private final UserEnitity userLoggedIn = UserHolder.getInstance().getUser();

    @FXML
    private TableColumn<CoursesEnitity, Integer> course_id;

    @FXML
    private TableColumn<CoursesEnitity, String> course_name;

    @FXML
    private TableColumn<CoursesEnitity, String> day_of_week;

    @FXML
    private TableColumn<CoursesEnitity, String> class_name_col;

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
                " class.class_grade, class.class_character, class.class_sequence FROM course" +
                " INNER JOIN users u ON course.course_teacher_id = u.user_id " +
                " INNER JOIN class ON course.class_id = class.class_id " +
                " WHERE course.course_teacher_id = " + userLoggedIn.getUser_id() +
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

                CoursesEnitity coursesEnitity = new CoursesEnitity();
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
                class_name_col.setCellValueFactory(p -> new ReadOnlyObjectWrapper<>(p.getValue().getClassEnitity().getClass_name()));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Thất bại");
            alert.setHeaderText("Không thể lấy danh sách lớp học");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getRenderCourseList();
    }

}
