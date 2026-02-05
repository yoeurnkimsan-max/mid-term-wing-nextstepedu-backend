package com.NextStepEdu.services;

import com.NextStepEdu.dto.requests.LoginRequest;
import com.NextStepEdu.dto.requests.RegisterRequest;
import com.NextStepEdu.dto.responses.AuthResponse;
import com.NextStepEdu.dto.responses.UserResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AuthService {

    void register(String email, String password, String firstname, String lastname, String phone, MultipartFile image);

    AuthResponse login(LoginRequest loginRequest);

}
