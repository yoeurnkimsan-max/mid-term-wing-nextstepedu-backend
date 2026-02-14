package com.NextStepEdu.repositories;

import com.NextStepEdu.models.ApplicantModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicantRepository extends JpaRepository<ApplicantModel, Long> {
    List<ApplicantModel> findByUser_Id(Integer userId);
    List<ApplicantModel> findByScholarship_Id(Integer scholarshipId);
    List<ApplicantModel> findByStatus(String status);
}
