package com.ead.course.dtos;

import com.fasterxml.jackson.annotation.JsonView;

import lombok.Data;

@Data
@JsonView
public class CourseDto {

	public interface CourseViewer {
		public static interface CourseRegistratio {
		}
	}
}
