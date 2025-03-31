package com.ead.course.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ead.course.models.LessonModel;

public interface LessonRepository extends JpaRepository<LessonModel, UUID> {

    @Query(value = "select * from tb_lesson where module_module_id = :moduleId", nativeQuery = true)
    List<LessonModel> findAllLessonIntoModule(@Param("moduleId") UUID moduleId);

    @Query(value = "select * from tb_lesson where module_module_id = :moduleId and lesson_id = :lessonId", nativeQuery = true)
    Optional<LessonModel> findLessonIntoModule(@Param("lessonId") UUID lessonId, @Param("moduleId") UUID moduleId);

    @Query(value = "select * from tb_lesson where module_module_id = :moduleId and title = :title", nativeQuery = true)
    Optional<LessonModel> findLessonWithTitleInCourse(@Param("moduleId") UUID moduleId, @Param("title") String title);
}
