package com.NextStepEdu.dto.responses;

public record FacultyResponse(
        Integer id,
        String name,
        String description,
        Integer universityId,
        String universityName
) {
}
