/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.springboot.controller;

import com.springboot.Entity.Address;
import com.springboot.Entity.CartInfo;
import com.springboot.Entity.Orders;
import com.springboot.Entity.Product;
import com.springboot.Entity.User;
import com.springboot.service.AddressService;
import com.springboot.service.CartInfoService;
import com.springboot.service.CategoryService;
import com.springboot.service.CustomFunctions;
import com.springboot.service.OrdersService;
import com.springboot.service.ProductService;
import com.springboot.service.UserService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Yash
 */

@Controller
@RequestMapping("/user/")
public class UserController {
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private CartInfoService cartInfoService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private AddressService addressService;
    
    @Autowired
    private OrdersService orderService;
    
    @ModelAttribute
    public void addCommonAttributes(Model m, HttpSession session){
        
        String username = (String) session.getAttribute("loggedInUser");
        
        if(username != null) {
            
            m.addAttribute("username", userService.getByEmail(username).getFullName());
        
            List<Integer> productIds = cartInfoService.getCartItemByUserId(username);
            m.addAttribute("productIds", productIds);
        } 
        
        
    }
    
    @RequestMapping("/home")
    public String home(Model m){
        
        m.addAttribute("categories", categoryService.getAllCategoryWithRandomProducts());
        m.addAttribute("pageName","home");
        
        return "home";
    }
    
    @GetMapping("/men")
    public String menPage(Model m){
        
        m.addAttribute("categories", categoryService.getCategoryWithRandomProducts("Male"));
        
        return "men";
    }
    
    @GetMapping("/women")
    public String womenPage(Model m){
        
        m.addAttribute("categories", categoryService.getCategoryWithRandomProducts("Female"));
        
        return "women";
    }
    
    @PostMapping("/add-cart-process")
    @ResponseBody
    public ResponseEntity<String> addCartProcess(@RequestParam("p_id") Integer p_id, @RequestParam("qty") Integer qty,HttpSession session){
        
        String username = (String) session.getAttribute("loggedInUser");
        
        CartInfo cartInfo = cartInfoService.addToCart(p_id, qty, username);
        
        if(cartInfo != null)
            return ResponseEntity.ok("Product added to cart successfully!");
        else
            return ResponseEntity.ok("Something went wrong!");
    }
    
