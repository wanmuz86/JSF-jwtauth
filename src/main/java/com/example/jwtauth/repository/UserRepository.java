package com.example.jwtauth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jwtauth.model.User;

public interface UserRepository extends JpaRepository<User,Integer> {
	
	Optional<User> findByEmail(String email);

}
