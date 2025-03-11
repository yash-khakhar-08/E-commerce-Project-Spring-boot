/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.springboot.service;

import com.springboot.Entity.Category;
import com.springboot.Entity.Product;
import com.springboot.repository.ProductRepo;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Yash
 */

@Service
public class ProductService {
    
    @Autowired
    private ProductRepo productRepo;
    
    public Product addProduct(Product p){
        
        return productRepo.save(p);
        
    }
    
    public Product getById(Integer id){
        return productRepo.findById(id).get();
    }
    
    public Product updateProduct(Product product){
        return productRepo.save(product);
    }
    
    /*public List<Product> getSearchProducts(String productName,  Integer minPrice, Integer maxPrice){
        
        return productRepo.getSearchProducts(productName, minPrice, maxPrice);
        
    }*/
    
    public List<Product> getSearchProducts(String query, Integer minPrice, Integer maxPrice){
        
        return productRepo.getSearchProducts(query, minPrice, maxPrice);
        
    }
    
    public List<Product> getByCategory(Category category){
        
        List<Product> products = productRepo.findByCategory(category);
        
        Collections.shuffle(products); // Shuffle list randomly
        
        return products.stream().limit(4).collect(Collectors.toList());
        
    }
  
    
}
