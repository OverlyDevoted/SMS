package com.example.programavimotechnologijos;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class SubjectTableView {
     SimpleIntegerProperty id;
     SimpleIntegerProperty credits;
     SimpleStringProperty name;
     SimpleIntegerProperty creator_id;
     SimpleStringProperty description;
     SimpleIntegerProperty parent_subject_id;

     public SubjectTableView() {
     }

     public SubjectTableView(SimpleIntegerProperty id, SimpleIntegerProperty credits, SimpleStringProperty name, SimpleIntegerProperty creator_id, SimpleStringProperty description) {
          this.id = id;
          this.credits = credits;
          this.name = name;
          this.creator_id = creator_id;
          this.description = description;
     }
     public SubjectTableView(SimpleIntegerProperty id, SimpleIntegerProperty credits, SimpleStringProperty name, SimpleIntegerProperty creator_id, SimpleStringProperty description, SimpleIntegerProperty parent_subject_id) {
          this.id = id;
          this.credits = credits;
          this.name = name;
          this.creator_id = creator_id;
          this.description = description;
          this.parent_subject_id = parent_subject_id;
     }
     public SubjectTableView(Subject subject) {
          setId(subject.getId());
          setCredits(subject.getCredits());
          setName(subject.getName());
          setCreator_id(subject.getCreator_id());
          setDescription(subject.getDescription());
          setParent_subject_id(subject.getParent_subject_id());
     }
     public SubjectTableView(SimpleStringProperty name)
     {
          this.name = name;
     }


     public int getParent_subject_id() {
          return parent_subject_id.get();
     }

     public SimpleIntegerProperty parent_subject_idProperty() {
          return parent_subject_id;
     }

     public void setParent_subject_id(int parent_subject_id) {
          this.parent_subject_id.set(parent_subject_id);
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

     public int getCredits() {
          return credits.get();
     }

     public SimpleIntegerProperty creditsProperty() {
          return credits;
     }

     public void setCredits(int credits) {
          this.credits.set(credits);
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

     public int getCreator_id() {
          return creator_id.get();
     }

     public SimpleIntegerProperty creator_idProperty() {
          return creator_id;
     }

     public void setCreator_id(int creator_id) {
          this.creator_id.set(creator_id);
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
}
