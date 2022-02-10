package com.example.programavimotechnologijos;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class TeacherTableView extends UserTableView{
    SimpleStringProperty qualification;

    public TeacherTableView(SimpleIntegerProperty id, SimpleStringProperty name, SimpleStringProperty surname, SimpleStringProperty password, SimpleStringProperty qualification) {
        super(id, name, surname, password);
        this.qualification = qualification;
    }

    public TeacherTableView() {
    }

    public String getQualification() {
        return qualification.get();
    }

    public SimpleStringProperty qualificationProperty() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification.set(qualification);
    }
}
