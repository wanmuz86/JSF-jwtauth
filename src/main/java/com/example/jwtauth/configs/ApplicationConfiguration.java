package com.example.jwtauth.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.jwtauth.repository.UserRepository;

@Configuration
public class ApplicationConfiguration {
	
//	@Autowired
//	 private UserRepository userRepository;

   private final UserRepository userRepository;


   public ApplicationConfiguration(UserRepository userRepository) {
       this.userRepository = userRepository;
   }


   // We tell spring security How to retrieve the user from the Entity (using username)
   @Bean
   UserDetailsService userDetailsService() {
       return username -> userRepository.findByEmail(username)
               .orElseThrow(() -> new UsernameNotFoundException("User not found"));
   }


   // Create a BCryptPasswordEncoder bean that's later going to be used for password encryption
   // by UserService
   @Bean
   BCryptPasswordEncoder passwordEncoder() {
       return new BCryptPasswordEncoder();
   }


   @Bean
   public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
       return config.getAuthenticationManager();
   }


   // To set the strategy that we use for authentication which is username and password
   @Bean
   AuthenticationProvider authenticationProvider() {
       DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();


       authProvider.setUserDetailsService(userDetailsService());
       authProvider.setPasswordEncoder(passwordEncoder());


       return authProvider;
   }
}
