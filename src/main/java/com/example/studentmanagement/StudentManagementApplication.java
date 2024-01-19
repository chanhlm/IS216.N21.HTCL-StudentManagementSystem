package com.example.studentmanagement;

import com.example.studentmanagement.Holder.RouteHolder;
import com.example.studentmanagement.Holder.UserHolder;
import com.example.studentmanagement.Utils.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class StudentManagementApplication extends Application {
    private static Scene scene;
    private static final DatabaseConnection databaseConnection = new DatabaseConnection();
    private static RouteHolder routeHolder = RouteHolder.getInstance();
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StudentManagementApplication.class.getResource("login.fxml"));
        scene = new Scene(fxmlLoader.load(), 1280, 720);
        stage.setTitle("Student Management System");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static void setRoot(String fxmlName) {
        try {
            if (routeHolder.getRoute().equals(fxmlName)) {
                return;
            }
            routeHolder.setRoute(fxmlName);
            scene.setRoot(new FXMLLoader(StudentManagementApplication.class.getResource(fxmlName)).load());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public static void reloadRoot() {
        try {
            scene.setRoot(new FXMLLoader(StudentManagementApplication.class.getResource(routeHolder.getRoute())).load());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}