package com.NextStepEdu.controllers;

import com.NextStepEdu.dto.requests.UniversityContactRequest;
import com.NextStepEdu.dto.requests.UniversityRequest;
import com.NextStepEdu.dto.requests.UniversityWithContactRequest;
import com.NextStepEdu.dto.responses.UniversityResponse;
import com.NextStepEdu.services.UniversityContactService;
import com.NextStepEdu.services.UniversityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/universities")
@RequiredArgsConstructor
public class UniversityController {

    private final UniversityService universityService;
    private final UniversityContactService contactService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UniversityResponse> createUniversityWithContact(
            @Valid @ModelAttribute UniversityWithContactRequest request,
            @RequestParam(value = "logoUrl", required = false) MultipartFile logoUrl,
            @RequestParam(value = "coverImageUrl", required = false) MultipartFile coverImageUrl) {

        // Create UniversityRequest from the combined request
        UniversityRequest universityRequest = UniversityRequest.builder()
                .name(request.getName())
                .slug(request.getSlug())
                .description(request.getDescription())
                .country(request.getCountry())
                .city(request.getCity())
                .officialWebsite(request.getOfficialWebsite())
                .status(request.getStatus())
                .build();

        // Create university with images
        UniversityResponse response = universityService.createUniversity(universityRequest, logoUrl, coverImageUrl);

        // Create contact if fields are provided
        if (request.getLabel() != null || request.getEmail() != null || request.getPhone() != null) {
            UniversityContactRequest contactRequest = UniversityContactRequest.builder()
                    .label(request.getLabel())
                    .email(request.getEmail())
                    .phone(request.getPhone())
                    .websiteUrl(request.getWebsiteUrl())
                    .universityId(response.getId())
                    .build();

            contactService.createContact(contactRequest);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<UniversityResponse>> getAllUniversities() {
        List<UniversityResponse> universities = universityService.getAllUniversities();
        return ResponseEntity.ok(universities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UniversityResponse> getUniversityById(@PathVariable Integer id) {
        UniversityResponse university = universityService.getUniversityById(id);
        return ResponseEntity.ok(university);
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<UniversityResponse> getUniversityBySlug(@PathVariable String slug) {
        UniversityResponse university = universityService.getUniversityBySlug(slug);
        return ResponseEntity.ok(university);
    }

    @GetMapping("/search")
    public ResponseEntity<List<UniversityResponse>> searchUniversities(
            @RequestParam String keyword) {
        List<UniversityResponse> universities = universityService.searchUniversities(keyword);
        return ResponseEntity.ok(universities);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UniversityResponse> updateUniversity(
            @PathVariable Integer id,
            @Valid @ModelAttribute UniversityRequest request,
            @RequestParam(value = "logoUrl", required = false) MultipartFile logoUrl,
            @RequestParam(value = "coverImageUrl", required = false) MultipartFile coverImageUrl) {

        UniversityResponse response = universityService.updateUniversity(id, request, logoUrl, coverImageUrl);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUniversity(@PathVariable Integer id) {
        universityService.deleteUniversity(id);
        return ResponseEntity.noContent().build();
    }
}
