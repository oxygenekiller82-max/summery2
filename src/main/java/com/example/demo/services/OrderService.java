package com.example.demo.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dtos.OrderRequestDTO;
import com.example.demo.dtos.OrderResponseDTO;
import com.example.demo.entities.Address;
import com.example.demo.entities.Cart;
import com.example.demo.entities.CartItem;
import com.example.demo.entities.Order;
import com.example.demo.entities.OrderItem;
import com.example.demo.entities.Product;
import com.example.demo.entities.StatutCommande;
import com.example.demo.entities.User;
import com.example.demo.mappers.OrderMapper;
import com.example.demo.repositories.AddressRepository;
import com.example.demo.repositories.CartRepository;
import com.example.demo.repositories.OrderRepository;
import com.example.demo.repositories.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
	
	private final CartRepository cartRepo;
	private final OrderRepository orderRepo;
    private final ProductRepository productRepo;
    private final AddressRepository addressRepo;
    
    private final OrderMapper orderMapper;
	
	//format de commande ORD-2026-XXXXX
	public String generateOrderNumber() {
		return "ORD-" + LocalDate.now().getYear()+"-" + UUID.randomUUID().toString().substring(0,8).toUpperCase();
		
	}//whh is UUID ?
	//-> UNIVERSALLY guaranteed 128bit number across rhe world.. 
	//how do they even do that wth..
	
	
	
	//place order 
	@Transactional 
	public OrderResponseDTO placeOrder(User customer,OrderRequestDTO request) {
		//user cart 
		Cart cart = cartRepo.findByCustomer(customer)
	            .orElseThrow(() -> new RuntimeException("Panier vide"));
		//TODO CUSTOM EXCEPTION
		
		if (cart.getLignes().isEmpty()) throw new RuntimeException("Panier vide");
		
		//addresse doit etre valide! !
		Address address = addressRepo.findById(request.getAddressId())
                .orElseThrow(() -> new RuntimeException("Adresse invalide"));
		
		//TODO CUSTOM EXCEPTIONS
		
		//new order finally..
		
		Order order = Order.builder()
	            .customer(customer)
	            .numeroCommande(generateOrderNumber())
	            //address livrasion -> rue,ville,code postal i guess
	            .adresseLivraison(address.getRue() + ", " + address.getVille() + " " + address.getCodePostal())
	            .statut(StatutCommande.PENDING)
	            .lignes(new ArrayList<>()) //new list Builder. soemthing thingy
	            .fraisLivraison(7.0) // ?? min 7DT .. TODO what is it?
	            .dateCommande(LocalDateTime.now())//PRE PERSIST DOESNT DO IT ?
	            .build();
		
		double runningSubTotal = 0;
		
		//Cart -> many CartItems (lignes) EACH IS A PRODUCT!
		for (CartItem cartItem : cart.getLignes()) {
			Product product = cartItem.getProduct();
			
			
			//stock enough ?
			if (product.getStock() < cartItem.getQuantite()) {
	            throw new RuntimeException("Insufficient Stock for the Porduct: " + product.getNom());
	        }
			
			//else subtract stock:  + SAVE AGAIN 
			product.setStock(product.getStock() - cartItem.getQuantite());
	        productRepo.save(product);
	        
	        //all good ? -> OrderItem = snapshot , doesn't change! 
	        OrderItem orderItem = OrderItem.builder()
	                .order(order)
	                .product(product)
	                .variant(cartItem.getVariant())
	                .quantite(cartItem.getQuantite())
	                .prixUnitaire(product.getPrix()) // PRICE IS FORZEN AND SET§§
	                .build();
	        
	        //-> add to order 
	        order.getLignes().add(orderItem);
	        runningSubTotal += orderItem.getPrixUnitaire() * orderItem.getQuantite();
			
		}
		order.setSousTotal(runningSubTotal);
	    order.setTotalTTC(runningSubTotal + order.getFraisLivraison());
	    
	    //if order saved -> cart is wiped I FORGOT! 
	    Order savedOrder = orderRepo.save(order);
	    cart.getLignes().clear(); // Orphan removal handles the DB cleanup
	    cartRepo.save(cart);
	    
	    return orderMapper.toResponseDTO(savedOrder);
		
	}
	
	//finally rest of API endpoints. 
	
	//get order by id 
	public OrderResponseDTO getOrderById(Long id) {
		Order order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
		return orderMapper.toResponseDTO(order);
	}
	
	// GET -> /api/orders/my all orders 
	public List<OrderResponseDTO> getMyOrders(User customer) {
		return orderRepo.findByCustomerOrderByDateCommandeDesc(customer)
                .stream()
                .map(orderMapper::toResponseDTO)
                .collect(Collectors.toList());
	}
	
	// PUT /api/orders/{id}/status -> update order status 
	public OrderResponseDTO updateStatus(Long id, StatutCommande newStatus) {
		Order order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatut(newStatus);
        
        Order savedOrder = orderRepo.save(order);
        
        return orderMapper.toResponseDTO(savedOrder);
	}
	
	//post cancel by id  /api/orders/{id}/cancel
	//which ones can be cancelled ?
	//YOUR commande + PENDING or PAID status otherwise no..
	//+ ADD BACK TO STOCK if successful cancellation! 
	
	public OrderResponseDTO cancelOrder(Long id, User customer) {
		Order order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
	    
	    //yours
	    if (!order.getCustomer().getId().equals(customer.getId())) {
	        throw new RuntimeException("Cannot Cancel Order.");
	    }
	    
	    //PENDING + PAID
	    if (order.getStatut() != StatutCommande.PENDING && order.getStatut() != StatutCommande.PAID) {
	        throw new RuntimeException("Cannot Cancel Order Currently.");
	    }
	    //TODO CUSTOM EXCPETIONS !!
	    
	    //+ stock -> order.getQuantite, for each Ligne (item) in order
	    for (OrderItem ligne : order.getLignes()) {
	        Product p = ligne.getProduct();
	        p.setStock(p.getStock() + ligne.getQuantite());
	        productRepo.save(p);
	    }
	    
	    order.setStatut(StatutCommande.CANCELLED);
	    
	    Order savedOrder = orderRepo.save(order);
	    
	    return orderMapper.toResponseDTO(savedOrder);
	}
	
	//ADMIN, all orders, GET /api/orders
	public List<OrderResponseDTO> getAllOrders() {
		return orderRepo.findAll()
	            .stream()
	            .map(orderMapper::toResponseDTO)
	            .collect(Collectors.toList());
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
