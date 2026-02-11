package com.NextStepEdu.repositories;

import com.NextStepEdu.models.ProgramModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface ProgramRepository extends JpaRepository<ProgramModel, Integer> {

    Optional<ProgramModel> findByName(String name);

    boolean existsByNameAndUniversity_Id(String name, Integer universityId);

    List<ProgramModel> findByUniversity_Id(Integer universityId);

    List<ProgramModel> findByFaculty_Id(Integer facultyId);

    List<ProgramModel> findByNameContainingIgnoreCase(String name);

    List<ProgramModel> findByDegreeLevel(Integer degreeLevel);

    List<ProgramModel> findByTuitionFeeAmountBetween(Double minFee, Double maxFee);
}
