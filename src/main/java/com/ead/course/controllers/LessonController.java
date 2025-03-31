package com.ead.course.controllers;

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
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.LessonService;
import com.ead.course.services.ModuleService;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping(path = "/lessons")
public class LessonController {

    @Autowired
    LessonService lessonService;

    @Autowired
    ModuleService moduleService;

    @PostMapping // CREATE
    public ResponseEntity<Object> registerLesson(
            @Validated(LessonDto.LessonView.LessonRegistration.class) @JsonView(LessonDto.LessonView.LessonRegistration.class) @RequestBody LessonDto lessonDto) {

        if (!moduleService.existsById(lessonDto.getModuleId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found!");
        }

        if (lessonService.existsByTitle(lessonDto.getTitle(), lessonDto.getModuleId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Already exists a lesson with this title in this module");
        }

        Optional<ModuleModel> module = moduleService.getModule(lessonDto.getModuleId());

        var lesson = new LessonModel();
        BeanUtils.copyProperties(lessonDto, lesson);

        lesson.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        lesson.setModule(module.get());

        return ResponseEntity.status(HttpStatus.CREATED).body(lessonService.save(lesson));
    }

    @GetMapping // LIST
    public ResponseEntity<Object> listLessons() {
        List<LessonModel> lessons = lessonService.findAll();
        List<LessonDto> lessonsDto = new ArrayList<>();

        for (LessonModel lesson : lessons) {
            LessonDto l = new LessonDto();
            BeanUtils.copyProperties(lesson, l);
            lessonsDto.add(l);
        }

        return ResponseEntity.status(HttpStatus.OK).body(lessonsDto);

    }

    @GetMapping(path = "/{lessonId}") // GET LESSON
    public ResponseEntity<LessonModel> getLesson(@PathVariable UUID lessonId) {

        Optional<LessonModel> lesson = lessonService.findById(lessonId);

        if (!lesson.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(lesson.get());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
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

    @PutMapping(path = "/{lessonId}") // UPDATE LESSON
    public ResponseEntity<Object> updateLesson(
            @Validated(LessonView.LessonUpdate.class) @JsonView(LessonView.LessonUpdate.class) @RequestBody LessonDto lessonDto) {

        Optional<LessonModel> lesson = lessonService.findById(lessonDto.getLessonId());
        Optional<ModuleModel> module = moduleService.getModule(lessonDto.getModuleId());

        if (module.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found!");
        }
        if (lesson.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found!");

        }

        var mLesson = lesson.get();
        BeanUtils.copyProperties(lessonDto, mLesson);

        return ResponseEntity.status(200).body(null);

    }

}