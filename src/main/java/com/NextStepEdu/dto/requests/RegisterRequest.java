package com.NextStepEdu.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


public record RegisterRequest(

        @NotBlank(message = "Email is required")
        String email,
        @NotBlank(message = " Password is required")
        @Pattern(regexp = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$",
                message ="Password must be contain minimum 8 character in length" +
                        "At least one uppercase English letter" +
                        "At least one English lowercase English Letter" +
                        "AT least one digit" +
                        "At least one special character") // Regular Expression
        String password,
        @NotBlank(message = "firstname is required")
        String firstname,
        @NotBlank(message = "lastname is required")
        String lastname,
        @NotBlank(message = "Phone is required")
        String phone
) {
}