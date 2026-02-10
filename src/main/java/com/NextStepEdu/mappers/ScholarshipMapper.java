package com.NextStepEdu.mappers;

import com.NextStepEdu.dto.requests.ScholarshipRequest;
import com.NextStepEdu.dto.responses.ScholarshipResponse;
import com.NextStepEdu.models.ScholarshipModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import tools.jackson.databind.ObjectMapper;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ScholarshipMapper {
    ScholarshipResponse toResponse(ScholarshipModel scholarshipModel);
    ScholarshipModel toModel(ScholarshipResponse scholarshipResponse);
    ScholarshipRequest toScholarshipRequest(ScholarshipModel scholarshipModel);
}
