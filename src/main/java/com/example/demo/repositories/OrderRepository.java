package com.example.demo.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entities.Order;
import com.example.demo.entities.Product;
import com.example.demo.entities.StatutCommande;
import com.example.demo.entities.User;

public interface OrderRepository extends JpaRepository<Order, Long> {

	//get all user orders 
	List<Order> findByCustomerOrderByDateCommandeDesc(User customer);
	//TODO wth ..
	
	//admin -> by date all
	List<Order> findAllByOrderByDateCommandeDesc(Pageable pageable);
	
	// recent Orders -> Customer by ID
	List<Order> findByCustomerIdOrderByDateCommandeDesc(Long customerId);
	
	
	
	
	
	//the dahsboard stuff is actaully complicated..
	//so ADMIN -> only revenue total
	//-> THE BOTTOM LINE
	
	@Query("SELECT COALESCE(SUM(o.totalTTC), 0) FROM Order o WHERE o.statut IN :statuts")
	Double sumPlatformRevenue(@Param("statuts") List<StatutCommande> statuts);
	
	//Seller -> specifric to their ID: 
	
	@Query("SELECT COALESCE(SUM(l.prixUnitaire * l.quantite), 0) FROM Order o JOIN o.lignes l " +
		   "WHERE l.product.seller.id = :sellerId AND o.statut IN :statuts")
	Double sumSellerRevenue(@Param("sellerId") Long sellerId, @Param("statuts") List<StatutCommande> statuts);
	
	
	//top selling
	@Query("SELECT l.product FROM OrderItem l GROUP BY l.product ORDER BY SUM(l.quantite) DESC")
	List<Product> findTopSellingProducts(Pageable pageable);
	
	//seller more stuff
	//-Pending + of this seller id 
	
	@Query("SELECT COUNT(DISTINCT o) FROM Order o JOIN o.lignes l " + 
		   "WHERE l.product.seller.id = :sellerId AND o.statut = :statut")
	Long countOrdersBySellerIdAndStatut(@Param("sellerId") Long sellerId, @Param("statut") StatutCommande statut);
	
	
}
