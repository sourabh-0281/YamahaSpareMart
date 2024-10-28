package com.yamaha.controller;

import java.io.File; 
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.hibernate.internal.build.AllowSysOut;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.razorpay.*;
import com.yamaha.entities.Cart;
import com.yamaha.entities.Orders;
import com.yamaha.entities.Products;
import com.yamaha.entities.User;
import com.yamaha.helper.MessageHelper;
import com.yamaha.helper.Parts;
import com.yamaha.repo.CartRepo;
import com.yamaha.repo.OrderRepo;
import com.yamaha.repo.ProductRepo;
import com.yamaha.repo.UserRepo;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;


@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepo userrepo;
	
	@Autowired
	private ProductRepo productrepo;
	
	@Autowired
	private CartRepo cartrepo;
	
	@Autowired
	private OrderRepo orderrepo;
	
	@Autowired
	private BCryptPasswordEncoder bcyptpasswordencoder;
	
	
	@ModelAttribute
	public void commondata(Principal p,Model m) {
		String name = p.getName();
		User byEmail = userrepo.findByEmail(name);
		m.addAttribute("name",byEmail);
	}
	
	//INDEX
	@GetMapping("/index")
	public String userhomepage(Model m,Principal p) {
		String email = p.getName();
		User byEmail = userrepo.findByEmail(email);
		m.addAttribute("user",byEmail);
		m.addAttribute("filter",parts());
		return "normal/index";
	}
	
	
	//EXPLORE
      private List<Parts> parts(){
		List<Parts> parts=new ArrayList<>();
		  parts.add(Parts.builder().imgurl("/img/explore/air-filter.png").name("FILTER").build());
		  parts.add(Parts.builder().imgurl("/img/explore/oilfilter.png").name("OIL FILTER").build());
		  parts.add(Parts.builder().imgurl("/img/explore/brake.png").name("Brake Pads and Brake Shoes").build());
		  parts.add(Parts.builder().imgurl("/img/explore/cdi.png").name("cdi unit assembly").build());
		  parts.add(Parts.builder().imgurl("/img/explore/cluch-plates.png").name("clutch and friction plates").build());
		  parts.add(Parts.builder().imgurl("/img/explore/throttle-cluch.png").name("control cables").build());
		  parts.add(Parts.builder().imgurl("/img/explore/drive-chain.png").name("drive chain and sprokets").build());		 
		  parts.add(Parts.builder().imgurl("/img/explore/gaskets.png").name("gaskets and oil seals ").build());
		  parts.add(Parts.builder().imgurl("/img/explore/rear-mirrior.png").name("rear view mirrors").build());
		  parts.add(Parts.builder().imgurl("/img/explore/spark-plug.png").name("spark plug").build());
		  parts.add(Parts.builder().imgurl("/img/explore/cylinder-piston.png").name("standard piston cylinder kit").build());
		  parts.add(Parts.builder().imgurl("/img/explore/v-belt.png").name("vbelt").build());
		  parts.add(Parts.builder().imgurl("/img/explore/Bearing.png").name("Bearing").build());
	      return parts;
	  }
      
      
      //ALL PRODUCTS
      //per page=5
      //current page=0[current]
      @GetMapping("/products/{page}")
      public String allproducts(@PathVariable int page,  Model m) {
    	  
    	  Pageable pageable = PageRequest.of(page, 5);
    	                                                //current page , contact per page
    	  
    	  Page<Products> all = productrepo.findAll(pageable);    	  
    	  m.addAttribute("allproducts",all);
    	  m.addAttribute("currentpage",page);
    	  m.addAttribute("totalpages",all.getTotalPages());
    	  
    	  return "normal/allproducts";
      }
      
      
      //LIST OF Single PRODUCT
      @GetMapping("/listsingleproduct/{product}")	
      public String listsingleproduct(@PathVariable String product,Model m) {
    	  //to check seals and gaskets 
    	  if(product.contains("seals") && product.contains("gaskets")) {
    		  
    		  List<Products> byProductNameContainingANDProductNameContaining = productrepo.findByProductNameContainingOrProductNameContaining("seal","gasket");
    		  m.addAttribute("product",product);
    		  m.addAttribute("allproducts",byProductNameContainingANDProductNameContaining);
    	      return  "normal/listsingleproduct";
    	  } 
    	  else {
    	 	  /*The split() method in Java is used to divide a string into an array of substrings based on a delimiter (in this case, a space " ").*/
	    	  String firstword = product.split(" ")[0];
	    	  List<Products> byProductNameContaining;
	    	  
	    	  //to check word oil as it also returns oil seals
	    	  if(firstword.equalsIgnoreCase("oil")) {
	    	     byProductNameContaining = productrepo.findByProductNameContaining("Oil Filter");
	    	  }else {
	    	     byProductNameContaining = productrepo.findByProductNameContaining(firstword);
	    	  }	    	  
	           m.addAttribute("product",product);
	           m.addAttribute("allproducts",byProductNameContaining);	     	  
    	      return  "normal/listsingleproduct";
    	  }
      }
      
      //SINGLE PRODUCT
      @GetMapping("/singleproduct/{id}")
      public String singleproduct(@PathVariable int id,Model m) {
    	  
    	  Products product = productrepo.findById(id).get();
    	  if(product==null) {
    		  
    	  }
    	  m.addAttribute("e",product);
    	  return "normal/singleproduct";
      }
      
      //PROCESS ADD TO CART 
      @GetMapping("/processaddtocart/{id}")
      public String getMethodName(@PathVariable int id,Model m,Principal p, @RequestParam("productName") String productName) {
    	  
    	  Products singleproduct = productrepo.findById(id).get();
    	  
    	  User user = userrepo.findByEmail( p.getName());
    	  
    	  if(cartrepo.existsById(singleproduct.getProductid())){
    		  return  "redirect:/user/cart";
    	  }
    	  Cart c=new Cart();
    	  c.setProductid(singleproduct.getProductid());
    	  c.setProductName(singleproduct.getProductName());
    	  c.setProductImg(singleproduct.getProductImg());
    	  c.setProductPrice(singleproduct.getProductPrice());
    	  
    	  //setting user
    	  user.getCart().add(c);
    	  c.setUser(user);
 	     
    	  //setting product
    	  cartrepo.save(c);
    	
    	  return  "redirect:/user/cart";
      }
      
      //CART
      @GetMapping("/cart")
      public String cart(Principal p,Model m) {
    	  String name = p.getName();
    	  User byEmail = userrepo.findByEmail(name);
    	  Set<Cart> byUser = cartrepo.findByUser(byEmail);    	  
    	  m.addAttribute("cart",byUser);
    	  return "normal/cart";
      }
      
      
      //REMOVE FROM CART
      @GetMapping("/removefromcart/{id}")
      @Transactional
      public String removeitem(@PathVariable int id,HttpSession session) {
           Cart cart = cartrepo.findById(id).get();
           cart.setUser(null);
           cartrepo.delete(cart);
    	   return "redirect:/user/cart";
      }
      
      
      //GO TO PAYMENT
      @GetMapping("/makepayment")
      public String makepayment(Principal p,Model m) {
    	  User user = userrepo.findByEmail(p.getName());   	  
    	  List<Cart> cart = user.getCart();
    	  
      // int sum=0;  we cant use sum in for each for that we need to declare it as final to use
    	  AtomicInteger totalsum=new AtomicInteger();//To fix this, you can use an external helper class like AtomicInteger
    	  cart.forEach(
    			c->{
    				totalsum.addAndGet( Integer.parseInt(c.getProductPrice()));  //Adds the value of c.getProductPrice() to the current sum.
    			});
    	  
    	  m.addAttribute("totalsum",totalsum.get());
    	  m.addAttribute("totalitems",cart.size());
    	  
    	  double discount=(double) (totalsum.get() * 0.05);
    	  m.addAttribute( "discount",discount);
    	  
    	  int delivery=40;
    	  m.addAttribute( "Delivery",delivery);
    	  
    	  double Totalamt=totalsum.get()-discount+delivery;
    	  m.addAttribute( "TotalAmount",Totalamt);
    	  
    	  return "normal/makepayment";
      }
      
       //Place Order
      @GetMapping("/placeorder/{totalamt}")
      public String placeorder(Principal p,Model m,@PathVariable double totalamt) {
    	  User user = userrepo.findByEmail(p.getName());
    	  m.addAttribute("address", user.getAddress());
    	  m.addAttribute("phone", user.getPhoneno());
    	  m.addAttribute("totalamt", totalamt);
    	  return "normal/placeorder";
      }
         
      
      //EDIT ADDRESS
      @GetMapping("/editaddress")
      public String editaddress() {
          return "normal/editaddress";
      }
      //PROCESSEDIT ADDRESS
      @PostMapping("/processeditaddress")
      public String processedit(@RequestParam("address")String address,@RequestParam("pnumber")String pnumber,Model m,Principal p) {
    	   
    	  User user = userrepo.findByEmail(p.getName());
    	  user.setAddress(address);
    	  user.setPhoneno(pnumber);  
    	  userrepo.save(user);
    	  m.addAttribute("message", new MessageHelper("Address updated Succesfully!", "alert-success"));
    	 return "redirect:/user/placeorder";
      }
      

      //PROFILE
      @GetMapping("/profile")
      public String profile() { 	  
    	  return "normal/profile";
      }


      //UPDATE
      @GetMapping("/update/{id}")
      public String update(@PathVariable int id,Model m) {
    	 User user = userrepo.findById(id).get();
    	 m.addAttribute("user",user);
    	  return "normal/update";
      }    
      //PROCESS UPDATE
      @PostMapping("/processupdate")
      public String processupdate(@Valid @ModelAttribute User u ,BindingResult res,
    		  @RequestParam("profileimage") MultipartFile newimg,Model m,Principal p,HttpSession session) {
    	  try {
    		  if(res.hasErrors()) {
    			  throw new Exception() ;
    		  }  		  
    		  String name = p.getName();
        	  User Olduser = userrepo.findByEmail(name);

        	  u.setUserid(Olduser.getUserid());
        	  //setting new image
			if(!newimg.isEmpty()) {				
				//delete old photo
				File filedelete=new ClassPathResource("static/img"+"/"+Olduser.getImg()).getFile() ;
				if(filedelete.exists()) {
					filedelete.delete();
					System.out.println("deleted succesfully");
				}
				
				//update new photo
				File file=new ClassPathResource("static/img").getFile() ;
				Path path=Paths.get(file.getAbsolutePath()+ File.separator+newimg.getOriginalFilename());
				Files.copy(newimg.getInputStream(), path,StandardCopyOption.REPLACE_EXISTING);
				u.setImg(newimg.getOriginalFilename());				
			}
			else {
				u.setImg(Olduser.getImg());
			}		 	  
    	     userrepo.save(u);
    	     session.setAttribute("message",new MessageHelper("Profile Succesfully Updated","alert-success"));
             return "redirect:/user/profile";
    	 }
    	  catch (Exception e) {
        	  m.addAttribute("message",new MessageHelper("Something went wrong!!","alert-danger"));
    		  return "normal/update";
  		}
     }
      
      
      //PAYMENT-CREATING ORDER      
      @PostMapping("create_order")
      @ResponseBody
      public String createorder(@RequestBody Map<String,Object> data,Principal p) throws Exception {
    	  
    	  User user = userrepo.findByEmail(p.getName());
    	  double amt=(double)data.get("amount");
    	   RazorpayClient razorpayClient = new RazorpayClient("rzp_test_epfZlkjtEtSgVY" , "wC888Byp7KxY1ILsxuNtbshI");
    	   JSONObject obj=new JSONObject();
    	   obj.put("amount", amt*100);
    	   obj.put("currency", "INR");
    	   obj.put("receipt", "TXn_3452w32");
    	   
    	   //creating new order
    	   Order order = razorpayClient.orders.create(obj); 
     	    
    	   //savind in order repository                 //above created order is different
            List<Cart> all = cartrepo.findAll();      
            all.forEach(f->{
            	  Orders   o=new Orders();
          	      o.setTransactionid(order.get("id"));
                  o.setDate(LocalDate.now());
                  o.setOrderprice(amt);
                  o.setOrdername(f.getProductName());
                  o.setOrderprice(Double.parseDouble(f.getProductPrice()));
                  o.setUser(user);
                  orderrepo.save(o);
            });
                       
    	  return order.toString();
      }
      
      
      //ORDER
      @GetMapping("/orders")
      public String order(Principal p,Model m){
    	  User user = userrepo.findByEmail(p.getName());
    	  List<Orders> orders = user.getOrders();
    	  m.addAttribute("orders",orders);
    	  return "normal/orders";
      }
      
      
      //CHANGE PASSWORD
      @GetMapping("/settings")
      public String changePassword() {
    	  return "normal/settings";
      }
      //process changepassword
      @PostMapping("/changepassword")
      public String processchangePassword(@RequestParam String oldpassword,@RequestParam String newpassword,HttpSession s,Principal p) {
    	  
    	  try {
              
    		  User user = userrepo.findByEmail(p.getName());
    		  
    		  if(bcyptpasswordencoder.matches(oldpassword, user.getPassword())) {
    			  //change password
    			  user.setPassword(bcyptpasswordencoder.encode(newpassword));
    			  userrepo.save(user);
    			  s.setAttribute("message", new MessageHelper("Password Changed Succesfuly", "alert-success"));
            	  return "redirect:/user/profile";
    		  }
    		  else {			  
    			  throw new Exception("Old password is incorrect");
    		  }
    		 
		} catch (Exception e) {
		     s.setAttribute("message", new MessageHelper(e.getMessage(), "alert-danger"));
      	  return "normal/settings";
		}
      }
      
}