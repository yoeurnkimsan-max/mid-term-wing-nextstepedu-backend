package com.NextStepEdu.services.impl;

import com.NextStepEdu.dto.requests.FacultyRequest;
import com.NextStepEdu.dto.responses.FacultyResponse;
import com.NextStepEdu.models.FacultyModel;
import com.NextStepEdu.models.UniversityModel;
import com.NextStepEdu.repositories.FacultyRepository;
import com.NextStepEdu.repositories.UniversityRepository;
import com.NextStepEdu.services.FacultyService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@AllArgsConstructor
public class FacultyServiceImpl implements FacultyService {

    private final FacultyRepository facultyRepository;
    private final UniversityRepository universityRepository;

    @Override
    @Transactional
    public FacultyResponse create(FacultyRequest request) {
        String normalizedName = request.name().trim();

        UniversityModel university = null;
        if (request.universityId() != null) {
            university = getUniversityOrThrow(request.universityId());
            if (facultyRepository.existsByUniversity_IdAndNameIgnoreCase(university.getId(), normalizedName)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Faculty already exists in this university");
            }
        }

        FacultyModel faculty = new FacultyModel();
        faculty.setName(normalizedName);
        faculty.setDescription(request.description());
        faculty.setUniversity(university);

        return toResponse(facultyRepository.save(faculty));
    }

    @Override
    @Transactional(readOnly = true)
    public FacultyResponse getById(Integer id) {
        FacultyModel faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Faculty not found"));

        return toResponse(faculty);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FacultyResponse> getAll(Integer universityId) {
        if (universityId != null) {
            return facultyRepository.findByUniversity_Id(universityId)
                    .stream()
                    .map(this::toResponse)
                    .toList();
        }

        return facultyRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public FacultyResponse update(Integer id, FacultyRequest request) {
        FacultyModel faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Faculty not found"));

        String normalizedName = request.name().trim();

        UniversityModel university = null;
        if (request.universityId() != null) {
            university = getUniversityOrThrow(request.universityId());
            Integer currentUniversityId = faculty.getUniversity() == null ? null : faculty.getUniversity().getId();
            if (currentUniversityId == null
                    || !currentUniversityId.equals(university.getId())
                    || !faculty.getName().equalsIgnoreCase(normalizedName)) {
                if (facultyRepository.existsByUniversity_IdAndNameIgnoreCase(university.getId(), normalizedName)) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Faculty already exists in this university");
                }
            }
        }

        faculty.setName(normalizedName);
        faculty.setDescription(request.description());
        faculty.setUniversity(university);

        return toResponse(facultyRepository.save(faculty));
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        FacultyModel faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Faculty not found"));
        facultyRepository.delete(faculty);
    }

    private UniversityModel getUniversityOrThrow(Integer universityId) {
        return universityRepository.findById(universityId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "University not found"));
    }

    private FacultyResponse toResponse(FacultyModel faculty) {
        Integer universityId = faculty.getUniversity() == null ? null : faculty.getUniversity().getId();
        String universityName = faculty.getUniversity() == null ? null : faculty.getUniversity().getName();
        return new FacultyResponse(
                faculty.getId(),
                faculty.getName(),
                faculty.getDescription(),
                universityId,
                universityName
        );
    }
}
