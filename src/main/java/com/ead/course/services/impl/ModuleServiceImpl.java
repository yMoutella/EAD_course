package com.ead.course.services.impl;

import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repository.LessonRepository;
import com.ead.course.repository.ModuleRepository;
import com.ead.course.services.ModuleService;

import jakarta.transaction.Transactional;

import java.util.List;

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

    throw new UnsupportedOperationException("Unimplemented method 'delete'");
  }
}