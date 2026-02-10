package com.NextStepEdu.controllers;

import com.NextStepEdu.dto.requests.FacultyRequest;
import com.NextStepEdu.dto.responses.FacultyResponse;
import com.NextStepEdu.services.FacultyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/faculties")
@AllArgsConstructor
@Tag(name = "Faculties", description = "Endpoints for faculty CRUD operations")
@SecurityRequirement(name = "bearerAuth")
public class FacultyController {
    private final FacultyService facultyService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Operation(summary = "Create faculty", description = "Create a faculty record")
    public FacultyResponse create(@Valid @RequestBody FacultyRequest request) {
        return facultyService.create(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get faculty by ID", description = "Return one faculty by its ID")
    public FacultyResponse getById(@PathVariable Integer id) {
        return facultyService.getById(id);
    }

    @GetMapping
    @Operation(summary = "List faculties", description = "Return all faculties, optionally filtered by universityId")
    public List<FacultyResponse> getAll(@RequestParam(required = false) Integer universityId) {
        return facultyService.getAll(universityId);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update faculty", description = "Update a faculty by ID")
    public FacultyResponse update(@PathVariable Integer id, @Valid @RequestBody FacultyRequest request) {
        return facultyService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete faculty", description = "Delete a faculty by ID")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        facultyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
