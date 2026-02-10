package com.NextStepEdu.services.impl;

import com.NextStepEdu.dto.requests.ScholarshipRequest;
import com.NextStepEdu.models.ProgramModel;
import com.NextStepEdu.models.ScholarshipModel;
import com.NextStepEdu.models.UniversityModel;
import com.NextStepEdu.repositories.ProgramRepository;
import com.NextStepEdu.repositories.ScholarshipRepository;
import com.NextStepEdu.repositories.UniversityRepository;
import com.NextStepEdu.services.ScholarshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScholarshipServiceImpl implements ScholarshipService {

    private final ScholarshipRepository scholarshipRepository;
    private final ProgramRepository programRepository;
    private final UniversityRepository universityRepository;

    @Override
    public List<ScholarshipModel> findAll() {
        return scholarshipRepository.findAll();
    }

    @Override
    public ScholarshipModel findById(Integer id) {
        return scholarshipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Scholarship not found: " + id));
    }

    @Override
    public ScholarshipModel create(ScholarshipRequest scholarshipRequest) {
        ProgramModel program = programRepository.findById(scholarshipRequest.programId())
                .orElseThrow(() -> new RuntimeException("Program not found: " + scholarshipRequest.programId()));

        UniversityModel university = universityRepository.findById(scholarshipRequest.universityId())
                .orElseThrow(() -> new RuntimeException("University not found: " + scholarshipRequest.universityId()));

        ScholarshipModel scholarship = new ScholarshipModel();
        scholarship.setName(scholarshipRequest.name());
        scholarship.setSlug(scholarshipRequest.slug());
        scholarship.setLogoUrl(scholarshipRequest.logoUrl());
        scholarship.setCoverImageUrl(scholarshipRequest.coverImageUrl());
        scholarship.setDescription(scholarshipRequest.description());
        scholarship.setLevel(scholarshipRequest.level());
        scholarship.setBenefits(scholarshipRequest.benefits());
        scholarship.setRequirements(scholarshipRequest.requirements());
        scholarship.setHowToApply(scholarshipRequest.howToApply());
        scholarship.setApplyLink(scholarshipRequest.applyLink());
        scholarship.setStatus(scholarshipRequest.status());
        scholarship.setDeadline(scholarshipRequest.deadline());
        scholarship.setProgram(program);
        scholarship.setUniversity(university);
        scholarship.setCreatedAt(LocalDateTime.now());
        scholarship.setUpdatedAt(LocalDateTime.now());

        return scholarshipRepository.save(scholarship);
    }
}
