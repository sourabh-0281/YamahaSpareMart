package com.yamaha.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.yamaha.entities.Products;

public interface ProductRepo extends JpaRepository<Products, Integer> {

	//also used for search
	List<Products> findByProductNameContaining(String name);
	List<Products> findByProductNameContainingOrProductNameContaining(String name,String name2);
	
}
