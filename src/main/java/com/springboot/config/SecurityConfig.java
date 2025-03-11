/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.springboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 *
 * @author Yash
 * 
 * execution flow:
 * dao method
 * user details service method
 * password encoder
 * security encoder method
 * 
 */

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    
    @Autowired
    private CustomAuthFailureHandler customAuthFailureHandler;
    
    @Autowired
    private CustomAuthSuccessHandler customAuthSuccessHandler;
    
    @Bean
    public PasswordEncoder passwordEncoder(){
        
        System.out.println("password encoder method ");
        
        return new BCryptPasswordEncoder();
        
    }
    
    @Bean
    public UserDetailsService getDetailsService(){
        System.out.println("user details service method ");
        
        return new CustomUserDetailsService();
    }
    
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        
        System.out.println("dao auth method ");
        
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(getDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        
        return daoAuthenticationProvider;
        
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        
        System.out.println("Security chain method ");
        
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF (if necessary)
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/","/images/**","/login","/createUser","/register"
                    ,"/forgot-password","/send-otp","/verify-otp",
                    "/change-password","/change-password-process","/google*.html","/sitemap.xml","/robots.txt").permitAll()
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .requestMatchers("/user/**").hasRole("USER")
                .anyRequest().authenticated() // Secure all endpoints
            )
            .formLogin(form -> form
                    //.defaultSuccessUrl("/home", true)
                    .loginPage("/signin")
                    //.failureUrl("/invalid")
                    .permitAll()
                    .loginProcessingUrl("/login")
                    .successHandler(customAuthSuccessHandler)
                    .failureHandler(customAuthFailureHandler)
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/userLogout")  
                    .permitAll()
            );
        
        return http.build();
        
    }
    
}
