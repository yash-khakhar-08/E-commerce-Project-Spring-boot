/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.springboot.service;

import com.springboot.Entity.Orders;
import com.springboot.Entity.Product;
import com.springboot.Entity.User;
import com.springboot.repository.OrdersRepo;
import com.springboot.repository.ProductRepo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Yash
 */

@Service
public class OrdersService {
    
    @Autowired
    private OrdersRepo ordersRepo;
    
    @Autowired
    private ProductRepo productRepo;
    
    public Orders saveOrder(Orders orders){
        
        return ordersRepo.save(orders);
        
    }
    
    public List<Orders> getOrders(User user){
        
        return ordersRepo.findByUserOrderByDateDesc(user);
        
    }
    
    public void cancelOrder(int orderId){
        
        Orders orders = ordersRepo.findById(orderId);
        orders.setStatus("Canceled");
        ordersRepo.save(orders);
        
        Product product = orders.getProduct();
        product.setProductQty(product.getProductQty() +orders.getPurchaseQty() );
        productRepo.save(product);
        
    }
    
}
