package com.example.webControllers;

import com.example.constants.Admin;
import com.example.programavimotechnologijos.*;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

@Controller
public class WebSubjectController {
    @RequestMapping(value = "/subject/allSubjects", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getAllSubjects() throws SQLException, ClassNotFoundException {
        ArrayList<Subject> list = Subject.getSubjectByCreatorID(SchoolManagementSystem.getCurrentUser());
        if(list.size() == 0)
            return "No students found";
        String res = "";
        for(Subject subject: list)
            res += subject.toString() + '\n';
        return res;
    }

    //used for students
    @RequestMapping(value = "/subject/getSubjectsByCourseID/{id}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getAllSubjects(@PathVariable("id") String id) throws SQLException, ClassNotFoundException {
        ArrayList<Subject> list = Subject.getSubjectsNotInCourseByCreatorID(Admin.ADMIN_ID,Integer.parseInt(id));
        if(list.size() == 0)
            return "No subjects found";
        String res = "[";
        int count = res.length();
        for(int i=0 ;i<count; i++)
        {
            if(i<count-1)
            {
                res += list.get(i).toString() + ',';
                continue;
            }
            res += list.get(i).toString();
        }
        return res+"]";
    }

    @RequestMapping(value = "/subject/addNewSubject", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String addNewSubject(@RequestBody String request) throws SQLException, ClassNotFoundException {
        Gson gson = new Gson();
        Properties properties = gson.fromJson(request, Properties.class);

        Teacher teacher = Teacher.getTeacherById(Integer.parseInt(properties.getProperty("creator_id")));

        if(teacher==null)
            return "Such teacher does not exist";


        Subject subject= new Subject(Integer.parseInt(properties.getProperty("id")),
                Integer.parseInt(properties.getProperty("credits")),
                properties.getProperty("name"),
                Integer.parseInt(properties.getProperty("creator_id")),
                properties.getProperty("description"));
        Subject.createSubject(subject);
        return "Success";
    }

    @RequestMapping(value = "/subject/updateSubject/{id}", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String updateSubject(@RequestBody String request, @PathVariable("id") String id) throws SQLException, ClassNotFoundException {
        Gson gson = new Gson();
        Properties properties = gson.fromJson(request, Properties.class);

        Teacher teacher = Teacher.getTeacherById(Integer.parseInt(properties.getProperty("creator_id")));
        if(teacher == null)
            return "No such teacher found";
        Subject subject = Subject.getSubjectById(Integer.parseInt(id));
        if(subject == null)
            return "No such subject found";

        subject.setCredits(Integer.parseInt(properties.getProperty("credits")));
        subject.setName(properties.getProperty("name"));
        subject.setCreator_id(Integer.parseInt(properties.getProperty("creator_id")));
        subject.setDescription(properties.getProperty("description"));

        Subject.updateSubject(subject);
        return "Success";
    }

    @RequestMapping(value = "/subject/deleteSubject/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String deleteSubject(@PathVariable("id") String id) throws SQLException, ClassNotFoundException {

        Subject subject = Subject.getSubjectById(Integer.parseInt(id));
        if(subject== null)
            return "No such subject found";
        Subject.deleteSubject(subject);
        return "Success";
    }
}
