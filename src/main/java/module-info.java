module com.example.programavimotechnologijos {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;
    requires spring.context;
    requires spring.web;
    requires spring.core;
    requires com.google.gson;

    opens com.example.programavimotechnologijos to javafx.fxml;
    exports com.example.programavimotechnologijos;
    exports com.example.control;
    opens com.example.control to javafx.fxml;
    exports com.example.gui;
    opens com.example.gui to javafx.fxml;
}