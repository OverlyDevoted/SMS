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

public class ManageSubjectWindow implements Initializable
{
    public TableColumn idColumn;
    public TableColumn nameColumn;
    public TableColumn creditsColumn;
    public TableColumn descriptionColumn;
    public TableColumn courseColumn;
    public TextField creditsText;
    public TextField nameText;
    public TextArea descriptionText;

    public TableView tableView;

    public ChoiceBox parentSubDropbox;

    private ObservableList<SubjectTableView> data = FXCollections.observableArrayList();

    private ObservableList<String> parentSubData = FXCollections.observableArrayList();

    public void createSubject(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        if(creditsText.getText()!="" && nameText.getText() != "" && descriptionText.getText() != "")
        {
            int parentSub = 0;
            Subject subject = new Subject();
            if(parentSubDropbox.getValue() != null)
            {
                String[] parentString = parentSubDropbox.getValue().toString().split(" ");
                parentSub = Integer.parseInt(parentString[0]);
                subject = new Subject(Integer.parseInt(creditsText.getText()), nameText.getText(), SchoolManagementSystem.getCurrentUser(), descriptionText.getText(), parentSub);
            }
            else
            {
                subject = new Subject(Integer.parseInt(creditsText.getText()), nameText.getText(), SchoolManagementSystem.getCurrentUser(), descriptionText.getText());
            }

            Subject.createSubject(subject);
            getTable();
            getParentSubDrop();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Fill out all the fields", ButtonType.OK);
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        idColumn.setCellValueFactory(new PropertyValueFactory<SubjectTableView, Integer>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<SubjectTableView, Integer>("name"));
        creditsColumn.setCellValueFactory(new PropertyValueFactory<SubjectTableView, String>("credits"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<SubjectTableView, Integer>("description"));
        courseColumn.setCellValueFactory(new PropertyValueFactory<SubjectTableView, String>("creator_id"));

        tableView.setEditable(true);
        descriptionColumn.setCellFactory(TextFieldTableCell.<SubjectTableView>forTableColumn());
        descriptionColumn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<SubjectTableView, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<SubjectTableView, String> t) {
                try {
                    DbUtils.tryQuery("UPDATE subjects SET description = '"+t.getNewValue()+"' WHERE id = '"+ idColumn.getCellObservableValue(t.getTablePosition().getRow()).getValue() +"'");
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                t.getTableView().getItems().get(t.getTablePosition().getRow()).setName(t.getNewValue());
            }
        });

        TableColumn<SubjectTableView, SubjectTableView> parentColumn = new TableColumn<>("parent");
        parentColumn.setCellFactory(col -> {
            try {
                return parentChoiceBox();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        });
        tableView.getColumns().add(parentColumn);

        TableColumn<SubjectTableView, SubjectTableView> editColumn = new TableColumn<>("delete");
        editColumn.setCellFactory(col -> deleteButton());
        tableView.getColumns().add(editColumn);

        try {
            getTable();
            getParentSubDrop();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void setUpData(ObservableList<String> data) throws SQLException, ClassNotFoundException {
        data.removeAll(data);
        data.add(null);
        ArrayList<Subject> subjects;
        if(SchoolManagementSystem.getCurrentUser()!= Admin.ADMIN_ID)
        {
            subjects = Subject.getSubjectByCreatorID(SchoolManagementSystem.getCurrentUser());
        }
        else
        {
            subjects = Subject.getSubject();
        }
        for(Subject subject: subjects)
        {
            data.add(Integer.toString(subject.getId())+ " " + subject.getName());
        }
    }

    private TableCell<SubjectTableView, SubjectTableView> parentChoiceBox() throws SQLException, ClassNotFoundException {
        ObservableList<String>data = FXCollections.observableArrayList();

        ChoiceBox choiceBox = new ChoiceBox();
        TableCell<SubjectTableView,SubjectTableView> cell = new TableCell<SubjectTableView, SubjectTableView>() {
            @Override
            public void updateItem(SubjectTableView subject, boolean empty) {
                super.updateItem(subject, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(choiceBox);

                }
            }
        };

        choiceBox.setOnAction(actionEvent -> {
            String[] parentString;
            if(choiceBox.getValue() != null)
            {
                parentString= choiceBox.getValue().toString().split(" ");
            }
            else
            {
                parentString = new String[1];
                parentString[0] = null;
            }

            try {
                Subject subject = Subject.getSubjectById(cell.getTableRow().getItem().getId());
                if(parentString[0] != null)
                {
                    subject.setParent_subject_id(Integer.parseInt(parentString[0]));
                }
                Subject.updateSubjectParent(subject);
                choiceBox.setValue(cell.getTableRow().getItem().getId());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        });
        setUpData(data);
        choiceBox.setItems(data);

        return cell;
    }
    private TableCell<SubjectTableView, SubjectTableView> deleteButton() {
        Button editButton = new Button("Delete");
        TableCell<SubjectTableView, SubjectTableView> cell = new TableCell<SubjectTableView, SubjectTableView>() {
            @Override
            public void updateItem(SubjectTableView subject, boolean empty) {
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
                SubjectTableView subject = cell.getTableView().getItems().get(cell.getIndex());
                try {
                    DbUtils.tryQuery("DELETE FROM subjects WHERE id = '"+ subject.getId() + "'");
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                data.remove(subject);

            }
        });

        return cell ;
    }
    private void getParentSubDrop() throws SQLException, ClassNotFoundException {
        parentSubData.removeAll(parentSubData);
        parentSubData.add(null);
        ArrayList<Subject> subjects = new ArrayList<Subject>();
        if(SchoolManagementSystem.getCurrentUser()!= Admin.ADMIN_ID)
        {
            subjects = Subject.getSubjectByCreatorID(SchoolManagementSystem.getCurrentUser());
        }
        else
        {
            subjects = Subject.getSubject();
        }
        for(Subject subject: subjects)
        {
            parentSubData.add(Integer.toString(subject.getId())+ " " + subject.getName());
        }
        parentSubDropbox.setItems(parentSubData);
    }
    private void getTable() throws SQLException, ClassNotFoundException {
        data.remove(data);
        tableView.getItems().clear();
        ArrayList<Subject> list = new ArrayList<Subject>();
        if(SchoolManagementSystem.getCurrentUser() == Admin.ADMIN_ID)
        {
            list = Subject.getSubject();
        }
        else
        {
            list = Subject.getSubjectByCreatorID(SchoolManagementSystem.getCurrentUser());
        }
        for(Subject subject: list)
        {
            SubjectTableView temp = new SubjectTableView(
                    new SimpleIntegerProperty(subject.getId()),
                    new SimpleIntegerProperty(subject.getCredits()),
                    new SimpleStringProperty(subject.getName()),
                    new SimpleIntegerProperty(subject.getCreator_id()),
                    new SimpleStringProperty(subject.getDescription())
            );
            data.add(temp);
        }

        tableView.setItems(data);

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
