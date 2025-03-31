package com.ead.course.dtos;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ModuleDto {

    public static interface ModuleView {
        public interface ModuleRegistration {
        }

        public interface ModuleUpdate {
        }
    }

    @NotNull(message = "The title doesn´t satisfy the size > 5 < 50", groups = { ModuleView.ModuleUpdate.class })
    @JsonView({ ModuleView.ModuleUpdate.class })
    private UUID moduleId;

    @NotNull
    @Size(min = 5, max = 30, message = "The title doesn´t satisfy the size > 5 < 50", groups = {
            ModuleView.ModuleRegistration.class, ModuleView.ModuleUpdate.class })
    @JsonView({ ModuleView.ModuleRegistration.class, ModuleView.ModuleUpdate.class })
    private String title;

    @NotNull
    @Size(min = 50, max = 255, message = "The description doesn´t satisfy the size > 50 < 255", groups = {
            ModuleView.ModuleRegistration.class, ModuleView.ModuleUpdate.class })
    @JsonView({ ModuleView.ModuleRegistration.class, ModuleView.ModuleUpdate.class })
    private String description;

    // @NotNull(message = "courseId must not be null", groups = {
    // ModuleView.ModuleRegistration.class,
    // ModuleView.ModuleUpdate.class })
    // @JsonView({ ModuleView.ModuleRegistration.class,
    // ModuleView.ModuleUpdate.class })
    // private UUID courseId;

}
