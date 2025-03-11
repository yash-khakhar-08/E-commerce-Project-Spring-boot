/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.springboot.controller;

import com.springboot.Entity.User;
import com.springboot.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.security.SecureRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Yash
 */

@Controller
public class HomeController {
    
    @Autowired
    private UserService userService;
    
    @RequestMapping("/signin")
    public String signin(){
        return "login";
    }
    
    /*@PostMapping("/login")
    public String login(){
        return "home";
    }*/
    
    /*@RequestMapping("/home")
    public String home(){
        return "home";
    }*/
    
    @RequestMapping("/register")
    public String register(){
        
        return "register";
    }
    
    @PostMapping("/createUser")
    public String createUser(@ModelAttribute User user, RedirectAttributes redirectAttributes){
        
        if(!userService.isUserExists(user.getEmail())){
            userService.save(user);
            return "redirect:/signin";
        }
        else{
            redirectAttributes.addFlashAttribute("userExists", "This email is already registered!! Try another");
            return "redirect:/register";
        }
        
        
        
    }
    
    @RequestMapping("/userLogout")
    public String logout(){
        return "logout";
    }
    
    @GetMapping("/forgot-password")
    public String showForgetPassoword(){
        
        return "forgot_password";
    } 
    
    @PostMapping("/send-otp")
    @ResponseBody
    public ResponseEntity<String> sendOtp(@RequestParam("email") String email){
        
        User user = userService.getByEmail(email);
        
        if(user != null){
        
            SecureRandom secureRandom = new SecureRandom();
            int otp = 100000 + secureRandom.nextInt(900000);
            userService.sendMail(user, otp);
            
            return ResponseEntity.ok("OTP has been sent to " + email);
        } else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid email provided!!");
        }
        
    }
    
    @PostMapping("/verify-otp")
    @ResponseBody
    public ResponseEntity<String> verifyOtp(@RequestParam("otpCode") int otpCode, @RequestParam("email") String email, HttpServletRequest request){
        
        System.out.println(otpCode);
        System.out.println(email);
        
        User user = userService.getByEmail(email);
        if(user != null){
            int otp = user.getOtpCode();
            
            if(otp == otpCode){
                HttpSession session = request.getSession();
                session.setAttribute("email", user.getEmail());
                
                user.setOtpCode(0);
                userService.updateUser(user);
                return ResponseEntity.ok("Otp verified successfully!");
                
                
            } else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid otp!!");
            }
            
        } else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong server!! Try again");
        }
        
        
    }
    
    @GetMapping("/change-password")
    public String changePassword(){
        return "change_password";
    }
    
    @PostMapping("change-password-process")
    public String changePasswordProcess(@RequestParam("newPassword") String newPassword, HttpSession session, RedirectAttributes redirectAttributes){
        
        String email = (String)session.getAttribute("email");
        User user = userService.getByEmail(email);
        if(user != null){
            userService.changePassword(user,newPassword);
            redirectAttributes.addFlashAttribute("success", "Password changed successfull!!");
            session.removeAttribute("email");
            return "redirect:/signin";
        }else{
            redirectAttributes.addFlashAttribute("failure", "Something went wrong on server!!");
            return "redirect:/change-password-process";
        }
        
    }
    
}
