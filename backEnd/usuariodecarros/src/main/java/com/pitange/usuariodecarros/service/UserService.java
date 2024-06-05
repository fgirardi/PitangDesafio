package com.pitange.usuariodecarros.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.pitange.usuariodecarros.dto.UserDTO;
import com.pitange.usuariodecarros.entities.User;
import com.pitange.usuariodecarros.exception.DuplicateLoginException;
import com.pitange.usuariodecarros.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {
	
	private final UserRepository userRepository;
	
	@Autowired
	public UserService(UserRepository userRepository, UserDTO personDTO) {
		this.userRepository = userRepository;
	}
	
	public Optional<UserDTO> findById(Long id) {
		
		if (id == null) {
			throw new IllegalArgumentException("O id da pessoa deve ser informado para consultar");
		}
		
		Optional<User> personModelOpt = userRepository.findById(id);
		
		return personModelOpt.map(UserDTO::toDTO);
	
	}
	
	public List<UserDTO> findAll() {
		
		return userRepository.findAll()
                .stream()
                .map(UserDTO::toDTO)
                .collect(Collectors.toList());
	}
	//Aqui o consumo pode ser feito na tela de cadastro de usuarios ou na tela de registrar. (signUp)
	public Optional<UserDTO> save(UserDTO personDTO) {
		
		Optional<UserDetails> userDetailsOpt = userRepository.findByLogin(personDTO.getLogin());
		
		if (userDetailsOpt.isPresent()) {
			//Encontrou alguem e nao pode continuar o processo de save.
			throw new DuplicateLoginException("Login " + personDTO.getLogin() + " already exists.");
		}
		
		User userModel = UserDTO.toModel(personDTO);
		var encryptedPassword = new BCryptPasswordEncoder().encode(userModel.getPassword());
		userModel.setPassword(encryptedPassword);
		userModel = userRepository.save(userModel);
		
		return Optional.of(UserDTO.toDTO(userModel));
	}
	
	public Optional<UserDTO> deleteById(Long id) {
		
		if (id == null) {
			throw new IllegalArgumentException("O id da pessoa deve ser informado para excluir");
		}
		
		return userRepository.findById(id)
			        .map(person -> {
			        	userRepository.deleteById(person.getId());
			            return UserDTO.toDTO(person);
			        });
	}

	
	public Optional<UserDTO> updateById(Long id, UserDTO personDTO) {
		
		if (id == null) {
			throw new IllegalArgumentException("O id da pessoa deve ser informado para atualizar");
		}
		
		User personModel = UserDTO.toModel(personDTO);
		personModel.setId(id);

		return userRepository.findById(id)
		        .map(person -> {
		        	userRepository.save(personModel);
		            return UserDTO.toDTO(personModel);
		        });
	}
	
	public Optional<List<UserDTO>>findByFirstName(String firstName) {
		
		return Optional.of(userRepository.findListByFirstName(firstName)
										 .stream()
										 .map(UserDTO::toDTO)
										 .collect(Collectors.toList()));
	
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return this.userRepository.findByLogin(username).orElseThrow(() -> new UsernameNotFoundException("Usuario Nao encontrado"));
	}
}
