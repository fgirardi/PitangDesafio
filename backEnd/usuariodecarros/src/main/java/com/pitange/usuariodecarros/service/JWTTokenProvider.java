package com.pitange.usuariodecarros.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pitange.usuariodecarros.dto.UserDTO;
import com.pitange.usuariodecarros.properties.JwtProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

@Service
public class JWTTokenProvider {
	
	private final JwtProperties jwtProperties;
	  
	private static final Logger logger = LoggerFactory.getLogger(JWTTokenProvider.class);

	@Autowired
    public JWTTokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }
	
    private Instant genAccessExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
    
    private SecretKey getPublicSigningKey() {
        byte[] decodedKey = Decoders.BASE64.decode(this.jwtProperties.getSecretKey());
        return new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256");
    }

    public String generateAccessToken(UserDTO user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.jwtProperties.getSecretKey());
            return JWT.create()
                    .withSubject(user.getEmail())
                    .withClaim("email", user.getEmail())
                    .withExpiresAt(Date.from(this.genAccessExpirationDate()))  // Conversão de Instant para Date
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new JWTCreationException("Error while generating token", exception);
        }
    }

    public String getSubjectFromToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.jwtProperties.getSecretKey());
            return JWT.require(algorithm)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new JWTVerificationException("Error while validating token", exception);
        }
    }

    public Claims getAllClaims(String token) {
       
        return Jwts.parser()
                .verifyWith(this.getPublicSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
