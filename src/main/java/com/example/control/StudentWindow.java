package com.example.control;

import com.example.constants.Admin;
import com.example.programavimotechnologijos.StartGUI;
import com.example.programavimotechnologijos.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class StudentWindow implements Initializable {
    public AnchorPane chooseGroupAnchor;
    @FXML
    public ChoiceBox groupDrop;
    public TreeTableView treeTable;
    public TreeTableColumn idTreeColumn;
    public TreeTableColumn nameTreeColumn;
    public TreeTableColumn descriptionTreeColumn;
    private ObservableList<String> groupDropData = FXCollections.observableArrayList();

    public AnchorPane chooseCourseAnchor;

    @FXML
    public ChoiceBox courseDrop;
    private ObservableList<String> courseDropData = FXCollections.observableArrayList();

    public AnchorPane courseInfoAnchor;
    public Label userText;
    public Label courseText;
    SchoolManagementSystem sys;

    public void setSys(SchoolManagementSystem sys) {
        this.sys = sys;
    }
    int currentUser;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpList(SchoolManagementSystem.getCurrentUser());
    }
    public void checkStudent() throws SQLException, ClassNotFoundException {

        Student student = Student.getStudentById(currentUser);
        if(student.getGroup_id()==0)
        {
            getCourseDropDown();
            chooseCourseAnchor.setVisible(true);
            chooseGroupAnchor.setVisible(false);
            courseInfoAnchor.setVisible(false);
        }
        else
        {
            chooseCourseAnchor.setVisible(false);
            chooseGroupAnchor.setVisible(false);
            courseInfoAnchor.setVisible(true);
        }
    }
    public void chooseGroup(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Student student = Student.getStudentById(currentUser);
        if(groupDrop.getValue() == null)
        {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a group from the \"Choose group\" dropdown menu", ButtonType.OK);
            alert.showAndWait();
            return;
        }
        String[] groupArray = groupDrop.getValue().toString().split(" ");
        student.setGroup_id(Integer.parseInt(groupArray[0]));
        Student.updateStudent(student);
        setUpList(SchoolManagementSystem.getCurrentUser());
        chooseCourseAnchor.setVisible(false);
        chooseGroupAnchor.setVisible(false);
        courseInfoAnchor.setVisible(true);
    }

    public void chooseCourse(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        enableChooseCourse();
    }
    public void enableChooseCourse() throws SQLException, ClassNotFoundException {

        if(getGroupDropDown())
        {
            chooseCourseAnchor.setVisible(false);
            chooseGroupAnchor.setVisible(true);
            courseInfoAnchor.setVisible(false);
        }
    }

    private void getCourseDropDown() throws SQLException, ClassNotFoundException {
        courseDropData.remove(courseDropData);
        ArrayList<Course> courses = Course.getCourses();

        courseDropData.add(null);
        for(Course course: courses)
        {
            courseDropData.add(Integer.toString(course.getId()) + " " + course.getName());
        }
        courseDrop.setItems(courseDropData);
    }
    private Boolean getGroupDropDown() throws SQLException, ClassNotFoundException {
        groupDropData = FXCollections.observableArrayList();
        ArrayList<Group> groups = Group.getAllGroups();
        groupDropData.add(null);
        if(courseDrop.getValue() == null)
        {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a course from the \"Choose course\" dropdown menu", ButtonType.OK);
            alert.showAndWait();
            return false;
        }
        String[] courseStrings = courseDrop.getValue().toString().split(" ");
        int course_id = Integer.parseInt(courseStrings[0]);
        int groupCount =0;
        for(Group group: groups)
        {
            if(group.getCourse_id() == course_id)
            {
                groupDropData.add(Integer.toString(group.getId()) + " " + group.getName());
                groupCount++;
            }

        }
        if(groupCount==0)
        {
            Alert alert = new Alert(Alert.AlertType.WARNING, "No groups in this course, please select different course", ButtonType.OK);
            alert.showAndWait();
            return false;
        }
        groupDrop.setItems(groupDropData);
        return true;
    }

    public int getCurrentUser() {
        return currentUser;
    }
    public void setUpList(int user_id) {
        try {
            Student student = Student.getStudentById(user_id);
            if(student==null)
                return;
            userText.setText("Hello, " + student.getId() + " " + student.getName() + " " + student.getSurname());
            Course parentCourse = Course.getCourseByStudent(user_id);
            if(parentCourse == null)
                return;
            courseText.setText("Here's your course management window for course " + parentCourse.getId() + " " + parentCourse.getName());

            idTreeColumn.setCellValueFactory(new TreeItemPropertyValueFactory<SubjectTableView,Integer>("id"));
            nameTreeColumn.setCellValueFactory(new TreeItemPropertyValueFactory<SubjectTableView,String>("name"));
            descriptionTreeColumn.setCellValueFactory(new TreeItemPropertyValueFactory<SubjectTableView,String>("description"));

            SubjectTableView parent = new SubjectTableView(
                    new SimpleIntegerProperty(parentCourse.getId()),
                    new SimpleIntegerProperty(0),
                    new SimpleStringProperty(parentCourse.getName()),
                    new SimpleIntegerProperty(parentCourse.getCreator_id()),
                    new SimpleStringProperty(parentCourse.getDescription()),
                    new SimpleIntegerProperty(0));
            TreeItem<SubjectTableView> itemRoot = new TreeItem<SubjectTableView>(parent);
            setUpCourseChildren(itemRoot, parentCourse.getId(), SchoolManagementSystem.getCurrentUser());

            treeTable.setRoot(itemRoot);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void setUpCourseChildren(TreeItem<SubjectTableView> parentItem, int course_id, int user_id) throws SQLException, ClassNotFoundException {
        ArrayList<Subject> subjects = Subject.getSubjectsNotInCourseByCreatorID(Admin.ADMIN_ID, course_id);
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
    public void goBack(ActionEvent actionEvent) throws IOException {
        chooseCourseAnchor.setVisible(true);
        chooseGroupAnchor.setVisible(false);
        courseInfoAnchor.setVisible(false);
    }

    public void logOut(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGUI.class.getResource("login-window.fxml"));
        Parent root = fxmlLoader.load();

        SchoolManagementSystem.setCurrentUser(0);

        Scene scene = new Scene(root);

        Stage stage = (Stage) courseDrop.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void unenroll(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Student student = Student.getStudentById(SchoolManagementSystem.getCurrentUser());
        student.setGroup_id(0);
        try
        {
            Student.updateStudent(student);
            FXMLLoader fxmlLoader = new FXMLLoader(StartGUI.class.getResource("student-window.fxml"));
            Parent root = fxmlLoader.load();

            StudentWindow controller = fxmlLoader.getController();
            SchoolManagementSystem.setCurrentUser(currentUser);
            controller.setCurrentUser(SchoolManagementSystem.getCurrentUser());
            controller.checkStudent();
            controller.setUpList(SchoolManagementSystem.getCurrentUser());
            Scene scene = new Scene(root);

            Stage stage = (Stage) chooseCourseAnchor.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e)
        {
            System.out.println(e);
            Alert alert = new Alert(Alert.AlertType.WARNING, "Unable to unenroll", ButtonType.OK);
            alert.showAndWait();
        }

    }
}
