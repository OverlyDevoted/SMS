package com.example.programavimotechnologijos;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class StudentTableView extends UserTableView{
    SimpleStringProperty email;
    SimpleIntegerProperty group_id;

    public StudentTableView(SimpleIntegerProperty id, SimpleStringProperty name, SimpleStringProperty surname, SimpleStringProperty password, SimpleStringProperty email, SimpleIntegerProperty group_id) {
        super(id, name, surname, password);
        this.email = email;
        this.group_id = group_id;
    }

    public StudentTableView() {
    }

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public int getGroup_id() {
        return group_id.get();
    }

    public SimpleIntegerProperty group_idProperty() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id.set(group_id);
    }
}
