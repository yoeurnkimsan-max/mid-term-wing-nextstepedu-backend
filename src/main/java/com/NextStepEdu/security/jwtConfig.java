package com.NextStepEdu.security;


import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

@Component
public class jwtConfig {

    @Bean(name = "accessTokenPair")
    KeyPair accessTokenPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }



    @Bean("accessTokenRSAKey")
    RSAKey accessTokenRSAKey(@Qualifier("accessTokenPair") KeyPair keyPair)  {
        return new RSAKey.Builder((RSAPublicKey)
                keyPair.getPublic())
                .privateKey(keyPair.getPrivate())
                .keyID(UUID.randomUUID().toString())
                .build();

    }


    @Bean(name = "accessTokenjwtDecoder")
    JwtDecoder accessTokenjwtDecoder(@Qualifier("accessTokenRSAKey") RSAKey rsaKey) throws JOSEException {
        return NimbusJwtDecoder
                .withPublicKey(rsaKey.toRSAPublicKey())
                .build();
    }


    @Bean("accessTokenJWKSource")
    JWKSource<SecurityContext> accessTokenJWKSource(@Qualifier("accessTokenRSAKey") RSAKey rsaKey) {
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet) ;

    }


    @Bean(name = "accessTokenJwtEncoder")
    JwtEncoder accessTokenJwtEncoder(@Qualifier("accessTokenJWKSource") JWKSource<SecurityContext> jwkSource) {
        return  new NimbusJwtEncoder(jwkSource);
    }







    //------------------Refresh Token-----------------------
    @Bean(name = "refreshTokenPair")
    KeyPair refreshTokenPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    @Bean(name = "refreshTokenRSAKey")
    RSAKey refreshTokenRSAKey(@Qualifier( "refreshTokenPair") KeyPair keyPair)  {
        return new RSAKey.Builder((RSAPublicKey) keyPair.getPublic())
                .privateKey(keyPair.getPrivate())
                .keyID(UUID.randomUUID().toString())
                .build();
    }

    @Bean("refreshTokenJwtDecoder")
    JwtDecoder refreshTokenJwtDecoder(@Qualifier("refreshTokenRSAKey") RSAKey rsaKey) throws JOSEException {
        return  NimbusJwtDecoder
                .withPublicKey(rsaKey.toRSAPublicKey())
                .build();
    }

    @Bean("refreshTokenJWKSource")
    JWKSource<SecurityContext> refreshTokenJWKSource(@Qualifier("refreshTokenRSAKey") RSAKey rsaKey) {
        JWKSet jwkSet = new JWKSet(rsaKey);
        return ((jwkSelector, securityContext) -> jwkSelector.select(jwkSet));
    }

    @Bean(name = "refreshTokenEncoder")
    JwtEncoder refreshTokenEncoder(@Qualifier("refreshTokenJWKSource") JWKSource<SecurityContext> jwkSource) {
        return  new NimbusJwtEncoder(jwkSource);
    }
}
