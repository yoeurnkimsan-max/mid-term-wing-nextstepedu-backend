package com.NextStepEdu.dto.responses;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UniversityContactResponse {
    private Integer id;
    private String label;
    private String email;
    private String phone;
    private Integer universityId;
}
