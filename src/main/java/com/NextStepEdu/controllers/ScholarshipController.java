package com.NextStepEdu.controllers;

import com.NextStepEdu.dto.requests.ScholarshipRequest;
import com.NextStepEdu.models.ScholarshipModel;
import com.NextStepEdu.services.ScholarshipService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/scholarship")
@AllArgsConstructor
public class ScholarshipController {

    private final ScholarshipService scholarshipService;

    @GetMapping
    public ResponseEntity<List<ScholarshipModel>> getAllScholarships() {
        List<ScholarshipModel> scholarships = scholarshipService.findAll();
        return ResponseEntity.ok(scholarships);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScholarshipModel> getScholarshipById(@PathVariable Integer id) {
        ScholarshipModel scholarship = scholarshipService.findById(id);
        return ResponseEntity.ok(scholarship);
    }

    @PostMapping
    public ResponseEntity<ScholarshipModel> createScholarship(@Valid @RequestBody ScholarshipRequest scholarshipRequest) {
        ScholarshipModel createdScholarship = scholarshipService.create(scholarshipRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdScholarship);
    }
}
