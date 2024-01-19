package com.example.studentmanagement.Controllers;

import com.example.studentmanagement.Enitities.AnnouncementEnitity;
import com.example.studentmanagement.Enitities.ClassEnitity;
import com.example.studentmanagement.Enitities.UserEnitity;
import com.example.studentmanagement.Holder.UserHolder;
import com.example.studentmanagement.Utils.DatabaseConnection;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ParentAnnouncementDashboardController implements Initializable {
    private final UserEnitity userLoggedIn = UserHolder.getInstance().getUser();
    @FXML
    private TableView<AnnouncementEnitity> deadline_table;

    @FXML
    private TableColumn<AnnouncementEnitity, String> content_col;

    @FXML
    private TableColumn<AnnouncementEnitity, LocalDate> datetime_col;

    @FXML
    private TableColumn<AnnouncementEnitity, Integer> id_col;

    @FXML
    private TableColumn<AnnouncementEnitity, String> title_col;

    private void loadAnnouncements() {
        try {
            String sql = "SELECT ANNOUNCEMENT_ID, ANNOUNCEMENT_TITLE, ANNOUNCEMENT_CONTENT, ANNOUNCEMENT_DATETIME, ANNOUNCEMENT_USER_ID, ANNOUNCEMENT_CLASS_ID, ANNOUNCEMENT_SCHOOL_ID " +
                    "FROM ANNOUNCEMENT " +
                    "WHERE ANNOUNCEMENT_USER_ID = " + userLoggedIn.getCurrent_children_viewing().getUser_id() + " " +
                    "OR ANNOUNCEMENT_USER_ID = " + userLoggedIn.getUser_id() + " " +
                    "OR ANNOUNCEMENT_CLASS_ID = " + userLoggedIn.getCurrent_children_viewing().getClassEnitity().getClass_id() + " " +
                    "OR ANNOUNCEMENT_SCHOOL_ID = " + userLoggedIn.getSchool().getSchool_id() + " " +
                    "ORDER BY announcement_datetime DESC";
            ResultSet rs = DatabaseConnection.query(sql);
            while (rs.next()) {
                AnnouncementEnitity annoucementEnitity = new AnnouncementEnitity();
                annoucementEnitity.setAnnouncement_id(rs.getInt("announcement_id"));
                annoucementEnitity.setAnnouncement_title(rs.getString("announcement_title"));
                annoucementEnitity.setAnnouncement_content(rs.getString("announcement_content"));
                annoucementEnitity.setAnnouncement_date(rs.getDate("announcement_datetime").toLocalDate());
                annoucementEnitity.setUserEnitity(new UserEnitity(rs.getInt("ANNOUNCEMENT_USER_ID")));
                ClassEnitity classEnitity = null;

                if (rs.getInt("announcement_class_id") != 0) {
                    classEnitity = new ClassEnitity();
                    classEnitity.setClass_id(rs.getInt("announcement_class_id"));
                };
                annoucementEnitity.setClassEnitity(classEnitity);


                deadline_table.getItems().add(annoucementEnitity);
                id_col.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getAnnouncement_id()));
                title_col.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getAnnouncement_title()));
                content_col.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getAnnouncement_content()));
                datetime_col.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getAnnouncement_date()));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadAnnouncements();
    }


}
