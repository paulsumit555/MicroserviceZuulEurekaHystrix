package com.example.galleryservice.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.galleryservice.model.Gallery;
import com.example.galleryservice.model.Image;
import com.example.galleryservice.model.Student;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@RestController
public class StudentController
{
	
	 @Autowired
	 Environment environment;
	
	@Autowired
	private RestTemplate restTemplate;
	
    @GetMapping("/name")
    public String getControllerName()
    {
    	 System.out.println("Inside MyRestController::backend...");
    	 
         String serverPort = environment.getProperty("local.server.port");
  
         System.out.println("Port : " + serverPort);
  
         return "Hello form Backend!!! " + " Host : localhost " + " :: Port : " + serverPort;
    }

    @GetMapping("/students/{student_id}")
    public Student getStudentById(@PathVariable("student_id") Integer studentId)
    {
        return new Student(1, "Chathuranga", "Bsc", "Sri Lanka");
    }

    @HystrixCommand(fallbackMethod = "callMovieDetailsFallBack")    
    @RequestMapping("/{id}")
	public Gallery getGallery(@PathVariable final int id) {
		// create gallery object
		Gallery gallery = new Gallery();
		gallery.setId(id);
		
		// get list of available images 
		List<Image> images = restTemplate.getForObject("http://image-service/images/", List.class);
		gallery.setImages(images);
	System.out.println(images);
		return gallery;
	}
    
    public Gallery callMovieDetailsFallBack(int id) {
    	Gallery gallery = new Gallery();
		gallery.setId(1);
    	List<Image> images = Arrays.asList(
    			new Image(1, "Treehouse of Horror V", "https://www.imdb.com/title/tt0096697/mediaviewer/rm3842005760"),
    			new Image(2, "The Town", "https://www.imdb.com/title/tt0096697/mediaviewer/rm3698134272"),
    			new Image(3, "The Last Traction Hero", "https://www.imdb.com/title/tt0096697/mediaviewer/rm1445594112"));
    	
    	gallery.setImages(images);
    	return gallery;
     }
	

    @GetMapping("/courses/{course_id}/students")
    public List<Student> getStudentsByCourses(@PathVariable("course_id") Integer courseId)
    {
        List<Student> studentList = new ArrayList<>();

        studentList.add(new Student(1, "Chathuranga", "Bsc", "Sri Lanka"));
        studentList.add(new Student(2, "Darshana", "Sun Certified", "Sri Lanka"));
        return studentList;
    }


    @GetMapping("/departments/{department_id}/courses/{course_id}/students")
    public List<Student> getStudentsByDepartmentCourses(
            @PathVariable("department_id") Integer departmentId,
            @PathVariable("course_id") Integer courseId) {
        List<Student> studentList = new ArrayList<>();

        studentList.add(new Student(1, "Chathuranga", "Bsc", "Sri Lanka"));
        studentList.add(new Student(2, "Darshana", "Sun Certified", "Sri Lanka"));
        studentList.add(new Student(3, "Tennakoon", "Zend Certified", "Sri Lanka"));
        return studentList;
    }
}