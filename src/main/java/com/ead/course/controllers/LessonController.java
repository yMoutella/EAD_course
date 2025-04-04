package com.ead.course.controllers;

import java.lang.classfile.ClassFile.Option;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.ead.course.dtos.LessonDto;
import com.ead.course.dtos.LessonDto.LessonView;
import com.ead.course.models.CourseModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.LessonService;
import com.ead.course.services.ModuleService;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class LessonController {

    @Autowired
    LessonService lessonService;

    @Autowired
    ModuleService moduleService;

    @Autowired
    CourseService courseService;

    @PostMapping(path = "/courses/{courseId}/modules/{moduleId}/lessons") // CREATE
    public ResponseEntity<Object> registerLesson(
            @Validated(LessonDto.LessonView.LessonRegistration.class) @JsonView(LessonDto.LessonView.LessonRegistration.class) @RequestBody LessonDto lessonDto,
            @PathVariable UUID courseId, @PathVariable UUID moduleId) {

        Optional<CourseModel> course = courseService.findByCourseId(courseId);

        if (course.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found!");
        }

        Optional<ModuleModel> module = moduleService.findModuleIntoCourse(courseId, moduleId);

        if (module.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found!");
        }

        Boolean lessonExists = lessonService.existsByTitle(lessonDto.getTitle(), courseId);

        if (lessonExists) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Already exists a lesson with this title in this module");
        }

        var lesson = new LessonModel();
        BeanUtils.copyProperties(lessonDto, lesson);

        lesson.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        lesson.setModule(module.get());

        return ResponseEntity.status(HttpStatus.CREATED).body(lessonService.save(lesson));
    }

    @GetMapping(path = "/courses/{courseId}/modules/{moduleId}/lessons") // LIST
    public ResponseEntity<Object> listLessons(@PathVariable UUID courseId, @PathVariable UUID moduleId) {
        Optional<CourseModel> course = courseService.findByCourseId(courseId);

        if (course.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found!");
        }

        Optional<ModuleModel> module = moduleService.findModuleIntoCourse(courseId, moduleId);

        if (module.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found!");
        }

        List<LessonModel> lessons = lessonService.findAll(courseId, moduleId);

        return ResponseEntity.status(HttpStatus.OK).body(lessons);
    }

    @GetMapping(path = "courses/{courseId}/modules/{moduleId}/lessons/{lessonId}") // GET LESSON
    public ResponseEntity<Object> getLesson(@PathVariable UUID courseId, @PathVariable UUID moduleId,
            @PathVariable UUID lessonId) {

        Optional<CourseModel> course = courseService.findByCourseId(courseId);

        if (course.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found!");
        }

        Optional<ModuleModel> module = moduleService.findModuleIntoCourse(courseId, moduleId);

        if (module.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found!");
        }

        Optional<LessonModel> lesson = lessonService.findById(lessonId);

        if (lesson.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("lessonId not found!");
        }

        return ResponseEntity.status(HttpStatus.OK).body(lesson);

    }

    @DeleteMapping(path = "/{lessonId}") // Delete Lesson
    public ResponseEntity<String> deleteLesson(@PathVariable UUID lessonId) {

        Optional<LessonModel> lesson = lessonService.findById(lessonId);

        if (lesson.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found!");
        }

        lessonService.deleteById(lesson.get());

        return ResponseEntity.status(HttpStatus.OK).body("Lesson: " + lessonId + "deleted successfully!");
    }

    // @PutMapping(path = "/{lessonId}") // UPDATE LESSON
    // public ResponseEntity<Object> updateLesson(
    // @Validated(LessonView.LessonUpdate.class)
    // @JsonView(LessonView.LessonUpdate.class) @RequestBody LessonDto lessonDto) {

    // Optional<LessonModel> lesson =
    // lessonService.findById(lessonDto.getLessonId());
    // Optional<ModuleModel> module =
    // moduleService.getModule(lessonDto.getModuleId());

    // if (module.isEmpty()) {
    // return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found!");
    // }
    // if (lesson.isEmpty()) {
    // return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found!");

    // }

    // var mLesson = lesson.get();
    // BeanUtils.copyProperties(lessonDto, mLesson);

    // return ResponseEntity.status(200).body(null);

    // }

}