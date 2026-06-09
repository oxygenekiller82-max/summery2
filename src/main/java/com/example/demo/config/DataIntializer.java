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
                    .motDePasse(passwordEncoder.encode("123456"))
                    .role(UserRole.SELLER) 
                    .actif(true)
                    .build();
			
			User savedUser = userRepo.save(testUser);
			
			
			//creating , seller profile ! !!  (=boutique)
			
			Seller testSeller = Seller.builder()
                    .nomBoutique("Techy Store")
                    .description("BEST STORE 67")
                    .user(savedUser)
                    .build();
			
			sellerRepo.save(testSeller);
			
		}
		
		//dummy category 
		if (categoryRepo.count() == 0) {
		    categoryRepo.save(Category.builder()
		            .nom("Electronics")
		            .description("power")
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
		 Category electronics = categoryRepo.findAll().get(0);
		 Seller store = sellerRepo.findAll().get(0);
		    
		 //product -> LIST of actegories: 
		 List<Category> productCategories = new ArrayList<>();
		 productCategories.add(electronics);
		 
		 Product phone = Product.builder()
		            .nom("Galaxy S25")
		            .prix(1200.0)
		            .categories(productCategories)
		            .seller(store)
		            .actif(true)
		            .stock(10)
		            .description("Whatever phone")
		            .images(List.of("https://fdn2.gsmarena.com/vv/bigpic/samsung-galaxy-s25-sm-s931.jpg"))
		            .build();
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














