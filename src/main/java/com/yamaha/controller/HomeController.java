package com.yamaha.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yamaha.entities.User;
import com.yamaha.helper.MessageHelper;
import com.yamaha.repo.UserRepo;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;



@Controller
@RequestMapping("/home")
public class HomeController {

	@Autowired
	private UserRepo userrepo; 
	
	@Autowired
	private PasswordEncoder pe;
	
	//SESSION CLEAR MESSAGE
	//FOR SESSION
	 @PostMapping("/clear-session-message")
	    @ResponseBody
	    public void clearSessionMessage(HttpSession session) {
	        session.removeAttribute("message");
	    }
	
	//HOME PAGE
	@GetMapping("")
	public String homepage() {
		return "home";
	}
	
	//REGISTRATION
	@GetMapping("/register")
	private String registration(Model m) {
		m.addAttribute("user",new User());
		return "register";
	}
	//process register
	@PostMapping("/doregister")
	public String doregister(@Valid @ModelAttribute("user") User user,BindingResult res,@RequestParam(value = "agreement",defaultValue ="false") boolean agree
			                              ,Model m,HttpSession s) {
		try {
	
			if(res.hasErrors()) {
				m.addAttribute("user",user);
				return "register";
			}
			if(!agree) {
				throw new Exception();
			}	 
			
			user.setRole("ROLE_USER");
		    user.setImg("default.png");
		    user.setPassword(pe.encode(user.getPassword()));
		   userrepo.save(user);
			s.setAttribute("message", new MessageHelper("Registered succesfully","alert-success"));
			m.addAttribute("user",new User());
			return "register";
		}
		catch(Exception e){
			m.addAttribute("user",user);
			s.setAttribute("message", new MessageHelper("Something went wrong!!","alert-danger"));
			return "register";
		}
	}
	
	//LOGIN PAGE
	@GetMapping("/login")
	public String loginpage() {
		return "login";
	}
	
	
}
