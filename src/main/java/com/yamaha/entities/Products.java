package com.yamaha.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Products {
/*	1	This is filter	rxfilter.png	Rx100 Air Filter	170	
		This is filter	FzV1filter.png	FZ V1 Air Filter	120	
		This is filter	FzV2filter.png	FZ V2 Air Filter	200	
		This is filter	FzV3filter.png	FZ V3 Air Filter	300	
		This is filter	R15filter.png	R15 Air Filter400	
		This is filter	R15V3filter.png	R15 V3 Air Filter.png	270	
		This is filter	Rayfilter.png	Ray Alfa Air Filter	309	
		This is filter	Facinofilter.png	Facino Air FIlter	290	*/
	@Id
	private int productid;
	private String productName;
	private String productDescription;
	private String productImg; 
	private String productPrice;
	
	@OneToOne(mappedBy = "products")
	@JsonIgnore
	private Cart cart;
	
	@ManyToMany
	@JsonIgnore
	private List<User> user;
}
