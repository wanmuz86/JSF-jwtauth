package com.example.jwtauth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.jwtauth.dtos.LoginResponse;
import com.example.jwtauth.dtos.LoginUserDto;
import com.example.jwtauth.dtos.RegisterUserDto;
import com.example.jwtauth.model.User;
import com.example.jwtauth.service.AuthenticationService;
import com.example.jwtauth.service.JwtService;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private AuthenticationService authenticationService;
	
	
	@PostMapping("/signup")
	public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto){
		User registeredUser = authenticationService.signUp(registerUserDto);
		return ResponseEntity.ok(registeredUser);
	}
	
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto){
		User authenticatedUser = authenticationService.authenticate(loginUserDto);
		String jwtToken = jwtService.generateToken(authenticatedUser);
		
		LoginResponse loginResponse = new LoginResponse();
		loginResponse.setExpiresIn(jwtService.getExpirationTime());
		loginResponse.setToken(jwtToken);
		return ResponseEntity.ok(loginResponse);
	}

}
