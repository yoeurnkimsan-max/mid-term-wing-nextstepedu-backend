package com.NextStepEdu.services;

import com.NextStepEdu.dto.requests.UniversityContactRequest;
import com.NextStepEdu.dto.responses.UniversityContactResponse;

import java.util.List;

public interface UniversityContactService {
    UniversityContactResponse createContact(UniversityContactRequest request);

    UniversityContactResponse getContactById(Integer id);

    List<UniversityContactResponse> getAllContacts();

    List<UniversityContactResponse> getContactsByUniversityId(Integer universityId);

    UniversityContactResponse updateContact(Integer id, UniversityContactRequest request);

    void deleteContact(Integer id);
}
