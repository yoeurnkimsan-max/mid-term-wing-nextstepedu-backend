package com.NextStepEdu.services;

import com.NextStepEdu.dto.requests.ApplicantRequest;
import com.NextStepEdu.dto.responses.ApplicantResponse;

import java.util.List;

public interface ApplicantService {
    ApplicantResponse create(ApplicantRequest request);
    ApplicantResponse getById(Long id);
    List<ApplicantResponse> getAll();
    List<ApplicantResponse> getByUserId(Integer userId);
    List<ApplicantResponse> getByStatus(String status);
    ApplicantResponse updateStatus(Long id, String status);
    void delete(Long id);
}
