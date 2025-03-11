/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.springboot.service;

import com.springboot.Entity.CartInfo;
import com.springboot.Entity.Product;
import com.springboot.Entity.User;
import com.springboot.repository.CartInfoRepo;
import com.springboot.repository.UserRepo;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Yash
 */

@Service
public class CartInfoService {
    
    @Autowired
    private CartInfoRepo cartInfoRepo;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private UserRepo userRepo;
    
    public CartInfo addToCart(Integer productId, Integer qty,String email){
        
        Product product  = productService.getById(productId);
        
        User user = userRepo.findByEmail(email);
        
        CartInfo cartInfo = new CartInfo();
        cartInfo.setProduct(product);
        cartInfo.setPurchaseQty(qty);
        cartInfo.setPurchaseAmt(product.getProductPrice() * qty);
        cartInfo.setUser(user);
        
        /*product.setProductQty(product.getProductQty() - qty);
        productService.updateProduct(product);*/
        
        return cartInfoRepo.save(cartInfo);
       
    }
    
    public List<CartInfo> getByUser(String email){
        
        User user = userRepo.findByEmail(email);
        return cartInfoRepo.findByUser(user);
        
    }
    
    public int getTotalCartItems(String email){
        
       List<CartInfo> cartInfo = getByUser(email);
       
       return cartInfo.size();
        
    }
    
    public List<Integer> getCartItemByUserId(String email){
        
        List<CartInfo> cartInfo = getByUser(email);
        
        List<Integer> productIds = new ArrayList<>();
        
        for(CartInfo cart : cartInfo){
        
            productIds.add(cart.getProduct().getId());
             
        }
        
        return productIds;
           
    }
    
    public CartInfo getCartInfoByProductAndUser(Product p,String email){
    
        User u = userRepo.findByEmail(email);
        CartInfo cartInfo = cartInfoRepo.findByProductAndUser(p, u);
        
        if(cartInfo != null)
            return cartInfo;
        else
            return null;
    
    }
    
    public List<CartInfo> getCartItems(String email){
        
        List<CartInfo> cartInfo = getByUser(email);
        
        return cartInfo;
        
    }
    
    public CartInfo getCartById(Integer id){
        
        return cartInfoRepo.findById(id).get();
        
    }
    
    public CartInfo updateCart(CartInfo cartInfo){
        
        return cartInfoRepo.save(cartInfo);
        
    }
    
    public void deleteFromCart(CartInfo cart){
        
        Product product = cart.getProduct();
        product.setProductQty(product.getProductQty() - cart.getPurchaseQty());
        productService.updateProduct(product);
        
        cartInfoRepo.delete(cart);
        
    }
    
}
