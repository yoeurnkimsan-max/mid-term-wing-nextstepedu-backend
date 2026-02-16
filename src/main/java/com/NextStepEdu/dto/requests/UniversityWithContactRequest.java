package com.NextStepEdu.dto.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UniversityWithContactRequest {

    // University fields
    @NotBlank(message = "University name is required")
    private String name;

    private String slug;
    private String description;
    private String country;
    private String city;
    private String officialWebsite;
    private String status;

    // Contact fields
    private String label;
    private String email;
    private String phone;
    private String websiteUrl;
}