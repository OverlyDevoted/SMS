package com.example.control;

import com.example.constants.Admin;
import com.example.programavimotechnologijos.StartGUI;
import com.example.programavimotechnologijos.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TeacherWindow implements Initializable {

    @FXML
    public AnchorPane course_create_anchor;
    @FXML
    public AnchorPane subject_create_anchor;
    @FXML
    public TextField subject_name;
    @FXML
    public TextArea subject_description;
    @FXML
    public TextField credits;


    public TreeTableView<SubjectTableView> treeTable;
    public TreeTableColumn idTreeColumn;
    public TreeTableColumn nameTreeColumn;
    public TreeTableColumn descriptionTreeColumn;
    public Label greetingField;



    private AnchorPane currentlyVisible;
    SchoolManagementSystem sys;
    private int currentUser;

    Alert emptyName;
    Alert success;

    public void setSys(SchoolManagementSystem sys) {
        this.sys = sys;
    }

    public void setCourseCreateAnchorVisible(ActionEvent actionEvent) {
        if(currentlyVisible!=null)
        currentlyVisible.setVisible(false);
        currentlyVisible = course_create_anchor;
        currentlyVisible.setVisible(true);
    }
    public void setSubjectCreateAnchorVisible(ActionEvent actionEvent) {
        if(currentlyVisible!=null)
        currentlyVisible.setVisible(false);
        currentlyVisible = subject_create_anchor;
        currentlyVisible.setVisible(true);
    }

    public void createSubject(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {

        if(subject_name.getText().length()!=0 && credits.getText().length() != 0){
            insertSubject();
            subject_name.setText("");
            subject_description.setText("");
            credits.setText("");
            success.showAndWait();
        }
        else
            emptyName.showAndWait();
    }
    private void insertSubject() throws ClassNotFoundException, SQLException {
        Connection connection = DbUtils.connectToDb();
        String Insert = "INSERT INTO subjects(name, credits, creator_id, description) VALUES (?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(Insert);
        preparedStatement.setString(1, subject_name.getText());
        preparedStatement.setInt(2, Integer.parseInt(credits.getText()));
        preparedStatement.setInt(3, currentUser);
        preparedStatement.setString(4, subject_description.getText());
        preparedStatement.execute();
        DbUtils.disconnectFromDb(connection, preparedStatement);
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        emptyName = new Alert(Alert.AlertType.INFORMATION, "Fill empty fields", ButtonType.OK);
        success = new Alert(Alert.AlertType.INFORMATION, "Success creating element", ButtonType.OK);
        greetingField.setText("Currently connected user: " + SchoolManagementSystem.getCurrentUser());

        setUpList();

    }
    public void setUpList() {
        try {
            ArrayList<Course> parents = new ArrayList<Course>();
            if(SchoolManagementSystem.getCurrentUser() == Admin.ADMIN_ID)
            {
                parents = Course.getCourses();
            }
            else
            {
                parents = Course.getCoursesByCreatorID(SchoolManagementSystem.getCurrentUser());
            }
            idTreeColumn.setCellValueFactory(new TreeItemPropertyValueFactory<SubjectTableView,Integer>("id"));
            nameTreeColumn.setCellValueFactory(new TreeItemPropertyValueFactory<SubjectTableView,String>("name"));
            descriptionTreeColumn.setCellValueFactory(new TreeItemPropertyValueFactory<SubjectTableView,String>("description"));

            SubjectTableView main = new SubjectTableView(new SimpleStringProperty("Courses"));

            TreeItem<SubjectTableView> itemRoot = new TreeItem<SubjectTableView>(main);
            for(Course course: parents)
            {
                SubjectTableView parent = new SubjectTableView(
                        new SimpleIntegerProperty(course.getId()),
                        new SimpleIntegerProperty(0),
                        new SimpleStringProperty(course.getName()),
                        new SimpleIntegerProperty(course.getCreator_id()),
                        new SimpleStringProperty(course.getDescription()),
                        new SimpleIntegerProperty(0));
                TreeItem<SubjectTableView> item = new TreeItem<SubjectTableView>(parent);
                setUpCourseChildren(item, course.getId(), SchoolManagementSystem.getCurrentUser());
                itemRoot.getChildren().add(item);
            }
            treeTable.setRoot(itemRoot);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void setUpCourseChildren(TreeItem<SubjectTableView> parentItem, int course_id, int user_id) throws SQLException, ClassNotFoundException {
        ArrayList<Subject> subjects = Subject.getSubjectsNotInCourseByCreatorID(user_id, course_id);

        for(Subject subject: subjects)
        {
            SubjectTableView parent = new SubjectTableView(
                    new SimpleIntegerProperty(subject.getId()),
                    new SimpleIntegerProperty(subject.getCredits()),
                    new SimpleStringProperty(subject.getName()),
                    new SimpleIntegerProperty(subject.getCreator_id()),
                    new SimpleStringProperty(subject.getDescription()),
                    new SimpleIntegerProperty(subject.getParent_subject_id()));
            TreeItem<SubjectTableView> item = new TreeItem<SubjectTableView>(parent);
            addChildren(item, parent);
            parentItem.getChildren().add(item);
        }
    }

    private void addChildren(TreeItem<SubjectTableView> item, SubjectTableView passParent) throws SQLException, ClassNotFoundException {
        ArrayList<Subject> subjects = Subject.getSubjectByParent(passParent.getId());

        for(Subject subject: subjects)
        {
            SubjectTableView parent = new SubjectTableView(
                    new SimpleIntegerProperty(subject.getId()),
                    new SimpleIntegerProperty(subject.getCredits()),
                    new SimpleStringProperty(subject.getName()),
                    new SimpleIntegerProperty(subject.getCreator_id()),
                    new SimpleStringProperty(subject.getDescription()),
                    new SimpleIntegerProperty(subject.getParent_subject_id()));
            TreeItem<SubjectTableView> childItem = new TreeItem<SubjectTableView>(parent);
            addChildren(childItem, parent);
            item.getChildren().add(childItem);
        }
    }

    public void setCurrentUser(int currentUser) {
        this.currentUser = currentUser;
    }
    public void openCourseManager(ActionEvent actionEvent) throws IOException {
            FXMLLoader fxmlLoader = new FXMLLoader(StartGUI.class.getResource("manage-course-window.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);

        Stage stage = (Stage) subject_name.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    public void loadLoginWindow(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGUI.class.getResource("login-window.fxml"));
        Parent root = fxmlLoader.load();

        SchoolManagementSystem.setCurrentUser(0);

        Scene scene = new Scene(root);

        Stage stage = (Stage) subject_name.getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }
    public void openSubjectManage(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGUI.class.getResource("manage-subject-window.fxml"));
        Parent root = fxmlLoader.load();



        Scene scene = new Scene(root);

        Stage stage = (Stage) subject_name.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    public void openStudents(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGUI.class.getResource("manage-student-window.fxml"));
        Parent root = fxmlLoader.load();


        Scene scene = new Scene(root);

        Stage stage = (Stage) subject_name.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    public void openTeacherManage(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGUI.class.getResource("manage-teachers-window.fxml"));
        Parent root = fxmlLoader.load();


        Scene scene = new Scene(root);

        Stage stage = (Stage) subject_name.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    public void openGroupsWindow(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGUI.class.getResource("manage-groups-window.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);

        Stage stage = (Stage) subject_name.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public int getCurrentUser() {
        return currentUser;
    }

}
