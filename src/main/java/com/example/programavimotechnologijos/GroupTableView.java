package com.example.programavimotechnologijos;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class GroupTableView {
    SimpleIntegerProperty id;
    SimpleStringProperty name;
    SimpleIntegerProperty course_id;
    SimpleIntegerProperty curator;
    SimpleIntegerProperty creator_id;

    public GroupTableView(SimpleIntegerProperty id, SimpleStringProperty name, SimpleIntegerProperty course_id, SimpleIntegerProperty curator, SimpleIntegerProperty creator_id) {
        this.id = id;
        this.name = name;
        this.course_id = course_id;
        this.curator = curator;
        this.creator_id = creator_id;
    }

    public GroupTableView() {
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public int getCourse_id() {
        return course_id.get();
    }

    public SimpleIntegerProperty course_idProperty() {
        return course_id;
    }

    public void setCourse_id(int course_id) {
        this.course_id.set(course_id);
    }

    public int getCurator() {
        return curator.get();
    }

    public SimpleIntegerProperty curatorProperty() {
        return curator;
    }

    public void setCurator(int curator) {
        this.curator.set(curator);
    }

    public int getCreator_id() {
        return creator_id.get();
    }

    public SimpleIntegerProperty creator_idProperty() {
        return creator_id;
    }

    public void setCreator_id(int creator_id) {
        this.creator_id.set(creator_id);
    }
}
