package com.NextStepEdu.services.impl;

import com.NextStepEdu.dto.requests.ProgramRequest;
import com.NextStepEdu.dto.responses.ProgramResponse;
import com.NextStepEdu.mappers.ProgramMapper;
import com.NextStepEdu.models.FacultyModel;
import com.NextStepEdu.models.ProgramModel;
import com.NextStepEdu.models.UniversityModel;
import com.NextStepEdu.repositories.FacultyRepository;
import com.NextStepEdu.repositories.ProgramRepository;
import com.NextStepEdu.repositories.UniversityRepository;
import com.NextStepEdu.services.ProgramService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProgramServiceImpl implements ProgramService {
    private final ProgramRepository programRepository;
    private final UniversityRepository universityRepository;
    private final FacultyRepository facultyRepository;
    private final ProgramMapper programMapper;


    @Override
    public List<ProgramResponse> getAllProgram() {
        return programRepository.findAll().stream()
                .map(programMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProgramResponse createProgram(ProgramRequest programRequest) {

        if (programRepository.existsByNameAndUniversity_Id(programRequest.name(), programRequest.universityId())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Program with name '" + programRequest.name() + "' already exists for this university"
            );
        }

        UniversityModel university = universityRepository.findById(programRequest.universityId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "University not found with ID: " + programRequest.universityId()
                ));

        FacultyModel faculty = null;
        if (programRequest.facultyId() != null) {
            faculty = facultyRepository.findById(programRequest.facultyId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Faculty not found with ID: " + programRequest.facultyId()
                    ));
        }

        ProgramModel program = programMapper.toModel(programRequest); // make sure mapper method name matches
        program.setUniversity(university);
        program.setFaculty(faculty);

        ProgramModel savedProgram = programRepository.save(program);

        return programMapper.toResponse(savedProgram);
    }

    @Override
    public ProgramResponse getProgramById(Integer id) {
        ProgramModel program = programRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Program not found with ID: " + id
                ));

        return programMapper.toResponse(program);
    }

    @Override
    public ProgramResponse updateProgram(Integer id, ProgramRequest programRequest) {

        ProgramModel existing = programRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Program not found with ID: " + id
                ));

        if (programRequest.name() != null
                && !programRequest.name().equals(existing.getName())
                && programRepository.existsByNameAndUniversity_Id(programRequest.name(), programRequest.universityId())) {

            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Program with name '" + programRequest.name() + "' already exists for this university"
            );
        }

        if (programRequest.universityId() != null
                && !programRequest.universityId().equals(existing.getUniversity().getId())) {

            UniversityModel university = universityRepository.findById(programRequest.universityId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "University not found with ID: " + programRequest.universityId()
                    ));

            existing.setUniversity(university);
        }
        if (programRequest.facultyId() != null) {
            if (existing.getFaculty() == null
                    || !programRequest.facultyId().equals(existing.getFaculty().getId())) {

                FacultyModel faculty = facultyRepository.findById(programRequest.facultyId())
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Faculty not found with ID: " + programRequest.facultyId()
                        ));

                existing.setFaculty(faculty);
            }
        } else {
            existing.setFaculty(null);
        }

        programMapper.updateModel(programRequest, existing);

        ProgramModel updated = programRepository.save(existing);
        return programMapper.toResponse(updated);
    }

    @Override
    public void deleteProgram(Integer id) {
        if (!programRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Program not found with ID: " + id);
        }
        programRepository.deleteById(id);
    }


    @Override
    @Transactional(readOnly = true)
    public List<ProgramResponse> getProgramByUniversity(Integer universityId) {
        return programRepository.findByUniversity_Id(universityId)
                .stream()
                .map(programMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgramResponse> getProgramsByFaculty(Integer facultyId) {
        return programRepository.findByFaculty_Id(facultyId)
                .stream()
                .map(programMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgramResponse> searchProgramsByName(String name) {
        return programRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(programMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgramResponse> getProgramsByDegreeLevel(Integer degreeLevel) {
        return programRepository.findByDegreeLevel(degreeLevel)
                .stream()
                .map(programMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgramResponse> getProgramsByTuitionFeeRange(Double minFee, Double maxFee) {
        return programRepository.findByTuitionFeeAmountBetween(minFee, maxFee)
                .stream()
                .map(programMapper::toResponse)
                .toList();
    }


}
