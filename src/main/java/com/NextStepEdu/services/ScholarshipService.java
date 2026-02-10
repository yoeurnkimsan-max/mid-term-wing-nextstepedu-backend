package com.NextStepEdu.services;

import com.NextStepEdu.models.ScholarshipModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

public interface ScholarshipService {

    List<ScholarshipModel> findAll();

    Page<ScholarshipModel> findAll(Pageable pageable);

    ScholarshipModel findById(Integer id);

    ScholarshipModel findBySlug(String slug);

    ScholarshipModel create(
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
    );

    ScholarshipModel update(
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
    );

    void delete(Integer id);
}
