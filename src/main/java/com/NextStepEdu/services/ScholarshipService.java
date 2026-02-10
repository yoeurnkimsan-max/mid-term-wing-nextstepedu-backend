package com.NextStepEdu.services;

import com.NextStepEdu.dto.requests.ScholarshipRequest;
import com.NextStepEdu.models.ScholarshipModel;

import java.util.List;

public interface ScholarshipService {

    List<ScholarshipModel> findAll();

    ScholarshipModel findById(Integer id);

    ScholarshipModel create(ScholarshipRequest scholarshipRequest);
}
