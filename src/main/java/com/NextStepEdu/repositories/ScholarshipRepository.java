package com.NextStepEdu.repositories;

import com.NextStepEdu.models.ScholarshipModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScholarshipRepository  extends JpaRepository<ScholarshipModel, Integer> {
    Optional<ScholarshipModel> findBySlug(String slug);
}
