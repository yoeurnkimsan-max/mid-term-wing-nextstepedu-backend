package com.NextStepEdu.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "applicants")
public class ApplicantModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relationship to user (who submitted the application)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;

    // Relationship to scholarship being applied for
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scholarship_id", nullable = false)
    private ScholarshipModel scholarship;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(nullable = false, length = 20)
    private String gender;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String address;

    @Column(nullable = false, length = 100)
    private String nationality;

    @Column(name = "high_school_name", nullable = false, length = 150)
    private String highSchoolName;

    @Column(nullable = false, precision = 3, scale = 2)
    private BigDecimal gpa;

    @Column(name = "intended_major", nullable = false, length = 150)
    private String intendedMajor;

    @Column(name = "scholarship_type", nullable = false, length = 100)
    private String scholarshipType;

    @Column(name = "family_income", precision = 12, scale = 2)
    private BigDecimal familyIncome;

    @Column(name = "motivation_letter", nullable = false, columnDefinition = "TEXT")
    private String motivationLetter;

    @Column(nullable = false, length = 50)
    private String status = "PENDING";

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
