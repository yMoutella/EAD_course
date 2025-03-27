// package com.ead.course.controllers;

// import java.lang.classfile.ClassFile.Option;
// import java.time.LocalDateTime;
// import java.time.ZoneId;
// import java.util.List;
// import java.util.Optional;
// import java.util.UUID;

// import org.springframework.beans.BeanUtils;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.validation.annotation.Validated;
// import org.springframework.web.bind.annotation.DeleteMapping;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.ead.course.dtos.LessonDto;
// import com.ead.course.models.LessonModel;
// import com.ead.course.services.LessonService;
// import com.fasterxml.jackson.annotation.JsonView;

// @RestController
// @RequestMapping(path = "/lessons")
// public class LessonController {

// @Autowired
// LessonService lessonService;

// @PostMapping
// public ResponseEntity<Object> registerLesson(
// @Validated(LessonDto.LessonView.LessonRegistration.class)
// @JsonView(LessonDto.LessonView.LessonRegistration.class) @RequestBody
// LessonDto lessonDto) {

// if (lessonService.existsById(lessonDto.getModuleId())) {
// return ResponseEntity.status(HttpStatus.CONFLICT).body("This lesson already
// exists!");
// }

// var lesson = new LessonModel();
// BeanUtils.copyProperties(lessonDto, lesson);

// lesson.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));

// return ResponseEntity.status(HttpStatus.CREATED).body(lesson);
// }

// @GetMapping
// public ResponseEntity<List<LessonModel>> listLessons() {

// List<LessonModel> lessons = lessonService.findAll();
// return ResponseEntity.status(HttpStatus.OK).body(lessons);

// }

// @GetMapping(path = "/{lessonId}")
// public ResponseEntity<LessonModel> getLesson(@PathVariable UUID lessonId) {

// Optional<LessonModel> lesson = lessonService.findById(lessonId);

// if (!lesson.isEmpty()) {
// return ResponseEntity.status(HttpStatus.OK).body(lesson.get());
// }

// return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
// }

// @DeleteMapping(path = "/{lessonId}")
// public ResponseEntity<String> deleteLesson(@PathVariable UUID lessonId) {

// Optional<LessonModel> lesson = lessonService.findById(lessonId);

// if (lesson.isEmpty()) {
// return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Lesson not found!");
// }

// lessonService.deleteById(lesson.get());

// return ResponseEntity.status(HttpStatus.OK).body("Lesson: " + lessonId + "
// deleted successfully!");
// }

// }