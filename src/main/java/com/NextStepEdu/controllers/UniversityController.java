package com.NextStepEdu.controllers;

import com.NextStepEdu.dto.requests.UniversityContactRequest;
import com.NextStepEdu.dto.requests.UniversityRequest;
import com.NextStepEdu.dto.requests.UniversityWithContactRequest;
import com.NextStepEdu.dto.responses.UniversityContactResponse;
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
            @RequestParam(value = "logoUrl", required = false) MultipartFile logoUrl,
            @RequestParam(value = "coverImageUrl", required = false) MultipartFile coverImageUrl) {

//        if (logoUrl == null && coverImageUrl == null) {
//            System.out.println("⚠️  WARNING: No files received!");
//            System.out.println("   Check that in Postman:");
//            System.out.println("   1. Body → form-data");
//            System.out.println("   2. logoUrl field type = File (NOT Text!)");
//            System.out.println("   3. coverImageUrl field type = File (NOT Text!)");
//            System.out.println("   4. Select actual image files\n");
//        }

        UniversityRequest universityRequest = UniversityRequest.builder()
                .name(name)
                .slug(slug)
                .description(description)
                .country(country)
                .city(city)
                .officialWebsite(officialWebsite)
                .status(status)
                .build();

        UniversityResponse response = universityService.createUniversity(universityRequest, logoUrl, coverImageUrl);

        if (label != null || email != null || phone != null) {
            UniversityContactRequest contactRequest = UniversityContactRequest.builder()
                    .label(label)
                    .email(email)
                    .phone(phone)
                    .websiteUrl(null)
                    .universityId(response.getId())
                    .build();

            contactService.createContact(contactRequest);
        }

        // Fetch and return the created university with contacts
        UniversityResponse fullResponse = universityService.getUniversityById(response.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(fullResponse);
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
            @RequestParam(value = "label", required = false) String label,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "logoUrl", required = false) MultipartFile logoUrl,
            @RequestParam(value = "coverImageUrl", required = false) MultipartFile coverImageUrl) {

        UniversityRequest universityRequest = UniversityRequest.builder()
                .name(name)
                .slug(slug)
                .description(description)
                .country(country)
                .city(city)
                .officialWebsite(officialWebsite)
                .status(status)
                .build();

        universityService.updateUniversity(id, universityRequest, logoUrl, coverImageUrl);

        // Update or create contact if contact fields are provided
        if (label != null || email != null || phone != null) {
            List<UniversityContactResponse> contacts = contactService.getContactsByUniversityId(id);

            UniversityContactRequest contactRequest = UniversityContactRequest.builder()
                    .label(label)
                    .email(email)
                    .phone(phone)
                    .websiteUrl(null)
                    .universityId(id)
                    .build();

            if (contacts.isEmpty()) {
                // Create new contact if none exists
                contactService.createContact(contactRequest);
            } else {
                // Update existing contact
                contactService.updateContact(contacts.get(0).getId(), contactRequest);
            }
        }

        // Fetch and return the updated university with contacts
        UniversityResponse response = universityService.getUniversityById(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUniversity(@PathVariable Integer id) {
        universityService.deleteUniversity(id);
        return ResponseEntity.noContent().build();
    }
}
