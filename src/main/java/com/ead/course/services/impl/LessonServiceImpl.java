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
    return lessonRepository.findAll();
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

}
