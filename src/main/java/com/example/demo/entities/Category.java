package com.example.demo.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Category {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id; 
	
	@Column(nullable=false)
	private String nom; 
	
	private String description; 
	
	@ManyToOne(fetch = FetchType.LAZY)// many sub categories -> belong to one category, linked by the parent_id column !!!
	@JoinColumn(name="parent_id")// create a column called parent_id 
	//Ex: categorie LAptop -> parent_id = id du catgories Electronics ! 
	//Many side = > sotres FORIEGN KEYS!! 
	private Category parent; 
	
	
	
	//NOW = > Parent perspective !! i am one category i have a list of sub categories!
	@OneToMany(mappedBy="parent", cascade=CascadeType.ALL)
	private List<Category> sousCategories;
	//mapped by ? -> "parent' variable in the child objects -> THEY tell how they're connceted!!
	//cascadeALL -> Ex: delete a category ? -> ALL sub categories will be deleted as well!
	//can also use "orphan Removal" -> automatically deleted the sub category if so huh..ù
	
	
	// catergory <->PRODUCT ! 
	//-> to make it bidirectionnelle = can ask category all its products! 
	@ManyToMany(mappedBy="categories")
	private List<Product> products = new ArrayList<>();
	
	//IN MANY TO MANY -> if you don't use mappedBy on one side it will ceate 2 join tables::
	//but here product = owner of the relationship
	// and Categopry = Mirror! ->  go use mapping in the product class!
}
