package com.example.control;

import com.example.constants.Admin;
import com.example.programavimotechnologijos.StartGUI;
import com.example.programavimotechnologijos.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ManageGroupsWindow implements Initializable {

    public TextField groupNameText;
    public ChoiceBox courseDropdown;
    public ChoiceBox curatorDropdown;

    public TableView tableView;

    public TableColumn idColumn;
    public TableColumn nameColumn;
    public TableColumn courseColumn;
    public TableColumn curatorColumn;
    public TableColumn creatorColumn;

    public ChoiceBox groupDropdown;
    public ChoiceBox studentDropdown;



    private ObservableList<String> teacherData = FXCollections.observableArrayList();
    private ObservableList<String> courseDropData = FXCollections.observableArrayList();
    private ObservableList<String> studentDropData = FXCollections.observableArrayList();
    private ObservableList<String> groupDropData = FXCollections.observableArrayList();
    private ObservableList<GroupTableView> tableData = FXCollections.observableArrayList();

    public void addStudentToGroup(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if(isStudentToGroupSelected()) {
            String[] studentString = studentDropdown.getValue().toString().split(" ");
            int studentID = Integer.parseInt(studentString[0]);

            String[] groupString = groupDropdown.getValue().toString().split(" ");
            int groupID= Integer.parseInt(groupString[0]);

            Student student = Student.getStudentById(studentID);
            if(student.getGroup_id() == groupID)
            {
                Alert alert = new Alert(Alert.AlertType.WARNING, student.getId() + " " + student.getName()+ " " + student.getSurname() +" student is already in this "+ groupDropdown.getValue().toString(), ButtonType.OK);
                alert.showAndWait();
                return;
            }
            student.setGroup_id(groupID);
            Student.updateStudent(student);
            Alert alert = new Alert(Alert.AlertType.WARNING, "Successfully added " + studentDropdown.getValue().toString() + " to group " + groupDropdown.getValue().toString(), ButtonType.OK);
            alert.showAndWait();
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Select a student a group to pair them", ButtonType.OK);
            alert.showAndWait();
        }
    }
    public boolean isStudentToGroupSelected()
    {
        if(studentDropdown.getValue() == null || groupDropdown.getValue() == null)
            return false;
        return true;
    }

    public void createGroup(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if(groupNameText.getText() != "" && courseDropdown.getValue() != null && curatorDropdown != null)
        {
            Group newGroup = new Group(groupNameText.getText(),
                    Integer.parseInt(courseDropdown.getValue().toString()),
                    Integer.parseInt(curatorDropdown.getValue().toString()),
                    SchoolManagementSystem.getCurrentUser()
                    );
            Group.createGroup(newGroup);
            getTable();
            getGroupDropDown();
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Fill out all the fields", ButtonType.OK);
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idColumn.setCellValueFactory(new PropertyValueFactory<GroupTableView, Integer>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<GroupTableView, String>("name"));
        courseColumn.setCellValueFactory(new PropertyValueFactory<GroupTableView, Integer>("course_id"));
        curatorColumn.setCellValueFactory(new PropertyValueFactory<GroupTableView, Integer>("curator"));
        creatorColumn.setCellValueFactory(new PropertyValueFactory<GroupTableView, Integer>("creator_id"));
        TableColumn<GroupTableView, GroupTableView> editColumn = new TableColumn<>("delete");
        editColumn.setCellFactory(col -> deleteButton());
        tableView.getColumns().add(editColumn);

        try {
            getTable();
            getCourseDropDown();
            getTeacherDropDown();
            getGroupDropDown();
            getStudentDropDown();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private TableCell<GroupTableView, GroupTableView> deleteButton() {
        Button editButton = new Button("Delete");
        TableCell<GroupTableView, GroupTableView> cell = new TableCell<GroupTableView, GroupTableView>() {
            @Override
            public void updateItem(GroupTableView subject, boolean empty) {
                super.updateItem(subject, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(editButton);
                }
            }
        };
        editButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                GroupTableView subject = cell.getTableView().getItems().get(cell.getIndex());
                try {
                    DbUtils.tryQuery("DELETE FROM groupings WHERE id = '"+ subject.getId() + "'");
                    getGroupDropDown();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                tableData.remove(subject);

            }
        });

        return cell ;
    }
    private void getCourseDropDown() throws SQLException, ClassNotFoundException {
        courseDropData.remove(courseDropData);
        ArrayList<Course> courses = new ArrayList<Course>();
        if(SchoolManagementSystem.getCurrentUser() == Admin.ADMIN_ID)
        {
            courses = Course.getCourses();
        }
        else
        {
            courses = Course.getCoursesByCreatorID(SchoolManagementSystem.getCurrentUser());
        }
        courseDropData.add(null);
        for(Course course: courses)
        {
            courseDropData.add(Integer.toString(course.getId()));
        }
        courseDropdown.setItems(courseDropData);
    }
    private void getTeacherDropDown() throws SQLException, ClassNotFoundException {
        teacherData.remove(teacherData);
        ArrayList<Teacher> teachers = Teacher.getTeachers();
        teacherData.add(null);
        for(Teacher teacher: teachers)
        {
            teacherData.add(Integer.toString(teacher.getId()));
        }
        curatorDropdown.setItems(teacherData);
    }
    private void getGroupDropDown() throws SQLException, ClassNotFoundException {
        groupDropData.remove(groupDropData);
        ArrayList<Group> groups = new ArrayList<Group>();
        if(SchoolManagementSystem.getCurrentUser() != Admin.ADMIN_ID)
        {
            groups = Group.getAllGroups();
        }
        else
        {
            groups = Group.getGroupByCreatorAndCurator(SchoolManagementSystem.getCurrentUser(), SchoolManagementSystem.getCurrentUser());
        }
        groupDropData.add(null);
        for(Group group: groups)
        {
            groupDropData.add(Integer.toString(group.getId()) + " " + group.getName());
        }
        groupDropdown.setItems(groupDropData);
    }
    private void getStudentDropDown() throws SQLException, ClassNotFoundException {
        studentDropData.remove(studentDropdown);
        ArrayList<Student> students = Student.getStudents();
        studentDropData.add(null);
        for(Student student: students)
        {
            studentDropData.add(Integer.toString(student.getId())+" " + student.getName() + " " + student.getSurname());
        }
            studentDropdown.setItems(studentDropData);
    }

    private void getTable() throws SQLException, ClassNotFoundException {
        tableData.remove(tableData);
        tableView.getItems().clear();
        ArrayList<Group> list = new ArrayList<Group>();
        if(SchoolManagementSystem.getCurrentUser() != Admin.ADMIN_ID)
        {
            list = Group.getGroupByCreatorAndCurator(SchoolManagementSystem.getCurrentUser(), SchoolManagementSystem.getCurrentUser());
        }
        else
        {
            list = Group.getAllGroups();
        }
        for(Group group: list)
        {

            GroupTableView temp = new GroupTableView(
                    new SimpleIntegerProperty(group.getId()),
                    new SimpleStringProperty(group.getName()),
                    new SimpleIntegerProperty(group.getCourse_id()),
                    new SimpleIntegerProperty(group.getCurator_id()),
                    new SimpleIntegerProperty(group.getCreator_id())
            );
            tableData.add(temp);
        }

        tableView.setItems(tableData);
    }

    public void goBack(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGUI.class.getResource("teacher-window.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);

        Stage stage = (Stage) tableView.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
