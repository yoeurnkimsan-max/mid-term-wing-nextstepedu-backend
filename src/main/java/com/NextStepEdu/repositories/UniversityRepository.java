package com.NextStepEdu.repositories;

import com.NextStepEdu.models.UniversityModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import java.util.Optional;

public interface UniversityRepository extends JpaRepository<UniversityModel, Integer> {

    Optional<UniversityModel> findBySlug(String slug);


    List<UniversityModel> findByNameContainingIgnoreCase(String keyword);

}
