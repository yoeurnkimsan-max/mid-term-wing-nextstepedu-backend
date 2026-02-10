package com.NextStepEdu.repositories;

import com.NextStepEdu.models.UniversityContactModel;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface UniversityContactRepository extends JpaRepository<UniversityContactModel, Integer> {

    List<UniversityContactModel> findByUniversityId(Integer universityId);

}
