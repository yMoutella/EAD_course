package com.ead.course.dtos;

import com.ead.course.enums.CourseLevel;
import com.ead.course.enums.CourseStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseDto {

  public interface CourseView {
    public static interface CourseRegistration {
    }

    public static interface CourseUpdate {
    }
  }

  private UUID courseId;

  @NotBlank
  @JsonView({ CourseView.CourseRegistration.class, CourseView.CourseUpdate.class })
  private String name;

  @NotBlank
  @JsonView({ CourseView.CourseRegistration.class, CourseView.CourseUpdate.class })
  private String description;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
  private LocalDateTime creationDate;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
  private LocalDateTime lastUpdateDate;

  @NotNull
  @JsonView({ CourseView.CourseRegistration.class, CourseView.CourseUpdate.class })
  private CourseStatus courseStatus;

  @NotNull
  @JsonView({ CourseView.CourseRegistration.class, CourseView.CourseUpdate.class })
  private CourseLevel courseLevel;

  @NotNull
  @JsonView(CourseView.CourseRegistration.class)
  private UUID userInstructor;
}
