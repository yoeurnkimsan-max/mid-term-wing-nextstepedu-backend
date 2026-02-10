package com.NextStepEdu.repositories;

import com.NextStepEdu.models.UniversityModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UniversityRepository extends JpaRepository<UniversityModel, Integer> {

    Optional<UniversityModel> findBySlug(String slug);
}
