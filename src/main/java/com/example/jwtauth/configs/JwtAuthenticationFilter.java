package com.example.jwtauth.configs;



import java.io.IOException;


import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;


import com.example.jwtauth.service.JwtService;




import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// If inside SecurityConfiguration user comes into the (authenticated) scenario
// They will come to the next check, which JWTAuthentication check

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
   private final HandlerExceptionResolver handlerExceptionResolver;


   private final JwtService jwtService;
   private final UserDetailsService userDetailsService;


   public JwtAuthenticationFilter(
       JwtService jwtService,
       UserDetailsService userDetailsService,
       HandlerExceptionResolver handlerExceptionResolver
   ) {
       this.jwtService = jwtService;
       this.userDetailsService = userDetailsService;
       this.handlerExceptionResolver = handlerExceptionResolver;
   }


   @Override
   protected void doFilterInternal(
       @NonNull HttpServletRequest request,
       @NonNull HttpServletResponse response,
       @NonNull FilterChain filterChain
   ) throws ServletException, IOException {
	   
	   // This filter will get the Header and look for "Authorization" key
       final String authHeader = request.getHeader("Authorization");

       // If nothing is sent, or it does not start with Bearer -> Reject it (return)
       if (authHeader == null || !authHeader.startsWith("Bearer ")) {
           filterChain.doFilter(request, response);
           return;
       }
       
       // I will get the JWT (7 onwards)
       // Get the username (which is passed when we create inside JwtService)

       try {
           final String jwt = authHeader.substring(7);
           final String userEmail = jwtService.extractUsername(jwt);


           Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

           // Use Sring Security to check if the username is valid, If not, I will return
           // It will get the username, check if it is valid, if user exits -> go to next part or reject

           if (userEmail != null && authentication == null) {
               UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

               // Check if the token is valid -> authority (is it created by us? expiry date (still valid?)
               // If ok, then give the response to user
               // (buildDetails.request)
               // setAuthentication(authToken)
               if (jwtService.isTokenValid(jwt, userDetails)) {
                   UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                           userDetails,
                           null,
                           userDetails.getAuthorities()
                   );


                   authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                   SecurityContextHolder.getContext().setAuthentication(authToken);
               }
           }

           // bring it to the next part which is pass to the controller
           filterChain.doFilter(request, response);
       } catch (Exception exception) {
           handlerExceptionResolver.resolveException(request, response, null, exception);
       }
   }
}
