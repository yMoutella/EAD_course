package com.ead.course.controllers;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ead.course.dtos.CourseDto;
import com.ead.course.models.CourseModel;
import com.ead.course.services.CourseService;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping(path = "/courses")
public class CourseController {

  @Autowired
  CourseService courseService;

  @GetMapping(path = "/list")
  public ResponseEntity<List<CourseModel>> getCourses() {

    List<CourseModel> courseModel = courseService.findAll();
    return ResponseEntity.status(HttpStatus.OK).body(courseModel);

  }

  @GetMapping(path = "/{courseId}")
  public ResponseEntity<Object> getCourses(@PathVariable UUID courseId) {

    Optional<CourseModel> courseModel = courseService.findByCourseId(courseId);
    if (!courseModel.isEmpty()) {
      return ResponseEntity.status(HttpStatus.OK).body(courseModel);
    }

    return ResponseEntity.status(HttpStatus.OK).body("Course not found!");
  }

  @PostMapping(path = "/register")
  public ResponseEntity<String> registerCourse(
      @RequestBody @JsonView(CourseDto.CourseView.CourseRegistration.class) CourseDto dto) {

      var courseModel = new CourseModel();
      BeanUtils.copyProperties(dto, courseModel);

      courseModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
      courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

      courseService.save(courseModel);

      return ResponseEntity.status(HttpStatus.CREATED).body("Course created with successfuly!");
  }
}
