package com.yamaha.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity	
public class SecurityCongif {

	@Bean
	public PasswordEncoder pe() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public UserDetailsService uds() {
		return new Userdetailserviceimplementation() ;
	}
	
	@Bean
	public DaoAuthenticationProvider dap() {
		DaoAuthenticationProvider dap=new DaoAuthenticationProvider();
		dap.setPasswordEncoder(pe());
		dap.setUserDetailsService(uds());
		return dap;
	}
	
	@Bean
	public SecurityFilterChain sf(HttpSecurity http) throws Exception {
		http.csrf(
			     	c->c.disable()
				)
				.authorizeHttpRequests(
				    a->a.requestMatchers("/user/**").hasRole("USER")
        				.requestMatchers("/admin/**").hasRole("ADMIN")
				        .requestMatchers("/**")
				        .permitAll()
				        .anyRequest()
				        .authenticated()
						)
				 .formLogin(
         				f->f
         				.loginPage("/home/login")
         				 .loginProcessingUrl("/dologin")
         				.defaultSuccessUrl("/user/index")		 
						 ) 
				 .logout(
						 l->l.permitAll()
						 );
		
		return http.build();
	}

}
