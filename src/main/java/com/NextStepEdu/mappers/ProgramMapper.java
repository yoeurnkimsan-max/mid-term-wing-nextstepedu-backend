package com.NextStepEdu.mappers;

import com.NextStepEdu.dto.requests.ProgramRequest;
import com.NextStepEdu.dto.responses.ProgramResponse;
import com.NextStepEdu.models.FacultyModel;
import com.NextStepEdu.models.ProgramModel;
import com.NextStepEdu.models.UniversityModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProgramMapper {

    @Mapping(target = "degreeLevelName", expression = "java(degreeLevelName(model.getDegreeLevel()))")
    @Mapping(target = "scholarshipCount",
            expression = "java(model.getScholarships() == null ? 0 : model.getScholarships().size())")
    ProgramResponse toResponse(ProgramModel model);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "university", ignore = true)
    @Mapping(target = "faculty", ignore = true)
    @Mapping(target = "scholarships", ignore = true)
    ProgramModel toModel(ProgramRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "university", ignore = true)
    @Mapping(target = "faculty", ignore = true)
    @Mapping(target = "scholarships", ignore = true)
    void updateModel(ProgramRequest request, @MappingTarget ProgramModel model);

    default ProgramResponse.UniversityBasicDTO toUniversityBasicDTO(UniversityModel model) {
        if (model == null) return null;
        return new ProgramResponse.UniversityBasicDTO(model.getId(), model.getName());
    }

    default ProgramResponse.FacultyBasicDTO toFacultyBasicDTO(FacultyModel model) {
        if (model == null) return null;
        return new ProgramResponse.FacultyBasicDTO(model.getId(), model.getName());
    }

    default String degreeLevelName(Integer level) {
        if (level == null) return "Unknown";
        return switch (level) {
            case 1 -> "Associate Degree";
            case 2 -> "Bachelor's Degree";
            case 3 -> "Master's Degree";
            case 4 -> "Doctoral Degree";
            case 5 -> "Postdoctoral";
            default -> "Unknown";
        };
    }
}
