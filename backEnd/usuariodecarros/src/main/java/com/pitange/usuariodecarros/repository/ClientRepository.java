package com.pitange.usuariodecarros.repository;

import org.springframework.stereotype.Repository;

import com.pitange.usuariodecarros.entities.Client;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
	
	List<Client> findListByName(String name);
	
	Optional<Client> findByName(String name);
	
	Optional<Client> findByEmail(String name);
	
}
