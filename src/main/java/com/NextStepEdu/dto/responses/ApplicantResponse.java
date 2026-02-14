package com.NextStepEdu.dto.responses;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ApplicantResponse {
    public Long id;

    public Integer userId;
    public Integer scholarshipId;

    public String firstName;
    public String lastName;
    public String gender;
    public LocalDate dateOfBirth;

    public String email;
    public String phoneNumber;
    public String address;
    public String nationality;

    public String highSchoolName;
    public BigDecimal gpa;

    public String intendedMajor;
    public String scholarshipType;

    public BigDecimal familyIncome;
    public String motivationLetter;

    public String status;
    public LocalDateTime createdAt;
    public LocalDateTime updatedAt;

    // Optional convenience fields
    public Integer universityId; // from scholarship.university
}
