package com.NextStepEdu.mappers;

import com.NextStepEdu.dto.requests.UniversityRequest;
import com.NextStepEdu.dto.responses.UniversityResponse;
import com.NextStepEdu.models.UniversityModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = { UniversityContactMapper.class }
)
public interface UniversityMapper {

    // ✅ Map entity fields -> response fields (different names)
    @Mapping(source = "logoUrl", target = "logo")
    @Mapping(source = "coverImageUrl", target = "coverImage")
    UniversityResponse toResponse(UniversityModel model);

    // ✅ Map request -> entity (ignore server-managed / relations / images)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)

    @Mapping(target = "faculties", ignore = true)
    @Mapping(target = "programs", ignore = true)
    @Mapping(target = "contacts", ignore = true)
    @Mapping(target = "scholarships", ignore = true)

    @Mapping(target = "logoUrl", ignore = true)
    @Mapping(target = "coverImageUrl", ignore = true)
    UniversityModel toModel(UniversityRequest request);

    // ✅ Update existing entity from request (same ignores)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)

    @Mapping(target = "faculties", ignore = true)
    @Mapping(target = "programs", ignore = true)
    @Mapping(target = "contacts", ignore = true)
    @Mapping(target = "scholarships", ignore = true)

    @Mapping(target = "logoUrl", ignore = true)
    @Mapping(target = "coverImageUrl", ignore = true)
    void updateModel(UniversityRequest request, @MappingTarget UniversityModel model);
}
