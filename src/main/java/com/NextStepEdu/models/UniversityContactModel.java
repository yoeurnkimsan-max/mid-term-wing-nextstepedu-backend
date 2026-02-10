package com.NextStepEdu.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "university_contacts")
public class UniversityContactModel {

    @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String label;

    private String email;

    private String phone;

    @Column(name = "website_url")
    private String websiteUrl;

    @ManyToOne
    @JoinColumn(name = "university_id", nullable = false)
    @JsonBackReference
    private UniversityModel university;
}
