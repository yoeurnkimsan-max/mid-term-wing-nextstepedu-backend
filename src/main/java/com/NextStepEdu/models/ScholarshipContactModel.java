package com.NextStepEdu.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "scholarship_contacts")
public class ScholarshipContactModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String label;

    private String email;

    private String phone;

    @Column(name = "website_url")
    private String websiteUrl;

    // Contact belongs to Scholarship
    @ManyToOne
    @JoinColumn(name = "scholarship_id", nullable = false)
    @JsonBackReference
    private ScholarshipModel scholarship;
}
