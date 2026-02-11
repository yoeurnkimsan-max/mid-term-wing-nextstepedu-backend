package com.NextStepEdu.controllers;

import com.NextStepEdu.dto.requests.ProgramRequest;
import com.NextStepEdu.dto.responses.ProgramResponse;
import com.NextStepEdu.services.ProgramService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/programs")
@RequiredArgsConstructor
public class ProgramController {
    private final ProgramService programService;

    @PostMapping
    public ResponseEntity<ProgramResponse> createProgram(@Valid @RequestBody ProgramRequest programRequest) {
        ProgramResponse createdProgram = programService.createProgram(programRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProgram);
    }


    @GetMapping
    public ResponseEntity<List<ProgramResponse>> getAllPrograms() {
        List<ProgramResponse> programResponses = programService.getAllProgram();
        return ResponseEntity.ok(programResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgramResponse> getProgramById(@PathVariable Integer id) {
        ProgramResponse programResponse = programService.getProgramById(id);
        return ResponseEntity.ok(programResponse);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ProgramResponse> updateProgram(
            @PathVariable Integer id, @Valid @RequestBody ProgramRequest programRequest) {
        ProgramResponse updateProgram = programService.updateProgram(id, programRequest);
        return ResponseEntity.ok(updateProgram);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProgramResponse> deleteProgram(@PathVariable Integer id) {
        programService.deleteProgram(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/university/{universityId}")
    public ResponseEntity<List<ProgramResponse>> getAllProgramsByUniversity(@PathVariable Integer universityId) {
        List<ProgramResponse> programResponses = programService.getProgramByUniversity(universityId);
        return ResponseEntity.ok(programResponses);
    }

    @GetMapping("/faculty/{facultyId}")
    public ResponseEntity<List<ProgramResponse>> getProgramsByFaculty(@PathVariable Integer facultyId) {
        List<ProgramResponse> programs = programService.getProgramsByFaculty(facultyId);
        return ResponseEntity.ok(programs);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProgramResponse>> searchPrograms(@RequestParam String name) {
        List<ProgramResponse> programs = programService.searchProgramsByName(name);
        return ResponseEntity.ok(programs);
    }

    @GetMapping("/degree-level/{level}")
    public ResponseEntity<List<ProgramResponse>> getProgramsByDegreeLevel(@PathVariable Integer level) {
        List<ProgramResponse> programs = programService.getProgramsByDegreeLevel(level);
        return ResponseEntity.ok(programs);
    }

    @GetMapping("/tuition-range")
    public ResponseEntity<List<ProgramResponse>> getProgramsByTuitionRange(
            @RequestParam Double min,
            @RequestParam Double max) {
        List<ProgramResponse> programs = programService.getProgramsByTuitionFeeRange(min, max);
        return ResponseEntity.ok(programs);
    }


}
