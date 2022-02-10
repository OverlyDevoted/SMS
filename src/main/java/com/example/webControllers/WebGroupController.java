package com.example.webControllers;

import com.example.programavimotechnologijos.Course;
import com.example.programavimotechnologijos.Group;
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
public class WebGroupController {
    @RequestMapping(value = "/group/getGroupByCourseID/{id}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getByID(@PathVariable("id") String id) throws SQLException, ClassNotFoundException {
        Gson gson = new Gson();
        int course_id = Integer.parseInt(id);
        ArrayList<Group> groups = Group.getAllGroups();
        ArrayList<Group> list = new ArrayList<Group>();
        for(Group group: groups)
        {
            if(group.getCourse_id() == course_id)
            {
                list.add(group);
            }
        }
        if(list.size() == 0)
            return "No groups found";
        String res = "[";
        int count= res.length();
        for(int i=0;i<count;i++)
        {
            if(i<count-1)
            {
                res+=list.get(i).toString()+",";
                continue;
            }
            res+=list.get(i).toString();
        }
        res+="]";
        return res;
    }
    @RequestMapping(value = "/group/allGroups", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getAllGroups() throws SQLException, ClassNotFoundException {
        ArrayList<Group> list = Group.getAllGroups();
        if(list.size() == 0)
            return "No groups found";
        String res = "";
        for(Group group: list)
            res += group.toString() + '\n';
        return res;
    }

    @RequestMapping(value = "/group/addNewGroup", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String addNewGroup(@RequestBody String request) throws SQLException, ClassNotFoundException {
        Gson gson = new Gson();
        Properties properties = gson.fromJson(request, Properties.class);

        Course course = Course.getCourseByID(Integer.parseInt(properties.getProperty("course_id")));
        if(course==null)
            return "Such course does not exist";
        Teacher teacher = Teacher.getTeacherById(Integer.parseInt(properties.getProperty("creator_id")));
        if(teacher==null)
            return "No such teacher found";
        teacher = Teacher.getTeacherById(Integer.parseInt(properties.getProperty("curator_id")));
        if(teacher==null)
            return "No such teacher found";

        Group group = new Group(properties.getProperty("name"),
                Integer.parseInt(properties.getProperty("course_id")),
                Integer.parseInt(properties.getProperty("curator_id")),
                Integer.parseInt(properties.getProperty("creator_id")));
        Group.createGroup(group);
        return "Success";
    }

    @RequestMapping(value = "/group/updateGroup/{id}", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String updateGroup(@RequestBody String request, @PathVariable("id") String id) throws SQLException, ClassNotFoundException {
        Gson gson = new Gson();
        Properties properties = gson.fromJson(request, Properties.class);
        Group group = Group.getGroupByID(Integer.parseInt(id));
        if(group == null)
            return "No such course found";

        group.setName(properties.getProperty("name"));
        group.setCurator_id(Integer.parseInt(properties.getProperty("curator_id")));
        group.setCourse_id(Integer.parseInt(properties.getProperty("course_id")));

        Group.updateGroup(group);
        return "Success";
    }

    @RequestMapping(value = "/group/deleteGroup/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String deleteGroup(@PathVariable("id") String id) throws SQLException, ClassNotFoundException {

        Group group = Group.getGroupByID(Integer.parseInt(id));
        if(group == null)
            return "No such course found";


        Group.deleteGroup(group);
        return "Success";
    }
}
