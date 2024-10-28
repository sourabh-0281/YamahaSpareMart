package com.yamaha.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.yamaha.entities.User;

public class Userdetails implements UserDetails{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private User user;
	
	public Userdetails(User u) {
		user=u;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		SimpleGrantedAuthority s=new SimpleGrantedAuthority(user.getRole());
		return List.of(s);
	}

	@Override
	public String getPassword() {	
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

}
