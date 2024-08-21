package com.pitange.usuariodecarros.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.pitange.usuariodecarros.dto.AuthenticationDTO;
import com.pitange.usuariodecarros.dto.LoginResponseDTO;
import com.pitange.usuariodecarros.dto.UserDTO;
import com.pitange.usuariodecarros.exception.UserCreationException;
import com.pitange.usuariodecarros.service.JWTTokenProvider;
import com.pitange.usuariodecarros.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private final UserService personService;
	
	private final AuthenticationManager authenticationManager;
	
	private final JWTTokenProvider tokenProvider;
	
	@Autowired
	public UserController(UserService personService, AuthenticationManager authenticationManager, JWTTokenProvider tokenProvider) {
		this.personService = personService;
		this.authenticationManager = authenticationManager;
		this.tokenProvider = tokenProvider;
	}

	@GetMapping
	public ResponseEntity<List<UserDTO>> findAll() {
		
		List<UserDTO> personDTOList = personService.findAll();		
		return ResponseEntity.status(HttpStatus.OK).body(personDTOList);
	}
	
	@GetMapping("{id}")
	public UserDTO findById(@PathVariable Long id) {
		return personService.findById(id)
							.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}
	
	 @PostMapping
	 @ResponseStatus(HttpStatus.CREATED)
	 public UserDTO save(@RequestBody @Valid UserDTO userDTO) {
		 return personService.save(userDTO).orElseThrow(() -> new UserCreationException("Failed to create user"));
	 }
	
	@DeleteMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteById(@PathVariable Long id) {
		
		if (personService.deleteById(id).isEmpty()) {
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
	    }
		
	}
	
	@PutMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateId(@PathVariable Long id, @RequestBody @Valid UserDTO userDTO) {
		
		if (personService.updateById(id, userDTO).isEmpty()) {
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
	    }
		
	}
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid UserDTO userDTO) {
		
		var usernamePassword = new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword());
		var auth = this.authenticationManager.authenticate(usernamePassword);
		
		var token = tokenProvider.generateAccessToken((AuthenticationDTO) auth.getPrincipal());
		
		return ResponseEntity.ok(new LoginResponseDTO(token));
		
	}
}
