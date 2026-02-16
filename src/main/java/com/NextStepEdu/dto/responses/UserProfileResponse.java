package com.NextStepEdu.dto.responses;

import com.NextStepEdu.models.AccountStatus;

import java.time.LocalDateTime;

public record UserProfileResponse(
        Integer id,
        Integer userId,
        String email,
        String firstname,
        String lastname,
        String phone,
        String image,
        LocalDateTime createdAt,
        String role,
        AccountStatus status
) {}

