package com.example.control;

import com.example.programavimotechnologijos.SchoolManagementSystem;
import com.example.programavimotechnologijos.StartGUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class LoginWindow implements Initializable {
    @FXML
    public TextField loginField;
    Parent root;
    @FXML
    public TextField passwordField;

    public void OpenSignUpWindow(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGUI.class.getResource("signup-window.fxml"));
        root = fxmlLoader.load();

        SignUpWindow controler = fxmlLoader.getController();

        Scene scene = new Scene(root);

        Stage stage = (Stage) loginField.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    public void OpenMainWindow(ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException {
        Alert noUser = new Alert(Alert.AlertType.ERROR, "No such user", ButtonType.OK);
        try {
            Integer.parseInt(loginField.getText());
        }
        catch (Exception e) {
            Alert onlyIntegers = new Alert(Alert.AlertType.WARNING, "You must enter your ID number to login", ButtonType.OK);
            onlyIntegers.showAndWait();
            return;
        }

        if(!checkIfEmpty())
        {
            if(SchoolManagementSystem.checkStudentsLogin(Integer.parseInt(loginField.getText()), passwordField.getText()))
                loadStudentWindow();
            else if(SchoolManagementSystem.checkTeachersLogin(Integer.parseInt(loginField.getText()),passwordField.getText()))
                loadTeacherWindow();
            else
                noUser.showAndWait();
        }

    }

    void loadStudentWindow() throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGUI.class.getResource("student-window.fxml"));
        root = fxmlLoader.load();

        StudentWindow controller = fxmlLoader.getController();
        SchoolManagementSystem.setCurrentUser(Integer.parseInt(loginField.getText()));
        controller.setCurrentUser(SchoolManagementSystem.getCurrentUser());
        controller.checkStudent();
        controller.setUpList(SchoolManagementSystem.getCurrentUser());
        Scene scene = new Scene(root);

        Stage stage = (Stage) loginField.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    void loadTeacherWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGUI.class.getResource("teacher-window.fxml"));

        root = fxmlLoader.load();
        TeacherWindow controller = fxmlLoader.getController();
        SchoolManagementSystem.setCurrentUser(Integer.parseInt(loginField.getText()));
        controller.greetingField.setText("Currently connected user: " + SchoolManagementSystem.getCurrentUser());
        controller.setUpList();
        Scene scene = new Scene(root);

        Stage stage = (Stage) loginField.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    boolean checkIfEmpty() {
        Alert empty = new Alert(Alert.AlertType.INFORMATION, "Login and password must have values", ButtonType.OK);
        if(loginField.getText().length() ==0 || passwordField.getText().length()==0)
        {
            empty.showAndWait();
            return true;
        }
        return false;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
