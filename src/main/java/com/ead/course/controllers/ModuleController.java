package com.ead.course.controllers;

import java.lang.classfile.ClassFile.Option;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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

import com.ead.course.dtos.ModuleDto;
import com.ead.course.models.CourseModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.ModuleService;
import com.fasterxml.jackson.annotation.JsonView;

@RestController
@RequestMapping(path = "/modules")
public class ModuleController {

    @Autowired
    ModuleService moduleService;

    @Autowired
    CourseService courseService;

    @PostMapping // CREATION
    public ResponseEntity<Object> registerModule(
            @Validated(ModuleDto.ModuleView.ModuleRegistration.class) @RequestBody @JsonView(ModuleDto.ModuleView.ModuleRegistration.class) ModuleDto moduleDto) {

        Optional<CourseModel> course = courseService.findByCourseId(moduleDto.getCourseId());

        if (course.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("The given courseId doesn't exists!");

        }

        if (moduleService.existsModuleGivenCourseId(moduleDto.getCourseId(), moduleDto.getTitle())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("The given module title exists in this course already!");
        }

        var module = new ModuleModel();

        BeanUtils.copyProperties(moduleDto, module);

        module.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        module.setLesson(Collections.emptySet());
        module.setCourse(course.get());

        moduleService.save(module);

        return ResponseEntity.status(HttpStatus.CREATED).body(module);

    }

    @GetMapping // LIST
    public ResponseEntity<Object> listModules() {

        List<ModuleModel> modules = moduleService.getModules();

        return ResponseEntity.status(HttpStatus.OK).body(modules);

    }

    @GetMapping(path = "/{moduleId}") // GET MODULE
    public ResponseEntity<Object> getModule(@PathVariable UUID moduleId) {

        Optional<ModuleModel> module = moduleService.getModule(moduleId);

        if (module.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found!");
        }

        return ResponseEntity.status(HttpStatus.OK).body(module.get());

    }

    @DeleteMapping(path = "/{moduleId}") // DELETE MODULE
    public ResponseEntity<Object> deleteModule(@PathVariable UUID moduleId) {

        Optional<ModuleModel> module = moduleService.getModule(moduleId);
        if (module.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found!!");
        }

        try {
            moduleService.delete(module.get());
            return ResponseEntity.status(HttpStatus.OK).body("Module deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("An internal error ocurred");
        }

    }

    @PutMapping // Update Module
    public ResponseEntity<Object> updateModule(
            @Validated(ModuleDto.ModuleView.ModuleUpdate.class) @JsonView(ModuleDto.ModuleView.ModuleUpdate.class) @RequestBody ModuleDto moduleDto) {

        Optional<CourseModel> course = courseService.findByCourseId(moduleDto.getCourseId());

        if (course.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("The given courseId doesn't exists!");
        }

        if (!moduleService.existsModuleGivenCourseId(moduleDto.getCourseId(), moduleDto.getTitle())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("The given module title not exists in this course!");
        }
        return ResponseEntity.status(200).body(null);

    }

}