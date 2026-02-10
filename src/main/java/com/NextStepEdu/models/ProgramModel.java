package com.NextStepEdu.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "programs")
public class ProgramModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Program name is required")
    private String name;

    private String description;

    @Column(name = "degree_level")
    private Integer degreeLevel;

    @Column(name = "exam_required")
    private Boolean examRequired;

    @Column(name = "tuition_fee_amount")
    private Double tuitionFeeAmount;

    private String currency;

    @Column(name = "study_period_months")
    private Integer studyPeriodMonths;

    @ManyToOne
    @JoinColumn(name = "university_id", nullable = false)
    private UniversityModel university;

    // Program belongs to Faculty
    @ManyToOne
    @JoinColumn(name = "faculty_id")
    private FacultyModel faculty;

    // Program has many Scholarships
    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ScholarshipModel> scholarships;
}
