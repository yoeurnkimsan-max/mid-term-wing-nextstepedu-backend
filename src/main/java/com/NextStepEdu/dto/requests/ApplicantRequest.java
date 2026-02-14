package com.NextStepEdu.dto.requests;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ApplicantRequest(
        Integer userId,
        Integer scholarshipId,
        String firstName,
        String lastName,
        String gender,
        LocalDate dateOfBirth,
        String email,
        String phoneNumber,
        String address,
        String nationality,
        String highSchoolName,
        BigDecimal gpa,
        String intendedMajor,
        String scholarshipType,
        BigDecimal familyIncome,
        String motivationLetter
) {}
