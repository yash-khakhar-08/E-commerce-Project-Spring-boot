/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.springboot.repository;

import com.springboot.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Yash
 */


public interface UserRepo extends JpaRepository<User, Integer> {
    
    public User findByEmail(String username);
    
}
