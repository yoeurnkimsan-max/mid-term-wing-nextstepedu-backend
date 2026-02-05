package com.NextStepEdu.dto.responses;


public record UserResponse(

        Integer id,
        String firstname,
        String lastname,
        String phone,
        String email

) {}