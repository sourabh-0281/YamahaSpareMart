package com.yamaha.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yamaha.entities.User;

public interface UserRepo extends JpaRepository<User, Integer>{

	User findByEmail(String name);
}
