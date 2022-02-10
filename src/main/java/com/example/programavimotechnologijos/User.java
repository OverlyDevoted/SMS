package com.example.programavimotechnologijos;

import java.io.Serializable;
import java.time.LocalDate;

public class  User implements Serializable {
    private int id;
    private String name;
    private String surname;
    private LocalDate dateCreated;
    private String login;
    private String password;

    public User() {

    }
    public User(int id_postpix, String name, String surname, String password) {
        this.name = name;
        this.surname = surname;
        this.password = password;
        id = LocalDate.now().getYear()*10000+id_postpix;
    }


    public User(String name, String surname, String login, String password, int id_postpix) {
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.password = password;
        id = LocalDate.now().getYear()*10000+id_postpix;
        dateCreated = LocalDate.now();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
