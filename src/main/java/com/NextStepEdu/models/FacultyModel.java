package com.NextStepEdu.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "faculties")
public class FacultyModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "Faculty name is required")
    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "university_id", nullable = false)
    private UniversityModel university;


}
