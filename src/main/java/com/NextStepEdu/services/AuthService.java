package com.NextStepEdu.services;

import com.NextStepEdu.dto.requests.LoginRequest;
import com.NextStepEdu.dto.responses.AuthResponse;
import org.springframework.web.multipart.MultipartFile;

public interface AuthService {

    void register(String email, String password, String firstname, String lastname, String phone, MultipartFile image);
    AuthResponse login(LoginRequest loginRequest);
}
