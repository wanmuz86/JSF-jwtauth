package com.example.jwtauth.configs;


import java.util.List;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
   private final AuthenticationProvider authenticationProvider;
   private final JwtAuthenticationFilter jwtAuthenticationFilter;


   public SecurityConfiguration(
       JwtAuthenticationFilter jwtAuthenticationFilter,
       AuthenticationProvider authenticationProvider
   ) {
       this.authenticationProvider = authenticationProvider;
       this.jwtAuthenticationFilter = jwtAuthenticationFilter;
   }

   // In line 40-43 we allow all requests that starts with /auth/ -> /auth/login , /auth/register
   // In line 45-47, we allow all requests that starts with /h2-console/ -> Your database access
   // In line 48- 49, FOr other request, I will need it to be authenticated
  // In line 53-54 We indicate that we use Stateless session authentication (JWT) /lab 35 this line is not there
   //57-58 - We add JWT configuration (Will be created later
   @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
       http.csrf()
               .disable()
               .authorizeHttpRequests()
               .requestMatchers("/auth/**")
               .permitAll()
               .requestMatchers("/h2-console/**")
               .permitAll()
               .anyRequest()
               .authenticated()
               .and()
               .headers().frameOptions().disable() // related to h2-console
               .and()
               .sessionManagement()
               .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
               .and()
               .authenticationProvider(authenticationProvider)
               .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);


       return http.build();
   }

   // For testing purposes, we set up Cors to allow it to be tested from local (From POSTMAN)

   @Bean
    CorsConfigurationSource corsConfigurationSource() {
       CorsConfiguration configuration = new CorsConfiguration();


       configuration.setAllowedOrigins(List.of("http://localhost:8080"));
       configuration.setAllowedMethods(List.of("GET","POST"));
       configuration.setAllowedHeaders(List.of("Authorization","Content-Type"));


       UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();


       source.registerCorsConfiguration("/**",configuration);


       return source;
   }
}
