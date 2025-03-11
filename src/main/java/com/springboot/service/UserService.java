/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.springboot.service;

import com.springboot.Entity.User;
import com.springboot.repository.UserRepo;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author Yash
 */

@Service
public class UserService {
    
    @Autowired
    private UserRepo userRepo;
    
    @Autowired
    private JavaMailSender mailSender;
    
    public User save(User user){
        
        user.setRole("ROLE_USER");
        
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        
        return userRepo.save(user);
        
    }
    
    public boolean isUserExists(String email){
        
        User user = userRepo.findByEmail(email);
        return user != null;
        
    }
    
    public User updateUser(User user){
        return userRepo.save(user);
    }
    
    public User getByEmail(String email){
        
        User user = userRepo.findByEmail(email);
        
        if(user != null)
            return user;
        else
            return null;
        
    }
    
    public boolean sendMail(User user, int otp){
        
        String toAddress = user.getEmail();
        String fromAddress = "yashkhakhkhar455@gmail.com";
        String senderName = "MarketMatrix";
        String subject = "OTP Verification";
        String content = "Dear [[name]],<br>"
            + "<h3>OTP [[otp]]</h3>"
            + "Thank you,<br>"
            + "MarketMatrix";
        
        try{
            
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom(fromAddress, senderName);
            helper.setTo(toAddress);
            helper.setSubject(subject);

            content = content.replace("[[name]]", user.getFullName());

            content = content.replace("[[otp]]", String.valueOf(otp));

            helper.setText(content, true);

            mailSender.send(message);
            
            user.setOtpCode(otp);
            updateUser(user);
            
            return true;
            
        }catch(MessagingException | UnsupportedEncodingException | MailException e){
            System.out.println("Exception in mail generation : ");
        }
        
        return false;
      
    }
    
    public Boolean changePassword(User user, String newPassword){
        
        user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        return updateUser(user) != null;
        
    }
    
}
