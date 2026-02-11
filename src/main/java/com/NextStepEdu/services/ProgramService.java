package com.NextStepEdu.services;

import com.NextStepEdu.dto.requests.ProgramRequest;
import com.NextStepEdu.dto.responses.ProgramResponse;
import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface ProgramService {

    List<ProgramResponse> getAllProgram();

    ProgramResponse createProgram(@Valid ProgramRequest programRequest);

    ProgramResponse getProgramById(Integer id);

    ProgramResponse updateProgram(Integer id, @Valid ProgramRequest programRequest);

    void deleteProgram(Integer id);

    List<ProgramResponse> getProgramsByTuitionFeeRange(Double min, Double max);

    List<ProgramResponse> getProgramsByDegreeLevel(Integer level);

    List<ProgramResponse> searchProgramsByName(String name);

    List<ProgramResponse> getProgramsByFaculty(Integer facultyId);

    List<ProgramResponse> getProgramByUniversity(Integer universityId);
}
