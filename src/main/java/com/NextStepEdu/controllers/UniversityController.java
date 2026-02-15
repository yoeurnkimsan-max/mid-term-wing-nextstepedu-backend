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
            @RequestParam(value = "name") String name,
            @RequestParam(value = "slug", required = false) String slug,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "country", required = false) String country,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "officialWebsite", required = false) String officialWebsite,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "label", required = false) String label,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "websiteUrl", required = false) String websiteUrl,
            @RequestParam(value = "logoUrl", required = false) MultipartFile logoUrl,
            @RequestParam(value = "coverImageUrl", required = false) MultipartFile coverImageUrl) {

        // Create UniversityRequest from the parameters
        UniversityRequest universityRequest = UniversityRequest.builder()
                .name(name)
                .slug(slug)
                .description(description)
                .country(country)
                .city(city)
                .officialWebsite(officialWebsite)
                .status(status)
                .build();

        // Create university with images
        UniversityResponse response = universityService.createUniversity(universityRequest, logoUrl, coverImageUrl);

        // Create contact if fields are provided
        if (label != null || email != null || phone != null) {
            UniversityContactRequest contactRequest = UniversityContactRequest.builder()
                    .label(label)
                    .email(email)
                    .phone(phone)
                    .websiteUrl(websiteUrl)
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
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "slug", required = false) String slug,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "country", required = false) String country,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "officialWebsite", required = false) String officialWebsite,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "logoUrl", required = false) MultipartFile logoUrl,
            @RequestParam(value = "coverImageUrl", required = false) MultipartFile coverImageUrl) {

        UniversityRequest request = UniversityRequest.builder()
                .name(name)
                .slug(slug)
                .description(description)
                .country(country)
                .city(city)
                .officialWebsite(officialWebsite)
                .status(status)
                .build();

        UniversityResponse response = universityService.updateUniversity(id, request, logoUrl, coverImageUrl);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUniversity(@PathVariable Integer id) {
        universityService.deleteUniversity(id);
        return ResponseEntity.noContent().build();
    }
}
