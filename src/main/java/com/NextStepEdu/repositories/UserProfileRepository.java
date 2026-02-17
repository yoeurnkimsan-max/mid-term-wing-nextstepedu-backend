package com.NextStepEdu.repositories;

import com.NextStepEdu.models.UserModel;
import com.NextStepEdu.models.UserProfileModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserProfileRepository  extends JpaRepository<UserProfileModel , Integer> {
    boolean existsByUser(UserModel user);


    @Query("""
   SELECT up FROM UserProfileModel up
   JOIN FETCH up.user u
   LEFT JOIN FETCH u.roles
""")
    List<UserProfileModel> findAllWithUser();

}
