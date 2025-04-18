package com.ead.course.controllers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ead.course.dtos.CourseDto;
import com.ead.course.models.CourseModel;
import com.ead.course.services.CourseService;
import com.ead.course.specifications.SpecificationTemplate;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping(path = "/courses")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseController {

    @Autowired
    CourseService courseService;

    @PostMapping
    public ResponseEntity<CourseModel> registerCourse(
            @Validated(CourseDto.CourseView.CourseRegistration.class) @RequestBody @JsonView(CourseDto.CourseView.CourseRegistration.class) CourseDto dto) {

        var courseModel = new CourseModel();
        BeanUtils.copyProperties(dto, courseModel);

        courseModel.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        courseModel.setUserInsctructor(UUID.randomUUID());

        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.save(courseModel));
    }

    @DeleteMapping(path = "/{courseId}")
    public ResponseEntity<String> deleteCourse(@PathVariable UUID courseId) {

        Optional<CourseModel> course = courseService.findByCourseId(courseId);

        if (course.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This course doesn't exist");
        }
        var courseModel = course.get();

        courseService.delete(courseModel);
        return ResponseEntity.status(HttpStatus.OK).body("Course: " + courseId + " deleted successfully!");

    }

    @GetMapping // Specification template used to filter data in request
    public ResponseEntity<Object> getCourses(SpecificationTemplate.CourseSpec spec,
            @PageableDefault(page = 0, size = 10, sort = "courseId", direction = Sort.Direction.ASC) Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(courseService.findAll(spec, pageable));

    }

    @GetMapping(path = "/{courseId}")
    public ResponseEntity<Object> getCourse(@PathVariable UUID courseId) {

        Optional<CourseModel> courseModel = courseService.findByCourseId(courseId);
        if (!courseModel.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(courseModel);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found!");
    }

    @PutMapping
    public ResponseEntity<CourseModel> updateCourse(
            @RequestBody CourseDto courseDto) {

        Optional<CourseModel> course = courseService.findByCourseId(courseDto.getCourseId());

        if (course.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        var courseModel = course.get();

        BeanUtils.copyProperties(courseDto, courseModel);

        courseModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));

        return ResponseEntity.status(HttpStatus.OK).body(courseService.save(courseModel));
    }

}
