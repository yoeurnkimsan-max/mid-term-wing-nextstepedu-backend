package com.NextStepEdu.controllers;

import com.NextStepEdu.dto.requests.ApplicantRequest;
import com.NextStepEdu.dto.responses.ApplicantResponse;
import com.NextStepEdu.services.ApplicantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/applicants")
public class ApplicantController {

    private final ApplicantService applicantService;

    public ApplicantController(ApplicantService applicantService) {
        this.applicantService = applicantService;
    }

    @PostMapping
    public ResponseEntity<ApplicantResponse> create(@RequestBody ApplicantRequest request) {
        return ResponseEntity.ok(applicantService.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicantResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(applicantService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<ApplicantResponse>> getAll() {
        return ResponseEntity.ok(applicantService.getAll());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ApplicantResponse>> getByUserId(@PathVariable Integer userId) {
        return ResponseEntity.ok(applicantService.getByUserId(userId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ApplicantResponse>> getByStatus(@PathVariable String status) {
        return ResponseEntity.ok(applicantService.getByStatus(status));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApplicantResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam String status
    ) {
        return ResponseEntity.ok(applicantService.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        applicantService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
