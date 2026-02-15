package com.NextStepEdu.services.impl;

import com.NextStepEdu.dto.responses.UserProfileResponse;
import com.NextStepEdu.models.UserModel;
import com.NextStepEdu.models.UserProfileModel;
import com.NextStepEdu.repositories.UserProfileRepository;
import com.NextStepEdu.repositories.UserRepository;
import com.NextStepEdu.services.CloudinaryImageService;
import com.NextStepEdu.services.UserProfileService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {

    private final UserRepository userRepository;
    private final CloudinaryImageService cloudinaryImageService;
    private final UserProfileRepository userProfileRepository;

    @Override
    @Transactional
    public void updateProfile(Integer userId, String firstname, String lastname, String phone, MultipartFile image) {

        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        UserProfileModel profile = user.getProfile();
        if (profile == null) {
            profile = new UserProfileModel();
            profile.setUser(user);
            user.setProfile(profile);
        }

        profile.setFirstname(firstname);
        profile.setLastname(lastname);
        profile.setPhone(phone);

        if (image != null && !image.isEmpty()) {
            try {
                Map<String, Object> upload = cloudinaryImageService.upload(image);
                String imageUrl = (String) upload.get("secure_url");
                profile.setImage(imageUrl);
            } catch (Exception e) {
                throw new RuntimeException("Upload image failed", e);
            }
        }

        userRepository.save(user); // cascade saves profile
    }

    @Override
    public void deleteProfile(Integer userId) {

        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (user.getProfile() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found");
        }

        // IMPORTANT: add orphanRemoval=true in UserModel mapping
        user.setProfile(null);
        userRepository.save(user);

    }

    @Override
    public List<UserProfileResponse> getAllProfiles() {
        return userProfileRepository.findAllWithUser()
                .stream()
                .map(p -> {
                    String role = "USER";

                    if (p.getUser().getRoles() != null && !p.getUser().getRoles().isEmpty()) {
                        role = p.getUser().getRoles().get(0).getName();
                    }

                    return new UserProfileResponse(
                            p.getId(),
                            p.getUser().getId(),
                            p.getUser().getEmail(),
                            p.getFirstname(),
                            p.getLastname(),
                            p.getPhone(),
                            p.getImage(),
                            p.getUser().getCreatedAt(),
                            role,
                            p.getUser().getStatus()

                    );
                })
                .toList();
    }

}
