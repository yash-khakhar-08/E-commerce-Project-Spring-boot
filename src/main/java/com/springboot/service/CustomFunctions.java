/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.springboot.service;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

/**
 *
 * @author Yash
 */

@Service
public class CustomFunctions {
    
     public static String extractGender(String query) {
        List<String> genderKeywords = Arrays.asList("men", "women", "boy", "girl", "unisex","male","female");
        for (String gender : genderKeywords) {
            if (query.toLowerCase().contains(gender)) {
                return gender;
            }
        }
        return null; // No gender found
    }
     
    public static Integer extractMinPrice(String query) {
        Pattern pattern = Pattern.compile("(above|more than|over|between)\\s*(\\d+)");
        Matcher matcher = pattern.matcher(query.toLowerCase());

        if (matcher.find()) {
            return Integer.valueOf(matcher.group(2)); // group 2 means 400 500 numeric value
        }
        return null; // No price found
    }
    
    public static Integer extractMaxPrice(String query) {
        Pattern pattern = Pattern.compile("(below|less than|under|and)\\s*(\\d+)");
        Matcher matcher = pattern.matcher(query.toLowerCase());

        if (matcher.find()) {
            return Integer.valueOf(matcher.group(2)); // group 2 means 400 500 numeric value
        }
        return null; // No price found
    }
    
    // Extract product name (excluding gender and price keywords)
    public static String extractProductName(String query, String gender, Integer minPrice, Integer maxPrice) {
        String cleanedQuery = query.toLowerCase();

        if (minPrice != null) {
            cleanedQuery = cleanedQuery.replaceAll("(above|more than|over|between)\\s*(\\d+)", "");
        }
        
        if (maxPrice != null) {
            cleanedQuery = cleanedQuery.replaceAll("(below|less than|under|and)\\s*(\\d+)", "");
        }
        
        return cleanedQuery.trim();
    }
    
    public static String getSectionName(String sectionName){
        
        List<String> maleGenderKeywords = Arrays.asList( "men", "boy", "unisex","male");
        for (String gender : maleGenderKeywords) {
            if (sectionName.toLowerCase().equals(gender)) {
                return "Male";
            }
        }
        
        List<String> femaleGenderKeywords = Arrays.asList( "women", "girl", "unisex","female");
        for (String gender : femaleGenderKeywords) {
            if (sectionName.toLowerCase().equals(gender)) {
                return "Female";
            }
        }
        
        return null; // No gender found
        
    }
    
}
