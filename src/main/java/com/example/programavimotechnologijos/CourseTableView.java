package com.example.programavimotechnologijos;



import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.SimpleTimeZone;

public class CourseTableView implements Serializable {
    SimpleIntegerProperty id;
    SimpleStringProperty name;
    SimpleStringProperty description;
    SimpleIntegerProperty creator_id;

    public CourseTableView(SimpleIntegerProperty id, SimpleStringProperty name, SimpleStringProperty description, SimpleIntegerProperty creator_id) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.creator_id = creator_id;
    }

    public CourseTableView() {
        id = new SimpleIntegerProperty();
        name = new SimpleStringProperty();
        description = new SimpleStringProperty();
        creator_id = new SimpleIntegerProperty();
    }

    @Override
    public String toString() {
        return id +
                " " + name +
                " " + description +
                " " + creator_id;
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

    public String getDescription() {
        return description.get();
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
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

