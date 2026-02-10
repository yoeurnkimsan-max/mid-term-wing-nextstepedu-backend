package com.NextStepEdu.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    @Bean
    JwtAuthenticationProvider configJwtAuthenticationProvider(
            @Qualifier("refreshTokenJwtDecoder") JwtDecoder refreshTokenJwtDecoder) {
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
    SecurityFilterChain configureApiSecurity(HttpSecurity http,
            @Qualifier("accessTokenjwtDecoder") JwtDecoder jwtDecoder) throws Exception {

        // Endpoint Security config
        http.authorizeHttpRequests(endpoint -> endpoint
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/api/v1/cloud/upload/**").permitAll()

                .requestMatchers("/api/v1/universities/**").permitAll()
                .requestMatchers("/api/v1/university-contacts/**").permitAll()

                .requestMatchers("/api/v1/faculties/**").permitAll()

                .requestMatchers("/api/v1/profile/**").authenticated()
                .anyRequest().authenticated());

        http.oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder)));
        http.csrf(csrf -> csrf.disable());

        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

}
