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
import org.springframework.web.bind.annotation.CrossOrigin;
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
@RequestMapping
@CrossOrigin(origins = "*", maxAge = 3600)
public class ModuleController {

    @Autowired
    ModuleService moduleService;

    @Autowired
    CourseService courseService;

    @PostMapping(path = "/courses/{courseId}/modules") // CREATION
    public ResponseEntity<Object> registerModule(
            @Validated(ModuleDto.ModuleView.ModuleRegistration.class) @RequestBody @JsonView(ModuleDto.ModuleView.ModuleRegistration.class) ModuleDto moduleDto,
            @PathVariable(value = "courseId") UUID courseId) {

        Optional<CourseModel> course = courseService.findByCourseId(courseId);

        if (course.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("The given courseId doesn't exists!");

        }

        if (moduleService.existsModuleGivenCourseId(courseId, moduleDto.getTitle())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("The given module title exists in this course already!");
        }

        var module = new ModuleModel();
        BeanUtils.copyProperties(moduleDto, module);

        module.setCreationDate(LocalDateTime.now(ZoneId.of("UTC")));
        module.setUpdateDateTime(LocalDateTime.now(ZoneId.of("UTC")));
        module.setLesson(Collections.emptySet());
        module.setCourse(course.get());

        try {
            moduleService.save(module);
        } catch (Exception e) {
            System.out.printf("Error trying to save module: %s \n\n ----> %s", module, e);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(module);

    }

    @GetMapping(path = "/courses/{courseId}/modules") // LIST
    public ResponseEntity<Object> listModules(@PathVariable UUID courseId) {

        List<ModuleModel> modules = moduleService.getModules(courseId);

        if (modules.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Modules not found for this course!");
        }

        return ResponseEntity.status(HttpStatus.OK).body(modules);

    }

    @GetMapping(path = "/courses/{courseId}/modules/{moduleId}") // GET MODULE
    public ResponseEntity<Object> getModule(@PathVariable UUID courseId, @PathVariable UUID moduleId) {

        Optional<ModuleModel> module = moduleService.findModuleIntoCourse(courseId, moduleId);

        if (module.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found!");
        }

        return ResponseEntity.status(HttpStatus.OK).body(module.get());

    }

    @DeleteMapping(path = "/courses/{courseId}/modules/{moduleId}") // DELETE MODULE
    public ResponseEntity<Object> deleteModule(@PathVariable UUID moduleId, @PathVariable UUID courseId) {

        Optional<ModuleModel> module = moduleService.findModuleIntoCourse(courseId, moduleId);
        if (module.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found for this course!");
        }

        try {
            moduleService.delete(module.get());
            return ResponseEntity.status(HttpStatus.OK).body("Module deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("An internal error ocurred");
        }

    }

    @PutMapping(path = "/courses/{courseId}/modules/{moduleId}") // Update Module
    public ResponseEntity<Object> updateModule(
            @Validated(ModuleDto.ModuleView.ModuleUpdate.class) @JsonView(ModuleDto.ModuleView.ModuleUpdate.class) @RequestBody ModuleDto moduleDto,
            @PathVariable UUID courseId, @PathVariable UUID moduleId) {

        Optional<ModuleModel> module = moduleService.findModuleIntoCourse(courseId, moduleId);

        if (module.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module not found for given course");
        }

        var moduleModel = module.get();
        BeanUtils.copyProperties(moduleDto, moduleModel);
        moduleModel.setUpdateDateTime(LocalDateTime.now(ZoneId.of("UTC")));
        moduleService.save(moduleModel);

        return ResponseEntity.status(HttpStatus.OK).body("Module updated successfully!");

    }

}