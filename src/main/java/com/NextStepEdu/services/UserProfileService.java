package com.NextStepEdu.services;

import com.NextStepEdu.dto.responses.UserProfileResponse;
import com.NextStepEdu.models.UserProfileModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserProfileService {
    void updateProfile(Integer userId, String firstname, String lastname, String phone, MultipartFile image);
    void deleteProfile(Integer userId);
    List<UserProfileResponse> getAllProfiles();
}
