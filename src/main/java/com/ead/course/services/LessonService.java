package com.ead.course.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.ead.course.models.LessonModel;

public interface LessonService {

    void deleteById(LessonModel lesson);

    LessonModel save(LessonModel lesson);

    Optional<LessonModel> findById(UUID lessonId);

    Boolean existsByTitle(String title, UUID courseId);

    Boolean existsById(UUID lessonId);

    List<LessonModel> findAll();
}
