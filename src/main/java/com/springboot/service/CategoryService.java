/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.springboot.service;

import com.springboot.Entity.Category;
import com.springboot.Entity.Product;
import com.springboot.repository.CategoryRepo;
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
public class CategoryService {
    
    @Autowired
    private CategoryRepo categoryRepo;
    
    public List<Category> getAllCategoryWithRandomProducts(){
        
        List<Category> categories = categoryRepo.findAll();

        for (Category category : categories) {
            List<Product> products = category.getProduct();
            Collections.shuffle(products); // Shuffle list randomly
            category.setProduct(products.stream().limit(4).collect(Collectors.toList())); // Limit to 4
        }
        
        return categories;
        
    }
    
    public List<Category> getCategoryWithRandomProducts(String sectionName){
        
        List<Category> categories = categoryRepo.findBySectionName(sectionName);

        for (Category category : categories) {
            List<Product> products = category.getProduct();
            Collections.shuffle(products); // Shuffle list randomly
            category.setProduct(products.stream().limit(4).collect(Collectors.toList())); // Limit to 4
        }
        
        return categories;
        
    }
    
    public List<Category> getSearchCategory(String sectionName){
        
        return categoryRepo.findBySectionNameStartingWith(sectionName);
        
    }
    
}
