package com.example.webControllers;

import com.example.control.DbUtils;
import com.example.programavimotechnologijos.Course;
import com.example.programavimotechnologijos.CourseTableView;
import com.example.programavimotechnologijos.Student;
import com.example.programavimotechnologijos.Teacher;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Properties;

@Controller
public class WebCourseController {

    @RequestMapping(value = "/course/allCourses", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getUsers() throws SQLException, ClassNotFoundException {
        ArrayList<Course> courseList = Course.getCourses();
        String res = "[";
        if(courseList.size()==0)
            return "No courses found";
        int lenght = courseList.size();
        for(int i=0; i<lenght;i++)
        {
            res += courseList.get(i).toString();
            if(i == lenght-1)
                continue;
            res += ',';
        }
        res += "]";

        return res;
    }

    @RequestMapping(value = "/course/getCourseByStudent/{id}", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getCourseByStudent(@PathVariable("id") String id) throws SQLException, ClassNotFoundException {
        Course course = Course.getCourseByStudent(Integer.parseInt(id));
        if(course == null)
        {
            return "No course found";
        }
        else {
            return course.toString();
        }
    }

    @RequestMapping(value = "/course/addNewCourse", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String addNewCourse(@RequestBody String request) throws SQLException, ClassNotFoundException {
        Gson gson = new Gson();
        Properties properties = gson.fromJson(request, Properties.class);
        //Course course = Course.getCourseByID(Integer.parseInt(properties.getProperty("id")));
        //if(course != null)
            //return "A course with such id already exists";
        Teacher teacher = Teacher.getTeacherById(Integer.parseInt(properties.getProperty("creator_id")));
        if(teacher==null)
            return "No such teacher found";
        if(properties.getProperty("name").equals("") && properties.getProperty("description").equals(""))
        {
            return "Failure";
        }
        Course course = new Course(0,properties.getProperty("name"),properties.getProperty("description"),Integer.parseInt(properties.getProperty("creator_id")));
        try {
            Course.createCourse(course);
            return "Success";
        }
        catch (Exception e)
        {
            return "Failure";
        }
    }

    @RequestMapping(value = "/course/updateCourse/{id}", method = RequestMethod.PUT)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String updateCourse(@RequestBody String request, @PathVariable("id") String id) throws SQLException, ClassNotFoundException {
        Gson gson = new Gson();
        Properties properties = gson.fromJson(request, Properties.class);
        Course course = Course.getCourseByID(Integer.parseInt(id));
        if(course == null)
            return "No such course found";

        course.setName(properties.getProperty("name"));
        course.setDescription(properties.getProperty("description"));
        Course.updateCourse(course);
        return "Success";
    }

    @RequestMapping(value = "/course/deleteCourse/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String deleteCourse(@PathVariable("id") String id) throws SQLException, ClassNotFoundException {
        Course course = Course.getCourseByID(Integer.parseInt(id));
        if(course == null)
            return "No such course found";
        Course.deleteCourse(course);
        return "Success";
    }
}
