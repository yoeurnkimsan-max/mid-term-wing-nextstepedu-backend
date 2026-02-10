package com.NextStepEdu.controllers;

import com.NextStepEdu.dto.requests.ScholarshipRequest;
import com.NextStepEdu.dto.responses.PageResponse;
import com.NextStepEdu.dto.responses.ScholarshipResponse;
import com.NextStepEdu.models.ScholarshipModel;
import com.NextStepEdu.services.CloudinaryImageService;
import com.NextStepEdu.services.ScholarshipService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/scholarship")
@AllArgsConstructor
public class ScholarshipController {

    private final ScholarshipService scholarshipService;
    private final CloudinaryImageService cloudinaryImageService;
//    private final ScholarshipMapper scholarshipMapper;

    @GetMapping
    public ResponseEntity<PageResponse<ScholarshipResponse>> getAllScholarships(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("asc") 
                ? Sort.by(sortBy).ascending() 
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<ScholarshipModel> scholarshipPage = scholarshipService.findAll(pageable);
        
        List<ScholarshipResponse> content = scholarshipPage.getContent()
                .stream()
                .map(ScholarshipResponse::fromEntity)
                .toList();
        
        PageResponse<ScholarshipResponse> response = PageResponse.<ScholarshipResponse>builder()
                .content(content)
                .page(scholarshipPage.getNumber())
                .size(scholarshipPage.getSize())
                .totalElements(scholarshipPage.getTotalElements())
                .totalPages(scholarshipPage.getTotalPages())
                .first(scholarshipPage.isFirst())
                .last(scholarshipPage.isLast())
                .build();
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScholarshipResponse> getScholarshipById(@PathVariable Integer id) {
        ScholarshipModel scholarship = scholarshipService.findById(id);
        return ResponseEntity.ok(ScholarshipResponse.fromEntity(scholarship));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<ScholarshipResponse> getScholarshipBySlug(@PathVariable String slug) {
        ScholarshipModel scholarship = scholarshipService.findBySlug(slug);
        return ResponseEntity.ok(ScholarshipResponse.fromEntity(scholarship));
    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createScholarship(
            @RequestPart("data") String data,
            @RequestParam(value = "logo", required = false) MultipartFile logo,
            @RequestParam(value = "coverImage", required = false) MultipartFile coverImage
    ) throws JsonProcessingException {

        System.out.println("Raw data string: " + data);

        // Parse JSON string to Object
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        // Option 1: Parse as Map
        Map<String, Object> jsonMap = mapper.readValue(data, Map.class);
        System.out.println("Parsed as Map: " + jsonMap);

        // Option 2: Parse as your DTO
        ScholarshipRequest request = mapper.readValue(data, ScholarshipRequest.class);
        System.out.println("Parsed as DTO - Name: " + request.getName());

        scholarshipService.create(request.getName(), request.getDescription(), request.getLevel(), request.getBenefits(), request.getRequirements(), request.getHowToApply(), request.getApplyLink(), request.getStatus(), request.getDeadline(), request.getProgramId(), request.getUniversityId(), logo, coverImage);

        // Option 3: Return structured response
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Scholarship created");
        response.put("data", jsonMap);
        response.put("logoReceived", logo != null);
        response.put("coverImageReceived", coverImage != null);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ScholarshipResponse> updateScholarship(
            @PathVariable Integer id,
            @RequestPart("data") String data,
            @RequestParam(value = "logo", required = false) MultipartFile logo,
            @RequestParam(value = "coverImage", required = false) MultipartFile coverImage
    ) throws JsonProcessingException {

        System.out.println("Raw data string: " + data);

        // Parse JSON string to Object
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        // Option 1: Parse as Map
        Map<String, Object> jsonMap = mapper.readValue(data, Map.class);
        System.out.println("Parsed as Map: " + jsonMap);

        // Parse as your DTO
        ScholarshipRequest request = mapper.readValue(data, ScholarshipRequest.class);
        System.out.println("Parsed as DTO - Name: " + request.getName());

        ScholarshipModel scholarship = scholarshipService.update(
                id,
                request.getName(),
                request.getDescription(),
                request.getLevel(),
                request.getBenefits(),
                request.getRequirements(),
                request.getHowToApply(),
                request.getApplyLink(),
                request.getStatus(),
                request.getDeadline(),
                request.getProgramId(),
                request.getUniversityId(),
                logo,
                coverImage
        );
        return ResponseEntity.ok(ScholarshipResponse.fromEntity(scholarship));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScholarship(@PathVariable Integer id) {
        scholarshipService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
