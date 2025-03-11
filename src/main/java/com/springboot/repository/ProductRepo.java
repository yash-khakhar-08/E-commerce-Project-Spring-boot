/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.springboot.repository;

import com.springboot.Entity.Category;
import com.springboot.Entity.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Yash
 */

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer> {
   
   /*@Query(value = "SELECT * FROM product p " +
               "WHERE (p.product_name LIKE CONCAT('%', :query, '%') " +
               "   OR p.product_desc LIKE CONCAT('%', :query, '%') " +
               "   OR :query LIKE CONCAT('%', p.product_name, '%') " +
               "   OR :query LIKE CONCAT('%', p.product_desc, '%')) " +
               "AND (:minPrice IS NULL OR p.product_price >= :minPrice) " +
               "AND (:maxPrice IS NULL OR p.product_price <= :maxPrice)", 
       nativeQuery = true)
        List<Product> getSearchProducts(
            @Param("query") String query, 
            @Param("minPrice") Integer minPrice, 
            @Param("maxPrice") Integer maxPrice
        );*/
        
        @Query(value = "SELECT * FROM product p " +
               "WHERE MATCH(p.product_name, p.product_desc) AGAINST (:query IN BOOLEAN MODE) " +
               "AND (:minPrice IS NULL OR p.product_price >= :minPrice) " +
               "AND (:maxPrice IS NULL OR p.product_price <= :maxPrice)",
       nativeQuery = true)
        List<Product> getSearchProducts(
            @Param("query") String query,
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice
        );
        
        public List<Product> findByCategory(Category category);

}
