package com.NextStepEdu.controllers;

import com.NextStepEdu.dto.requests.UniversityRequest;
import com.NextStepEdu.dto.responses.UniversityResponse;
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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UniversityResponse> createUniversity(
            @RequestParam String name,
            @RequestParam String slug,
            @RequestParam String description,
            @RequestParam String country,
            @RequestParam String city,
            @RequestParam String officialWebsite,
            @RequestParam String status,
            @RequestPart(value = "logo", required = false) MultipartFile logo,
            @RequestPart(value = "coverImage", required = false) MultipartFile coverImage
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(universityService.createUniversity(
                        name, slug, description, country, city, officialWebsite,
                        status,  logo, coverImage
                ));
    }




    @GetMapping("/{id}")
    public ResponseEntity<UniversityResponse> getUniversityById(@PathVariable Integer id) {
        UniversityResponse response = universityService.getUniversityById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<UniversityResponse> getUniversityBySlug(@PathVariable String slug) {
        UniversityResponse response = universityService.getUniversityBySlug(slug);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<UniversityResponse>> getAllUniversities() {
        List<UniversityResponse> responses = universityService.getAllUniversities();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/search")
    public ResponseEntity<List<UniversityResponse>> searchUniversities(@RequestParam String keyword) {
        List<UniversityResponse> responses = universityService.searchUniversities(keyword);
        return ResponseEntity.ok(responses);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UniversityResponse> updateUniversity(
            @PathVariable Integer id,
            @Valid @ModelAttribute UniversityRequest request,
            @RequestParam(value = "logo", required = false) MultipartFile logo,
            @RequestParam(value = "coverImage", required = false) MultipartFile coverImage
    ) {
        UniversityResponse response = universityService.updateUniversity(id, request, logo, coverImage);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUniversity(@PathVariable Integer id) {
        universityService.deleteUniversity(id);
        return ResponseEntity.noContent().build();
    }
}
