package com.NextStepEdu.dto.responses;

public record UserProfileResponse(
        Integer id,
        Integer userId,
        String email,
        String firstname,
        String lastname,
        String phone,
        String image
) {
}
