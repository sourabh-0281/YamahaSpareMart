package com.yamaha.repo;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yamaha.entities.Cart;
import com.yamaha.entities.User;

public interface CartRepo extends JpaRepository<Cart, Integer> {
      
	 Set<Cart> findByUser(User u); 
}
