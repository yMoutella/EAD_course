package com.ead.course.services.impl;

import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repository.LessonRepository;
import com.ead.course.repository.ModuleRepository;
import com.ead.course.services.ModuleService;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModuleServiceImpl implements ModuleService {

  @Autowired
  ModuleRepository moduleRepository;

  @Autowired
  LessonRepository lessonRepository;

  @Transactional
  @Override
  public void delete(ModuleModel module) {

    List<LessonModel> lessonList = lessonRepository.findAllLessonIntoModule(module.getModuleId());

    if (!lessonList.isEmpty()) {
      lessonRepository.deleteAll(lessonList);
    }

    moduleRepository.delete(module);

  }

  @Override
  public Boolean existsById(UUID moduleId) {
    return moduleRepository.existsById(moduleId);
  }

  @Override
  public Boolean existsModuleGivenCourseId(UUID courseId, String title) {

    List<ModuleModel> modules = moduleRepository.findAllModulesIntoCourse(courseId);

    for (ModuleModel model : modules) {
      if (model.getTitle().equals(title)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public ModuleModel save(ModuleModel module) {
    try {
      moduleRepository.save(module);
      return module;
    } catch (Exception e) {
      System.out.printf("Error trying to save module %s ----> %s", module.getModuleId(), e);
      return null;
    }
  }

  @Override
  public List<ModuleModel> getModules() {
    return moduleRepository.findAll();
  }

  @Override
  public Optional<ModuleModel> getModule(UUID moduleId) {
    return moduleRepository.findById(moduleId);
  }
}