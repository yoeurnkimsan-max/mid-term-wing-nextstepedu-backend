package com.NextStepEdu.mappers;

import com.NextStepEdu.dto.responses.ApplicantResponse;
import com.NextStepEdu.models.ApplicantModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ApplicantMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "scholarship.id", target = "scholarshipId")
    @Mapping(source = "scholarship.university.id", target = "universityId")
    ApplicantResponse toResponse(ApplicantModel model);
}
