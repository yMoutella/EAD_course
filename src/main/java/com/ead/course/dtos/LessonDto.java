package com.ead.course.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

import com.ead.course.dtos.LessonDto.LessonView.LessonRegistration;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @Size(min = 5, max = 50, message = "title size exceed limit >5 <50", groups = {
            LessonView.LessonUpdate.class })
    @NotBlank(message = "title cannot be null")
    private String title;

    @JsonView(LessonView.LessonRegistration.class)
    @Size(min = 20, max = 200, message = "description size exceed limit >20 <200", groups = {
            LessonView.LessonUpdate.class })
    @NotBlank(message = "description cannot be null")
    private String description;

    @JsonView(LessonView.LessonRegistration.class)
    @NotNull(message = "videoUrl cannot be null")
    private String videoUrl;

    @JsonView(LessonView.LessonRegistration.class)
    @NotNull(message = "lessonId cannot be null")
    private UUID lessonId;

    @JsonView(LessonView.LessonRegistration.class)
    @NotNull(message = "moduleId cannot be null")
    private UUID moduleId;

    private LocalDateTime creationDate;
}
