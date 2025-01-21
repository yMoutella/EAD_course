package com.ead.course.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

import com.ead.course.enums.CourseStatus;
import com.ead.course.enums.CourseLevel;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseDto {

	public interface CourseView {
		public static interface CourseRegistration {
		}
	}

	@JsonView(CourseView.CourseRegistration.class)
	private UUID courseId;

	@JsonView(CourseView.CourseRegistration.class)
	private String name;

	@JsonView(CourseView.CourseRegistration.class)
	private String description;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
	@JsonView(CourseView.CourseRegistration.class)
	private LocalDateTime creationDate;

	@JsonView(CourseView.CourseRegistration.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
	private LocalDateTime lastUpdateDate;

	@JsonView(CourseView.CourseRegistration.class)
	private CourseStatus courseStatus;

	@JsonView(CourseView.CourseRegistration.class)
	private CourseLevel courseLevel;

	@JsonView(CourseView.CourseRegistration.class)
	private UUID userInsctructor;

}
