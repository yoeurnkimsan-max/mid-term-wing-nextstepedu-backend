package com.NextStepEdu.dto.requests;

import jakarta.validation.constraints.NotBlank;

public record FacultyRequest(
        @NotBlank(message = "Faculty name is required")
        String name,
        String description,
        Integer universityId
) {
}
