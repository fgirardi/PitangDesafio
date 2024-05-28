package com.pitange.usuariodecarros.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import com.pitange.usuariodecarros.dto.AuthenticationDTO;
import com.pitange.usuariodecarros.entities.User;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;


@Service
public class TokenProvider {
	
	@Value("${security.jwt.token.secret-key}")
	private String JWT_SECRET;

	private Instant genAccessExpirationDate() {
		return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
	}

	
	public String generateAccessToken(AuthenticationDTO user) {
		try {
		      Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
		      return JWT.create()
		          .withSubject(user.login())
		          .withClaim("username", user.login())
		          .withExpiresAt(Date.from(this.genAccessExpirationDate()))  // Conversão de Instant para Date
		          .sign(algorithm);
		    } catch (JWTCreationException exception) {
		      throw new JWTCreationException("Error while generating token", exception);
		    }
	}

	public String validateToken(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET);
			return JWT.require(algorithm)
					.build()
					.verify(token)
					.getSubject();
		} catch (JWTVerificationException exception) {
			throw new JWTVerificationException("Error while validating token", exception);
		}
	}

}
