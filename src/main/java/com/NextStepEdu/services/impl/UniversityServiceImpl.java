package com.NextStepEdu.services.impl;

import com.NextStepEdu.dto.requests.UniversityRequest;
import com.NextStepEdu.dto.responses.UniversityResponse;
import com.NextStepEdu.mappers.UniversityMapper;
import com.NextStepEdu.models.UniversityModel;
import com.NextStepEdu.repositories.UniversityRepository;
import com.NextStepEdu.services.CloudinaryImageService;
import com.NextStepEdu.services.UniversityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UniversityServiceImpl implements UniversityService {

    private final UniversityRepository universityRepository;
    private final UniversityMapper universityMapper;
    private final CloudinaryImageService cloudinaryImageService;

    @Override
    @Transactional
    public UniversityResponse createUniversity(UniversityRequest request, MultipartFile logo,
            MultipartFile coverImage) {
        System.out.println("📝 createUniversity called with request: " + request.getName());
        System.out.println("📁 logo file: " + (logo != null ? "received (" + logo.getSize() + " bytes)" : "null"));
        System.out.println("📁 coverImage file: " + (coverImage != null ? "received (" + coverImage.getSize() + " bytes)" : "null"));

        UniversityModel university = universityMapper.toModel(request);
        university.setCreatedAt(LocalDateTime.now());
        university.setUpdatedAt(LocalDateTime.now());

        // Upload logo (optional)
        try {
            if (logo != null && !logo.isEmpty()) {
                System.out.println("🔄 Uploading logo to Cloudinary...");
                Map<String, Object> upload = cloudinaryImageService.upload(logo);
                String logoUrl = (String) upload.get("secure_url");
                System.out.println("✅ Logo uploaded: " + logoUrl);
                university.setLogoUrl(logoUrl);
            } else {
                System.out.println("⏭️  Skipping logo upload (null or empty)");
            }
        } catch (Exception e) {
            System.err.println("❌ Upload logo failed: " + e.getMessage());
            throw new RuntimeException("Upload logo failed", e);
        }

        // Upload cover image (optional)
        try {
            if (coverImage != null && !coverImage.isEmpty()) {
                System.out.println("🔄 Uploading cover image to Cloudinary...");
                Map<String, Object> upload = cloudinaryImageService.upload(coverImage);
                String coverImageUrl = (String) upload.get("secure_url");
                System.out.println("✅ Cover image uploaded: " + coverImageUrl);
                university.setCoverImageUrl(coverImageUrl);
            } else {
                System.out.println("⏭️  Skipping cover image upload (null or empty)");
            }
        } catch (Exception e) {
            System.err.println("❌ Upload cover image failed: " + e.getMessage());
            throw new RuntimeException("Upload cover image failed", e);
        }

        UniversityModel savedUniversity = universityRepository.save(university);
        System.out.println("💾 University saved with ID: " + savedUniversity.getId());
        return universityMapper.toResponse(savedUniversity);
    }

    @Override
    public UniversityResponse getUniversityById(Integer id) {
        UniversityModel university = universityRepository.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "University not found with id: " + id));
        return universityMapper.toResponse(university);
    }

    @Override
    public UniversityResponse getUniversityBySlug(String slug) {
        UniversityModel university = universityRepository.findBySlug(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "University not found with slug: " + slug));
        return universityMapper.toResponse(university);
    }

    @Override
    public List<UniversityResponse> getAllUniversities() {
        return universityRepository.findAll().stream()
                .map(universityMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<UniversityResponse> searchUniversities(String keyword) {
        return universityRepository.findByNameContainingIgnoreCase(keyword).stream()
                .map(universityMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UniversityResponse updateUniversity(Integer id, UniversityRequest request, MultipartFile logo,
            MultipartFile coverImage) {
        UniversityModel university = universityRepository.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "University not found with id: " + id));

        universityMapper.updateModel(request, university);
        university.setUpdatedAt(LocalDateTime.now());

        // Upload logo (optional)
        try {
            if (logo != null && !logo.isEmpty()) {
                Map<String, Object> upload = cloudinaryImageService.upload(logo);
                String logoUrl = (String) upload.get("secure_url");
                university.setLogoUrl(logoUrl);
            }
        } catch (Exception e) {
            throw new RuntimeException("Upload logo failed", e);
        }

        // Upload cover image (optional)
        try {
            if (coverImage != null && !coverImage.isEmpty()) {
                Map<String, Object> upload = cloudinaryImageService.upload(coverImage);
                String coverImageUrl = (String) upload.get("secure_url");
                university.setCoverImageUrl(coverImageUrl);
            }
        } catch (Exception e) {
            throw new RuntimeException("Upload cover image failed", e);
        }

        UniversityModel updatedUniversity = universityRepository.save(university);
        return universityMapper.toResponse(updatedUniversity);
    }

    @Override
    @Transactional
    public void deleteUniversity(Integer id) {
        if (!universityRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "University not found with id: " + id);
        }
        universityRepository.deleteById(id);
    }
}
