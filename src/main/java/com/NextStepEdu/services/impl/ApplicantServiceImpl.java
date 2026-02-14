package com.NextStepEdu.services.impl;

import com.NextStepEdu.dto.requests.ApplicantRequest;
import com.NextStepEdu.dto.responses.ApplicantResponse;
import com.NextStepEdu.mappers.ApplicantMapper;
import com.NextStepEdu.models.ApplicantModel;
import com.NextStepEdu.models.ScholarshipModel;
import com.NextStepEdu.models.UserModel;
import com.NextStepEdu.repositories.ApplicantRepository;
import com.NextStepEdu.repositories.ScholarshipRepository;
import com.NextStepEdu.repositories.UserRepository;
import com.NextStepEdu.services.ApplicantService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicantServiceImpl implements ApplicantService {

    private final ApplicantRepository applicantRepository;
    private final UserRepository userRepository;
    private final ScholarshipRepository scholarshipRepository;
    private final ApplicantMapper applicantMapper;

    public ApplicantServiceImpl(
            ApplicantRepository applicantRepository,
            UserRepository userRepository,
            ScholarshipRepository scholarshipRepository,
            ApplicantMapper applicantMapper
    ) {
        this.applicantRepository = applicantRepository;
        this.userRepository = userRepository;
        this.scholarshipRepository = scholarshipRepository;
        this.applicantMapper = applicantMapper;
    }

    @Override
    public ApplicantResponse create(ApplicantRequest request) {
        UserModel user = userRepository.findById(request.userId())
                .orElseThrow(() -> new RuntimeException("User not found: " + request.userId()));

        ScholarshipModel scholarship = scholarshipRepository.findById(request.scholarshipId())
                .orElseThrow(() -> new RuntimeException("Scholarship not found: " + request.scholarshipId()));

        ApplicantModel model = new ApplicantModel();
        model.setUser(user);
        model.setScholarship(scholarship);

        model.setFirstName(request.firstName());
        model.setLastName(request.lastName());
        model.setGender(request.gender());
        model.setDateOfBirth(request.dateOfBirth());
        model.setEmail(request.email());
        model.setPhoneNumber(request.phoneNumber());
        model.setAddress(request.address());
        model.setNationality(request.nationality());
        model.setHighSchoolName(request.highSchoolName());
        model.setGpa(request.gpa());
        model.setIntendedMajor(request.intendedMajor());
        model.setScholarshipType(request.scholarshipType());
        model.setFamilyIncome(request.familyIncome());
        model.setMotivationLetter(request.motivationLetter());

        return applicantMapper.toResponse(applicantRepository.save(model));
    }

    @Override
    public ApplicantResponse getById(Long id) {
        return applicantRepository.findById(id)
                .map(applicantMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("Applicant not found: " + id));
    }

    @Override
    public List<ApplicantResponse> getAll() {
        return applicantRepository.findAll().stream()
                .map(applicantMapper::toResponse)
                .toList();
    }

    @Override
    public List<ApplicantResponse> getByUserId(Integer userId) {
        return applicantRepository.findByUser_Id(userId).stream()
                .map(applicantMapper::toResponse)
                .toList();
    }

    @Override
    public List<ApplicantResponse> getByStatus(String status) {
        return applicantRepository.findByStatus(status).stream()
                .map(applicantMapper::toResponse)
                .toList();
    }

    @Override
    public ApplicantResponse updateStatus(Long id, String status) {
        ApplicantModel model = applicantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Applicant not found: " + id));

        model.setStatus(status);
        return applicantMapper.toResponse(applicantRepository.save(model));
    }

    @Override
    public void delete(Long id) {
        if (!applicantRepository.existsById(id)) {
            throw new RuntimeException("Applicant not found: " + id);
        }
        applicantRepository.deleteById(id);
    }
}
