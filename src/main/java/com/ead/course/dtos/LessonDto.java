package com.ead.course.dtos;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LessonDto {

    public interface LessonView {
        public interface LessonRegistration {
        }

        public interface LessonUpdate {
        }

    }

    @JsonView(LessonView.LessonRegistration.class)
    @NotBlank
    private String title;

    @JsonView(LessonView.LessonRegistration.class)
    @NotBlank
    private String description;

    @JsonView(LessonView.LessonRegistration.class)
    @NotNull
    private String videoUrl;

    @JsonView(LessonView.LessonRegistration.class)
    @NotNull
    private UUID moduleId;

}
