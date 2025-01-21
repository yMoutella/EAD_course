package com.ead.course.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ead.course.models.CourseModel;
import com.ead.course.services.CourseService;

@RestController
@RequestMapping(path = "/courses")
public class CourseController {

  @Autowired
  CourseService courseService;

  @GetMapping(path = "/list")
  public ResponseEntity<?> getCourses() {

    List<CourseModel> courseModel = courseService.findAll();

    return ResponseEntity.status(HttpStatus.OK).body(courseModel);
  }

  @PutMapping(path = "/register")
  public ResponseEntity<?> registerCourse() {
    return new ResponseEntity.status(HttpStatus.OK).body("Deu bom");
  }
}
