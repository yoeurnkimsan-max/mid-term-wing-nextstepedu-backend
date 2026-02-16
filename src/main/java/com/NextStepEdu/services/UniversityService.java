package com.NextStepEdu.services;

import com.NextStepEdu.dto.requests.UniversityRequest;
import com.NextStepEdu.dto.responses.UniversityResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UniversityService {
    UniversityResponse createUniversity(String name ,
                                        String slug  ,
                                        String description,
                                        String country,
                                        String city,
                                        String officialWebsite,
                                        String status,
                                        MultipartFile logo,
                                        MultipartFile coverImage);

    UniversityResponse getUniversityById(Integer id);

    UniversityResponse getUniversityBySlug(String slug);

    List<UniversityResponse> getAllUniversities();

    List<UniversityResponse> searchUniversities(String keyword);

    UniversityResponse updateUniversity(Integer id, UniversityRequest request, MultipartFile logo,
            MultipartFile coverImage);

    void deleteUniversity(Integer id);
}
