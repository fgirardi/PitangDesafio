package com.pitange.usuariodecarros.repository;

import org.springframework.stereotype.Repository;

import com.pitange.usuariodecarros.entities.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	List<User> findListByFirstName(String firstName);
	
	UserDetails findByLogin(String login);
	
}
