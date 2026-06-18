package com.example.demo.config;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.entities.Address;
import com.example.demo.entities.Category;
import com.example.demo.entities.Order;
import com.example.demo.entities.OrderItem;
import com.example.demo.entities.Product;
import com.example.demo.entities.Seller;
import com.example.demo.entities.StatutCommande;
import com.example.demo.entities.User;
import com.example.demo.entities.UserRole;
import com.example.demo.repositories.AddressRepository;
import com.example.demo.repositories.CategoryRepository;
import com.example.demo.repositories.OrderItemRepository;
import com.example.demo.repositories.OrderRepository;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.repositories.SellerRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.OrderService;

import lombok.RequiredArgsConstructor;


//PLEASE ADD ACCOUNT BEFORE TESTING BRIHHH
@Component
@RequiredArgsConstructor
public class DataIntializer implements CommandLineRunner {
	
	private final UserRepository userRepo;
    private final SellerRepository sellerRepo;
    private final PasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepo;
    private final ProductRepository productRepo;
    private final OrderRepository orderRepo;
    private final OrderItemRepository orderItemRepo;
    
    private final AddressRepository addressRepo;
    private final OrderService orderService;
    
    
	@Override
	public void run(String... args) throws Exception {
		if (userRepo.findByEmail("spring@test.com").isEmpty()) {
			//create the user .
			User testUser = User.builder()
                    .prenom("spring")
                    .nom("spring")
                    .email("spring@test.com")
                    .motDePasse(passwordEncoder.encode("1"))
                    .role(UserRole.SELLER) 
                    .actif(true)
                    .build();
			
			User savedUser = userRepo.save(testUser);
			
			
			//creating , seller profile ! !!  (=boutique)
			
			Seller testSeller = Seller.builder()
                    .nomBoutique("aespa official store trust")
                    .description("I GOT NO ETA TA TA!")
                    .user(savedUser)
                    .build();
			
			sellerRepo.save(testSeller);
			
		}
		
		//dummy category 
		if (categoryRepo.count() ==0) {
		    categoryRepo.save(Category.builder()
		            .nom("Cold")
		            .description("don't sue me for brain freeze mfs")
		            .build());
		}
		
		//normal customer not seller 
		if (userRepo.findByEmail("customer@test.com").isEmpty()) {
		    User customer = User.builder()
		            .prenom("i buy")
		            .nom("buyer")
		            .email("customer@test.com")
		            .motDePasse(passwordEncoder.encode("123456"))
		            .role(UserRole.CUSTOMER)
		            .actif(true)
		            .build();
		    userRepo.save(customer);
		    
		 //admin..
		    if (userRepo.findByEmail("admin@test.com").isEmpty()) {
			    User admin = User.builder()
			            .prenom("System")
			            .nom("Admin")
			            .email("admin@test.com")
			            .motDePasse(passwordEncoder.encode("123456"))
			            .role(UserRole.ADMIN) // Make sure this matches your Enum exactly
			            .actif(true)
			            .build();
			    userRepo.save(admin);
			}
		    
		//need an address now for order. that's fine..
		Address testAddress = Address.builder()
		            .rue("67 China number1 road")
		            .ville("zhang jia jie")
		            .codePostal("五千六百零二")
		            .user(customer) 
		            .build();
		addressRepo.save(testAddress);
		    
		
		//un produit 
		 Category cold = categoryRepo.findAll().get(0);
		 Seller store = sellerRepo.findAll().get(0);
		    
		 //product -> LIST of actegories: 
		 List<Category> productCategories = new ArrayList<>();
		 productCategories.add(cold);
		 
		 Product phone = Product.builder()
		            .nom("aespa Lemonade tang")
		            .prix(20.0)
		            .categories(productCategories)
		            .seller(store)
		            .actif(true)
		            .stock(10)
		            .description("Lemonade special!")
		            .images(List.of("https://images.unsplash.com/photo-1623084921164-4a8c5c37a912?q=80&w=687&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"))
		            .build();
		 
		 Product phone1 = Product.builder()
		            .nom("aespa Lemonade tang Extra ice")
		            .prix(25.0)
		            .categories(productCategories)
		            .seller(store)
		            .actif(true)
		            .stock(10)
		            .description("Lemonade special!")
		            .images(List.of("https://images.unsplash.com/photo-1623084921164-4a8c5c37a912?q=80&w=687&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"))
		            .build();
		 productRepo.save(phone1);
		 
		 productRepo.save(phone);
		 
		 
		 //order item PAID for this customer for test 
		 Order order = Order.builder()
			        .customer(customer)
			        .statut(StatutCommande.PAID)
			        .numeroCommande(orderService.generateOrderNumber()) //the helper generateeee
			        .adresseLivraison(testAddress.getRue() + ", " + testAddress.getVille())
			        .fraisLivraison(7.0)
			        .sousTotal(12000.0) // 10  * 1200
			        .totalTTC(12007.0)
			        .dateCommande(LocalDateTime.now())
			        .build();
			orderRepo.save(order);
			
			
			OrderItem item = OrderItem.builder()
			        .order(order)
			        .product(phone)
			        .quantite(10)
			        .prixUnitaire(1200.0)
			        .build();
			orderItemRepo.save(item);
		    
		
		}
	}
	


}














