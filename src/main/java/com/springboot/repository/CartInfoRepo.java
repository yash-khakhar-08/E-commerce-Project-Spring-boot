/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.springboot.repository;

import com.springboot.Entity.CartInfo;
import com.springboot.Entity.Product;
import com.springboot.Entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Yash
 */

@Repository
public interface CartInfoRepo extends JpaRepository<CartInfo, Integer> {
    
    public List<CartInfo> findByUser(User user);
    
    public CartInfo findByProductAndUser(Product p, User u);
    
}
