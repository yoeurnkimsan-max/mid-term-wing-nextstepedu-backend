package com.NextStepEdu.dto.responses;

import lombok.Builder;

import java.util.List;

@Builder
public record FacultyResponse(
        Integer id,
        String name,
        String description,
        List<UniversityResponse> data
) {
}
