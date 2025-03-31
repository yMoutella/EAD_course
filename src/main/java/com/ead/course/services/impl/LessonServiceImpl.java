package com.ead.course.services.impl;

import com.ead.course.models.LessonModel;
import com.ead.course.repository.LessonRepository;
import com.ead.course.services.LessonService;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LessonServiceImpl implements LessonService {

  @Autowired
  LessonRepository lessonRepository;

  @Override
  public Optional<LessonModel> findById(UUID lessonId) {
    return lessonRepository.findById(lessonId);
  }

  @Override
  public List<LessonModel> findAll() {
    try {
      List<LessonModel> lessons = lessonRepository.findAll();
      return lessons;
    } catch (Exception e) {
      System.out.printf("Error in retrieve lesson list -----> %s", e);
      return null;
    }
  }

  @Transactional
  @Override
  public void deleteById(LessonModel lesson) {

    try {
      lessonRepository.delete(lesson);
    } catch (Exception e) {
      System.out.printf("Error in deletion -----> %s", e);
    }
  }

  @Override
  public Boolean existsById(UUID lessonId) {
    return lessonRepository.existsById(lessonId);
  }

  @Override
  public Boolean existsByTitle(String title, UUID courseId) {
    Optional<LessonModel> lesson = lessonRepository.findLessonWithTitleInCourse(courseId, title);

    if (lesson.isEmpty()) {
      return false;
    }

    return true;
  }

  @Override
  public LessonModel save(LessonModel lesson) {
    try {
      return lessonRepository.save(lesson);
    } catch (Exception e) {
      System.out.printf("Error on save lesson %s -----> %s", lesson.getLessonId(), e);
      return lesson;
    }
  }

  @Override
  public Optional<LessonModel> getLessonInModule(UUID lessonId, UUID moduleId) {
    try {
      return lessonRepository.findLessonIntoModule(lessonId, moduleId);
    } catch (Exception e) {
      System.out.printf("Error in retrieve lesson %s into module %s -----> %s", lessonId, moduleId, e);
      return null;
    }
  }

}
