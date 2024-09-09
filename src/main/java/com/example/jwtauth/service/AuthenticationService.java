package com.example.jwtauth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.jwtauth.dtos.LoginUserDto;
import com.example.jwtauth.dtos.RegisterUserDto;
import com.example.jwtauth.model.User;
import com.example.jwtauth.repository.UserRepository;

@Service
public class AuthenticationService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private PasswordEncoder passwordEncoder; // BcryptPasswordEncoder
	
	
	public User signUp(RegisterUserDto input) {
		
		User user = new User();
		user.setFullName(input.getFullName());
		user.setEmail(input.getEmail());
		user.setPassword(passwordEncoder.encode(input.getPassword()));
		return userRepository.save(user);
		
	}
	
	public User authenticate(LoginUserDto input) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						input.getEmail(),
						input.getPassword()
						));
		return userRepository.findByEmail(input.getEmail()).orElseThrow();
	}

}
