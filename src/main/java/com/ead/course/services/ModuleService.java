package com.ead.course.services;

import java.util.UUID;

import com.ead.course.models.ModuleModel;

public interface ModuleService {

    Boolean existsById(UUID moduleId);

    Boolean existsModuleGivenCourseId(UUID courseId, String title);

    void delete(ModuleModel module);

    ModuleModel save(ModuleModel module);
}
