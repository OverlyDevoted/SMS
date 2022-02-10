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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ManageStudentWindow implements Initializable {
    public TableColumn nameColumn;
    public TableColumn surnameColumn;
    public TableColumn emailColumn;
    public TableColumn groupColumn;
    public ChoiceBox courseChoice;
    public ChoiceBox groupChoice;
    public TableView tableView;
    public TableColumn idColumn;
    private ObservableList<String> courseChoiceData = FXCollections.observableArrayList();
    private ObservableList<String> groupChoiceData = FXCollections.observableArrayList();
    private ObservableList<StudentTableView> tableData = FXCollections.observableArrayList();

    public void filterList(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        getTable();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idColumn.setCellValueFactory(new PropertyValueFactory<StudentTableView, Integer>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<StudentTableView, String>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<StudentTableView, String>("surname"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<StudentTableView, String>("email"));
        groupColumn.setCellValueFactory(new PropertyValueFactory<StudentTableView, Integer>("group_id"));

        tableView.setEditable(true);
        emailColumn.setCellFactory(TextFieldTableCell.<StudentTableView>forTableColumn());
        emailColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<StudentTableView, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<StudentTableView, String> t) {
                try {
                    DbUtils.tryQuery("UPDATE students SET email = '"+t.getNewValue()+"' WHERE id = '"+ idColumn.getCellObservableValue(t.getTablePosition().getRow()).getValue() +"'");
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                t.getTableView().getItems().get(t.getTablePosition().getRow()).setName(t.getNewValue());
            }
        });

        TableColumn<StudentTableView, StudentTableView> editColumn = new TableColumn<>("delete");
        editColumn.setCellFactory(col -> deleteButton());
        if(SchoolManagementSystem.getCurrentUser() == Admin.ADMIN_ID)
        {
            tableView.getColumns().add(editColumn);
        }

        try {

            getGroupChoiceDrop();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
    private void getTable() throws SQLException, ClassNotFoundException {
        tableData.remove(tableData);
        tableView.getItems().clear();
        ArrayList<Student> list = Student.getStudents();
        String[] selectedGroup = groupChoice.getValue().toString().split(" ");
        for(Student student: list)
        {
            if(student.getGroup_id() == Integer.parseInt(selectedGroup[0]) && groupChoice.getValue().toString() != "")
            {

                StudentTableView temp = new StudentTableView(
                        new SimpleIntegerProperty(student.getId()),
                        new SimpleStringProperty(student.getName()),
                        new SimpleStringProperty(student.getSurname()),
                        new SimpleStringProperty(student.getPassword()),
                        new SimpleStringProperty(student.getEmail()),
                        new SimpleIntegerProperty(student.getGroup_id()));
                    tableData.add(temp);
            }
        }

        tableView.setItems(tableData);

    }

    private void getGroupChoiceDrop() throws SQLException, ClassNotFoundException {
        groupChoiceData.remove(groupChoiceData);
        ArrayList<Group> groups = new ArrayList<Group>();
        if(SchoolManagementSystem.getCurrentUser() == Admin.ADMIN_ID)
        {
            groups = Group.getAllGroups();
        }
        else
        {
            groups = Group.getGroupByCreatorAndCurator(SchoolManagementSystem.getCurrentUser(), SchoolManagementSystem.getCurrentUser());
        }
        for(Group group: groups)
        {
            groupChoiceData.add(Integer.toString(group.getId()) + " " + group.getName());
        }
        groupChoice.setItems(groupChoiceData);

    }

    private TableCell<StudentTableView, StudentTableView> deleteButton() {
        Button editButton = new Button("Delete");
        TableCell<StudentTableView, StudentTableView> cell = new TableCell<StudentTableView, StudentTableView>() {
            @Override
            public void updateItem(StudentTableView subject, boolean empty) {
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
                StudentTableView subject = cell.getTableView().getItems().get(cell.getIndex());
                try {
                    DbUtils.tryQuery("DELETE FROM students WHERE id = '"+ subject.getId() + "'");
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

    public void goBack(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGUI.class.getResource("teacher-window.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);

        Stage stage = (Stage) tableView.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
