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
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ManageTeachersWindow implements Initializable {
    public TableView tableView;

    public TableColumn nameColumn;
    public TableColumn surnameColumn;
    public TableColumn qualificationColumn;
    public TableColumn idColumn;
    int currentUser;
    ObservableList<TeacherTableView> tableData = FXCollections.observableArrayList();


    public void returnToMain(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartGUI.class.getResource("teacher-window.fxml"));
        Parent root = fxmlLoader.load();

        TeacherWindow controller = fxmlLoader.getController();
        controller.setCurrentUser(currentUser);
        Scene scene = new Scene(root);

        Stage stage = (Stage) tableView.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idColumn.setCellValueFactory(new PropertyValueFactory<StudentTableView, Integer>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<StudentTableView, String>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<StudentTableView, String>("surname"));
        qualificationColumn.setCellValueFactory(new PropertyValueFactory<StudentTableView, String>("qualification"));


        tableView.setEditable(true);

        qualificationColumn.setCellFactory(TextFieldTableCell.<TeacherTableView>forTableColumn());
        qualificationColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TeacherTableView, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TeacherTableView, String> t) {
                try {

                    DbUtils.tryQuery("UPDATE teachers SET qualification = '"+t.getNewValue()+"' WHERE id = '"+ idColumn.getCellObservableValue(t.getTablePosition().getRow()).getValue() +"'");
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                t.getTableView().getItems().get(t.getTablePosition().getRow()).setName(t.getNewValue());
            }
        });

        TableColumn<TeacherTableView, TeacherTableView> editColumn = new TableColumn<>("delete");
        editColumn.setCellFactory(col -> deleteButton());
        if(SchoolManagementSystem.getCurrentUser() == Admin.ADMIN_ID)
        {
            tableView.getColumns().add(editColumn);
        }

        try {
            getTable();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void getTable() throws SQLException, ClassNotFoundException {
        tableData.remove(tableData);
        tableView.getItems().clear();
        ArrayList<Teacher> list = Teacher.getTeachers();
        for(Teacher subject: list)
        {
            TeacherTableView temp = new TeacherTableView(
                    new SimpleIntegerProperty(subject.getId()),
                    new SimpleStringProperty(subject.getName()),
                    new SimpleStringProperty(subject.getSurname()),
                    new SimpleStringProperty(subject.getQualification()),
                    new SimpleStringProperty(subject.getQualification())
            );
            tableData.add(temp);
        }

        tableView.setItems(tableData);

    }
    private TableCell<TeacherTableView, TeacherTableView> deleteButton() {
        Button editButton = new Button("Delete");
        TableCell<TeacherTableView, TeacherTableView> cell = new TableCell<TeacherTableView, TeacherTableView>() {
            @Override
            public void updateItem(TeacherTableView subject, boolean empty) {
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
                TeacherTableView subject = cell.getTableView().getItems().get(cell.getIndex());
                try {
                    DbUtils.tryQuery("DELETE FROM teachers WHERE id = '"+ subject.getId() + "'");
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
}
