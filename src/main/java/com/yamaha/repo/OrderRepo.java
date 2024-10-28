package com.yamaha.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yamaha.entities.Orders;

public interface OrderRepo extends JpaRepository<Orders, Integer>{

	boolean existsByTransactionid(String id);
}
