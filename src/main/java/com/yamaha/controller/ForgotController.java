package com.yamaha.controller;

import java.util.Random; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.yamaha.entities.User;
import com.yamaha.helper.EmailService;
import com.yamaha.helper.MessageHelper;
import com.yamaha.repo.UserRepo;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
public class ForgotController {

	@Autowired
	UserRepo userrepo;
	
	@Autowired
	private EmailService emailservice;
	
	@Autowired
	private PasswordEncoder pe;
	
	Random r=new Random(100000); //in constructor we provide minimum value
	
	//FORM OPEN
	@GetMapping("/forgot")
	public String openform() {
		return "forgotpassword";	
	}
	
	
	//SEND OTP
	@PostMapping("/sendOTP")
	public String sendOTP(@RequestParam String email,HttpSession session,Model m) {
		System.out.println(email);
		User byEmail = userrepo.findByEmail(email);
		  if(byEmail==null) {
				session.setAttribute("message", new MessageHelper("No user exist with this email id", "alert-danger"));
				return "redirect:/forgotpassword";	
			}
		//generating random number\
		int otp = r.nextInt(999999);
		String to=email;
		String from="sourabhdummy7@gmail.com";
		String subject="OTP from Yamaha Motors:Anil Automobiles";
		String text=""+"<div style='border:1px solid #e2e2e2; padding:20px;'>"
                +"<h3>"
                +"OTP is "
                +"<b>"
                +otp
                +"</b>"
                +"</h3>"
                +"</div>";
		
        if( emailservice.sendEmail(to, from, subject,text)) {
        	m.addAttribute("otp",otp);
        	m.addAttribute("mail",email);
        	return "verifyOtp";
        }
        else {
            session.setAttribute("message", new MessageHelper("Please enter valid registered email_id","alert-danger"));
     		return "forgotpassword";
		}
	}
	
	
	//PROCESS VERIFY
	@PostMapping("/verify")
	public String verify(@RequestParam int otp, @RequestParam String enteredotp,@RequestParam String mail,HttpSession session,Model m) {		
		//change password
		if(otp == Integer.parseInt(enteredotp)){
			m.addAttribute("mail",mail);
			return "changepassword";
		}	
	    session.setAttribute("message", new MessageHelper("Please enter valid OtP","alert-danger"));
		return "verifyOtp";
	}
	
	//CHANGE PASSWORD
		@PostMapping("/processchange")
		public String changepassword(@RequestParam String mail,@RequestParam String npass) {
          User byEmail = userrepo.findByEmail(mail);
			byEmail.setPassword(pe.encode(npass));
			userrepo.save(byEmail);
			return "redirect:/home/login?change=Password has Succesfully changed";
		}
		
}
