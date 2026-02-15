package com.NextStepEdu.dto.responses;

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
        String role
) {}

