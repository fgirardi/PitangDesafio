package com.pitange.usuariodecarros.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.token.TokenService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.pitange.usuariodecarros.dto.AuthenticationDTO;
import com.pitange.usuariodecarros.dto.LoginResponseDTO;
import com.pitange.usuariodecarros.dto.ClientDTO;
import com.pitange.usuariodecarros.exception.UserCreationException;
import com.pitange.usuariodecarros.service.ClientService;
import com.pitange.usuariodecarros.service.JWTTokenProvider;
import com.pitange.usuariodecarros.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

	private final ClientService clientService;
	
	private final AuthenticationManager authenticationManager;
	
	private final JWTTokenProvider tokenProvider;
	
	@Autowired
	public ClientController(ClientService clientService, AuthenticationManager authenticationManager, JWTTokenProvider tokenProvider) {
		this.clientService = clientService;
		this.authenticationManager = authenticationManager;
		this.tokenProvider = tokenProvider;
	}

	@GetMapping
	public ResponseEntity<List<ClientDTO>> findAll() {
		
		List<ClientDTO> personDTOList = clientService.findAll();		
		return ResponseEntity.status(HttpStatus.OK).body(personDTOList);
	}
	
	@GetMapping("{id}")
	public ClientDTO findById(@PathVariable Long id) {
		return clientService.findById(id)
							.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}
	
	 @PostMapping
	 @ResponseStatus(HttpStatus.CREATED)
	 public ClientDTO save(@RequestBody @Valid ClientDTO personDTO) {
		 return clientService.save(personDTO).orElseThrow(() -> new UserCreationException("Failed to create user"));
	 }
	
	@DeleteMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteById(@PathVariable Long id) {
		
		if (clientService.deleteById(id).isEmpty()) {
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
	    }
		
	}
	
	@PutMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateId(@PathVariable Long id, @RequestBody @Valid ClientDTO personDTO) {
		
		if (clientService.updateById(id, personDTO).isEmpty()) {
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
	    }
		
	}
	
	@GetMapping("/findListByUsername")
	public List<ClientDTO> findListByUsername(@RequestParam(value = "clientName") String userName) {
		
		return clientService.findListByUsername(userName)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}
	
}
