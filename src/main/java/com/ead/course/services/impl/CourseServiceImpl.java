package com.ead.course.services.impl;

import com.ead.course.services.CourseService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ead.course.models.CourseModel;

@Service
public class CourseServiceImpl implements CourseService {

  @Autowired
  CourseService courseService;

  public List<CourseModel> findAll() {
    return courseService.findAll();
  }

}
