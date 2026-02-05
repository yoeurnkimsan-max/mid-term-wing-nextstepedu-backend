package com.NextStepEdu.controllers;

import com.NextStepEdu.dto.responses.UserProfileResponse;
import com.NextStepEdu.models.UserProfileModel;
import com.NextStepEdu.services.UserProfileService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/profile")
@AllArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @PutMapping(value = "/users/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProfile(
            @PathVariable Integer userId,
            @RequestParam String firstname,
            @RequestParam String lastname,
            @RequestParam String phone,
            @RequestParam(required = false) MultipartFile image
    ) {
        userProfileService.updateProfile(userId, firstname, lastname, phone, image);
        return ResponseEntity.ok("Profile updated");
    }
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<?> deleteProfile(@PathVariable Integer userId) {
        userProfileService.deleteProfile(userId);
        return ResponseEntity.ok("Profile deleted");
    }
    @GetMapping("/getAll")
    public ResponseEntity<List<UserProfileResponse>> getAllProfiles() {
        return ResponseEntity.ok(userProfileService.getAllProfiles());
    }
}
