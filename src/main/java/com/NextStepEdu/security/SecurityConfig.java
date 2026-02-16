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
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.Customizer;

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
                .requestMatchers(HttpMethod.PATCH, "/api/v1/users/**").hasRole("ADMIN")


                .requestMatchers(HttpMethod.GET, "/api/v1/applicants", "/api/v1/applicants/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/applicants/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/v1/applicants/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/applicants/**").hasRole("ADMIN")



                .requestMatchers(HttpMethod.GET, "/api/v1/universities/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/universities/**").hasAnyRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/universities/**").hasAnyRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/universities/**").hasAnyRole("ADMIN")


                .requestMatchers(HttpMethod.GET, "/api/v1/faculties/**", "/api/v1/faculties").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/faculties/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/faculties/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/faculties/**").hasRole("ADMIN")

                .requestMatchers(HttpMethod.GET, "/api/v1/programs/**", "/api/v1/programs").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/programs/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/programs/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/programs/**").hasRole("ADMIN")

                .requestMatchers(HttpMethod.GET, "/api/v1/scholarship-contact/**", "/api/v1/scholarship-contact").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/scholarship-contact/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/scholarship-contact/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/scholarship-contact/**").hasRole("ADMIN")


                .requestMatchers(HttpMethod.GET, "/api/v1/scholarship/**", "/api/v1/scholarship").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/scholarship/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/v1/scholarship/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/scholarship/**").hasRole("ADMIN")


                .requestMatchers("/api/v1/profile/**").permitAll()
                        .anyRequest().authenticated());

        http.oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwtConfigurer -> jwtConfigurer
                        .decoder(jwtDecoder)
                        .jwtAuthenticationConverter(jwtAuthenticationConverter())));
        http.csrf(csrf -> csrf.disable());

        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.cors(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(java.util.List.of(
                "https://next-step-edu.vercel.app",
                "http://localhost:3000",
                "http://localhost:5173"
        ));
        config.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(java.util.List.of("Authorization", "Content-Type", "Accept", "Origin"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName("role");
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }



}
