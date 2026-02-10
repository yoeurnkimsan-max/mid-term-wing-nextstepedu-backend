package com.NextStepEdu.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
public class ScholarshipRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Level is required")
    private Integer level;

    private String benefits;           // Optional
    private String requirements;       // Optional
    private String howToApply;         // Optional
    private String applyLink;          // Optional
    private String status;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime deadline;    // Optional

    private Integer programId;         // Optional
    private Integer universityId;      // Optional

    private MultipartFile logo;        // Optional
    private MultipartFile coverImage;  // Optional
}