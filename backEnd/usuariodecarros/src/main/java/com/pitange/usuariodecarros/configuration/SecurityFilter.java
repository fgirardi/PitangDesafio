package com.pitange.usuariodecarros.configuration;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.pitange.usuariodecarros.repository.UserRepository;
import com.pitange.usuariodecarros.service.JWTTokenProvider;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

	@Autowired
	JWTTokenProvider tokenService;

	@Autowired
	UserRepository userRepository;
	
	private String recoverJWTToken(HttpServletRequest request) {

		var authHeader = request.getHeader("Authorization");

		if (authHeader == null || !authHeader.startsWith("Bearer "))
			return null;

		return authHeader.replace("Bearer ", ""); // So quero saber do token.
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		var token = this.recoverJWTToken(request);

		if (token != null) {
			var email = tokenService.getSubjectFromToken(token);
			var userOpt = userRepository.findByEmail(email);
			if (userOpt.isPresent()) {
				var authentication = new UsernamePasswordAuthenticationToken(userOpt.get(), null,userOpt.get().getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
		filterChain.doFilter(request, response); // Chama o proximo filtro.
	}

	
}
