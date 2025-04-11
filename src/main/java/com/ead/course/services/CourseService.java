package com.ead.course.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.ead.course.models.CourseModel;

public interface CourseService {

	Page<CourseModel> findAll(Specification<CourseModel> spec, Pageable pageable);

	Optional<CourseModel> findByCourseId(UUID courseId);

	Boolean existsByCourseId(UUID courseId);

	CourseModel save(CourseModel course);

	void delete(CourseModel course);
}
