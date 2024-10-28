package com.yamaha.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.yamaha.entities.Products;
import com.yamaha.repo.ProductRepo;

@RestController
public class SearchController {

	@Autowired
	private ProductRepo productrepo;
	
	//search handler
	@GetMapping("/search/{query}")
	public  ResponseEntity<?> search(@PathVariable String query ){
		List<Products> byProductNameContaining = productrepo.findByProductNameContaining(query);

		return ResponseEntity.ok(byProductNameContaining);
	}
}
