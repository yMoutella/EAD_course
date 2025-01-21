package com.ead.course.services.impl;

import com.ead.course.services.CourseService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ead.course.models.CourseModel;
import com.ead.course.repository.CourseRepository;

@Service
public class CourseServiceImpl implements CourseService {

  @Autowired
  CourseRepository courseRepository;

  public List<CourseModel> findAll() {
    return courseRepository.findAll();
  }

}
