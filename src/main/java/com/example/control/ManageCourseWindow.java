package com.example.control;

import com.example.constants.Admin;
import com.example.programavimotechnologijos.StartGUI;
import com.example.programavimotechnologijos.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
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
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ManageCourseWindow implements Initializable {
    @FXML
    public TableView<CourseTableView> tableView;
    @FXML
    public TextArea course_description;
    @FXML
    public TextField course_name;
    @FXML
    public TextField creator_idField;
    @FXML
    public ChoiceBox courseDropdown;
    private ObservableList<String> courseData = FXCollections.observableArrayList();
    @FXML
    public ListView InCourseList;
    private ObservableList<String> inCourseListData = FXCollections.observableArrayList();
    @FXML
    public ListView ToAddList;
    private ObservableList<String> toAddListData = FXCollections.observableArrayList();


    TableColumn<CourseTableView, Integer> treeTableColumn1 = new TableColumn<>("ID");
    TableColumn<CourseTableView, String> treeTableColumn2 = new TableColumn<>("Name");
    TableColumn<CourseTableView, String> treeTableColumn3 = new TableColumn<>("Description");
    TableColumn<CourseTableView, Integer> treeTableColumn4 = new TableColumn<>("Creator");

    private ObservableList<CourseTableView> data = FXCollections.observableArrayList();

    Alert emptyName;
    Alert success;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        emptyName = new Alert(Alert.AlertType.INFORMATION, "Fill empty fields", ButtonType.OK);
        success = new Alert(Alert.AlertType.INFORMATION, "Success creating element", ButtonType.OK);

        tableView.setEditable(true);

        treeTableColumn1.setCellValueFactory(new PropertyValueFactory<>("id"));
        treeTableColumn2.setCellValueFactory(new PropertyValueFactory<>("name"));
        treeTableColumn3.setCellValueFactory(new PropertyValueFactory<>("description"));
        treeTableColumn4.setCellValueFactory(new PropertyValueFactory<>("creator_id"));

        TableColumn<CourseTableView, CourseTableView> editColumn = new TableColumn<>("delete");
        editColumn.setCellFactory(col -> deleteButton());

        treeTableColumn2.setCellFactory(TextFieldTableCell.<CourseTableView>forTableColumn());
        treeTableColumn2.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<CourseTableView, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<CourseTableView, String> t) {
                try {
                    DbUtils.tryQuery("UPDATE course SET `"+t.getTablePosition().getTableColumn().getText()+"` = '"+t.getNewValue()+"' WHERE (`"+t.getTablePosition().getTableColumn().getText() +"` = '"+ t.getOldValue() +"')");
                    getCoursesDrop();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                t.getTableView().getItems().get(t.getTablePosition().getRow()).setName(t.getNewValue());
            }
        });

        tableView.getColumns().add(treeTableColumn1);
        tableView.getColumns().add(treeTableColumn2);
        tableView.getColumns().add(treeTableColumn3);
        tableView.getColumns().add(treeTableColumn4);
        tableView.getColumns().add(editColumn);

        try {
            getCourses();
            getCoursesDrop();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getCourses() throws ClassNotFoundException, SQLException {
        creator_idField.setText(Integer.toString(SchoolManagementSystem.getCurrentUser()));
        data.removeAll(data);
        Connection connection = DbUtils.connectToDb();
        Statement statement = connection.createStatement();
        String query= new String();
        if(SchoolManagementSystem.getCurrentUser()==Admin.ADMIN_ID)
        {
            query = "SELECT * FROM course";
        }
        else
        {
            query = "SELECT * FROM course WHERE creator_id = " + SchoolManagementSystem.getCurrentUser();
        }
        //String query = "SELECT * FROM course";
        ResultSet rs = statement.executeQuery(query);
        while(rs.next()){
            CourseTableView temp = new CourseTableView();
            temp.setId(rs.getInt("id"));
            temp.setName(rs.getString("name"));
            temp.setDescription(rs.getString("description"));
            temp.setCreator_id(rs.getInt("creator_id"));
            data.add(temp);
        }
        //tableView.getItems().clear();
        tableView.setItems(data);
        DbUtils.disconnectFromDb(connection, statement);
    }
    private void getCoursesDrop() throws SQLException, ClassNotFoundException {
        courseData.removeAll(courseData);
        courseData.add(null);
        ArrayList<Course> courses = new ArrayList<Course>();
        if(SchoolManagementSystem.getCurrentUser()!= Admin.ADMIN_ID)
        {
            courses = Course.getCoursesByCreatorID(SchoolManagementSystem.getCurrentUser());
        }
        else
        {
            courses = Course.getCourses();
        }
        for(Course course: courses)
        {
            courseData.add(Integer.toString(course.getId())+ " " + course.getName());
        }
        courseDropdown.setItems(courseData);
        courseDropdown.setOnAction(actionEvent -> {
            try {
                generateLists();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

    }
    private void generateLists() throws SQLException, ClassNotFoundException {
        if(courseDropdown.getValue() != null)
        {
            String[] selectedCourse = courseDropdown.getValue().toString().split(" ");
            generateToAddLists(Integer.parseInt(selectedCourse[0]), SchoolManagementSystem.getCurrentUser());
            generateInCourseList(Integer.parseInt(selectedCourse[0]), SchoolManagementSystem.getCurrentUser());
        }
        else
        {
            toAddListData.remove(toAddListData);
            ToAddList.setItems(toAddListData);
            inCourseListData.remove(inCourseListData);
            InCourseList.setItems(inCourseListData);
        }
    }
    private void generateToAddLists(int course_id, int user_id) throws SQLException, ClassNotFoundException {
        toAddListData.removeAll(toAddListData);

        ArrayList<Subject> list = Subject.getSubjectsInCourseByCreatorID(user_id, course_id);

        for(Subject subject:list)
        {
            toAddListData.add(subject.getId() + " " + subject.getName());
        }
        ToAddList.setItems(toAddListData);

    }
    private void generateInCourseList(int course_id, int user_id) throws SQLException, ClassNotFoundException {
        inCourseListData.removeAll(inCourseListData);
        ArrayList<Subject> list = Subject.getSubjectsNotInCourseByCreatorID(user_id, course_id);
        for(Subject subject:list)
        {
            inCourseListData.add(subject.getId() + " " + subject.getName());
        }
        InCourseList.setItems(inCourseListData);
    }


    private TableCell<CourseTableView, CourseTableView> deleteButton() {
        Button editButton = new Button("Delete");
        TableCell<CourseTableView, CourseTableView> cell = new TableCell<CourseTableView, CourseTableView>() {
            @Override
            public void updateItem(CourseTableView course, boolean empty) {
                super.updateItem(course, empty);
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
                CourseTableView course = cell.getTableView().getItems().get(cell.getIndex());
                try {
                    DbUtils.tryQuery("DELETE FROM course WHERE id = '"+ course.getId() + "'");
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                data.remove(course);

            }
        });

        return cell ;
    }

    public void createCourse(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if(course_name.getText().length()!=0){
            insertCourse();
            course_name.setText("");
            course_description.setText("");
            success.showAndWait();
        }
        else
            emptyName.showAndWait();
    }
    private void insertCourse() throws ClassNotFoundException, SQLException {
        Connection connection = DbUtils.connectToDb();
        String Insert = "INSERT INTO course(name, description, creator_id) VALUES (?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(Insert);
        preparedStatement.setString(1, course_name.getText());
        preparedStatement.setString(2, course_description.getText());
        preparedStatement.setInt(3, SchoolManagementSystem.getCurrentUser());
        preparedStatement.execute();
        DbUtils.disconnectFromDb(connection, preparedStatement);
        getCourses();
        getCoursesDrop();
    }
    public void loadTable(Event event) throws SQLException, ClassNotFoundException {
        getCourses();
    }

    public void goBack(ActionEvent actionEvent) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(StartGUI.class.getResource("teacher-window.fxml"));
        Parent root = fxmlLoader.load();


        Scene scene = new Scene(root);

        Stage stage = (Stage) tableView.getScene().getWindow();
        stage.setScene(scene);
        stage.show();

    }

    public void moveToRight(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if(ToAddList.getFocusModel().getFocusedItem() != null) {
            String[] selectedSubject = ToAddList.getFocusModel().getFocusedItem().toString().split(" ");
            String[] selectedCourse = courseDropdown.getValue().toString().split(" ");
            SubjectsInCourses.createRelation(Integer.parseInt(selectedSubject[0]), Integer.parseInt(selectedCourse[0]));
            generateLists();
        }
    }

    public void moveToLeft(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        if(InCourseList.getFocusModel().getFocusedItem() != null) {
            String[] selectedSubject = InCourseList.getFocusModel().getFocusedItem().toString().split(" ");
            String[] selectedCourse = courseDropdown.getValue().toString().split(" ");
            SubjectsInCourses.deleteRelation(Integer.parseInt(selectedSubject[0]), Integer.parseInt(selectedCourse[0]));
            generateLists();
        }
    }
}
