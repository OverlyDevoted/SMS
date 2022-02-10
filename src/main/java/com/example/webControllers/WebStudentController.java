package com.example.webControllers;

import com.example.programavimotechnologijos.Course;
import com.example.programavimotechnologijos.Group;
import com.example.programavimotechnologijos.Student;
import com.example.programavimotechnologijos.Teacher;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Properties;

@Controller
public class WebStudentController {

    @RequestMapping(value = "/student/getByCredentials", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getByCredentials(@RequestBody String request) throws SQLException, ClassNotFoundException {
        Gson gson = new Gson();
        Properties properties = gson.fromJson(request, Properties.class);
        Student student = Student.getStudentById(Integer.parseInt(properties.getProperty("id")));
        student.setId(Integer.parseInt(properties.getProperty("id")));
        if(!Objects.equals(student.getPassword(), properties.getProperty("password")))
        {
            return "Wrong password";
        }
        if(student== null)
            return "No students found";

        return student.toString();
    }


    @RequestMapping(value = "/student/allStudents", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getAllStudents() throws SQLException, ClassNotFoundException {
        ArrayList<Student> list = Student.getStudents();
        if(list.size() == 0)
            return "No students found";
        String res = "";
        for(Student student: list)
            res += student.toString() + '\n';
        return res;
    }

    @RequestMapping(value = "/student/addNewStudent", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String addNewStudent(@RequestBody String request) throws SQLException, ClassNotFoundException {
        Gson gson = new Gson();
        Properties properties = gson.fromJson(request, Properties.class);

        Group group = Group.getGroupByID(Integer.parseInt(properties.getProperty("group_id")));
        if(group==null)
            return "Such course does not exist";


        Student student = new Student(
                Integer.parseInt(properties.getProperty("id")),
                properties.getProperty("name"),
                properties.getProperty("surname"),
                properties.getProperty("password"),
                properties.getProperty("email"),
                Integer.parseInt(properties.getProperty("group_id")));
        Student.createStudents(student);
        return "Success";
    }

    @RequestMapping(value = "/student/updateStudent/{id}", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String updateStudent(@RequestBody String request, @PathVariable("id") String id) throws SQLException, ClassNotFoundException {
        Gson gson = new Gson();
        Properties properties = gson.fromJson(request, Properties.class);

        Student student = Student.getStudentById(Integer.parseInt(id));
        if(student == null)
            return "No such student found";


        student.setId(Integer.parseInt(id));
        student.setName(properties.getProperty("name"));
        student.setSurname(properties.getProperty("surname"));
        student.setPassword(properties.getProperty("password"));
        student.setEmail(properties.getProperty("email"));
        if(properties.getProperty("group_id").equals(""))
        {
            student.setGroup_id(0);
        }
        else
        {
            student.setGroup_id(Integer.parseInt(properties.getProperty("group_id")));
        }

        try{
            Student.updateStudent(student);
            return "Success";
        }
        catch (Exception e)
        {
            return "Unable to edit";
        }

    }

    @RequestMapping(value = "/student/deleteStudent/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String deleteStudent(@PathVariable("id") String id) throws SQLException, ClassNotFoundException {

        Student student = Student.getStudentById(Integer.parseInt(id));
        if(student== null)
            return "No such student found";
        student.setId(Integer.parseInt(id));
        Student.deleteStudent(student);
        return "Success";
    }
}
