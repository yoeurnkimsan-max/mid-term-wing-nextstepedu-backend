package com.NextStepEdu.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ScholarshipRequest(

        @NotBlank(message = "Scholarship name is required")
        String name,

        String slug,

        String logoUrl,

        String coverImageUrl,

        String description,

        Integer level,

        String benefits,

        String requirements,

        String howToApply,

        String applyLink,

        String status,

        LocalDateTime deadline,

        @NotNull(message = "Program ID is required")
        Integer programId,

        @NotNull(message = "University ID is required")
        Integer universityId

) {
}
