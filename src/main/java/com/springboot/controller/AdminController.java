/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.springboot.controller;

import com.springboot.Entity.Category;
import com.springboot.Entity.Product;
import com.springboot.repository.CategoryRepo;
import com.springboot.service.FileService;
import com.springboot.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Yash
 */

@Controller
@RequestMapping("/admin/")
public class AdminController {
    
    @Autowired
    private CategoryRepo categoryRepo;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private FileService fileService;
    
    @Value("${aws.s3.bucket.products}")
    private String productBucket;
    
    @GetMapping("/home")
    public String home(){
        return "admin/home";
    }
    
    @GetMapping("/add-category")
    public String addCategory(){
        return "admin/add-category";
    }
    
    @PostMapping("/add-category-process")
    public String addCategoryProcess(@ModelAttribute Category cat, RedirectAttributes redirectAttributes){
        
        categoryRepo.save(cat);
        
        redirectAttributes.addFlashAttribute("success", "Category Added");
        
        return "redirect:add-category";
        
    }
    
    @GetMapping("/add-product")
    public String addProduct(Model m){
        
        m.addAttribute("categories", categoryRepo.findAll());
        
        return "admin/add-product";
    }
    
    @PostMapping("/add-product-process")
    public String addProductProcess(@ModelAttribute Product p, @RequestParam("image") MultipartFile file, HttpServletRequest req, RedirectAttributes redirectAttributes){
        
        try {
                /*File uploadImage = new ClassPathResource("static").getFile();
                
                Path path = Paths.get(uploadImage.getAbsolutePath() + File.separator + "products" + File.separator + file.getOriginalFilename());
                
                System.out.println("Path : " + path);
                
                Files.copy(file.getInputStream(), path,StandardCopyOption.REPLACE_EXISTING);*/
               
                // url https://marketmatrix-products.s3.eu-north-1.amazonaws.com/Dsh4_Black.jpg
                
                String imageUrl = "https://"+productBucket+".s3.eu-north-1.amazonaws.com/"+file.getOriginalFilename();
                
                p.setProductImage(imageUrl);
                
                productService.addProduct(p);
                
                fileService.uploadFileToS3(file, 1);


            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("success", "Product Adding Failed!!");
                return "redirect:add-product";
            }
        
        redirectAttributes.addFlashAttribute("success", "Product Added Successfully!!");
        
        return "redirect:add-product";
        
    }
    
}
