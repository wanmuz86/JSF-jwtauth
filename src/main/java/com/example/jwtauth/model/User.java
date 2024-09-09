package com.example.jwtauth.model;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

// We add implements UserDetails to indicate that we will use Spring Security 
// As authentication (and Authorization) method, and this class, will be used as 
// "User" class for Spring Security
// Since we add implements, there are certain methods needed as part of "contract"

@Entity
@Table(name = "users")
public class User  implements UserDetails{
	
	// Fields used for the application/system

	 	@Id
	   @GeneratedValue(strategy = GenerationType.AUTO)
	   @Column(nullable = false)
	   private Integer id;


	   @Column(nullable = false)
	   private String fullName;


	   @Column(unique = true, length = 100, nullable = false)
	   private String email;


	   @Column(nullable = false)
	   private String password;


	   @CreationTimestamp
	   @Column(updatable = false, name = "created_at")
	   private Date createdAt;


	   @UpdateTimestamp
	   @Column(name = "updated_at")
	   private Date updatedAt;
	   
	   
	   /// This is for Spring Security configuration

	   
// Which properties store the Authorization for the User List<Role>
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		return List.of();
	}


	// Which property should Spring look for password
	@Override
	public String getPassword() {
		
		return password;
	}

	// Which property should spring look for username
	// It might be username, since we use email based authentication
	
	@Override
	public String getUsername() {

		return email;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getFullName() {
		return fullName;
	}


	public void setFullName(String fullName) {
		this.fullName = fullName;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public Date getCreatedAt() {
		return createdAt;
	}


	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}


	public Date getUpdatedAt() {
		return updatedAt;
	}


	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}


	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
