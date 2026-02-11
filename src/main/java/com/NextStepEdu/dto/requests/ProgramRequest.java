package com.NextStepEdu.dto.requests;

import jakarta.validation.constraints.*;

public record ProgramRequest(
        @NotBlank(message = "Program name is required")
        @Size(min = 2, max = 255, message = "Program name must be between 2 and 255 characters")
        String name,

        @Size(max = 1000, message = "Description must not exceed 1000 characters")
        String description,

        @Min(value = 1, message = "Degree level must be at least 1")
        @Max(value = 5, message = "Degree level must not exceed 5")
        Integer degreeLevel,

        Boolean examRequired,

        @Positive(message = "Tuition fee must be positive")
        Double tuitionFeeAmount,

        @Size(min = 3, max = 3, message = "Currency must be 3 characters (e.g., USD, EUR)")
        String currency,

        @Positive(message = "Study period must be positive")
        @Max(value = 120, message = "Study period must not exceed 120 months")
        Integer studyPeriodMonths,

        @NotNull(message = "University ID is required")
        Integer universityId,

        Integer facultyId
) {
}
