package com.NextStepEdu.services.impl;

import com.NextStepEdu.dto.responses.error_response.ResourceNotFoundException;
import com.NextStepEdu.models.ProgramModel;
import com.NextStepEdu.models.ScholarshipModel;
import com.NextStepEdu.models.UniversityModel;
import com.NextStepEdu.repositories.ProgramRepository;
import com.NextStepEdu.repositories.ScholarshipRepository;
import com.NextStepEdu.repositories.UniversityRepository;
import com.NextStepEdu.services.CloudinaryImageService;
import com.NextStepEdu.services.ScholarshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ScholarshipServiceImpl implements ScholarshipService {

    private final ScholarshipRepository scholarshipRepository;
    private final ProgramRepository programRepository;
    private final UniversityRepository universityRepository;
    private final CloudinaryImageService cloudinaryImageService;

    @Override
    public List<ScholarshipModel> findAll() {
        return scholarshipRepository.findAll();
    }

    @Override
    public Page<ScholarshipModel> findAll(Pageable pageable) {
        return scholarshipRepository.findAll(pageable);
    }

    @Override
    public ScholarshipModel findById(Integer id) {
        return scholarshipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Scholarship not found: " + id));
    }

    @Override
    public ScholarshipModel findBySlug(String slug) {
        return scholarshipRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Scholarship not found with slug: " + slug));
    }

    @Override
    @Transactional
    public ScholarshipModel create(
            String name,
            String description,
            Integer level,
            String benefits,
            String requirements,
            String howToApply,
            String applyLink,
            String status,
            LocalDateTime deadline,
            Integer programId,
            Integer universityId,
            MultipartFile logo,
            MultipartFile coverImage
    ) {
        ProgramModel program = programRepository.findById(programId)
                .orElseThrow(() -> new RuntimeException("Program not found: " + programId));

        UniversityModel university = universityRepository.findById(universityId)
                .orElseThrow(() -> new RuntimeException("University not found: " + universityId));

        ScholarshipModel scholarship = new ScholarshipModel();
        scholarship.setName(name);
        scholarship.setSlug(generateSlug(name));
        scholarship.setDescription(description);
        scholarship.setLevel(level);
        scholarship.setBenefits(benefits);
        scholarship.setRequirements(requirements);
        scholarship.setHowToApply(howToApply);
        scholarship.setApplyLink(applyLink);
        scholarship.setStatus(status);
        scholarship.setDeadline(deadline);
        scholarship.setProgram(program);
        scholarship.setUniversity(university);
        scholarship.setCreatedAt(LocalDateTime.now());
        scholarship.setUpdatedAt(LocalDateTime.now());

        // Upload images to Cloudinary
        if (logo != null && !logo.isEmpty()) {
            String logoUrl = uploadImage(logo);
            scholarship.setLogoUrl(logoUrl);
        }

        if (coverImage != null && !coverImage.isEmpty()) {
            String coverImageUrl = uploadImage(coverImage);
            scholarship.setCoverImageUrl(coverImageUrl);
        }

        return scholarshipRepository.save(scholarship);
    }

    @Override
    @Transactional
    public ScholarshipModel update(
            Integer id,
            String name,
            String description,
            Integer level,
            String benefits,
            String requirements,
            String howToApply,
            String applyLink,
            String status,
            LocalDateTime deadline,
            Integer programId,
            Integer universityId,
            MultipartFile logo,
            MultipartFile coverImage
    ) {
        ScholarshipModel scholarship = scholarshipRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Scholarship not found: " + id));

        if (name != null) {
            scholarship.setName(name);
            scholarship.setSlug(generateSlug(name));
        }
        if (description != null) scholarship.setDescription(description);
        if (level != null) scholarship.setLevel(level);
        if (benefits != null) scholarship.setBenefits(benefits);
        if (requirements != null) scholarship.setRequirements(requirements);
        if (howToApply != null) scholarship.setHowToApply(howToApply);
        if (applyLink != null) scholarship.setApplyLink(applyLink);
        if (status != null) scholarship.setStatus(status);
        if (deadline != null) scholarship.setDeadline(deadline);

        if (programId != null) {
            ProgramModel program = programRepository.findById(programId)
                    .orElseThrow(() -> new RuntimeException("Program not found: " + programId));
            scholarship.setProgram(program);
        }

        if (universityId != null) {
            UniversityModel university = universityRepository.findById(universityId)
                    .orElseThrow(() -> new RuntimeException("University not found: " + universityId));
            scholarship.setUniversity(university);
        }

        if (logo != null && !logo.isEmpty()) {
            String logoUrl = uploadImage(logo);
            scholarship.setLogoUrl(logoUrl);
        }

        if (coverImage != null && !coverImage.isEmpty()) {
            String coverImageUrl = uploadImage(coverImage);
            scholarship.setCoverImageUrl(coverImageUrl);
        }

        scholarship.setUpdatedAt(LocalDateTime.now());

        return scholarshipRepository.save(scholarship);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        ScholarshipModel scholarship = scholarshipRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Scholarship not found: " + id));
        scholarshipRepository.delete(scholarship);
    }

    private String uploadImage(MultipartFile file) {
        try {
            Map result = cloudinaryImageService.upload(file);
            return (String) result.get("secure_url");
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image: " + e.getMessage());
        }
    }

    private String generateSlug(String name) {
        return name.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-+", "-")
                .trim();
    }
}
