package com.NextStepEdu.dto.responses;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UniversityResponse {
    private Integer id;
    private String name;
    private String slug;
    private String logoUrl;
    private String coverImageUrl;
    private String description;
    private String country;
    private String city;
    private String officialWebsite;
    private String status;
    private String email;
    private String label;
    private String phone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
