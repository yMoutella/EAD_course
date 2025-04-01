package com.ead.course.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.ead.course.models.ModuleModel;

public interface ModuleService {

    Boolean existsById(UUID moduleId);

    Boolean existsModuleGivenCourseId(UUID courseId, String title);

    List<ModuleModel> getModules(UUID courseId);

    Optional<ModuleModel> findModuleIntoCourse(UUID courseId, UUID moduleId);

    void delete(ModuleModel module);

    ModuleModel save(ModuleModel module);
}
