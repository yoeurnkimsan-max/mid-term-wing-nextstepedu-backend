package com.NextStepEdu.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    @Bean
    JwtAuthenticationProvider configJwtAuthenticationProvider(@Qualifier("refreshTokenJwtDecoder") JwtDecoder refreshTokenJwtDecoder) {
        JwtAuthenticationProvider auth = new JwtAuthenticationProvider(refreshTokenJwtDecoder);
        return auth;
    }

    @Bean
    DaoAuthenticationProvider configDaoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    @Order(1)
    SecurityFilterChain publicSecurity(HttpSecurity http) throws Exception {
        // Public endpoints should not be processed by the JWT resource server.
        // This avoids 401s when a client accidentally sends an invalid/expired token.
        http.securityMatcher("/api/v1/auth/**", "/api/v1/cloud/upload/**");
        http.authorizeHttpRequests(endpoint -> endpoint.anyRequest().permitAll());
        http.csrf(csrf -> csrf.disable());
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    @Bean
    @Order(2)
    SecurityFilterChain configureApiSecurity(HttpSecurity http, @Qualifier("accessTokenjwtDecoder") JwtDecoder jwtDecoder) throws Exception {
        DefaultBearerTokenResolver delegateBearerTokenResolver = new DefaultBearerTokenResolver();
        BearerTokenResolver safeBearerTokenResolver = (HttpServletRequest request) -> {
            String path = request.getRequestURI();
            if (path.startsWith("/api/v1/auth/") || path.startsWith("/api/v1/cloud/upload/")) {
                return null;
            }
            return delegateBearerTokenResolver.resolve(request);
        };

        // Endpoint Security config
        http.authorizeHttpRequests(endpoint -> endpoint
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/api/v1/cloud/upload/**").permitAll()
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/faculties/**").hasAnyAuthority("SCOPE_USER", "SCOPE_ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/v1/faculties/**").hasAuthority("SCOPE_ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/faculties/**").hasAuthority("SCOPE_ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/faculties/**").hasAuthority("SCOPE_ADMIN")
                .requestMatchers("/api/v1/profile/**").authenticated()
                .anyRequest().authenticated());


        http.oauth2ResourceServer(oauth2 -> oauth2
                .bearerTokenResolver(safeBearerTokenResolver)
                .jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder)));
        http.csrf(csrf -> csrf.disable());

        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

}
