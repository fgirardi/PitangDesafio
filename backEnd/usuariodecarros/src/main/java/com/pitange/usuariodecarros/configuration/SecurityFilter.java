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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

@Component
public class SecurityFilter extends OncePerRequestFilter {

	@Autowired
	JWTTokenProvider tokenService;

	@Autowired
	UserRepository userRepository;
	
	private String recoverJWTToken(HttpServletRequest request) {

		var authHeader = request.getHeader("Authorization");
		/*
		 *  O Bearer Token é um tipo de token de autenticação utilizado em sistemas que implementam o protocolo OAuth 2.0. 
		 *  Ele é chamado de "Bearer" porque qualquer sistema que possua esse token pode "portá-lo" (em inglês, "bear") para acessar recursos protegidos, sem a necessidade de mais credenciais.
		 */

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return null;
		}

		return authHeader.replace("Bearer ", ""); // So quero saber do token.
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, 
			                        HttpServletResponse response, 
			                        FilterChain filterChain) throws ServletException, IOException {

		var token = this.recoverJWTToken(request);

		if (token != null) {
			var email = tokenService.getSubjectFromToken(token);
			var userOpt = userRepository.findByEmail(email);
			
			if (userOpt.isPresent()) {
				UserDetails userDetails = userOpt.get();
				var authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), 
																			null,  // Senha não é necessária aqui
																			 userDetails.getAuthorities());
				
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
		filterChain.doFilter(request, response); // Chama o proximo filtro.
	}

	
}
