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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SignUpWindow implements Initializable {

    @FXML
    public RadioButton radioStudent;
    @FXML
    public RadioButton radioTeacher;
    @FXML
    public TextField studentEmail;
    @FXML
    public TextField teacherQualification;
    @FXML
    public TextField name;
    @FXML
    public TextField surname;

    @FXML
    public TextField password;

    ArrayList<TextField> defaultFields;
    ArrayList<TextField> teacherFields;
    ArrayList<TextField> studentFields;


    SchoolManagementSystem sys;
    public void setSys(SchoolManagementSystem sys) {
        this.sys = sys;
    }

    public void register(ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException {
        Alert suchUserExists = new Alert(Alert.AlertType.WARNING, "Such user already exists enter new login", ButtonType.OK);
        Alert registered = new Alert(Alert.AlertType.INFORMATION, "Successfully registered", ButtonType.OK);
        Alert hasValues = new Alert(Alert.AlertType.INFORMATION, "All fields have to be filled", ButtonType.OK);
        if(checkIfFilled())
        {
            if(radioStudent.isSelected()){
                registerStudent();
                loadLoginWindow();
            }
            else
            {
                registerTeacher();
                loadLoginWindow();
            }
        }
        else
            hasValues.showAndWait();
    }

    private void registerTeacher() throws ClassNotFoundException, SQLException {
        int id = LocalDate.now().getYear() * 1000 + SchoolManagementSystem.getNumberOfTeachers();

        Connection connection = DbUtils.connectToDb();
        String Insert = "INSERT INTO teachers(id, name, surname, password, qualification) VALUES (?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(Insert);
        preparedStatement.setInt(1, id);
        preparedStatement.setString(2, name.getText());
        preparedStatement.setString(3, surname.getText());
        preparedStatement.setString(4, password.getText());
        preparedStatement.setString(5, teacherQualification.getText());
        boolean isInserted=false;
        while(!isInserted)
        {
            try {
                preparedStatement.execute();
                break;
            }
            catch (SQLException e) {
                System.out.println(e);
            }
            id+=1;;
            preparedStatement.setInt(1,id);
        }
        Alert displayId = new Alert(Alert.AlertType.INFORMATION, "Your account ID is: " + id, ButtonType.OK);
        displayId.showAndWait();
        DbUtils.disconnectFromDb(connection, preparedStatement);
    }
    private void registerStudent() throws SQLException, ClassNotFoundException {
        int id = LocalDate.now().getYear() * 10000 + SchoolManagementSystem.getNumberOfStudents();
        Connection connection = DbUtils.connectToDb();
        String Insert = "INSERT INTO students(id, name, surname, password, email) VALUES (?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(Insert);
        preparedStatement.setInt(1, id);
        preparedStatement.setString(2, name.getText());
        preparedStatement.setString(3, surname.getText());
        preparedStatement.setString(4, password.getText());
        preparedStatement.setString(5, studentEmail.getText());
        boolean isInserted=false;
        while(!isInserted)
        {
            try {
                preparedStatement.execute();
                break;
            }
            catch (SQLException e) {
                System.out.println(e + " Generating new ID");
            }
            id+=1;;
            preparedStatement.setInt(1,id);
        }
        Alert displayId = new Alert(Alert.AlertType.INFORMATION, "Your account ID is: " + id, ButtonType.OK);
        displayId.showAndWait();
        DbUtils.disconnectFromDb(connection, preparedStatement);
    }


    public void openLoginWindow(ActionEvent actionEvent) throws IOException {
        loadLoginWindow();
    }
    private void loadLoginWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGUI.class.getResource("login-window.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);

        Stage stage = (Stage) name.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void hideStudent(ActionEvent actionEvent) {
        studentEmail.setDisable(true);
        teacherQualification.setDisable(false);
        radioTeacher.setSelected(true);
        radioStudent.setSelected(false);
    }
    public void hideTeacher(ActionEvent actionEvent) {
        studentEmail.setDisable(false);
        teacherQualification.setDisable(true);
        radioTeacher.setSelected(false);
        radioStudent.setSelected(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        studentEmail.setDisable(false);
        teacherQualification.setDisable(true);
        defaultFields = new ArrayList<TextField>();
        teacherFields = new ArrayList<TextField>();
        studentFields = new ArrayList<TextField>();
        defaultFields.add(name);
        defaultFields.add(surname);
        defaultFields.add(password);
        teacherFields.add(teacherQualification);
        studentFields.add(studentEmail);
    }

    private boolean checkIfFilled(){
        for(TextField field:defaultFields)
        {
            if(field.getText().length()==0)
                return false;
        }

        if(radioStudent.isSelected())
            return checkIfStudentFilled();
        else
            return checkIfTeacherFilled();
    }
    private boolean checkIfStudentFilled() {
        for(TextField field:studentFields)
        {
            if(field.getText().length()==0)
                return false;
        }
        return true;
    }
    private boolean checkIfTeacherFilled() {
        for(TextField field:teacherFields)
        {
            if(field.getText().length()==0)
                return false;
        }
        return true;
    }

}
