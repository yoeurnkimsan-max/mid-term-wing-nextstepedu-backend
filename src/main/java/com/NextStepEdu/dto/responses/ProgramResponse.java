package com.NextStepEdu.dto.responses;

public record ProgramResponse(
        Integer id,
        String name,
        String description,

        Integer degreeLevel,
        String degreeLevelName,

        Boolean examRequired,
        Double tuitionFeeAmount,
        String currency,
        Integer studyPeriodMonths,

        UniversityBasicDTO university,
        FacultyBasicDTO faculty,

        Integer scholarshipCount
) {
    public record UniversityBasicDTO(Integer id, String name) {}
    public record FacultyBasicDTO(Integer id, String name) {}
}
