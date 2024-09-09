package com.example.jwtauth.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.example.jwtauth.model.User;
import com.example.jwtauth.repository.UserRepository;


@Service
public class UserService {
	
	@Autowired
   private  UserRepository userRepository;


   public List<User> allUsers() {
       List<User> users = new ArrayList<>();


       userRepository.findAll().forEach(users::add);


       return users;
   }
}

