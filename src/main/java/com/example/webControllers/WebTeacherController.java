package com.example.webControllers;

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
public class WebTeacherController {
    @RequestMapping(value = "/teacher/getByCredentials", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getByCredentials(@RequestBody String request) throws SQLException, ClassNotFoundException {
        Gson gson = new Gson();
        Properties properties = gson.fromJson(request, Properties.class);

        Teacher teacher = Teacher.getTeacherById(Integer.parseInt(properties.getProperty("id")));
        teacher.setId(Integer.parseInt(properties.getProperty("id")));
        if(!Objects.equals(teacher.getPassword(), properties.getProperty("password")))
        {
            return "Wrong password";
        }
        if(teacher== null)
            return "No teachers found";

        return teacher.toString();
    }

    @RequestMapping(value = "/teacher/allTeachers", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getAllTeachers() throws SQLException, ClassNotFoundException {
        ArrayList<Teacher> list = Teacher.getTeachers();
        if(list.size() == 0)
            return "No teachers found";
        String res = "[";
        int count = list.size();

        for(int i = 0; i < count; i++)
        {
            res += list.get(i).toString();
            if(i==count-1)
                continue;
            res += ',';
        }

        return res+"]";
    }

    @RequestMapping(value = "/teacher/addNewTeacher", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String addNewTeacher(@RequestBody String request) throws SQLException, ClassNotFoundException {
        Gson gson = new Gson();
        Properties properties = gson.fromJson(request, Properties.class);

        Teacher teacher = new Teacher(
                Integer.parseInt(properties.getProperty("id")),
                properties.getProperty("name"),
                properties.getProperty("surname"),
                properties.getProperty("password"),
                properties.getProperty("qualification"));
        Teacher.createTeacher(teacher);
        return "Success";
    }

    @RequestMapping(value = "/teacher/updateTeacher/{id}", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String updateStudent(@RequestBody String request, @PathVariable("id") String id) throws SQLException, ClassNotFoundException {
        Gson gson = new Gson();
        Properties properties = gson.fromJson(request, Properties.class);

        Teacher teacher = Teacher.getTeacherById(Integer.parseInt(id));
        if(teacher == null)
            return "No such teacher found";

        teacher.setId(Integer.parseInt(id));
        teacher.setName(properties.getProperty("name"));
        teacher.setSurname(properties.getProperty("surname"));
        teacher.setPassword(properties.getProperty("password"));
        teacher.setQualification(properties.getProperty("qualification"));

        Teacher.updateTeacher(teacher);
        return "Success";
    }

    @RequestMapping(value = "/teacher/deleteTeacher/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String deleteStudent(@PathVariable("id") String id) throws SQLException, ClassNotFoundException {

        Teacher teacher = Teacher.getTeacherById(Integer.parseInt(id));
        if(teacher== null)
            return "No such student found";
        teacher.setId(Integer.parseInt(id));
        Teacher.deleteTeacher(teacher);
        return "Success";
    }
}
