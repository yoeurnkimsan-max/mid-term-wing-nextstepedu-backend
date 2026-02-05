package com.NextStepEdu.repositories;

import com.NextStepEdu.models.FacultyModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FacultyRepository extends JpaRepository<FacultyModel, Integer> {
    Optional<FacultyModel> findByName(String name);
}
