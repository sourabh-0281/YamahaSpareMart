package com.yamaha.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userid;
	
	@NotNull(message =" please enter your name")
	@Size(min = 2,max = 20,message = "min 2 and max 20 characters are allowed")
	private String name;
	
	@Email(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", message = "Invalid Email")
	private String email;
	
	@NotEmpty(message ="please enter password")
	private String password;
	
	@NotEmpty(message ="please enter your address")
	@Column(length = 100)
	private String address;
	
	@NotNull(message = "enter your phone number")									 
	@Size(min = 10,max = 10,message = "please enter valid phone number")
	private String phoneno;
	
	private String role;
	
	private String img;
	
	@AssertTrue(message = "Agree Terms and Conditions")
	private boolean agreement;
	
	@OneToMany(mappedBy = "user")
	private List<Cart> cart=new ArrayList<>();
	
	@ManyToMany(mappedBy = "user")
	private List<Products> products=new ArrayList<>();
	
	@OneToMany(mappedBy = "user")
	private List<Orders> orders=new ArrayList<>();

	
}