    @GetMapping("/view-product")
    public String viewProduct(@RequestParam("id") Integer id, Model m,HttpSession session){
        
        String username = (String) session.getAttribute("loggedInUser");
        
        Product product = productService.getById(id);
        m.addAttribute("product", product);
        
        CartInfo cartInfo = cartInfoService.getCartInfoByProductAndUser(product, username);
        if(cartInfo != null)
            m.addAttribute("cartInfo", cartInfo);
        
        List<Product> products = productService.getByCategory(product.getCategory());
        m.addAttribute("products", products);
        
        return "view-product";
        
    }
    
    
    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model m){
        
        String username = (String) session.getAttribute("loggedInUser");
        List<CartInfo> cartInfo = cartInfoService.getCartItems(username);
        m.addAttribute("cartInfo", cartInfo);
        
        return "cart";
    }
    
    
    @PostMapping("/update-cart")
    @ResponseBody
    public ResponseEntity<String> updateCart(@RequestParam("c_id") Integer c_id, @RequestParam("qty") Integer qty,HttpSession session){
        
        System.out.println("C Id : " + c_id);
        System.out.println("Qty : " + qty);
        
        CartInfo cartInfo = cartInfoService.getCartById(c_id);
        cartInfo.setPurchaseQty(qty);
        cartInfo.setPurchaseAmt(cartInfo.getProduct().getProductPrice() * qty  );
        
        if(cartInfoService.updateCart(cartInfo) != null)
            return ResponseEntity.ok("Cart Updated!!");
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Something went wrong!!");
        
    }
    
    @GetMapping("/search-product")
    public String searchProduct(@RequestParam("search-query") String query, Model m){
        
        String gender = CustomFunctions.extractGender(query);
        Integer minPrice = CustomFunctions.extractMinPrice(query);
        Integer maxPrice = CustomFunctions.extractMaxPrice(query);
        String productName = CustomFunctions.extractProductName(query, gender, minPrice, maxPrice);

        System.out.println("Product Name: " + productName);
        System.out.println("Gender: " + gender);
        System.out.println("Min Price: " + minPrice);
        System.out.println("Max Price: " + maxPrice);
        
        /*String sectionName = CustomFunctions.getSectionName(gender != null ? gender : "Male");
        System.out.println("Section Name : " + sectionName);
        
        List<Category> categories = categoryService.getSearchCategory(sectionName);
        for(Category category : categories){
            System.out.println("Category Id :" + category.getId());
        }
        
        List<Product> products = productService.getSearchProducts(productName, minPrice, maxPrice);
        System.out.println("Product Size : " + products.size());
        for(Product product : products){
            System.out.println("Product Id :" + product.getId());
        }*/
        
        List<Product> products = productService.getSearchProducts(productName, minPrice, maxPrice);
        m.addAttribute("products", products);
        m.addAttribute("query", query);
        
        if(!products.isEmpty()) {
            List<Product> relatedProducts = productService.getByCategory(products.get(0).getCategory());
            m.addAttribute("relatedProducts", relatedProducts);
        }
        
        return "search-product";
        
    }
    
    @PostMapping("/checkout")
    public String checkout(@RequestParam("isProductId") Integer isProductId, HttpSession session, Model m){
        
        String username = (String) session.getAttribute("loggedInUser");
        User user = userService.getByEmail(username);
        m.addAttribute("user", user);
        if(isProductId == 0){
            List<CartInfo> cartInfo = cartInfoService.getCartItems(username);
            m.addAttribute("totalAmt", cartInfo.stream().mapToLong(CartInfo::getPurchaseAmt).sum());
            
        }
        
        m.addAttribute("isProductId", isProductId);
      
        return "checkout";
        
    }
    
    @PostMapping("/save-address")
    @ResponseBody
    public ResponseEntity<String> saveAddress(@ModelAttribute Address address, HttpSession session){
        
        String username = (String) session.getAttribute("loggedInUser");
        User user = userService.getByEmail(username);
        
        if(address != null){
            
            if(user.getAddress() == null){
                address.setUser(user);
                addressService.saveAddress(address);
                user.setAddress(address);
                userService.updateUser(user);
            } else{
                
                Address addr = user.getAddress();
                addr.setBlockNo(address.getBlockNo());
                addr.setApartmentName(address.getApartmentName());
                addr.setCity(address.getCity());
                addr.setState(address.getState());
                addr.setPinCode(address.getPinCode());
                addr.setCountry(address.getCountry());
                addressService.saveAddress(addr);
            
            }
            
            return ResponseEntity.ok("Address updated!!");
        }
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong!!");
        
    }
    
    @PostMapping("/place-order")
    @ResponseBody
    public ResponseEntity<String> placeOrder(@RequestParam("payment_mode") String paymentMode, @RequestParam("isProductId") Integer isProductId ,HttpSession session){
        
        String username = (String) session.getAttribute("loggedInUser");
        if(isProductId == 0){
            List<CartInfo> cartInfo = cartInfoService.getCartItems(username);
            
            for(CartInfo cart : cartInfo){
            
                Orders order = new Orders();
                order.setProduct(cart.getProduct());
                order.setPurchaseAmt(cart.getPurchaseAmt());
                order.setPurchaseQty(cart.getPurchaseQty());
                order.setUser(cart.getUser());
                order.setPaymentMode(paymentMode);
                order.setStatus("Pending");
                orderService.saveOrder(order);
                cartInfoService.deleteFromCart(cart);
            
            }
            
        }
        
        
        return ResponseEntity.ok("Order placed Successfully!!");
        
    }
    
    @PostMapping("/delete-cart-item")
    @ResponseBody
    public ResponseEntity<String> deleteCartItem(@RequestParam("cartId") Integer cartId){
    
        CartInfo cartInfo = cartInfoService.getCartById(cartId);
        
        if(cartInfo != null){
            cartInfoService.deleteFromCart(cartInfo);
            return ResponseEntity.ok("Item removed from cart");
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something went wrong!");
        }
           
    
    }
    
    @GetMapping("/orders")
    public String viewOrders(HttpSession session, Model m){
        
        String username = (String) session.getAttribute("loggedInUser");
        User user = userService.getByEmail(username);
        List<Orders> orders = orderService.getOrders(user);
        m.addAttribute("orders", orders);
        
        return "orders";
    }
    
    @PostMapping("/cancel-order")
    @ResponseBody
    public ResponseEntity<String> cancelOrder(@RequestParam("orderId") int orderId){
        
        orderService.cancelOrder(orderId);
        
        return ResponseEntity.ok("Order canceled successfully!");
        
        
    }
    
}
