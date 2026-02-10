package com.NextStepEdu.services;

import com.NextStepEdu.dto.requests.FacultyRequest;
import com.NextStepEdu.dto.responses.FacultyResponse;

import java.util.List;

public interface FacultyService {
    FacultyResponse create(FacultyRequest request);
    FacultyResponse getById(Integer id);
    List<FacultyResponse> getAll(Integer universityId);
    FacultyResponse update(Integer id, FacultyRequest request);
    void delete(Integer id);
}
