package com.NextStepEdu.mappers;

import com.NextStepEdu.dto.requests.UniversityRequest;
import com.NextStepEdu.dto.responses.UniversityResponse;
import com.NextStepEdu.models.UniversityModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UniversityMapper {

    UniversityResponse toResponse(UniversityModel model);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "faculties", ignore = true)
    @Mapping(target = "programs", ignore = true)
    @Mapping(target = "scholarships", ignore = true)
    @Mapping(target = "logoUrl", ignore = true)
    @Mapping(target = "coverImageUrl", ignore = true)
    UniversityModel toModel(UniversityRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "faculties", ignore = true)
    @Mapping(target = "programs", ignore = true)
    @Mapping(target = "scholarships", ignore = true)
    @Mapping(target = "logoUrl", ignore = true)
    @Mapping(target = "coverImageUrl", ignore = true)
    void updateModel(UniversityRequest request, @MappingTarget UniversityModel model);
}
