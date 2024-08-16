package com.pitange.usuariodecarros.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pitange.usuariodecarros.dto.ClientDTO;
import com.pitange.usuariodecarros.entities.Client;
import com.pitange.usuariodecarros.exception.DuplicateLoginException;
import com.pitange.usuariodecarros.repository.ClientRepository;


@Service
public class ClientService {
	
	private final ClientRepository clientRepository;
	
	@Autowired
	public ClientService(ClientRepository clientRepository, ClientDTO clientDTO) {
		this.clientRepository = clientRepository;
	}
	
	public Optional<ClientDTO> findById(Long id) {
		
		if (id == null) {
			throw new IllegalArgumentException("O id do cliente deve ser informado para consultar");
		}
		
		Optional<Client> personModelOpt = clientRepository.findById(id);
		
		return personModelOpt.map(ClientDTO::toDTO);
	
	}
	
	public List<ClientDTO> findAll() {
		
		return clientRepository.findAll()
                .stream()
                .map(ClientDTO::toDTO)
                .collect(Collectors.toList());
	}

	public Optional<ClientDTO> save(ClientDTO clientDTO) {
		
		Optional<Client> client = clientRepository.findByEmail(clientDTO.getEmail());
		
		if (client.isPresent()) {
			//Encontrou alguem e nao pode continuar o processo de save.
			throw new DuplicateLoginException("Email: " + clientDTO.getEmail() + " already exists.");
		}
		
		Client clientModel = ClientDTO.toModel(clientDTO);
		clientModel = clientRepository.save(clientModel);
		
		return Optional.of(ClientDTO.toDTO(clientModel));
	}
	
	public Optional<ClientDTO> deleteById(Long id) {
		
		if (id == null) {
			throw new IllegalArgumentException("O id do cliente deve ser informado para excluir");
		}
		
		return clientRepository.findById(id)
			        .map(client -> {
			        	clientRepository.deleteById(client.getId());
			            return ClientDTO.toDTO(client);
			        });
	}

	
	public Optional<ClientDTO> updateById(Long id, ClientDTO clientDTO) {
		
		if (id == null) {
			throw new IllegalArgumentException("O id do cliente deve ser informado para atualizar");
		}
		
		Client personModel = ClientDTO.toModel(clientDTO);
		personModel.setId(id);

		return clientRepository.findById(id)
		        .map(person -> {
		        	clientRepository.save(personModel);
		            return ClientDTO.toDTO(personModel);
		        });
	}
	
	public Optional<List<ClientDTO>>findListByUsername(String name) {
		
		return Optional.of(clientRepository.findListByName(name)
										 .stream()
										 .map(ClientDTO::toDTO)
										 .collect(Collectors.toList()));
	
	}

}
