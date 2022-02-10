package com.example.programavimotechnologijos;

import com.example.control.DbUtils;

import java.io.Serializable;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Student extends User implements Serializable {
    String email;
    int group_id;
    public Student(int postfix, String name, String surname, String password, String email) {
        super(postfix, name, surname, password);
        this.email = email;
    }

    public Student(int postfix, String name, String surname, String password, String email, int group) {
        super(postfix, name, surname, password);

        this.email = email;
        this.group_id = group;
    }
    public Student() {}


    public static Student getStudentById(int id) throws ClassNotFoundException, SQLException {
        Connection connection = DbUtils.connectToDb();
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM students WHERE id = " + id;

        Student student = new Student();
        try
        {
            ResultSet rs = statement.executeQuery(query);
            rs.next();
            student = new Student(rs.getInt(1), rs.getString(2),rs.getString(3),rs.getString(4), rs.getString(5),rs.getInt(6));
            student.setId(id);
        }
        catch (Exception e)
        {
            System.out.println("Student by ID " + e);
            student = null;
        }
        DbUtils.disconnectFromDb(connection, statement);
        return student;
    }
    public static void createStudents(Student student) throws ClassNotFoundException, SQLException {
        Connection connection = DbUtils.connectToDb();
        String Insert = "INSERT INTO students(id, name, surname, password, email, group_id) VALUES (?,?,?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(Insert);

        preparedStatement.setInt(1, student.getId());
        preparedStatement.setString(2, student.getName());
        preparedStatement.setString(3, student.getSurname());
        preparedStatement.setString(4, student.getPassword());
        preparedStatement.setString(5, student.getEmail());
        preparedStatement.setInt(6, student.getGroup_id());

        preparedStatement.execute();
        DbUtils.disconnectFromDb(connection, preparedStatement);
    }
    public static void updateStudent(Student student) throws SQLException, ClassNotFoundException {
        if(student.getGroup_id()==0)
        {
            DbUtils.tryQuery("UPDATE students SET name = '"+student.getName()+"', surname = '"+student.getSurname()+"', password = '"+ student.getPassword()+"', email = '"+student.getEmail()+"', group_id = NULL WHERE id = '" + student.getId() + "'");
        }
        else
        {
            DbUtils.tryQuery("UPDATE students SET name = '"+student.getName()+"', surname = '"+student.getSurname()+"', password = '"+ student.getPassword()+"', email = '"+student.getEmail()+"', group_id = '"+ student.getGroup_id()+"' WHERE id = '" + student.getId() + "'");

        }
    }
    public static void deleteStudent(Student student) throws SQLException, ClassNotFoundException {
        DbUtils.tryQuery("DELETE FROM students WHERE id = '"+ student.getId() + "'");
    }
    public static ArrayList<Student> getStudents() throws ClassNotFoundException, SQLException {
        Connection connection = DbUtils.connectToDb();
        Statement statement = connection.createStatement();
        String query = "SELECT * FROM students";
        ArrayList<Student> list = new ArrayList<Student>();
        try
        {
            ResultSet rs = statement.executeQuery(query);
            while(rs.next())
            {
                Student student = new Student(rs.getInt(1), rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getInt(6));
                student.setId(rs.getInt(1));
                list.add(student);
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }

        DbUtils.disconnectFromDb(connection, statement);
        return list;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        String group = Integer.toString(group_id);
        if(group_id==0)
        {
            group = "";
        }
        return "{\"id\":\""+getId()+"\", " +
                "\"name\":\""+getName()+"\"," +
                "\"surname\":\""+getSurname()+"\","+
                "\"password\":\""+getPassword()+"\","+
                "\"email\":\""+getEmail()+"\","+
                "\"group_id\":\""+group+"\"}";
    }
}
