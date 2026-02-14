package com.NextStepEdu.services.impl;

import com.NextStepEdu.dto.requests.LoginRequest;
import com.NextStepEdu.dto.responses.AuthResponse;
import com.NextStepEdu.mappers.UserMapper;
import com.NextStepEdu.models.RoleModel;
import com.NextStepEdu.models.UserModel;
import com.NextStepEdu.models.UserProfileModel;
import com.NextStepEdu.repositories.RoleRepository;
import com.NextStepEdu.repositories.UserRepository;
import com.NextStepEdu.services.AuthService;
import com.NextStepEdu.services.CloudinaryImageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final CloudinaryImageService cloudinaryImageService;

    private final JwtEncoder accessTokenJwtEncoder;
    private final JwtEncoder refreshTokenEncoder;

    private final AuthenticationManager authenticationManager;


    @Override
    public void register(String email, String password, String firstname, String lastname, String phone, MultipartFile image) {
        if (userRepository.existsAllByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }

        // Create User
        UserModel user = new UserModel();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        RoleModel roleModel = roleRepository.findRoleUser();
        user.setRoles(List.of(roleModel));

        // Upload image (optional)
        String imageUrl = null;
        try {
            if (image != null && !image.isEmpty()) {
                Map<String, Object> upload = cloudinaryImageService.upload(image);
                imageUrl = (String) upload.get("secure_url");
            }
        } catch (Exception e) {
            throw new RuntimeException("Upload image failed", e);
        }

        // Create Profile
        UserProfileModel profile = new UserProfileModel();
        profile.setFirstname(firstname);
        profile.setLastname(lastname);
        profile.setPhone(phone);
        profile.setImage(imageUrl);

        // Link both sides
        profile.setUser(user);
        user.setProfile(profile);

        // Save once
        userRepository.save(user);
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.email(),
                        loginRequest.password()));

        String scope = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .filter(role -> role.startsWith("ROLE_"))
                // Add this line to strip the prefix:
                .map(role -> role.replace("ROLE_", ""))
                .collect(Collectors.joining(" "));


        Instant now = Instant.now();

        // Access Token
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .id(authentication.getName())
                .subject(authentication.getName())
                .issuer("taskflow-api")
                .issuedAt(now)
                .expiresAt(now.plus(10, ChronoUnit.MINUTES)) // ✅ Adjust expiration
                .audience(List.of("NextJs", "Android", "IOS"))
                .claim("role", scope)
                .claim("scope", scope)
                .build();

        // Refresh Token
        JwtClaimsSet jwtRefreshClaimsSet = JwtClaimsSet.builder()
                .id(authentication.getName())
                .subject(authentication.getName())
                .issuer("taskflow-api")
                .issuedAt(now)
                .expiresAt(now.plus(7, ChronoUnit.DAYS))
                .audience(List.of("NextJs", "Android", "IOS"))
                .claim("role", scope)
                .claim("scope", scope)
                .build();

        String accessToken = accessTokenJwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet))
                .getTokenValue();
        String refreshToken = refreshTokenEncoder.encode(JwtEncoderParameters.from(jwtRefreshClaimsSet))
                .getTokenValue();

        return AuthResponse.builder()
                .tokenType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

}
