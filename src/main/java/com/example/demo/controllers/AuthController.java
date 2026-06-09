package com.example.demo.controllers;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.auth.AuthenticationService;
import com.example.demo.auth.RegisterRequest;
import com.example.demo.dtos.UserRegistrationDTO;
import com.example.demo.entities.Product;
import com.example.demo.entities.UserRole;
import com.example.demo.services.CategoryService;
import com.example.demo.services.ProductService;

import jakarta.validation.Valid;

//NOT A REST ONE -> for the .html !!!
@Controller
public class AuthController {
	private final AuthenticationService authService;
	
	private final ProductService productService;
    private final CategoryService categoryService;
	
	public AuthController(AuthenticationService authService,
				ProductService productService, 
				CategoryService categoryService) {
        this.authService = authService;
        this.productService = productService;
        this.categoryService = categoryService;
    }
	
	@GetMapping("/login")
	public String login() {
		return "login"; //->src/main/resources/tempaltes/login.html 
	}
	
	@GetMapping("/register")
		public String register(Model model) {
			model.addAttribute("user", new UserRegistrationDTO());
			// UserRegistrationDTO ? -> Empty java object... (No email
			//no paswword no name nothing ->labeled "user" !!
			//then add it to thymealf from the model!
			//in thymelaf -> WHICH OBJECT to put the form info (password name..) in ?? -> this one!!
			return "register";
			
			//THEN WHY no for login ??
			//->login does not have @NotBlank, and @size !!
			//-> no need to validate the DTO, omg this framework...
		}
	
	//... need to send back that DTO OMG pls...
	@PostMapping("/register")
	// we will return DATA NOT A PAGE .. ... nope
	public String processRegistration(@Valid @ModelAttribute("user") UserRegistrationDTO user,
            BindingResult result,
            Model model) {
		
	
	// passwords check confirm 
	if (!user.getPassword().equals(user.getConfirmPassword())) {
	result.rejectValue("confirmPassword", "error.user", "Passwords do not match!");
	}
	//error.error we don't have a error users file right..
	
	// errors -> same page
	if (result.hasErrors()) {
	return "register"; 
	}
	
	// 3. SAVE ! 
	RegisterRequest request = RegisterRequest.builder()
	.email(user.getEmail())
	.password(user.getPassword())
	.prenom(user.getPrenom())
	.nom(user.getNom())
	.role(UserRole.CUSTOMER)//default cutsomer -> seperate pages to chenage role ?? ASK PROF
	.build();
	
	authService.register(request);

	// 4. Success! Go to login
	return "redirect:/login?success";
	}
	
	//catalog TESTING TODO
	@GetMapping({"/catalog","/shop"})
    public String getCatalogPage(
    		@RequestParam(required = false) String q,
            @RequestParam(required = false, name = "categoryName") String category,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Double minNote, // note
            Model model
    		) {
        // Prepare the data for Thymeleaf
		//var productsPage = productService.getAllProducts(q, category, minPrice, maxPrice, minNote, null, true, PageRequest.of(0, 12));
        
		//no.. if saerch query -> USE IT TO SEARCH!!!
		List<?> products;
		if (q != null && !q.isEmpty()) {
		    products = productService.search(q); 
		} else {
		    var productsPage = productService.getAllProducts(null, category, minPrice, maxPrice, minNote, null, true, PageRequest.of(0, 12));
		    products = productsPage.getContent();
		}


		
		model.addAttribute("products", products); //  .getContent() ->List
	    model.addAttribute("categories", categoryService.findAll()); 
	    
	    model.addAttribute("selectedNote", minNote);
        
        // Return the name of the file: src/main/resources/templates/catalog.html
        return "catalog";
    }
	
	//oh well this becomes the global controller i guess lol 
	@GetMapping("/product/{id}")
	public String productDetailPage(@PathVariable Long id) {
	    return "product"; // The name of the HTML file
	}
	
	
	@GetMapping("/cart")
    public String showCartPage() {
        return "cart"; 
    }
	
	@GetMapping("/checkout")
    public String showCheckoutPage() {
        return "checkout"; 
    }
	
	@GetMapping("/home")
    public String showHomePage() {
        return "home"; 
    }
	
	@GetMapping("/become-seller")
	public String becomeSeller() {
	    return "become-seller"; // src/main/resources/templates/become-seller.html
	}

	@GetMapping("/seller-dashboard")
	public String sellerDashboard() {
	    return "seller-dashboard"; // You'll build this next
	}

	@GetMapping("/post-product")
	public String postProduct(Model model) {
	    model.addAttribute("categories", categoryService.findAll());
	    return "post-product"; // For sellers to create products
	}
}
	


