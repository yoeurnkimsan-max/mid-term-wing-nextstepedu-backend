package com.NextStepEdu.repositories;

import com.NextStepEdu.models.ProgramModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ProgramRepository extends JpaRepository<ProgramModel, Integer> {

    Optional<ProgramModel> findByName(String name);

}
