package com.yamaha.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.yamaha.entities.User;
import com.yamaha.repo.UserRepo;

public class Userdetailserviceimplementation implements UserDetailsService{

	@Autowired
	UserRepo userrepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User userByName = userrepo.findByEmail(username);
	
		if(userByName==null) {
			throw new UsernameNotFoundException("no user found");
		}
		
		Userdetails u=new 	Userdetails(userByName); 
		return u ;
	}

}
