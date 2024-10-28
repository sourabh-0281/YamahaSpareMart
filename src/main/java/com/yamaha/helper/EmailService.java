package com.yamaha.helper;

import java.util.Properties;

import org.springframework.stereotype.Service;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

	public boolean sendEmail(String to,String from,String subject,String text) {
		boolean flag=false;
	//1)setting smtp properties
	 	Properties properties = new Properties();                 // it is used to store the configuration settings for the email session.
        properties.put("mail.smtp.auth", true);		              //This property specifies that the SMTP server requires authentication. In other words, you need to provide a username and password to send an email.
        properties.put("mail.smtp.starttls.enable", true);  //STARTTLS is a way to take an existing insecure connection and upgrade it to a secure connection using SSL/TLS. It ensures that your email data is encrypted during transmission.
        properties.put("mail.smtp.port", "587");                 // Port 587 is the standard port for sending emails using SMTP with STARTTLS
        properties.put("mail.smtp.host", "smtp.gmail.com");// Specifies the SMTP server host, which is Gmail's server in this case.
        
        String pass="yfym rlwm oync kanm";
        System.out.println("1");
     //2) Session   
		Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
                  return new PasswordAuthentication(from, pass);
			}			
		});
        System.out.println("2");
		try {
		//3) MESSAGE	
			Message msg=new MimeMessage(session);
	        System.out.println("3");
		//4)Setting values	
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
			msg.setFrom(new InternetAddress(from));
			msg.setSubject(subject);
	    	//	msg.setText(text);
			msg.setContent(text, "text/html");  //we can use this for html 
	        System.out.println("4");
		//5)TRANSPORT
			Transport.send(msg);
	        System.out.println("5");
			flag=true;
			
		}catch (Exception e) {
		
		}
		return flag;
	}
 
}
