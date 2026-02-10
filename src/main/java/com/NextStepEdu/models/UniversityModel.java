package com.NextStepEdu.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "universities")
public class UniversityModel {

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id;

    @NotBlank(message = "University name is required")
    private String name;

    private String slug;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "cover_image_url")
    private String coverImageUrl;

    private String description;

    private String country;

    private String city;

    @Column(name = "official_website")
    private String officialWebsite;

    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "university", cascade = CascadeType.ALL)
    private List<FacultyModel> faculties;


    @OneToMany(mappedBy = "university", cascade = CascadeType.ALL)
    private List<ProgramModel> programs;

    @OneToMany(mappedBy = "university", cascade = CascadeType.ALL)
    private List<UniversityContactModel> contacts;

    @OneToMany(mappedBy = "university", cascade = CascadeType.ALL)
    private List<ScholarshipModel> scholarships;

}
