module com.example.studentmanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.sql;
//    requires com.jfoenix;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires io.github.cdimascio.dotenv.java;
    requires jbcrypt;
    requires jfxtras.controls;
    requires java.desktop;

    opens com.example.studentmanagement to javafx.fxml;
    exports com.example.studentmanagement;
    exports com.example.studentmanagement.Controllers;
    opens com.example.studentmanagement.Controllers to javafx.fxml;
}