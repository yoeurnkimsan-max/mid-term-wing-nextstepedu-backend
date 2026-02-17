package com.NextStepEdu.dto.responses;

import com.NextStepEdu.models.ScholarshipContactModel;
import com.NextStepEdu.models.ScholarshipModel;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ScholarshipResponse(
        Integer id,
        String name,
        String slug,
        String logoUrl,
        String coverImageUrl,
        String description,
        String level,
        Integer maxApplicant,
        Integer currentApplicant,
        String benefits,
        String requirements,
        String howToApply,
        String applyLink,
        String status,
        LocalDateTime deadline,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        ProgramInfo program,
        UniversityInfo university,
        List<ContactInfo> contacts
) {
    @Builder
    public record ProgramInfo(
            Integer id,
            String name,
            String description,
            Integer degreeLevel,
            Boolean examRequired,
            Double tuitionFeeAmount,
            String currency,
            Integer studyPeriodMonths
    ) {
        public static ProgramInfo fromEntity(com.NextStepEdu.models.ProgramModel program) {
            if (program == null) return null;
            return ProgramInfo.builder()
                    .id(program.getId())
                    .name(program.getName())
                    .description(program.getDescription())
                    .degreeLevel(program.getDegreeLevel())
                    .examRequired(program.getExamRequired())
                    .tuitionFeeAmount(program.getTuitionFeeAmount())
                    .currency(program.getCurrency())
                    .studyPeriodMonths(program.getStudyPeriodMonths())
                    .build();
        }
    }

    @Builder
    public record UniversityInfo(
            Integer id,
            String name,
            String slug,
            String logoUrl,
            String coverImageUrl,
            String description,
            String country,
            String city,
            String officialWebsite,
            String status
    ) {
        public static UniversityInfo fromEntity(com.NextStepEdu.models.UniversityModel university) {
            if (university == null) return null;
            return UniversityInfo.builder()
                    .id(university.getId())
                    .name(university.getName())
                    .slug(university.getSlug())
                    .logoUrl(university.getLogoUrl())
                    .coverImageUrl(university.getCoverImageUrl())
                    .description(university.getDescription())
                    .country(university.getCountry())
                    .city(university.getCity())
                    .officialWebsite(university.getOfficialWebsite())
                    .status(university.getStatus())
                    .build();
        }
    }

    @Builder
    public record ContactInfo(
            Integer id,
            String label,
            String email,
            String phone,
            String websiteUrl
    ) {
        public static ContactInfo fromEntity(ScholarshipContactModel contact) {
            return ContactInfo.builder()
                    .id(contact.getId())
                    .label(contact.getLabel())
                    .email(contact.getEmail())
                    .phone(contact.getPhone())
                    .websiteUrl(contact.getWebsiteUrl())
                    .build();
        }
    }

    public static ScholarshipResponse fromEntity(ScholarshipModel scholarship) {
        return ScholarshipResponse.builder()
                .id(scholarship.getId())
                .name(scholarship.getName())
                .slug(scholarship.getSlug())
                .logoUrl(scholarship.getLogoUrl())
                .coverImageUrl(scholarship.getCoverImageUrl())
                .description(scholarship.getDescription())
                .level(scholarship.getLevel())
                .maxApplicant(scholarship.getMaxApplicant())
                .currentApplicant(scholarship.getCurrentApplicant())
                .benefits(scholarship.getBenefits())
                .requirements(scholarship.getRequirements())
                .howToApply(scholarship.getHowToApply())
                .applyLink(scholarship.getApplyLink())
                .status(scholarship.getStatus())
                .deadline(scholarship.getDeadline())
                .createdAt(scholarship.getCreatedAt())
                .updatedAt(scholarship.getUpdatedAt())
                .program(ProgramInfo.fromEntity(scholarship.getProgram()))
                .university(UniversityInfo.fromEntity(scholarship.getUniversity()))
                .contacts(scholarship.getContacts() != null
                        ? scholarship.getContacts().stream().map(ContactInfo::fromEntity).toList()
                        : List.of())
                .build();
    }
}
