/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.springboot.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Set;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 *
 * @author Yash
 */

@Component
public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        
        // Get the current session or create a new one
        HttpSession session = request.getSession();
        
        // Store the logged-in username in session
        String username = authentication.getName(); // return email
        session.setAttribute("loggedInUser", username);
        
        Set<String> roles =AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        
        if(roles.contains("ROLE_ADMIN")){
            response.sendRedirect("/admin/home");
        }else{
            
            response.sendRedirect("/user/home");
        }
        
    }
    
}
