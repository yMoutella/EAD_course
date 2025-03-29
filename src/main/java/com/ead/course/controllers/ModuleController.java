package com.ead.course.controllers;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ead.course.dtos.ModuleDto;
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

    @PostMapping
    public ResponseEntity<Object> registerModule(
            @Validated(ModuleDto.ModuleView.ModuleRegistration.class) @RequestBody @JsonView(ModuleDto.ModuleView.ModuleRegistration.class) ModuleDto moduleDto) {

        if (!courseService.existsByCourseId(moduleDto.getCourseId())) {
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
        System.out.println(module);
        moduleService.save(module);

        return ResponseEntity.status(HttpStatus.CREATED).body(module);

    }

    @GetMapping
    public ResponseEntity<Object> registerModule() {

        List<ModuleModel> modules = moduleService.getModules();
        return ResponseEntity.status(HttpStatus.CREATED).body(modules);

    }

}