package com.NextStepEdu.services.impl;

import com.NextStepEdu.dto.requests.LoginRequest;
import com.NextStepEdu.dto.requests.RefreshTokenRequest;
import com.NextStepEdu.dto.responses.AuthResponse;
import com.NextStepEdu.mappers.UserMapper;
import com.NextStepEdu.models.AccountStatus;
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
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
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
    private final JwtAuthenticationProvider jwtAuthenticationProvider;


    @Override
    public void register(String email, String password, String firstname, String lastname, String phone, MultipartFile image) {
        if (userRepository.existsAllByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }

        // Create User
        UserModel user = new UserModel();
        user.setEmail(email);
        user.setStatus(AccountStatus.ACTIVE);
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
        // ✅ 1) If email doesn't exist -> return 404
        boolean emailExists = userRepository.existsAllByEmail(loginRequest.email());
        if (!emailExists) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User Email not found");
        }

        // ✅ 2) If email exists but password wrong -> return 401
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.email(),
                            loginRequest.password()
                    )
            );
        } catch (BadCredentialsException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        }

        // ✅ 3) Build scope/role string
        String scope = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .filter(role -> role.startsWith("ROLE_"))
                .map(role -> role.replace("ROLE_", ""))
                .collect(Collectors.joining(" "));

        Instant now = Instant.now();

        // ✅ 4) Access Token claims
        JwtClaimsSet accessClaims = JwtClaimsSet.builder()
                .id(authentication.getName())
                .subject(authentication.getName())
                .issuer("taskflow-api")
                .issuedAt(now)
                .expiresAt(now.plus(10, ChronoUnit.MINUTES))
                .audience(List.of("NextJs", "Android", "IOS"))
                .claim("role", scope)
                .claim("scope", scope)
                .build();

        // ✅ 5) Refresh Token claims
        JwtClaimsSet refreshClaims = JwtClaimsSet.builder()
                .id(authentication.getName())
                .subject(authentication.getName())
                .issuer("taskflow-api")
                .issuedAt(now)
                .expiresAt(now.plus(7, ChronoUnit.DAYS))
                .audience(List.of("NextJs", "Android", "IOS"))
                .claim("role", scope)
                .claim("scope", scope)
                .build();

        // ✅ 6) Encode tokens
        String accessToken = accessTokenJwtEncoder
                .encode(JwtEncoderParameters.from(accessClaims))
                .getTokenValue();

        String refreshToken = refreshTokenEncoder
                .encode(JwtEncoderParameters.from(refreshClaims))
                .getTokenValue();

        // ✅ 7) Return response
        return AuthResponse.builder()
                .tokenType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String incomingRefreshToken = refreshTokenRequest.refreshToken();

        Authentication auth = new BearerTokenAuthenticationToken(incomingRefreshToken);
        auth = jwtAuthenticationProvider.authenticate(auth);

        Jwt refreshJwt = (Jwt) auth.getPrincipal();
        Instant now = Instant.now();


        String role = refreshJwt.getClaimAsString("role");
        String scope = refreshJwt.getClaimAsString("scope");


        if (role == null) role = scope;
        if (scope == null) scope = role;


        JwtClaimsSet accessClaims = JwtClaimsSet.builder()
                .id(refreshJwt.getId())
                .subject(refreshJwt.getSubject())
                .issuer("taskflow-api")
                .issuedAt(now)
                .expiresAt(now.plus(10, ChronoUnit.MINUTES))
                .audience(refreshJwt.getAudience() != null ? refreshJwt.getAudience() : List.of("NextJs"))
                .claim("role", role)
                .claim("scope", scope)
                .build();

        String newAccessToken = accessTokenJwtEncoder
                .encode(JwtEncoderParameters.from(accessClaims))
                .getTokenValue();

        String newRefreshToken = incomingRefreshToken;
        Instant refreshExpiresAt = refreshJwt.getExpiresAt();

        if (refreshExpiresAt != null) {
            long remainingDays = Duration.between(now, refreshExpiresAt).toDays();
            if (remainingDays < 1) {
                JwtClaimsSet refreshClaims = JwtClaimsSet.builder()
                        .id(refreshJwt.getId())
                        .subject(refreshJwt.getSubject())
                        .issuer("taskflow-api")
                        .issuedAt(now)
                        .expiresAt(now.plus(7, ChronoUnit.DAYS))
                        .audience(refreshJwt.getAudience() != null ? refreshJwt.getAudience() : List.of("NextJs"))
                        .claim("role", role)
                        .claim("scope", scope)
                        .build();

                newRefreshToken = refreshTokenEncoder
                        .encode(JwtEncoderParameters.from(refreshClaims))
                        .getTokenValue();
            }
        }

        return AuthResponse.builder()
                .tokenType("Bearer")
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();

    }


}
