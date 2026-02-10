package com.NextStepEdu.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "scholarships")
public class ScholarshipModel {

    @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Scholarship name is required")
    private String name;

    private String slug;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "cover_image_url")
    private String coverImageUrl;

    private String description;

    private Integer level;


    private String benefits;


    private String requirements;

    @Column(name = "how_to_apply")
    private String howToApply;

    @Column(name = "apply_link")
    private String applyLink;

    private String status;

    private LocalDateTime deadline;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "program_id", nullable = false)
    private ProgramModel program;

    @ManyToOne
    @JoinColumn(name = "university_id", nullable = false)
    private UniversityModel university;

    @OneToMany(mappedBy = "scholarship", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ScholarshipContactModel> contacts;
}
