package com.ead.course.services.impl;

import com.ead.course.services.CourseService;
import com.ead.course.specifications.SpecificationTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;

import com.ead.course.models.CourseModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repository.CourseRepository;
import com.ead.course.repository.LessonRepository;
import com.ead.course.repository.ModuleRepository;

@Service
public class CourseServiceImpl implements CourseService {

  @Autowired
  CourseRepository courseRepository;

  @Autowired
  ModuleRepository moduleRepository;

  @Autowired
  LessonRepository lessonRepository;

  @Override
  public Page<CourseModel> findAll(Specification<CourseModel> spec,
      Pageable pageable) {
    return courseRepository.findAll(spec, pageable);
  }

  @Override
  public Optional<CourseModel> findByCourseId(UUID courseId) {
    return courseRepository.findById(courseId);
  }

  @Override
  public CourseModel save(CourseModel course) {
    return courseRepository.save(course);
  }

  @Transactional
  @Override
  public void delete(CourseModel course) {

    List<ModuleModel> moduleList = moduleRepository.findAllModulesIntoCourse(course.getCourseId());

    try {
      if (!moduleList.isEmpty()) {
        for (ModuleModel module : moduleList) {
          List<LessonModel> lessonList = lessonRepository.findAllLessonIntoModule(module.getModuleId());
          if (!lessonList.isEmpty()) {
            lessonRepository.deleteAll(lessonList);
          }
        }

        moduleRepository.deleteAll(moduleList);

      }
      courseRepository.deleteById(course.getCourseId());
    } catch (Exception e) {
      System.out.printf("Error in course deletion %s", e);
    }

  }

  @Override
  public Boolean existsByCourseId(UUID courseId) {
    return courseRepository.existsById(courseId);
  }
}
